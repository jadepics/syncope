package org.apache.syncope.core.provisioning.java;

import java.text.ParseException;
import java.time.temporal.TemporalAccessor;
import java.util.*;
import java.util.stream.Collectors;
import org.apache.commons.jexl3.JexlContext;
import org.apache.commons.lang3.*;
import org.apache.commons.lang3.mutable.*;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.syncope.common.lib.Attr;
import org.apache.syncope.common.lib.to.*;
import org.apache.syncope.common.lib.types.*;
import org.apache.syncope.core.persistence.api.EncryptorManager;
import org.apache.syncope.core.persistence.api.dao.*;
import org.apache.syncope.core.persistence.api.entity.*;
import org.apache.syncope.core.persistence.api.entity.group.Group;
import org.apache.syncope.core.persistence.api.entity.user.*;
import org.apache.syncope.core.persistence.api.utils.FormatUtils;
import org.apache.syncope.core.provisioning.api.*;
import org.apache.syncope.core.provisioning.api.data.ItemTransformer;
import org.apache.syncope.core.provisioning.api.jexl.*;
import org.apache.syncope.core.provisioning.java.utils.*;
import org.apache.syncope.core.spring.security.AuthContextUtils;
import org.identityconnectors.framework.common.FrameworkUtil;
import org.identityconnectors.framework.common.objects.*;
import org.slf4j.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@SuppressWarnings({"PMD.CouplingBetweenObjects", "PMD.TooManyMethods"})
public class DefaultMappingManager implements MappingManager {

    protected static final Logger LOG = LoggerFactory.getLogger(MappingManager.class);

    private static final String INVALID_INT_ATTR_NAME = "Invalid intAttrName '{}' specified, ignoring";

    protected static Optional<String> processPreparedAttr(
            final PreparedAttr preparedAttr,
            final Set<Attribute> attributes) {

        if (preparedAttr == null) {
            return Optional.empty();
        }

        String connObjectKey = null;

        if (preparedAttr.connObjectLink() != null) {
            connObjectKey = preparedAttr.connObjectLink();
        }

        if (preparedAttr.attribute() != null) {
            Optional.ofNullable(AttributeUtil.find(preparedAttr.attribute().getName(), attributes)).ifPresentOrElse(
                    alreadyAdded -> {
                        attributes.remove(alreadyAdded);

                        Set<Object> values = new HashSet<>();
                        if (!CollectionUtils.isEmpty(alreadyAdded.getValue())) {
                            values.addAll(alreadyAdded.getValue());
                        }
                        if (preparedAttr.attribute().getValue() != null) {
                            values.addAll(preparedAttr.attribute().getValue());
                        }

                        attributes.add(AttributeBuilder.build(preparedAttr.attribute().getName(), values));
                    },
                    () -> attributes.add(preparedAttr.attribute()));
        }

        return Optional.ofNullable(connObjectKey);
    }

    protected static Name getName(final String evalConnObjectLink, final String connObjectKey) {
        // If connObjectLink evaluates to an empty string, just use the provided connObjectKey as Name(),
        // otherwise evaluated connObjectLink expression is taken as Name().
        Name name;
        if (StringUtils.isBlank(evalConnObjectLink)) {
            // add connObjectKey as __NAME__ attribute ...
            LogSupport.debug("Add connObjectKey [{}] as {}", connObjectKey, Name.NAME);
            name = new Name(connObjectKey);
        } else {
            LogSupport.debug("Add connObjectLink [{}] as {}", evalConnObjectLink, Name.NAME);
            name = new Name(evalConnObjectLink);

            // connObjectKey not propagated: it will be used to set the value for __UID__ attribute
            LogSupport.debug("connObjectKey [{}] will be used as {}", connObjectKey, Uid.NAME);
        }

        return name;
    }

    protected static PlainAttrValue clonePlainAttrValue(final PlainAttrValue src) {
        PlainAttrValue dst = new PlainAttrValue();

        dst.setBinaryValue(src.getBinaryValue());
        dst.setBooleanValue(src.getBooleanValue());
        dst.setDateValue(src.getDateValue());
        dst.setDoubleValue(src.getDoubleValue());
        dst.setLongValue(src.getLongValue());
        dst.setStringValue(src.getStringValue());

        return dst;
    }

    protected final UserDAO userDAO;

    protected final AnyObjectDAO anyObjectDAO;

    protected final GroupDAO groupDAO;

    protected final RelationshipTypeDAO relationshipTypeDAO;

    protected final RealmSearchDAO realmSearchDAO;

    protected final ImplementationDAO implementationDAO;

    protected final DerAttrHandler derAttrHandler;

    protected final IntAttrNameParser intAttrNameParser;

    protected final EncryptorManager encryptorManager;

    protected final JexlTools jexlTools;

    @SuppressWarnings("PMD.ExcessiveParameterList")
    public DefaultMappingManager(
            final UserDAO userDAO,
            final AnyObjectDAO anyObjectDAO,
            final GroupDAO groupDAO,
            final RelationshipTypeDAO relationshipTypeDAO,
            final RealmSearchDAO realmSearchDAO,
            final ImplementationDAO implementationDAO,
            final DerAttrHandler derAttrHandler,
            final IntAttrNameParser intAttrNameParser,
            final EncryptorManager encryptorManager,
            final JexlTools jexlTools) {

        this.userDAO = userDAO;
        this.anyObjectDAO = anyObjectDAO;
        this.groupDAO = groupDAO;
        this.relationshipTypeDAO = relationshipTypeDAO;
        this.realmSearchDAO = realmSearchDAO;
        this.implementationDAO = implementationDAO;
        this.derAttrHandler = derAttrHandler;
        this.intAttrNameParser = intAttrNameParser;
        this.encryptorManager = encryptorManager;
        this.jexlTools = jexlTools;
    }

    protected List<Implementation> getTransformers(final Item item) {
        return item.getTransformers().stream().
                map(implementationDAO::findById).
                flatMap(Optional::stream).
                collect(Collectors.toList());
    }

    /**
     * Build __NAME__ for propagation.
     * First look if there is a defined connObjectLink for the given resource (and in
     * this case evaluate as JEXL); otherwise, take given connObjectKey.
     *
     * @param any given any object
     * @param provision external resource
     * @param connObjectKey connector object key
     * @return the value to be propagated as __NAME__
     */
    protected Name evaluateNAME(final Any any, final Provision provision, final String connObjectKey) {
        if (StringUtils.isBlank(connObjectKey)) {
            // log but avoid to throw exception: leave it to the external resource
            LogSupport.debug("Missing connObjectKey for {}", any.getType().getKey());
        }

        // Evaluate connObjectKey expression
        String connObjectLink = Optional.ofNullable(provision.getMapping()).
                map(Mapping::getConnObjectLink).
                orElse(null);
        String evalConnObjectLink = null;
        if (StringUtils.isNotBlank(connObjectLink)) {
            JexlContext jexlContext = new JexlContextBuilder().
                    fields(any).
                    plainAttrs(any.getPlainAttrs()).
                    derAttrs(derAttrHandler.getValues(any)).
                    build();

            evalConnObjectLink = jexlTools.evaluateExpression(connObjectLink, jexlContext).toString();
        }

        return getName(evalConnObjectLink, connObjectKey);
    }

