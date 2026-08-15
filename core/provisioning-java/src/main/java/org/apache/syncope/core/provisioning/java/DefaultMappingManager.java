package org.apache.syncope.core.provisioning.java;

import java.text.ParseException;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.jexl3.JexlContext;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.mutable.Mutable;
import org.apache.commons.lang3.mutable.MutableObject;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.syncope.common.lib.Attr;
import org.apache.syncope.common.lib.to.AnyObjectTO;
import org.apache.syncope.common.lib.to.AnyTO;
import org.apache.syncope.common.lib.to.GroupTO;
import org.apache.syncope.common.lib.to.GroupableRelatableTO;
import org.apache.syncope.common.lib.to.Item;
import org.apache.syncope.common.lib.to.Mapping;
import org.apache.syncope.common.lib.to.MembershipTO;
import org.apache.syncope.common.lib.to.OrgUnit;
import org.apache.syncope.common.lib.to.Provision;
import org.apache.syncope.common.lib.to.RealmTO;
import org.apache.syncope.common.lib.to.UserTO;
import org.apache.syncope.common.lib.types.AnyTypeKind;
import org.apache.syncope.common.lib.types.AttrSchemaType;
import org.apache.syncope.core.persistence.api.EncryptorManager;
import org.apache.syncope.core.persistence.api.dao.AnyObjectDAO;
import org.apache.syncope.core.persistence.api.dao.GroupDAO;
import org.apache.syncope.core.persistence.api.dao.ImplementationDAO;
import org.apache.syncope.core.persistence.api.dao.RealmSearchDAO;
import org.apache.syncope.core.persistence.api.dao.RelationshipTypeDAO;
import org.apache.syncope.core.persistence.api.dao.UserDAO;
import org.apache.syncope.core.persistence.api.entity.Any;
import org.apache.syncope.core.persistence.api.entity.DerSchema;
import org.apache.syncope.core.persistence.api.entity.ExternalResource;
import org.apache.syncope.core.persistence.api.entity.Groupable;
import org.apache.syncope.core.persistence.api.entity.Implementation;
import org.apache.syncope.core.persistence.api.entity.Membership;
import org.apache.syncope.core.persistence.api.entity.PlainAttr;
import org.apache.syncope.core.persistence.api.entity.PlainAttrValue;
import org.apache.syncope.core.persistence.api.entity.PlainSchema;
import org.apache.syncope.core.persistence.api.entity.Realm;
import org.apache.syncope.core.persistence.api.entity.Relatable;
import org.apache.syncope.core.persistence.api.entity.Relationship;
import org.apache.syncope.core.persistence.api.entity.RelationshipType;
import org.apache.syncope.core.persistence.api.entity.group.Group;
import org.apache.syncope.core.persistence.api.entity.user.Account;
import org.apache.syncope.core.persistence.api.entity.user.LinkedAccount;
import org.apache.syncope.core.persistence.api.entity.user.User;
import org.apache.syncope.core.persistence.api.utils.FormatUtils;
import org.apache.syncope.core.provisioning.api.AccountGetter;
import org.apache.syncope.core.provisioning.api.DerAttrHandler;
import org.apache.syncope.core.provisioning.api.IntAttrName;
import org.apache.syncope.core.provisioning.api.IntAttrNameParser;
import org.apache.syncope.core.provisioning.api.MappingManager;
import org.apache.syncope.core.provisioning.api.PlainAttrGetter;
import org.apache.syncope.core.provisioning.api.data.ItemTransformer;
import org.apache.syncope.core.provisioning.api.jexl.JexlContextBuilder;
import org.apache.syncope.core.provisioning.api.jexl.JexlTools;
import org.apache.syncope.core.provisioning.java.utils.ConnObjectUtils;
import org.apache.syncope.core.provisioning.java.utils.MappingUtils;
import org.apache.syncope.core.spring.security.AuthContextUtils;
import org.identityconnectors.framework.common.FrameworkUtil;
import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.AttributeBuilder;
import org.identityconnectors.framework.common.objects.AttributeUtil;
import org.identityconnectors.framework.common.objects.Name;
import org.identityconnectors.framework.common.objects.OperationalAttributes;
import org.identityconnectors.framework.common.objects.Uid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

public class DefaultMappingManager implements MappingManager {

    protected static final Logger LOG = LoggerFactory.getLogger(DefaultMappingManager.class);