    /**
     * Build __NAME__ for propagation.
     * First look if there is a defined connObjectLink for the given resource (and in
     * this case evaluate as JEXL); otherwise, take given connObjectKey.
     *
     * @param realm given any object
     * @param orgUnit external resource
     * @param connObjectKey connector object key
     * @return the value to be propagated as __NAME__
     */
    protected Name evaluateNAME(final Realm realm, final OrgUnit orgUnit, final String connObjectKey) {
        if (StringUtils.isBlank(connObjectKey)) {
            // log but avoid to throw exception: leave it to the external resource
            LogSupport.debug("Missing connObjectKey for Realms");
        }

        // Evaluate connObjectKey expression
        String connObjectLink = orgUnit.getConnObjectLink();
        String evalConnObjectLink = null;
        if (StringUtils.isNotBlank(connObjectLink)) {
            JexlContext jexlContext = new JexlContextBuilder().
                    fields(realm).
                    plainAttrs(realm.getPlainAttrs()).
                    derAttrs(derAttrHandler.getValues(realm)).
                    build();

            evalConnObjectLink = jexlTools.evaluateExpression(connObjectLink, jexlContext).toString();
        }

        return getName(evalConnObjectLink, connObjectKey);
    }

    @Transactional(readOnly = true)
    @Override
    public PreparedAttrs prepareAttrsFromAny(
            final Any any,
            final String password,
            final boolean changePwd,
            final Boolean enable,
            final ExternalResource resource,
            final Provision provision) {

        LogSupport.debug("Preparing resource attributes for {} with provision {} for attributes {}",
                any, provision, any.getPlainAttrs());

        Set<Attribute> attributes = new HashSet<>();
        Mutable<String> connObjectKeyValue = new MutableObject<>();

        MappingUtils.getPropagationItems(provision.getMapping().getItems().stream()).forEach(item -> {
            LogSupport.debug("Processing expression '{}'", item.getIntAttrName());

            try {
                processPreparedAttr(
                        prepareAttr(
                                resource,
                                provision,
                                item,
                                any,
                                password,
                                AccountGetter.DEFAULT,
                                AccountGetter.DEFAULT,
                                PlainAttrGetter.DEFAULT),
                        attributes).ifPresent(connObjectKeyValue::setValue);
            } catch (RuntimeException e) {
                LogSupport.error("Expression '{}' processing failed", item.getIntAttrName(), e);
            }
        });

        MappingUtils.getConnObjectKeyItem(provision).ifPresent(item -> {
            Attribute connObjectKeyAttr = AttributeUtil.find(item.getExtAttrName(), attributes);
            if (connObjectKeyAttr != null) {
                attributes.remove(connObjectKeyAttr);
                attributes.add(AttributeBuilder.build(item.getExtAttrName(), connObjectKeyValue.get()));
            }

            Name name = evaluateNAME(any, provision, connObjectKeyValue.get());
            attributes.add(name);

            Optional.ofNullable(connObjectKeyValue.get()).
                    filter(cokv -> connObjectKeyAttr == null && !cokv.equals(name.getNameValue())).
                    ifPresent(cokv -> attributes.add(AttributeBuilder.build(item.getExtAttrName(), cokv)));
        });

        Optional.ofNullable(enable).ifPresent(e -> attributes.add(AttributeBuilder.buildEnabled(e)));

        if (!changePwd) {
            Optional.ofNullable(AttributeUtil.find(OperationalAttributes.PASSWORD_NAME, attributes)).
                    ifPresent(attributes::remove);
        }

        return new PreparedAttrs(connObjectKeyValue.get(), attributes);
    }


    @Transactional(readOnly = true)
    @Override
    public Set<Attribute> prepareAttrsFromLinkedAccount(
            final User user,
            final LinkedAccount account,
            final String password,
            final boolean changePwd,
            final Provision provision) {

        return LinkedAccountPreparationSupport.prepare(this, user, account, password, changePwd, provision);
    }

    @Override
    public PreparedAttrs prepareAttrsFromRealm(final Realm realm, final ExternalResource resource) {
        if (resource.getOrgUnit() == null) {
            LogSupport.error("No mapping configured for Realms");
            return new PreparedAttrs(null, Set.of());
        }

        LogSupport.debug("Preparing resource attributes for {} with orgUnit {}", realm, resource.getOrgUnit());

        Set<Attribute> attributes = new HashSet<>();
        Mutable<String> connObjectKeyValue = new MutableObject<>();

        MappingUtils.getPropagationItems(resource.getOrgUnit().getItems().stream()).forEach(item -> {
            LogSupport.debug("Processing expression '{}'", item.getIntAttrName());

            try {
                processPreparedAttr(
                        prepareAttr(
                                resource,
                                item,
                                realm),
                        attributes).ifPresent(connObjectKeyValue::setValue);
            } catch (RuntimeException e) {
                LogSupport.error("Expression '{}' processing failed", item.getIntAttrName(), e);
            }
        });

        resource.getOrgUnit().getConnObjectKeyItem().ifPresent(item -> {
            Attribute connObjectKeyAttr = AttributeUtil.find(item.getExtAttrName(), attributes);
            if (connObjectKeyAttr != null) {
                attributes.remove(connObjectKeyAttr);
                attributes.add(AttributeBuilder.build(item.getExtAttrName(), connObjectKeyValue.get()));
            }

            Name name = evaluateNAME(realm, resource.getOrgUnit(), connObjectKeyValue.get());
            attributes.add(name);

            Optional.ofNullable(connObjectKeyValue.get()).
                    filter(cokv -> connObjectKeyAttr == null && !cokv.equals(name.getNameValue())).
                    ifPresent(cokv -> attributes.add(AttributeBuilder.build(item.getExtAttrName(), cokv)));
        });

        return new PreparedAttrs(connObjectKeyValue.get(), attributes);
    }

    @SuppressWarnings("PMD.AvoidCatchingGenericException")
    protected Optional<String> decodePassword(final Account account) {
        try {
            return Optional.of(encryptorManager.getInstance().
                    decode(account.getPassword(), account.getCipherAlgorithm()));
        } catch (Exception e) {
            LogSupport.error("Could not decode password for {}", account, e);
            return Optional.empty();
        }
    }