    protected static final String MUST_CHANGE_PASSWORD = "mustChangePassword";

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
            LOG.debug("Add connObjectKey [{}] as {}", connObjectKey, Name.NAME);
            name = new Name(connObjectKey);
        } else {
            LOG.debug("Add connObjectLink [{}] as {}", evalConnObjectLink, Name.NAME);
            name = new Name(evalConnObjectLink);

            // connObjectKey not propagated: it will be used to set the value for __UID__ attribute
            LOG.debug("connObjectKey [{}] will be used as {}", connObjectKey, Uid.NAME);
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


    @SuppressWarnings("java:S107")
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
            LOG.debug("Missing connObjectKey for {}", any.getType().getKey());
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
            LOG.debug("Missing connObjectKey for Realms");
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

        LOG.debug("Preparing resource attributes for {} with provision {} for attributes {}",
                any, provision, any.getPlainAttrs());

        Set<Attribute> attributes = new HashSet<>();
        Mutable<String> connObjectKeyValue = new MutableObject<>();

        MappingUtils.getPropagationItems(provision.getMapping().getItems().stream()).forEach(item -> {
            LOG.debug("Processing expression '{}'", item.getIntAttrName());

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
            } catch (Exception e) {
                LOG.error("Expression '{}' processing failed", item.getIntAttrName(), e);
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

        LOG.debug("Preparing resource attributes for linked account {} of user {} with provision {} "
                        + "for user attributes {} with override {}",
                account, user, provision, user.getPlainAttrs(), account.getPlainAttrs());

        Set<Attribute> attributes = new HashSet<>();

        MappingUtils.getPropagationItems(provision.getMapping().getItems().stream()).
                forEach(item -> prepareLinkedAccountItem(user, account, password, provision, attributes, item));

        String connObjectKey = account.getConnObjectKeyValue();
        MappingUtils.getConnObjectKeyItem(provision).
                ifPresent(item -> addLinkedAccountConnObjectKey(user, provision, attributes, connObjectKey, item));

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

    private void prepareLinkedAccountItem(
            final User user,
            final LinkedAccount account,
            final String password,
            final Provision provision,
            final Set<Attribute> attributes,
            final Item item) {

        LOG.debug("Processing expression '{}'", item.getIntAttrName());

        try {
            processPreparedAttr(
                    prepareAttr(
                            account.getResource(),
                            provision,
                            item,
                            user,
                            password,
                            acct -> account.getUsername() == null ? AccountGetter.DEFAULT.apply(acct) : account,
                            acct -> account.getPassword() == null ? AccountGetter.DEFAULT.apply(acct) : account,
                            (attributable, schema) -> {
                                PlainAttr result = null;
                                if (attributable instanceof User) {
                                    result = account.getPlainAttr(schema).orElse(null);
                                }
                                if (result == null) {
                                    result = PlainAttrGetter.DEFAULT.apply(attributable, schema);
                                }
                                return result;
                            }),
                    attributes);
        } catch (Exception e) {
            LOG.error("Expression '{}' processing failed", item.getIntAttrName(), e);
        }
    }

    private void addLinkedAccountConnObjectKey(
            final User user,
            final Provision provision,
            final Set<Attribute> attributes,
            final String connObjectKey,
            final Item connObjectKeyItem) {

        Attribute connObjectKeyExtAttr = AttributeUtil.find(connObjectKeyItem.getExtAttrName(), attributes);
        if (connObjectKeyExtAttr != null) {
            attributes.remove(connObjectKeyExtAttr);
            attributes.add(AttributeBuilder.build(connObjectKeyItem.getExtAttrName(), connObjectKey));
        }

        Name name = evaluateNAME(user, provision, connObjectKey);
        attributes.add(name);
        if (!connObjectKey.equals(name.getNameValue()) && connObjectKeyExtAttr == null) {
            attributes.add(AttributeBuilder.build(connObjectKeyItem.getExtAttrName(), connObjectKey));
        }
    }

    @Override
    public PreparedAttrs prepareAttrsFromRealm(final Realm realm, final ExternalResource resource) {
        if (resource.getOrgUnit() == null) {
            LOG.error("No mapping configured for Realms");
            return new PreparedAttrs(null, Set.of());
        }

        LOG.debug("Preparing resource attributes for {} with orgUnit {}", realm, resource.getOrgUnit());

        Set<Attribute> attributes = new HashSet<>();
        Mutable<String> connObjectKeyValue = new MutableObject<>();

        MappingUtils.getPropagationItems(resource.getOrgUnit().getItems().stream()).forEach(item -> {
            LOG.debug("Processing expression '{}'", item.getIntAttrName());

            try {
                processPreparedAttr(
                        prepareAttr(
                                resource,
                                item,
                                realm),
                        attributes).ifPresent(connObjectKeyValue::setValue);
            } catch (Exception e) {
                LOG.error("Expression '{}' processing failed", item.getIntAttrName(), e);
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

    protected Optional<String> decodePassword(final Account account) {
        try {
            return Optional.of(encryptorManager.getInstance().
                    decode(account.getPassword(), account.getCipherAlgorithm()));
        } catch (Exception e) {
            LOG.error("Could not decode password for {}", account, e);
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
            LOG.error("Invalid intAttrName '{}' specified, ignoring", item.getIntAttrName(), e);
            return null;
        }

        AttrSchemaType schemaType = getAttrSchemaType(intAttrName);
        IntValues intValues = doGetIntValues(
                new MappingContext(resource, provision),
                item,
                intAttrName,
                schemaType,
                any,
                usernameAccountGetter,
                plainAttrGetter);
        schemaType = intValues.attrSchemaType();
        List<PlainAttrValue> values = intValues.values();

        logMapping(item, intAttrName, schemaType, values);

        List<Object> objValues = toObjectValues(intAttrName, schemaType, values);
        return buildPreparedAttr(item, any, password, passwordAccountGetter, objValues);
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
            LOG.error("Invalid intAttrName '{}' specified, ignoring", item.getIntAttrName(), e);
            return null;
        }

        AttrSchemaType schemaType = getAttrSchemaType(intAttrName);
        IntValues intValues = doGetIntValues(resource, item, intAttrName, schemaType, realm);
        schemaType = intValues.attrSchemaType();
        List<PlainAttrValue> values = intValues.values();

        logMapping(item, intAttrName, schemaType, values);

        List<Object> objValues = toObjectValues(intAttrName, schemaType, values);
        return buildPreparedAttr(item, objValues);
    }

    private AttrSchemaType getAttrSchemaType(final IntAttrName intAttrName) {
        return Optional.ofNullable(intAttrName.getSchemaInfo()).
                filter(schemaInfo -> schemaInfo.schema() instanceof PlainSchema).
                map(schemaInfo -> schemaInfo.schema().getType()).
                orElse(AttrSchemaType.String);
    }

    private void logMapping(
            final Item item,
            final IntAttrName intAttrName,
            final AttrSchemaType schemaType,
            final List<PlainAttrValue> values) {

        LOG.debug(
                """
                  Define mapping for: 
                  * Item {}
                  * Schema {}
                  * ClassType {}
                  * AttrSchemaType {}
                  * Values {}""",
                item, intAttrName.getSchemaInfo(), schemaType.getType().getName(), schemaType, values);
    }

    private List<Object> toObjectValues(
            final IntAttrName intAttrName,
            final AttrSchemaType schemaType,
            final List<PlainAttrValue> values) {

        List<Object> objValues = new ArrayList<>();
        values.forEach(value -> objValues.add(toObjectValue(intAttrName, schemaType, value)));
        return objValues;
    }

    private Object toObjectValue(
            final IntAttrName intAttrName,
            final AttrSchemaType schemaType,
            final PlainAttrValue value) {

        if (schemaType == AttrSchemaType.Encrypted
                && intAttrName.getSchemaInfo().schema() instanceof PlainSchema schema) {

            return decodePlainAttrValue(intAttrName, schema, value);
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

    private Object decodePlainAttrValue(
            final IntAttrName intAttrName,
            final PlainSchema schema,
            final PlainAttrValue value) {

        String decoded = null;
        try {
            decoded = encryptorManager.getInstance(schema.getSecretKey()).
                    decode(value.getStringValue(), schema.getCipherAlgorithm());
        } catch (Exception e) {
            LOG.warn("Could not decode value for {} with algorithm {}",
                    intAttrName.getSchemaInfo(), schema.getCipherAlgorithm(), e);
        }
        return Optional.ofNullable(decoded).orElse(value.getStringValue());
    }

    private PreparedAttr buildPreparedAttr(
            final Item item,
            final Any any,
            final String password,
            final AccountGetter passwordAccountGetter,
            final List<Object> objValues) {

        if (item.isConnObjectKey()) {
            return new PreparedAttr(objValues.isEmpty() ? null : objValues.getFirst().toString(), null);
        }

        if (item.isPassword() && any instanceof User user) {
            return getPasswordAttrValue(passwordAccountGetter.apply(user), password).
                    map(passwordAttrValue -> new PreparedAttr(
                            null, AttributeBuilder.buildPassword(passwordAttrValue.toCharArray()))).
                    orElse(null);
        }

        return buildPreparedAttr(item, objValues);
    }

    private PreparedAttr buildPreparedAttr(final Item item, final List<Object> objValues) {
        if (item.isConnObjectKey()) {
            return new PreparedAttr(objValues.isEmpty() ? null : objValues.getFirst().toString(), null);
        }

        if (objValues.isEmpty()) {
            return new PreparedAttr(null, AttributeBuilder.build(item.getExtAttrName()));
        }

        if (OperationalAttributes.PASSWORD_NAME.equals(item.getExtAttrName())) {
            return new PreparedAttr(
                    null,
                    AttributeBuilder.buildPassword(objValues.iterator().next().toString().toCharArray()));
        }

        return new PreparedAttr(null, AttributeBuilder.build(item.getExtAttrName(), objValues));
    }

    private record MappingContext(ExternalResource resource, Provision provision) {
    }

    private record ReferenceContext(
            List<Any> references,
            Relationship<?, ?> relationship,
            Membership<?> membership) {
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

        return doGetIntValues(
                new MappingContext(resource, provision),
                item,
                intAttrName,
                schemaType,
                any,
                usernameAccountGetter,
                plainAttrGetter);
    }

    private IntValues doGetIntValues(
            final MappingContext context,
            final Item item,
            final IntAttrName intAttrName,
            final AttrSchemaType schemaType,
            final Any any,
            final AccountGetter usernameAccountGetter,
            final PlainAttrGetter plainAttrGetter) {

        LOG.debug("Get internal values for {} as '{}' on {}", any, item.getIntAttrName(), context.resource());

        ReferenceContext referenceContext = resolveReferenceContext(intAttrName, any);
        if (referenceContext.references().isEmpty()) {
            LOG.warn("Could not determine the reference instance for {}", item.getIntAttrName());
            return new IntValues(schemaType, List.of());
        }

        List<PlainAttrValue> values = new ArrayList<>();
        for (Any ref : referenceContext.references()) {
            addReferenceValues(
                    context,
                    intAttrName,
                    ref,
                    usernameAccountGetter,
                    plainAttrGetter,
                    referenceContext,
                    values);
        }

        LOG.debug("Internal values: {}", values);

        IntValues transformed = transformBeforePropagation(item, any, new IntValues(schemaType, values));
        LOG.debug("Transformed values: {}", values);
        return transformed;
    }

    private ReferenceContext resolveReferenceContext(final IntAttrName intAttrName, final Any any) {
        List<Any> references = new ArrayList<>();
        if (intAttrName.getExternalGroup() == null
                && intAttrName.getExternalAnyObject() == null
                && intAttrName.getExternalUser() == null) {

            references.add(any);
        }

        Relationship<?, ?> relationship = null;
        Membership<?> membership = null;

        if (intAttrName.getExternalUser() != null) {
            userDAO.findByUsername(intAttrName.getExternalUser()).ifPresentOrElse(
                    references::add,
                    () -> LOG.warn("Could not find user {}, ignoring", intAttrName.getExternalUser()));
        } else if (intAttrName.getExternalGroup() != null) {
            groupDAO.findByName(intAttrName.getExternalGroup()).ifPresentOrElse(
                    references::add,
                    () -> LOG.warn("Could not find group {}, ignoring", intAttrName.getExternalGroup()));
        } else if (intAttrName.getExternalAnyObject() != null) {
            references.addAll(anyObjectDAO.findByName(intAttrName.getExternalAnyObject()));
        } else if (intAttrName.getMembership() != null && any instanceof Groupable<?, ?, ?> groupable) {
            membership = groupDAO.findByName(intAttrName.getMembership()).
                    flatMap(group -> groupable.getMembership(group.getKey())).
                    orElse(null);
        } else if (intAttrName.getRelationshipInfo() != null && any instanceof Relatable<?, ?> relatable) {
            relationship = resolveRelationship(intAttrName, relatable);
        }

        return new ReferenceContext(references, relationship, membership);
    }

    private Relationship<?, ?> resolveRelationship(final IntAttrName intAttrName, final Relatable<?, ?> relatable) {
        RelationshipType relationshipType = relationshipTypeDAO.findById(
                intAttrName.getRelationshipInfo().type()).orElse(null);
        if (relationshipType == null) {
            LOG.warn("Could not find relationship type {}, ignoring", intAttrName.getRelationshipInfo().type());
            return null;
        }

        return anyObjectDAO.findByName(
                        relationshipType.getRightEndAnyType().getKey(), intAttrName.getRelationshipInfo().anyObject()).
                flatMap(otherEnd -> relatable.getRelationship(relationshipType, otherEnd.getKey())).
                orElse(null);
    }

    private void addReferenceValues(
            final MappingContext context,
            final IntAttrName intAttrName,
            final Any ref,
            final AccountGetter usernameAccountGetter,
            final PlainAttrGetter plainAttrGetter,
            final ReferenceContext referenceContext,
            final List<PlainAttrValue> values) {

        if (intAttrName.getField() != null) {
            addFieldValue(context, intAttrName.getField(), ref, usernameAccountGetter, values);
        } else if (intAttrName.getSchemaInfo() != null) {
            addSchemaValues(intAttrName, ref, plainAttrGetter, referenceContext, values);
        }
    }

    private void addFieldValue(
            final MappingContext context,
            final String field,
            final Any ref,
            final AccountGetter usernameAccountGetter,
            final List<PlainAttrValue> values) {

        if ("password".equals(field)) {
            return;
        }

        switch (field) {
            case "key" -> addStringValue(ref.getKey(), values);
            case "username" -> addUsernameValue(ref, usernameAccountGetter, values);
            case "realm" -> addStringValue(ref.getRealm().getFullPath(), values);
            case "uManager", "gManager" -> addManagerValue(context, ref, values);
            case "suspended" -> addSuspendedValue(ref, values);
            case MUST_CHANGE_PASSWORD -> addMustChangePasswordValue(ref, values);
            default -> addReflectedFieldValue(field, ref, values);
        }
    }

    private void addStringValue(final String value, final List<PlainAttrValue> values) {
        PlainAttrValue attrValue = new PlainAttrValue();
        attrValue.setStringValue(value);
        values.add(attrValue);
    }

    private void addUsernameValue(
            final Any ref,
            final AccountGetter usernameAccountGetter,
            final List<PlainAttrValue> values) {

        if (ref instanceof Account account) {
            addStringValue(usernameAccountGetter.apply(account).getUsername(), values);
        }
    }

    private void addManagerValue(
            final MappingContext context,
            final Any ref,
            final List<PlainAttrValue> values) {

        Mapping uMappingTO = context.provision().getAnyType().equals(AnyTypeKind.USER.name())
                ? context.provision().getMapping()
                : null;
        Mapping gMappingTO = context.provision().getAnyType().equals(AnyTypeKind.GROUP.name())
                ? context.provision().getMapping()
                : null;

        String managerValue = null;
        if (ref.getUManager() != null && uMappingTO != null) {
            managerValue = getManagerValue(context.resource(), context.provision(), ref.getUManager());
        }
        if (ref.getGManager() != null && gMappingTO != null) {
            managerValue = getManagerValue(context.resource(), context.provision(), ref.getGManager());
        }

        if (StringUtils.isNotBlank(managerValue)) {
            addStringValue(managerValue, values);
        }
    }

    private void addSuspendedValue(final Any ref, final List<PlainAttrValue> values) {
        if (ref instanceof User user) {
            PlainAttrValue attrValue = new PlainAttrValue();
            attrValue.setBooleanValue(user.isSuspended());
            values.add(attrValue);
        }
    }

    private void addMustChangePasswordValue(final Any ref, final List<PlainAttrValue> values) {
        if (ref instanceof User user) {
            PlainAttrValue attrValue = new PlainAttrValue();
            attrValue.setBooleanValue(user.isMustChangePassword());
            values.add(attrValue);
        }
    }

    private void addReflectedFieldValue(
            final String field,
            final Any ref,
            final List<PlainAttrValue> values) {

        PlainAttrValue attrValue = new PlainAttrValue();
        try {
            Object fieldValue = FieldUtils.readField(ref, field, true);
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
            values.add(attrValue);
        } catch (Exception e) {
            LOG.error("Could not read value of '{}' from {}", field, ref, e);
        }
    }

    private void addSchemaValues(
            final IntAttrName intAttrName,
            final Any ref,
            final PlainAttrGetter plainAttrGetter,
            final ReferenceContext referenceContext,
            final List<PlainAttrValue> values) {

        switch (intAttrName.getSchemaInfo().type()) {
            case PLAIN -> addPlainSchemaValues(intAttrName, ref, plainAttrGetter, referenceContext, values);
            case DERIVED -> addDerivedSchemaValue(intAttrName, ref, referenceContext, values);
        }
    }

    private void addPlainSchemaValues(
            final IntAttrName intAttrName,
            final Any ref,
            final PlainAttrGetter plainAttrGetter,
            final ReferenceContext referenceContext,
            final List<PlainAttrValue> values) {

        PlainAttr attr = resolvePlainAttr(intAttrName, ref, plainAttrGetter, referenceContext);
        if (attr == null) {
            return;
        }

        if (attr.getUniqueValue() != null) {
            values.add(clonePlainAttrValue(attr.getUniqueValue()));
        } else if (attr.getValues() != null) {
            attr.getValues().forEach(value -> values.add(clonePlainAttrValue(value)));
        }
    }

    private PlainAttr resolvePlainAttr(
            final IntAttrName intAttrName,
            final Any ref,
            final PlainAttrGetter plainAttrGetter,
            final ReferenceContext referenceContext) {

        String schemaKey = intAttrName.getSchemaInfo().schema().getKey();
        if (referenceContext.membership() == null && referenceContext.relationship() == null) {
            return plainAttrGetter.apply(ref, schemaKey);
        }
        if (referenceContext.membership() == null) {
            return ((Relatable<?, ?>) ref).getPlainAttr(schemaKey, referenceContext.relationship()).orElse(null);
        }
        return ((Groupable<?, ?, ?>) ref).getPlainAttr(schemaKey, referenceContext.membership()).orElse(null);
    }

    private void addDerivedSchemaValue(
            final IntAttrName intAttrName,
            final Any ref,
            final ReferenceContext referenceContext,
            final List<PlainAttrValue> values) {

        DerSchema derSchema = (DerSchema) intAttrName.getSchemaInfo().schema();
        String derValue = resolveDerivedValue(ref, derSchema, referenceContext);
        if (derValue != null) {
            addStringValue(derValue, values);
        }
    }

    private String resolveDerivedValue(
            final Any ref,
            final DerSchema derSchema,
            final ReferenceContext referenceContext) {

        if (referenceContext.membership() == null && referenceContext.relationship() == null) {
            return derAttrHandler.getValue(ref, derSchema);
        }
        if (referenceContext.membership() == null) {
            return derAttrHandler.getValue((Relatable<?, ?>) ref, referenceContext.relationship(), derSchema);
        }
        return derAttrHandler.getValue((Groupable<?, ?, ?>) ref, referenceContext.membership(), derSchema);
    }

    private IntValues transformBeforePropagation(
            final Item item,
            final Any any,
            final IntValues intValues) {

        IntValues transformed = intValues;
        for (ItemTransformer transformer
                : MappingUtils.getItemTransformers(AuthContextUtils.getDomain(), item, getTransformers(item))) {

            transformed = transformer.beforePropagation(
                    item, any, transformed.attrSchemaType(), transformed.values());
        }
        return transformed;
    }

    @Transactional(readOnly = true)
    @Override
    public IntValues getIntValues(
            final ExternalResource resource,
            final Item item,
            final IntAttrName intAttrName,
            final AttrSchemaType schemaType,
            final Realm realm) {

        return doGetIntValues(resource, item, intAttrName, schemaType, realm);
    }

    private IntValues doGetIntValues(
            final ExternalResource resource,
            final Item item,
            final IntAttrName intAttrName,
            final AttrSchemaType schemaType,
            final Realm realm) {

        LOG.debug("Get internal values for {} as '{}' on {}", realm, item.getIntAttrName(), resource);

        List<PlainAttrValue> values = new ArrayList<>();
        if (intAttrName.getField() != null) {
            addRealmFieldValue(intAttrName.getField(), realm, values);
        } else if (intAttrName.getSchemaInfo() != null) {
            addRealmSchemaValues(intAttrName, realm, values);
        }

        LOG.debug("Internal values: {}", values);

        IntValues transformed = transformBeforePropagation(item, realm, new IntValues(schemaType, values));
        LOG.debug("Transformed values: {}", values);
        return transformed;
    }

    private void addRealmFieldValue(
            final String field,
            final Realm realm,
            final List<PlainAttrValue> values) {

        switch (field) {
            case "key" -> addStringValue(realm.getKey(), values);
            case "name" -> addStringValue(realm.getName(), values);
            case "fullPath" -> addStringValue(realm.getFullPath(), values);
        }
    }

    private void addRealmSchemaValues(
            final IntAttrName intAttrName,
            final Realm realm,
            final List<PlainAttrValue> values) {

        switch (intAttrName.getSchemaInfo().type()) {
            case PLAIN -> realm.getPlainAttr(intAttrName.getSchemaInfo().schema().getKey()).
                    ifPresent(attr -> addPlainAttrValues(attr, values));
            case DERIVED -> Optional.ofNullable(
                            derAttrHandler.getValue(realm, (DerSchema) intAttrName.getSchemaInfo().schema())).
                    ifPresent(derValue -> addStringValue(derValue, values));
        }
    }

    private void addPlainAttrValues(final PlainAttr attr, final List<PlainAttrValue> values) {
        if (attr.getUniqueValue() != null) {
            values.add(clonePlainAttrValue(attr.getUniqueValue()));
        } else if (attr.getValues() != null) {
            attr.getValues().forEach(value -> values.add(clonePlainAttrValue(value)));
        }
    }

    private IntValues transformBeforePropagation(
            final Item item,
            final Realm realm,
            final IntValues intValues) {

        IntValues transformed = intValues;
        for (ItemTransformer transformer
                : MappingUtils.getItemTransformers(AuthContextUtils.getDomain(), item, getTransformers(item))) {

            transformed = transformer.beforePropagation(
                    item, realm, transformed.attrSchemaType(), transformed.values());
        }
        return transformed;
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
            LOG.error("Unable to locate conn object key item for {}", any.getType().getKey());
            return Optional.empty();
        }

        Item item = connObjectKeyItem.get();
        IntValues intValues;
        try {
            intValues = doGetIntValues(
                    new MappingContext(resource, provision),
                    item,
                    intAttrNameParser.parse(item.getIntAttrName(), any.getType().getKind()),
                    AttrSchemaType.String,
                    any,
                    AccountGetter.DEFAULT,
                    PlainAttrGetter.DEFAULT);
        } catch (ParseException e) {
            LOG.error("Invalid intAttrName '{}' specified, ignoring", item.getIntAttrName(), e);
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
            LOG.error("No mapping configured for Realms");
            return Optional.empty();
        }

        Optional<Item> connObjectKeyItem = resource.getOrgUnit().getConnObjectKeyItem();
        if (connObjectKeyItem.isEmpty()) {
            LOG.error("Unable to locate conn object key item for Realms");
            return Optional.empty();
        }

        Item item = connObjectKeyItem.get();
        IntValues intValues;
        try {
            intValues = doGetIntValues(
                    resource,
                    item,
                    intAttrNameParser.parse(item.getIntAttrName()),
                    AttrSchemaType.String,
                    realm);
        } catch (ParseException e) {
            LOG.error("Invalid intAttrName '{}' specified, ignoring", item.getIntAttrName(), e);
            intValues = new IntValues(AttrSchemaType.String, List.of());
        }
        return intValues.values().isEmpty()
                ? Optional.empty()
                : Optional.of(intValues.values().getFirst().getValueAsString());
    }

    private record MembershipContext(GroupableRelatableTO groupableTO, Group group) {
    }

    @Transactional(readOnly = true)
    @Override
    public void setIntValues(final Item item, final Attribute attr, final AnyTO anyTO) {
        List<Object> values = getPullValues(item, attr, anyTO);

        IntAttrName intAttrName;
        try {
            intAttrName = intAttrNameParser.parse(item.getIntAttrName(), AnyTypeKind.fromTOClass(anyTO.getClass()));
        } catch (ParseException e) {
            LOG.error("Invalid intAttrName '{}' specified, ignoring", item.getIntAttrName(), e);
            return;
        }

        if (intAttrName.getField() != null && !values.isEmpty() && values.getFirst() != null) {
            setAnyFieldValue(intAttrName.getField(), values.getFirst(), anyTO);
        } else if (intAttrName.getSchemaInfo() != null && attr != null) {
            setAnySchemaValues(intAttrName, values, anyTO);
        }
    }

    private List<Object> getPullValues(final Item item, final Attribute attr, final AnyTO anyTO) {
        List<Object> values = null;
        if (attr != null) {
            values = attr.getValue();
            for (ItemTransformer transformer
                    : MappingUtils.getItemTransformers(AuthContextUtils.getDomain(), item, getTransformers(item))) {

                values = transformer.beforePull(item, anyTO, values);
            }
        }
        return Optional.ofNullable(values).orElseGet(List::of);
    }

    private void setAnyFieldValue(final String field, final Object value, final AnyTO anyTO) {
        switch (field) {
            case "password" -> setPassword(value, anyTO);
            case "username" -> setUsername(value, anyTO);
            case "name" -> setName(value, anyTO);
            case MUST_CHANGE_PASSWORD -> setMustChangePassword(value, anyTO);
            case "uManager" -> anyTO.setUManager(value.toString());
            case "gManager" -> anyTO.setGManager(value.toString());
        }
    }

    private void setPassword(final Object value, final AnyTO anyTO) {
        if (anyTO instanceof UserTO userTO) {
            userTO.setPassword(ConnObjectUtils.getPassword(value));
        }
    }

    private void setUsername(final Object value, final AnyTO anyTO) {
        if (anyTO instanceof UserTO userTO) {
            userTO.setUsername(value.toString());
        }
    }

    private void setName(final Object value, final AnyTO anyTO) {
        if (anyTO instanceof GroupTO groupTO) {
            groupTO.setName(value.toString());
        } else if (anyTO instanceof AnyObjectTO anyObjectTO) {
            anyObjectTO.setName(value.toString());
        }
    }

    private void setMustChangePassword(final Object value, final AnyTO anyTO) {
        if (anyTO instanceof UserTO userTO) {
            userTO.setMustChangePassword(BooleanUtils.toBoolean(value.toString()));
        }
    }

    private void setAnySchemaValues(
            final IntAttrName intAttrName,
            final List<Object> values,
            final AnyTO anyTO) {

        MembershipContext membershipContext = getMembershipContext(intAttrName, anyTO);
        switch (intAttrName.getSchemaInfo().type()) {
            case PLAIN -> addPlainAttr(
                    anyTO, membershipContext, buildPlainAttr(intAttrName, values));
            case DERIVED -> addDerivedAttr(
                    anyTO, membershipContext, buildDerivedAttr(intAttrName));
        }
    }

    private MembershipContext getMembershipContext(final IntAttrName intAttrName, final AnyTO anyTO) {
        if (anyTO instanceof final GroupableRelatableTO groupableRelatableTO
                && intAttrName.getMembership() != null) {

            return new MembershipContext(
                    groupableRelatableTO,
                    groupDAO.findByName(intAttrName.getMembership()).orElse(null));
        }
        return new MembershipContext(null, null);
    }

    private Attr buildPlainAttr(final IntAttrName intAttrName, final List<Object> values) {
        Attr attrTO = new Attr();
        attrTO.setSchema(intAttrName.getSchemaInfo().schema().getKey());

        PlainSchema schema = (PlainSchema) intAttrName.getSchemaInfo().schema();
        for (Object value : values) {
            AttrSchemaType schemaType = schema == null ? AttrSchemaType.String : schema.getType();
            if (value != null) {
                if (schemaType == AttrSchemaType.Binary) {
                    attrTO.getValues().add(Base64.getEncoder().encodeToString((byte[]) value));
                } else {
                    attrTO.getValues().add(value.toString());
                }
            }
        }
        return attrTO;
    }

    private Attr buildDerivedAttr(final IntAttrName intAttrName) {
        Attr attrTO = new Attr();
        attrTO.setSchema(intAttrName.getSchemaInfo().schema().getKey());
        return attrTO;
    }

    private void addPlainAttr(
            final AnyTO anyTO,
            final MembershipContext membershipContext,
            final Attr attrTO) {

        if (membershipContext.groupableTO() == null || membershipContext.group() == null) {
            anyTO.getPlainAttrs().add(attrTO);
        } else {
            getOrCreateMembership(membershipContext).getPlainAttrs().add(attrTO);
        }
    }

    private void addDerivedAttr(
            final AnyTO anyTO,
            final MembershipContext membershipContext,
            final Attr attrTO) {

        if (membershipContext.groupableTO() == null || membershipContext.group() == null) {
            anyTO.getDerAttrs().add(attrTO);
        } else {
            getOrCreateMembership(membershipContext).getDerAttrs().add(attrTO);
        }
    }

    private MembershipTO getOrCreateMembership(final MembershipContext membershipContext) {
        return membershipContext.groupableTO().getMembership(membershipContext.group().getKey()).orElseGet(() -> {
            MembershipTO newMemb = new MembershipTO.Builder(membershipContext.group().getKey()).build();
            membershipContext.groupableTO().getMemberships().add(newMemb);
            return newMemb;
        });
    }

    @Override
    public void setIntValues(final Item item, final Attribute attr, final RealmTO realmTO) {
        List<Object> values = getPullValues(item, attr, realmTO);

        IntAttrName intAttrName;
        try {
            intAttrName = intAttrNameParser.parse(item.getIntAttrName());
        } catch (ParseException e) {
            LOG.error("Invalid intAttrName '{}' specified, ignoring", item.getIntAttrName(), e);
            return;
        }

        if (intAttrName.getField() != null) {
            setRealmFieldValue(intAttrName.getField(), values, realmTO);
        } else if (intAttrName.getSchemaInfo() != null && attr != null) {
            setRealmSchemaValues(intAttrName, values, realmTO);
        }
    }

    private List<Object> getPullValues(final Item item, final Attribute attr, final RealmTO realmTO) {
        List<Object> values = null;
        if (attr != null) {
            values = attr.getValue();
            for (ItemTransformer transformer
                    : MappingUtils.getItemTransformers(AuthContextUtils.getDomain(), item, getTransformers(item))) {

                values = transformer.beforePull(item, realmTO, values);
            }
        }
        return Optional.ofNullable(values).orElseGet(List::of);
    }

    private void setRealmFieldValue(
            final String field,
            final List<Object> values,
            final RealmTO realmTO) {

        switch (field) {
            case "name" -> realmTO.setName(values.isEmpty() || values.getFirst() == null
                    ? null
                    : values.getFirst().toString());
            case "fullpath" -> setRealmParent(values, realmTO);
        }
    }

    private void setRealmParent(final List<Object> values, final RealmTO realmTO) {
        String parentFullPath = StringUtils.substringBeforeLast(values.getFirst().toString(), "/");
        realmSearchDAO.findByFullPath(parentFullPath).ifPresentOrElse(
                parent -> realmTO.setParent(parent.getFullPath()),
                () -> LOG.warn("Could not find Realm with path {}, ignoring", parentFullPath));
    }

    private void setRealmSchemaValues(
            final IntAttrName intAttrName,
            final List<Object> values,
            final RealmTO realmTO) {

        switch (intAttrName.getSchemaInfo().type()) {
            case PLAIN -> realmTO.getPlainAttrs().add(buildPlainAttr(intAttrName, values));
            case DERIVED -> realmTO.getDerAttrs().add(buildDerivedAttr(intAttrName));
        }
    }

    @Override
    public boolean hasMustChangePassword(final Provision provision) {
        return Optional.ofNullable(provision.getMapping()).
                map(mapping -> mapping.getItems().stream().
                        anyMatch(item -> MUST_CHANGE_PASSWORD.equals(item.getIntAttrName()))).
                orElse(false);
    }
}