    protected Optional<String> getPasswordAttrValue(final Account account, final String defaultValue) {
        Optional<String> passwordAttrValue;
        if (account instanceof LinkedAccount) {
            passwordAttrValue = account.getPassword() == null
                    ? Optional.of(defaultValue)
                    : decodePassword(account);
        } else {
            if (StringUtils.isNotBlank(defaultValue)) {
                passwordAttrValue = Optional.of(defaultValue);
            } else if (account.canDecodeSecrets()) {
                passwordAttrValue = decodePassword(account);
            } else {
                passwordAttrValue = Optional.empty();
            }
        }

        return passwordAttrValue;
    }


    @Override
    public PreparedAttr prepareAttr(
            final ExternalResource resource,
            final Provision provision,
            final Item item,
            final Any any,
            final String password,
            final AccountGetter usernameAccountGetter,
            final AccountGetter passwordAccountGetter,
            final PlainAttrGetter plainAttrGetter) {

        IntAttrName intAttrName;
        try {
            intAttrName = intAttrNameParser.parse(item.getIntAttrName(), any.getType().getKind());
        } catch (ParseException e) {
            LogSupport.error(INVALID_INT_ATTR_NAME, item.getIntAttrName(), e);
            return null;
        }

        AttrSchemaType schemaType = ValueConversionSupport.schemaType(intAttrName);
        IntValues intValues = getIntValues(
                resource, provision, item, intAttrName, schemaType, any, usernameAccountGetter, plainAttrGetter);

        LogSupport.mappingDefinition(item, intAttrName, intValues);

        List<Object> objValues = ValueConversionSupport.convert(this, intAttrName, intValues);
        return PreparedAttrSupport.forAny(this, item, any, password, passwordAccountGetter, objValues);
    }


    @Override
    public PreparedAttr prepareAttr(
            final ExternalResource resource,
            final Item item,
            final Realm realm) {

        IntAttrName intAttrName;
        try {
            intAttrName = intAttrNameParser.parse(item.getIntAttrName());
        } catch (ParseException e) {
            LogSupport.error(INVALID_INT_ATTR_NAME, item.getIntAttrName(), e);
            return null;
        }

        AttrSchemaType schemaType = ValueConversionSupport.schemaType(intAttrName);
        IntValues intValues = getIntValues(resource, item, intAttrName, schemaType, realm);

        LogSupport.mappingDefinition(item, intAttrName, intValues);

        List<Object> objValues = ValueConversionSupport.convert(this, intAttrName, intValues);
        return PreparedAttrSupport.forRealm(item, objValues);
    }


    @Transactional(readOnly = true)
    @Override
    public IntValues getIntValues(
            final ExternalResource resource,
            final Provision provision,
            final Item item,
            final IntAttrName intAttrName,
            final AttrSchemaType schemaType,
            final Any any,
            final AccountGetter usernameAccountGetter,
            final PlainAttrGetter plainAttrGetter) {

        return AnyIntValuesSupport.get(
                this, resource, provision, item, intAttrName, schemaType, any, usernameAccountGetter, plainAttrGetter);
    }


    @Transactional(readOnly = true)
    @Override
    public IntValues getIntValues(
            final ExternalResource resource,
            final Item item,
            final IntAttrName intAttrName,
            final AttrSchemaType schemaType,
            final Realm realm) {

        return RealmIntValuesSupport.get(this, resource, item, intAttrName, schemaType, realm);
    }

    protected String getManagerValue(
            final ExternalResource resource,
            final Provision provision,
            final Any any) {

        Optional<Item> connObjectKeyItem = MappingUtils.getConnObjectKeyItem(provision);

        PreparedAttr preparedAttr = null;
        if (connObjectKeyItem.isPresent()) {
            preparedAttr = prepareAttr(
                    resource,
                    provision,
                    connObjectKeyItem.get(),
                    any,
                    null,
                    AccountGetter.DEFAULT,
                    AccountGetter.DEFAULT,
                    PlainAttrGetter.DEFAULT);
        }

        return Optional.ofNullable(preparedAttr).
                map(attr -> evaluateNAME(any, provision, attr.connObjectLink()).getNameValue()).orElse(null);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<String> getConnObjectKeyValue(
            final Any any,
            final ExternalResource resource,
            final Provision provision) {

        Optional<Item> connObjectKeyItem = provision.getMapping().getConnObjectKeyItem();
        if (connObjectKeyItem.isEmpty()) {
            LogSupport.error("Unable to locate conn object key item for {}", any.getType().getKey());
            return Optional.empty();
        }

        Item item = connObjectKeyItem.get();
        IntValues intValues;
        try {
            intValues = getIntValues(
                    resource,
                    provision,
                    item,
                    intAttrNameParser.parse(item.getIntAttrName(), any.getType().getKind()),
                    AttrSchemaType.String,
                    any,
                    AccountGetter.DEFAULT,
                    PlainAttrGetter.DEFAULT);
        } catch (ParseException e) {
            LogSupport.error(INVALID_INT_ATTR_NAME, item.getIntAttrName(), e);
            intValues = new IntValues(AttrSchemaType.String, List.of());
        }
        return intValues.values().isEmpty()
                ? Optional.empty()
                : Optional.of(intValues.values().getFirst().getValueAsString());
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<String> getConnObjectKeyValue(final Realm realm, final ExternalResource resource) {
        if (resource.getOrgUnit() == null) {
            LogSupport.error("No mapping configured for Realms");
            return Optional.empty();
        }

        Optional<Item> connObjectKeyItem = resource.getOrgUnit().getConnObjectKeyItem();
        if (connObjectKeyItem.isEmpty()) {
            LogSupport.error("Unable to locate conn object key item for Realms");
            return Optional.empty();
        }

        Item item = connObjectKeyItem.get();
        IntValues intValues;
        try {
            intValues = getIntValues(
                    resource,
                    item,
                    intAttrNameParser.parse(item.getIntAttrName()),
                    AttrSchemaType.String,
                    realm);
        } catch (ParseException e) {
            LogSupport.error(INVALID_INT_ATTR_NAME, item.getIntAttrName(), e);
            intValues = new IntValues(AttrSchemaType.String, List.of());
        }
        return intValues.values().isEmpty()
                ? Optional.empty()
                : Optional.of(intValues.values().getFirst().getValueAsString());
    }


    @Transactional(readOnly = true)
    @Override
    public void setIntValues(final Item item, final Attribute attr, final AnyTO anyTO) {
        AnyToSetSupport.set(this, item, attr, anyTO);
    }


    @Override
    public void setIntValues(final Item item, final Attribute attr, final RealmTO realmTO) {
        RealmToSetSupport.set(this, item, attr, realmTO);
    }

    @Override
    public boolean hasMustChangePassword(final Provision provision) {
        return Optional.ofNullable(provision.getMapping()).
                map(mapping -> mapping.getItems().stream().
                anyMatch(item -> "mustChangePassword".equals(item.getIntAttrName()))).
                orElse(false);
    }

    private static final class LogSupport {

        private LogSupport() {
        }

        static void debug(final String message, final Object... arguments) {
            if (LOG.isDebugEnabled()) {
                LOG.debug(message, arguments);
            }
        }

        static void warn(final String message, final Object... arguments) {
            if (LOG.isWarnEnabled()) {
                LOG.warn(message, arguments);
            }
        }

        static void error(final String message, final Object... arguments) {
            if (LOG.isErrorEnabled()) {
                LOG.error(message, arguments);
            }
        }

        static void mappingDefinition(
                final Item item,
                final IntAttrName intAttrName,
                final IntValues intValues) {

            debug(
                    """
                      Define mapping for: 
                      * Item {}
                      * Schema {}
                      * ClassType {}
                      * AttrSchemaType {}
                      * Values {}""",
                    item,
                    intAttrName.getSchemaInfo(),
                    intValues.attrSchemaType().getType().getName(),
                    intValues.attrSchemaType(),
                    intValues.values());
        }
    }

    private static final class LinkedAccountPreparationSupport {

        private LinkedAccountPreparationSupport() {
        }

        static Set<Attribute> prepare(
                final DefaultMappingManager owner,
                final User user,
                final LinkedAccount account,
                final String password,
                final boolean changePwd,
                final Provision provision) {

            LogSupport.debug("Preparing resource attributes for linked account {} of user {} with provision {} "
                    + "for user attributes {} with override {}",
                    account, user, provision, user.getPlainAttrs(), account.getPlainAttrs());

            Set<Attribute> attributes = new HashSet<>();
            AccountGetter usernameGetter =
                    acct -> account.getUsername() == null ? AccountGetter.DEFAULT.apply(acct) : account;
            AccountGetter passwordGetter =
                    acct -> account.getPassword() == null ? AccountGetter.DEFAULT.apply(acct) : account;
            PlainAttrGetter plainAttrGetter = (attributable, schema) -> {
                PlainAttr result = attributable instanceof User
                        ? account.getPlainAttr(schema).orElse(null)
                        : null;
                return result == null
                        ? PlainAttrGetter.DEFAULT.apply(attributable, schema)
                        : result;
            };

            MappingUtils.getPropagationItems(provision.getMapping().getItems().stream()).forEach(item -> {
                LogSupport.debug("Processing expression '{}'", item.getIntAttrName());

                try {
                    processPreparedAttr(
                            owner.prepareAttr(
                                    account.getResource(),
                                    provision,
                                    item,
                                    user,
                                    password,
                                    usernameGetter,
                                    passwordGetter,
                                    plainAttrGetter),
                            attributes);
                } catch (RuntimeException e) {
                    LogSupport.error("Expression '{}' processing failed", item.getIntAttrName(), e);
                }
            });

            addConnObjectKey(owner, user, account, provision, attributes);

            if (account.isSuspended() != null) {
                attributes.add(AttributeBuilder.buildEnabled(BooleanUtils.negate(account.isSuspended())));
            }
            if (!changePwd) {
                Attribute pwdAttr = AttributeUtil.find(OperationalAttributes.PASSWORD_NAME, attributes);
                if (pwdAttr != null) {
                    attributes.remove(pwdAttr);
                }
            }

            return attributes;
        }

        private static void addConnObjectKey(
                final DefaultMappingManager owner,
                final User user,
                final LinkedAccount account,
                final Provision provision,
                final Set<Attribute> attributes) {

            String connObjectKey = account.getConnObjectKeyValue();
            MappingUtils.getConnObjectKeyItem(provision).ifPresent(connObjectKeyItem -> {
                Attribute connObjectKeyExtAttr =
                        AttributeUtil.find(connObjectKeyItem.getExtAttrName(), attributes);
                if (connObjectKeyExtAttr != null) {
                    attributes.remove(connObjectKeyExtAttr);
                    attributes.add(AttributeBuilder.build(connObjectKeyItem.getExtAttrName(), connObjectKey));
                }

                Name name = owner.evaluateNAME(user, provision, connObjectKey);
                attributes.add(name);
                if (!connObjectKey.equals(name.getNameValue()) && connObjectKeyExtAttr == null) {
                    attributes.add(AttributeBuilder.build(connObjectKeyItem.getExtAttrName(), connObjectKey));
                }
            });
        }
    }

    private static final class ValueConversionSupport {

        private ValueConversionSupport() {
        }

        static AttrSchemaType schemaType(final IntAttrName intAttrName) {
            return Optional.ofNullable(intAttrName.getSchemaInfo()).
                    filter(schemaInfo -> schemaInfo.schema() instanceof PlainSchema).
                    map(schemaInfo -> schemaInfo.schema().getType()).
                    orElse(AttrSchemaType.String);
        }

        static List<Object> convert(
                final DefaultMappingManager owner,
                final IntAttrName intAttrName,
                final IntValues intValues) {

            List<Object> result = new ArrayList<>();
            for (PlainAttrValue value : intValues.values()) {
                result.add(convertValue(owner, intAttrName, intValues.attrSchemaType(), value));
            }
            return result;
        }

        private static Object convertValue(
                final DefaultMappingManager owner,
                final IntAttrName intAttrName,
                final AttrSchemaType schemaType,
                final PlainAttrValue value) {

            if (schemaType == AttrSchemaType.Encrypted
                    && intAttrName.getSchemaInfo().schema() instanceof PlainSchema schema) {

                return decodeEncrypted(owner, intAttrName, schema, value);
            }
            if (FrameworkUtil.isSupportedAttributeType(schemaType.getType())) {
                return value.getValue();
            }

            PlainSchema plainSchema = Optional.ofNullable(intAttrName.getSchemaInfo()).
                    map(IntAttrName.SchemaInfo::schema).
                    filter(PlainSchema.class::isInstance).
                    map(PlainSchema.class::cast).
                    orElse(null);
            return plainSchema == null || plainSchema.getType() != schemaType
                    ? value.getValueAsString(schemaType)
                    : value.getValueAsString(plainSchema);
        }

        @SuppressWarnings("PMD.AvoidCatchingGenericException")
        private static Object decodeEncrypted(
                final DefaultMappingManager owner,
                final IntAttrName intAttrName,
                final PlainSchema schema,
                final PlainAttrValue value) {

            try {
                String decoded = owner.encryptorManager.getInstance(schema.getSecretKey()).
                        decode(value.getStringValue(), schema.getCipherAlgorithm());
                return Optional.ofNullable(decoded).orElse(value.getStringValue());
            } catch (Exception e) {
                LogSupport.warn("Could not decode value for {} with algorithm {}",
                        intAttrName.getSchemaInfo(), schema.getCipherAlgorithm(), e);
                return value.getStringValue();
            }
        }
    }

    private static final class PreparedAttrSupport {

        private PreparedAttrSupport() {
        }

        static PreparedAttr forAny(
                final DefaultMappingManager owner,
                final Item item,
                final Any any,
                final String password,
                final AccountGetter passwordAccountGetter,
                final List<Object> objValues) {

            if (item.isConnObjectKey()) {
                return new PreparedAttr(objValues.isEmpty() ? null : objValues.getFirst().toString(), null);
            }
            if (item.isPassword() && any instanceof User user) {
                return owner.getPasswordAttrValue(passwordAccountGetter.apply(user), password).
                        map(passwordAttrValue -> new PreparedAttr(
                        null, AttributeBuilder.buildPassword(passwordAttrValue.toCharArray()))).
                        orElse(null);
            }
            return forRegularAttribute(item, objValues);
        }

        static PreparedAttr forRealm(final Item item, final List<Object> objValues) {
            if (item.isConnObjectKey()) {
                return new PreparedAttr(objValues.isEmpty() ? null : objValues.getFirst().toString(), null);
            }
            return forRegularAttribute(item, objValues);
        }

        private static PreparedAttr forRegularAttribute(final Item item, final List<Object> objValues) {
            if (objValues.isEmpty()) {
                return new PreparedAttr(null, AttributeBuilder.build(item.getExtAttrName()));
            }
            if (OperationalAttributes.PASSWORD_NAME.equals(item.getExtAttrName())) {
                return new PreparedAttr(
                        null,
                        AttributeBuilder.buildPassword(objValues.getFirst().toString().toCharArray()));
            }
            return new PreparedAttr(null, AttributeBuilder.build(item.getExtAttrName(), objValues));
        }
    }

    private record ReferenceContext(
            List<Any> references,
            Relationship<?, ?> relationship,
            Membership<?> membership) {
    }

    private static final class ReferenceSupport {

        private ReferenceSupport() {
        }

        static ReferenceContext resolve(
                final DefaultMappingManager owner,
                final IntAttrName intAttrName,
                final Any any) {

            List<Any> references = new ArrayList<>();
            if (intAttrName.getExternalGroup() == null
                    && intAttrName.getExternalAnyObject() == null
                    && intAttrName.getExternalUser() == null) {

                references.add(any);
            }

            Relationship<?, ?> relationship = null;
            Membership<?> membership = null;

            if (intAttrName.getExternalUser() != null) {
                owner.userDAO.findByUsername(intAttrName.getExternalUser()).ifPresentOrElse(
                        references::add,
                        () -> LogSupport.warn(
                                "Could not find user {}, ignoring", intAttrName.getExternalUser()));
            } else if (intAttrName.getExternalGroup() != null) {
                owner.groupDAO.findByName(intAttrName.getExternalGroup()).ifPresentOrElse(
                        references::add,
                        () -> LogSupport.warn(
                                "Could not find group {}, ignoring", intAttrName.getExternalGroup()));
            } else if (intAttrName.getExternalAnyObject() != null) {
                references.addAll(owner.anyObjectDAO.findByName(intAttrName.getExternalAnyObject()));
            } else if (intAttrName.getMembership() != null && any instanceof Groupable<?, ?, ?> groupable) {
                membership = owner.groupDAO.findByName(intAttrName.getMembership()).
                        flatMap(group -> groupable.getMembership(group.getKey())).
                        orElse(null);
            } else if (intAttrName.getRelationshipInfo() != null && any instanceof Relatable<?, ?> relatable) {
                relationship = resolveRelationship(owner, intAttrName, relatable);
            }

            return new ReferenceContext(references, relationship, membership);
        }

        private static Relationship<?, ?> resolveRelationship(
                final DefaultMappingManager owner,
                final IntAttrName intAttrName,
                final Relatable<?, ?> relatable) {

            RelationshipType relationshipType =
                    owner.relationshipTypeDAO.findById(intAttrName.getRelationshipInfo().type()).orElse(null);
            if (relationshipType == null) {
                LogSupport.warn(
                        "Could not find relationship type {}, ignoring", intAttrName.getRelationshipInfo().type());
                return null;
            }

            return owner.anyObjectDAO.findByName(
                    relationshipType.getRightEndAnyType().getKey(),
                    intAttrName.getRelationshipInfo().anyObject()).
                    flatMap(otherEnd -> relatable.getRelationship(relationshipType, otherEnd.getKey())).
                    orElse(null);
        }
    }

    private static final class AnyIntValuesSupport {

        private AnyIntValuesSupport() {
        }

        static IntValues get(
                final DefaultMappingManager owner,
                final ExternalResource resource,
                final Provision provision,
                final Item item,
                final IntAttrName intAttrName,
                final AttrSchemaType schemaType,
                final Any any,
                final AccountGetter usernameAccountGetter,
                final PlainAttrGetter plainAttrGetter) {

            LogSupport.debug("Get internal values for {} as '{}' on {}", any, item.getIntAttrName(), resource);

            ReferenceContext context = ReferenceSupport.resolve(owner, intAttrName, any);
            if (context.references().isEmpty()) {
                LogSupport.warn("Could not determine the reference instance for {}", item.getIntAttrName());
                return new IntValues(schemaType, List.of());
            }

            List<PlainAttrValue> values = new ArrayList<>();
            for (Any ref : context.references()) {
                addReferenceValues(
                        owner,
                        resource,
                        provision,
                        intAttrName,
                        usernameAccountGetter,
                        plainAttrGetter,
                        context,
                        ref,
                        values);
            }

            LogSupport.debug("Internal values: {}", values);
            return TransformerSupport.beforePropagation(owner, item, any, schemaType, values);
        }

        private static void addReferenceValues(
                final DefaultMappingManager owner,
                final ExternalResource resource,
                final Provision provision,
                final IntAttrName intAttrName,
                final AccountGetter usernameAccountGetter,
                final PlainAttrGetter plainAttrGetter,
                final ReferenceContext context,
                final Any ref,
                final List<PlainAttrValue> values) {

            if (intAttrName.getField() != null) {
                AnyFieldValueSupport.add(
                        owner, resource, provision, intAttrName, usernameAccountGetter, ref, values);
            } else if (intAttrName.getSchemaInfo() != null) {
                AnySchemaValueSupport.add(
                        owner,
                        intAttrName,
                        plainAttrGetter,
                        context.membership(),
                        context.relationship(),
                        ref,
                        values);
            }
        }
    }

    private static final class AnyFieldValueSupport {

        private AnyFieldValueSupport() {
        }

        static void add(
                final DefaultMappingManager owner,
                final ExternalResource resource,
                final Provision provision,
                final IntAttrName intAttrName,
                final AccountGetter usernameAccountGetter,
                final Any ref,
                final List<PlainAttrValue> values) {

            switch (intAttrName.getField()) {
                case "key" -> addString(values, ref.getKey());
                case "username" -> addUsername(usernameAccountGetter, ref, values);
                case "realm" -> addString(values, ref.getRealm().getFullPath());
                case "password" -> {
                }
                case "uManager", "gManager" -> addManager(owner, resource, provision, ref, values);
                case "suspended" -> addSuspended(ref, values);
                case "mustChangePassword" -> addMustChangePassword(ref, values);
                default -> addReflective(intAttrName, ref, values);
            }
        }

        private static void addString(final List<PlainAttrValue> values, final String value) {
            PlainAttrValue attrValue = new PlainAttrValue();
            attrValue.setStringValue(value);
            values.add(attrValue);
        }

        private static void addUsername(
                final AccountGetter usernameAccountGetter,
                final Any ref,
                final List<PlainAttrValue> values) {

            if (ref instanceof Account account) {
                addString(values, usernameAccountGetter.apply(account).getUsername());
            }
        }

        private static void addManager(
                final DefaultMappingManager owner,
                final ExternalResource resource,
                final Provision provision,
                final Any ref,
                final List<PlainAttrValue> values) {

            Mapping uMappingTO = provision.getAnyType().equals(AnyTypeKind.USER.name())
                    ? provision.getMapping()
                    : null;
            Mapping gMappingTO = provision.getAnyType().equals(AnyTypeKind.GROUP.name())
                    ? provision.getMapping()
                    : null;

            String managerValue = null;
            if (ref.getUManager() != null && uMappingTO != null) {
                managerValue = owner.getManagerValue(resource, provision, ref.getUManager());
            }
            if (ref.getGManager() != null && gMappingTO != null) {
                managerValue = owner.getManagerValue(resource, provision, ref.getGManager());
            }

            if (StringUtils.isNotBlank(managerValue)) {
                addString(values, managerValue);
            }
        }

        private static void addSuspended(final Any ref, final List<PlainAttrValue> values) {
            if (ref instanceof User user) {
                PlainAttrValue attrValue = new PlainAttrValue();
                attrValue.setBooleanValue(user.isSuspended());
                values.add(attrValue);
            }
        }

        private static void addMustChangePassword(final Any ref, final List<PlainAttrValue> values) {
            if (ref instanceof User user) {
                PlainAttrValue attrValue = new PlainAttrValue();
                attrValue.setBooleanValue(user.isMustChangePassword());
                values.add(attrValue);
            }
        }

        private static void addReflective(
                final IntAttrName intAttrName,
                final Any ref,
                final List<PlainAttrValue> values) {

            try {
                Object fieldValue = FieldUtils.readField(ref, intAttrName.getField(), true);
                PlainAttrValue attrValue = new PlainAttrValue();
                setReflectiveValue(attrValue, fieldValue);
                values.add(attrValue);
            } catch (IllegalAccessException | RuntimeException e) {
                LogSupport.error("Could not read value of '{}' from {}", intAttrName.getField(), ref, e);
            }
        }

        private static void setReflectiveValue(final PlainAttrValue attrValue, final Object fieldValue) {
            if (fieldValue instanceof TemporalAccessor temporalAccessor) {
                // needed because ConnId does not natively supports the Date type
                attrValue.setStringValue(FormatUtils.format(temporalAccessor));
            } else if (Boolean.TYPE.isInstance(fieldValue)) {
                attrValue.setBooleanValue((Boolean) fieldValue);
            } else if (Double.TYPE.isInstance(fieldValue) || Float.TYPE.isInstance(fieldValue)) {
                attrValue.setDoubleValue((Double) fieldValue);
            } else if (Long.TYPE.isInstance(fieldValue) || Integer.TYPE.isInstance(fieldValue)) {
                attrValue.setLongValue((Long) fieldValue);
            } else {
                attrValue.setStringValue(fieldValue.toString());
            }
        }
    }

    private static final class AnySchemaValueSupport {

        private AnySchemaValueSupport() {
        }

        static void add(
                final DefaultMappingManager owner,
                final IntAttrName intAttrName,
                final PlainAttrGetter plainAttrGetter,
                final Membership<?> membership,
                final Relationship<?, ?> relationship,
                final Any ref,
                final List<PlainAttrValue> values) {

            switch (intAttrName.getSchemaInfo().type()) {
                case PLAIN -> addPlain(intAttrName, plainAttrGetter, membership, relationship, ref, values);
                case DERIVED -> addDerived(owner, intAttrName, membership, relationship, ref, values);
                default -> {
                }
            }
        }

        private static void addPlain(
                final IntAttrName intAttrName,
                final PlainAttrGetter plainAttrGetter,
                final Membership<?> membership,
                final Relationship<?, ?> relationship,
                final Any ref,
                final List<PlainAttrValue> values) {

            PlainAttr attr = membership == null && relationship == null
                    ? plainAttrGetter.apply(ref, intAttrName.getSchemaInfo().schema().getKey())
                    : membership == null
                            ? ((Relatable<?, ?>) ref).getPlainAttr(
                                    intAttrName.getSchemaInfo().schema().getKey(), relationship).
                                    orElse(null)
                            : ((Groupable<?, ?, ?>) ref).getPlainAttr(
                                    intAttrName.getSchemaInfo().schema().getKey(), membership).
                                    orElse(null);
            addClonedValues(attr, values);
        }

        private static void addClonedValues(final PlainAttr attr, final List<PlainAttrValue> values) {
            if (attr == null) {
                return;
            }
            if (attr.getUniqueValue() != null) {
                values.add(clonePlainAttrValue(attr.getUniqueValue()));
            } else if (attr.getValues() != null) {
                attr.getValues().forEach(value -> values.add(clonePlainAttrValue(value)));
            }
        }

        private static void addDerived(
                final DefaultMappingManager owner,
                final IntAttrName intAttrName,
                final Membership<?> membership,
                final Relationship<?, ?> relationship,
                final Any ref,
                final List<PlainAttrValue> values) {

            DerSchema derSchema = (DerSchema) intAttrName.getSchemaInfo().schema();
            String derValue = membership == null && relationship == null
                    ? owner.derAttrHandler.getValue(ref, derSchema)
                    : membership == null
                            ? owner.derAttrHandler.getValue((Relatable<?, ?>) ref, relationship, derSchema)
                            : owner.derAttrHandler.getValue((Groupable<?, ?, ?>) ref, membership, derSchema);
            if (derValue != null) {
                PlainAttrValue attrValue = new PlainAttrValue();
                attrValue.setStringValue(derValue);
                values.add(attrValue);
            }
        }
    }

    private static final class TransformerSupport {

        private TransformerSupport() {
        }

        static IntValues beforePropagation(
                final DefaultMappingManager owner,
                final Item item,
                final Any any,
                final AttrSchemaType schemaType,
                final List<PlainAttrValue> values) {

            IntValues transformed = new IntValues(schemaType, values);
            for (ItemTransformer transformer
                    : MappingUtils.getItemTransformers(
                            AuthContextUtils.getDomain(), item, owner.getTransformers(item))) {

                transformed = transformer.beforePropagation(
                        item, any, transformed.attrSchemaType(), transformed.values());
            }
            LogSupport.debug("Transformed values: {}", values);
            return transformed;
        }

        static IntValues beforePropagation(
                final DefaultMappingManager owner,
                final Item item,
                final Realm realm,
                final AttrSchemaType schemaType,
                final List<PlainAttrValue> values) {

            IntValues transformed = new IntValues(schemaType, values);
            for (ItemTransformer transformer
                    : MappingUtils.getItemTransformers(
                            AuthContextUtils.getDomain(), item, owner.getTransformers(item))) {

                transformed = transformer.beforePropagation(
                        item, realm, transformed.attrSchemaType(), transformed.values());
            }
            LogSupport.debug("Transformed values: {}", values);
            return transformed;
        }
    }

    private static final class RealmIntValuesSupport {

        private RealmIntValuesSupport() {
        }

        static IntValues get(
                final DefaultMappingManager owner,
                final ExternalResource resource,
                final Item item,
                final IntAttrName intAttrName,
                final AttrSchemaType schemaType,
                final Realm realm) {

            LogSupport.debug("Get internal values for {} as '{}' on {}", realm, item.getIntAttrName(), resource);

            List<PlainAttrValue> values = new ArrayList<>();
            if (intAttrName.getField() != null) {
                RealmFieldValueSupport.add(intAttrName, realm, values);
            } else if (intAttrName.getSchemaInfo() != null) {
                RealmSchemaValueSupport.add(owner, intAttrName, realm, values);
            }

            LogSupport.debug("Internal values: {}", values);
            return TransformerSupport.beforePropagation(owner, item, realm, schemaType, values);
        }
    }

    private static final class RealmFieldValueSupport {

        private RealmFieldValueSupport() {
        }

        static void add(
                final IntAttrName intAttrName,
                final Realm realm,
                final List<PlainAttrValue> values) {

            switch (intAttrName.getField()) {
                case "key" -> addString(realm.getKey(), values);
                case "name" -> addString(realm.getName(), values);
                case "fullPath" -> addString(realm.getFullPath(), values);
                default -> {
                }
            }
        }

        private static void addString(final String value, final List<PlainAttrValue> values) {
            PlainAttrValue attrValue = new PlainAttrValue();
            attrValue.setStringValue(value);
            values.add(attrValue);
        }
    }

    private static final class RealmSchemaValueSupport {

        private RealmSchemaValueSupport() {
        }

        static void add(
                final DefaultMappingManager owner,
                final IntAttrName intAttrName,
                final Realm realm,
                final List<PlainAttrValue> values) {

            switch (intAttrName.getSchemaInfo().type()) {
                case PLAIN -> addPlain(intAttrName, realm, values);
                case DERIVED -> addDerived(owner, intAttrName, realm, values);
                default -> {
                }
            }
        }

        private static void addPlain(
                final IntAttrName intAttrName,
                final Realm realm,
                final List<PlainAttrValue> values) {

            realm.getPlainAttr(intAttrName.getSchemaInfo().schema().getKey()).
                    ifPresent(attr -> AnySchemaValueSupport.addClonedValues(attr, values));
        }

        private static void addDerived(
                final DefaultMappingManager owner,
                final IntAttrName intAttrName,
                final Realm realm,
                final List<PlainAttrValue> values) {

            Optional.ofNullable(owner.derAttrHandler.getValue(
                    realm, (DerSchema) intAttrName.getSchemaInfo().schema())).
                    ifPresent(derValue -> {
                        PlainAttrValue attrValue = new PlainAttrValue();
                        attrValue.setStringValue(derValue);
                        values.add(attrValue);
                    });
        }
    }

    private static final class PullTransformationSupport {

        private PullTransformationSupport() {
        }

        static List<Object> forAny(
                final DefaultMappingManager owner,
                final Item item,
                final Attribute attr,
                final AnyTO anyTO) {

            List<Object> values = attr == null ? null : attr.getValue();
            if (attr != null) {
                for (ItemTransformer transformer
                        : MappingUtils.getItemTransformers(
                                AuthContextUtils.getDomain(), item, owner.getTransformers(item))) {

                    values = transformer.beforePull(item, anyTO, values);
                }
            }
            return Optional.ofNullable(values).orElseGet(List::of);
        }

        static List<Object> forRealm(
                final DefaultMappingManager owner,
                final Item item,
                final Attribute attr,
                final RealmTO realmTO) {

            List<Object> values = attr == null ? null : attr.getValue();
            if (attr != null) {
                for (ItemTransformer transformer
                        : MappingUtils.getItemTransformers(
                                AuthContextUtils.getDomain(), item, owner.getTransformers(item))) {

                    values = transformer.beforePull(item, realmTO, values);
                }
            }
            return Optional.ofNullable(values).orElseGet(List::of);
        }
    }

    private static final class AnyToSetSupport {

        private AnyToSetSupport() {
        }

        static void set(
                final DefaultMappingManager owner,
                final Item item,
                final Attribute attr,
                final AnyTO anyTO) {

            List<Object> values = PullTransformationSupport.forAny(owner, item, attr, anyTO);

            IntAttrName intAttrName;
            try {
                intAttrName = owner.intAttrNameParser.parse(
                        item.getIntAttrName(), AnyTypeKind.fromTOClass(anyTO.getClass()));
            } catch (ParseException e) {
                LogSupport.error(INVALID_INT_ATTR_NAME, item.getIntAttrName(), e);
                return;
            }

            if (intAttrName.getField() != null && !values.isEmpty() && values.getFirst() != null) {
                AnyToFieldSupport.set(intAttrName.getField(), values.getFirst(), anyTO);
            } else if (intAttrName.getSchemaInfo() != null && attr != null) {
                AnyToSchemaSupport.set(owner, intAttrName, values, anyTO);
            }
        }
    }

    private static final class AnyToFieldSupport {

        private AnyToFieldSupport() {
        }

        static void set(final String field, final Object value, final AnyTO anyTO) {
            switch (field) {
                case "password" -> setPassword(value, anyTO);
                case "username" -> setUsername(value, anyTO);
                case "name" -> setName(value, anyTO);
                case "mustChangePassword" -> setMustChangePassword(value, anyTO);
                case "uManager" -> anyTO.setUManager(value.toString());
                case "gManager" -> anyTO.setGManager(value.toString());
                default -> {
                }
            }
        }

        private static void setPassword(final Object value, final AnyTO anyTO) {
            if (anyTO instanceof UserTO userTO) {
                userTO.setPassword(ConnObjectUtils.getPassword(value));
            }
        }

        private static void setUsername(final Object value, final AnyTO anyTO) {
            if (anyTO instanceof UserTO userTO) {
                userTO.setUsername(value.toString());
            }
        }

        private static void setName(final Object value, final AnyTO anyTO) {
            switch (anyTO) {
                case GroupTO groupTO -> groupTO.setName(value.toString());
                case AnyObjectTO anyObjectTO -> anyObjectTO.setName(value.toString());
                default -> {
                }
            }
        }

        private static void setMustChangePassword(final Object value, final AnyTO anyTO) {
            if (anyTO instanceof UserTO userTO) {
                userTO.setMustChangePassword(BooleanUtils.toBoolean(value.toString()));
            }
        }
    }

    private record MembershipContext(GroupableRelatableTO groupableTO, Group group) {
    }

    private static final class AnyToSchemaSupport {

        private AnyToSchemaSupport() {
        }

        static void set(
                final DefaultMappingManager owner,
                final IntAttrName intAttrName,
                final List<Object> values,
                final AnyTO anyTO) {

            Optional<MembershipContext> membership = membership(owner, intAttrName, anyTO);
            switch (intAttrName.getSchemaInfo().type()) {
                case PLAIN -> addPlain(intAttrName, values, anyTO, membership);
                case DERIVED -> addDerived(intAttrName, anyTO, membership);
                default -> {
                }
            }
        }

        private static Optional<MembershipContext> membership(
                final DefaultMappingManager owner,
                final IntAttrName intAttrName,
                final AnyTO anyTO) {

            if (!(anyTO instanceof GroupableRelatableTO groupableTO)
                    || intAttrName.getMembership() == null) {

                return Optional.empty();
            }

            return owner.groupDAO.findByName(intAttrName.getMembership()).
                    map(group -> new MembershipContext(groupableTO, group));
        }

        private static void addPlain(
                final IntAttrName intAttrName,
                final List<Object> values,
                final AnyTO anyTO,
                final Optional<MembershipContext> membership) {

            Attr attrTO = new Attr();
            attrTO.setSchema(intAttrName.getSchemaInfo().schema().getKey());
            PlainSchema schema = (PlainSchema) intAttrName.getSchemaInfo().schema();
            addValues(attrTO, schema, values);

            if (membership.isEmpty()) {
                anyTO.getPlainAttrs().add(attrTO);
            } else {
                getOrCreateMembership(membership.get()).getPlainAttrs().add(attrTO);
            }
        }

        private static void addDerived(
                final IntAttrName intAttrName,
                final AnyTO anyTO,
                final Optional<MembershipContext> membership) {

            Attr attrTO = new Attr();
            attrTO.setSchema(intAttrName.getSchemaInfo().schema().getKey());

            if (membership.isEmpty()) {
                anyTO.getDerAttrs().add(attrTO);
            } else {
                getOrCreateMembership(membership.get()).getDerAttrs().add(attrTO);
            }
        }

        private static void addValues(
                final Attr attrTO,
                final PlainSchema schema,
                final List<Object> values) {

            for (Object value : values) {
                addValue(attrTO, schema, value);
            }
        }

        private static void addValue(final Attr attrTO, final PlainSchema schema, final Object value) {
            AttrSchemaType schemaType = schema == null ? AttrSchemaType.String : schema.getType();
            if (value == null) {
                return;
            }
            if (schemaType == AttrSchemaType.Binary) {
                attrTO.getValues().add(Base64.getEncoder().encodeToString((byte[]) value));
            } else {
                attrTO.getValues().add(value.toString());
            }
        }

        private static MembershipTO getOrCreateMembership(final MembershipContext context) {
            return context.groupableTO().getMembership(context.group().getKey()).orElseGet(() -> {
                MembershipTO newMemb = new MembershipTO.Builder(context.group().getKey()).build();
                context.groupableTO().getMemberships().add(newMemb);
                return newMemb;
            });
        }
    }

    private static final class RealmToSetSupport {

        private RealmToSetSupport() {
        }

        static void set(
                final DefaultMappingManager owner,
                final Item item,
                final Attribute attr,
                final RealmTO realmTO) {

            List<Object> values = PullTransformationSupport.forRealm(owner, item, attr, realmTO);

            IntAttrName intAttrName;
            try {
                intAttrName = owner.intAttrNameParser.parse(item.getIntAttrName());
            } catch (ParseException e) {
                LogSupport.error(INVALID_INT_ATTR_NAME, item.getIntAttrName(), e);
                return;
            }

            if (intAttrName.getField() != null) {
                RealmToFieldSupport.set(owner, intAttrName.getField(), values, realmTO);
            } else if (intAttrName.getSchemaInfo() != null && attr != null) {
                RealmToSchemaSupport.set(intAttrName, values, realmTO);
            }
        }
    }

    private static final class RealmToFieldSupport {

        private RealmToFieldSupport() {
        }

        static void set(
                final DefaultMappingManager owner,
                final String field,
                final List<Object> values,
                final RealmTO realmTO) {

            switch (field) {
                case "name" -> realmTO.setName(
                        values.isEmpty() || values.getFirst() == null
                                ? null
                                : values.getFirst().toString());

                case "fullpath" -> setFullPath(owner, values, realmTO);
                default -> {
                }
            }
        }

        private static void setFullPath(
                final DefaultMappingManager owner,
                final List<Object> values,
                final RealmTO realmTO) {

            String parentFullPath = StringUtils.substringBeforeLast(values.getFirst().toString(), "/");
            owner.realmSearchDAO.findByFullPath(parentFullPath).ifPresentOrElse(
                    parent -> realmTO.setParent(parent.getFullPath()),
                    () -> LogSupport.warn("Could not find Realm with path {}, ignoring", parentFullPath));
        }
    }

    private static final class RealmToSchemaSupport {

        private RealmToSchemaSupport() {
        }

        static void set(
                final IntAttrName intAttrName,
                final List<Object> values,
                final RealmTO realmTO) {

            switch (intAttrName.getSchemaInfo().type()) {
                case PLAIN -> addPlain(intAttrName, values, realmTO);
                case DERIVED -> addDerived(intAttrName, realmTO);
                default -> {
                }
            }
        }

        private static void addPlain(
                final IntAttrName intAttrName,
                final List<Object> values,
                final RealmTO realmTO) {

            Attr attrTO = new Attr();
            attrTO.setSchema(intAttrName.getSchemaInfo().schema().getKey());
            PlainSchema schema = (PlainSchema) intAttrName.getSchemaInfo().schema();
            AnyToSchemaSupport.addValues(attrTO, schema, values);
            realmTO.getPlainAttrs().add(attrTO);
        }

        private static void addDerived(final IntAttrName intAttrName, final RealmTO realmTO) {
            Attr attrTO = new Attr();
            attrTO.setSchema(intAttrName.getSchemaInfo().schema().getKey());
            realmTO.getDerAttrs().add(attrTO);
        }
    }

}
