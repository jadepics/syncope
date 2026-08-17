package org.apache.syncope.core.provisioning.java;

import java.security.*;
import java.text.ParseException;
import java.time.temporal.TemporalAccessor;
import java.util.*;
import java.util.stream.Collectors;
import javax.crypto.*;
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

@SuppressWarnings({"PMD.CouplingBetweenObjects", "PMD.TooManyMethods", "PMD.CyclomaticComplexity"})
public class DefaultMappingManager implements MappingManager {

    protected static final Logger LOG = LoggerFactory.getLogger(MappingManager.class);

    private static final String INVALID_INT_ATTR_NAME = "Invalid intAttrName '{}' specified, ignoring";

    private static void logDebug(final String message, final Object... arguments) {
        if (LOG.isDebugEnabled()) {
            LOG.debug(message, arguments);
        }
    }

    private static void logWarn(final String message, final Object... arguments) {
        if (LOG.isWarnEnabled()) {
            LOG.warn(message, arguments);
        }
    }

    private static void logError(final String message, final Object... arguments) {
        if (LOG.isErrorEnabled()) {
            LOG.error(message, arguments);
        }
    }

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
            logDebug("Add connObjectKey [{}] as {}", connObjectKey, Name.NAME);
            name = new Name(connObjectKey);
        } else {
            logDebug("Add connObjectLink [{}] as {}", evalConnObjectLink, Name.NAME);
            name = new Name(evalConnObjectLink);

            // connObjectKey not propagated: it will be used to set the value for __UID__ attribute
            logDebug("connObjectKey [{}] will be used as {}", connObjectKey, Uid.NAME);
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
            logDebug("Missing connObjectKey for {}", any.getType().getKey());
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
            logDebug("Missing connObjectKey for Realms");
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

        logDebug("Preparing resource attributes for {} with provision {} for attributes {}",
                any, provision, any.getPlainAttrs());

        Set<Attribute> attributes = new HashSet<>();
        Mutable<String> connObjectKeyValue = new MutableObject<>();

        MappingUtils.getPropagationItems(provision.getMapping().getItems().stream()).forEach(item -> {
            logDebug("Processing expression '{}'", item.getIntAttrName());

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
                logError("Expression '{}' processing failed", item.getIntAttrName(), e);
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

        logDebug("Preparing resource attributes for linked account {} of user {} with provision {} "
                + "for user attributes {} with override {}",
                account, user, provision, user.getPlainAttrs(), account.getPlainAttrs());

        Set<Attribute> attributes = new HashSet<>();
        AccountGetter usernameGetter =
                acct -> account.getUsername() == null ? AccountGetter.DEFAULT.apply(acct) : account;
        AccountGetter passwordGetter =
                acct -> account.getPassword() == null ? AccountGetter.DEFAULT.apply(acct) : account;
        PlainAttrGetter plainAttrGetter = linkedAccountPlainAttrGetter(account);

        MappingUtils.getPropagationItems(provision.getMapping().getItems().stream()).forEach(item -> {
            logDebug("Processing expression '{}'", item.getIntAttrName());

            try {
                processPreparedAttr(
                        prepareAttr(
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
                logError("Expression '{}' processing failed", item.getIntAttrName(), e);
            }
        });

        String connObjectKey = account.getConnObjectKeyValue();
        MappingUtils.getConnObjectKeyItem(provision).
                ifPresent(item -> addLinkedAccountConnObjectKey(user, provision, item, connObjectKey, attributes));

        if (account.isSuspended() != null) {
            attributes.add(AttributeBuilder.buildEnabled(BooleanUtils.negate(account.isSuspended())));
        }

        removePasswordAttributeIfNeeded(changePwd, attributes);
        return attributes;
    }

    @Override
    public PreparedAttrs prepareAttrsFromRealm(final Realm realm, final ExternalResource resource) {
        if (resource.getOrgUnit() == null) {
            logError("No mapping configured for Realms");
            return new PreparedAttrs(null, Set.of());
        }

        logDebug("Preparing resource attributes for {} with orgUnit {}", realm, resource.getOrgUnit());

        Set<Attribute> attributes = new HashSet<>();
        Mutable<String> connObjectKeyValue = new MutableObject<>();

        MappingUtils.getPropagationItems(resource.getOrgUnit().getItems().stream()).forEach(item -> {
            logDebug("Processing expression '{}'", item.getIntAttrName());

            try {
                processPreparedAttr(
                        prepareAttr(
                                resource,
                                item,
                                realm),
                        attributes).ifPresent(connObjectKeyValue::setValue);
            } catch (RuntimeException e) {
                logError("Expression '{}' processing failed", item.getIntAttrName(), e);
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
        } catch (NoSuchAlgorithmException
                | NoSuchPaddingException
                | InvalidKeyException
                | IllegalBlockSizeException
                | BadPaddingException
                | RuntimeException e) {

            logError("Could not decode password for {}", account, e);
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

        Optional<IntAttrName> parsed = parseIntAttrName(item, any.getType().getKind());
        if (parsed.isEmpty()) {
            return null;
        }

        IntAttrName intAttrName = parsed.get();
        AttrSchemaType schemaType = schemaType(intAttrName);

        IntValues intValues = getIntValues(
                resource, provision, item, intAttrName, schemaType, any, usernameAccountGetter, plainAttrGetter);
        schemaType = intValues.attrSchemaType();

        logMapping(item, intAttrName, schemaType, intValues.values());

        List<Object> objValues = toObjectValues(intAttrName, schemaType, intValues.values());
        return preparedAttrForAny(item, any, password, passwordAccountGetter, objValues);
    }

    @Override
    public PreparedAttr prepareAttr(
            final ExternalResource resource,
            final Item item,
            final Realm realm) {

        Optional<IntAttrName> parsed = parseIntAttrName(item);
        if (parsed.isEmpty()) {
            return null;
        }

        IntAttrName intAttrName = parsed.get();
        AttrSchemaType schemaType = schemaType(intAttrName);

        IntValues intValues = getIntValues(resource, item, intAttrName, schemaType, realm);
        schemaType = intValues.attrSchemaType();

        logMapping(item, intAttrName, schemaType, intValues.values());

        List<Object> objValues = toObjectValues(intAttrName, schemaType, intValues.values());
        return preparedAttrForRealm(item, objValues);
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

        logDebug("Get internal values for {} as '{}' on {}", any, item.getIntAttrName(), resource);

        ReferenceResolution resolution = resolveReferences(intAttrName, any);
        if (resolution.references().isEmpty()) {
            logWarn("Could not determine the reference instance for {}", item.getIntAttrName());
            return new IntValues(schemaType, List.of());
        }

        List<PlainAttrValue> values = new ArrayList<>();
        for (Any ref : resolution.references()) {
            appendReferenceValues(
                    resource,
                    provision,
                    intAttrName,
                    ref,
                    usernameAccountGetter,
                    plainAttrGetter,
                    resolution.relationship(),
                    resolution.membership(),
                    values);
        }

        logDebug("Internal values: {}", values);
        return transformBeforePropagation(item, any, schemaType, values);
    }

    @Transactional(readOnly = true)
    @Override
    public IntValues getIntValues(
            final ExternalResource resource,
            final Item item,
            final IntAttrName intAttrName,
            final AttrSchemaType schemaType,
            final Realm realm) {

        logDebug("Get internal values for {} as '{}' on {}", realm, item.getIntAttrName(), resource);

        List<PlainAttrValue> values = new ArrayList<>();
        appendRealmValues(intAttrName, realm, values);

        logDebug("Internal values: {}", values);
        return transformBeforePropagation(item, realm, schemaType, values);
    }


    private PlainAttrGetter linkedAccountPlainAttrGetter(final LinkedAccount account) {
        return (attributable, schema) -> {
            if (attributable instanceof User) {
                Optional<PlainAttr> overridden = account.getPlainAttr(schema);
                if (overridden.isPresent()) {
                    return overridden.get();
                }
            }
            return PlainAttrGetter.DEFAULT.apply(attributable, schema);
        };
    }

    private void addLinkedAccountConnObjectKey(
            final User user,
            final Provision provision,
            final Item item,
            final String connObjectKey,
            final Set<Attribute> attributes) {

        Attribute connObjectKeyExtAttr = AttributeUtil.find(item.getExtAttrName(), attributes);
        if (connObjectKeyExtAttr != null) {
            attributes.remove(connObjectKeyExtAttr);
            attributes.add(AttributeBuilder.build(item.getExtAttrName(), connObjectKey));
        }

        Name name = evaluateNAME(user, provision, connObjectKey);
        attributes.add(name);
        if (!connObjectKey.equals(name.getNameValue()) && connObjectKeyExtAttr == null) {
            attributes.add(AttributeBuilder.build(item.getExtAttrName(), connObjectKey));
        }
    }

    private static void removePasswordAttributeIfNeeded(
            final boolean changePwd,
            final Set<Attribute> attributes) {

        if (!changePwd) {
            Optional.ofNullable(AttributeUtil.find(OperationalAttributes.PASSWORD_NAME, attributes)).
                    ifPresent(attributes::remove);
        }
    }

    private Optional<IntAttrName> parseIntAttrName(final Item item, final AnyTypeKind kind) {
        try {
            return Optional.of(intAttrNameParser.parse(item.getIntAttrName(), kind));
        } catch (ParseException e) {
            logError(INVALID_INT_ATTR_NAME, item.getIntAttrName(), e);
            return Optional.empty();
        }
    }

    private Optional<IntAttrName> parseIntAttrName(final Item item) {
        try {
            return Optional.of(intAttrNameParser.parse(item.getIntAttrName()));
        } catch (ParseException e) {
            logError(INVALID_INT_ATTR_NAME, item.getIntAttrName(), e);
            return Optional.empty();
        }
    }

    private static AttrSchemaType schemaType(final IntAttrName intAttrName) {
        return Optional.ofNullable(intAttrName.getSchemaInfo()).
                filter(schemaInfo -> schemaInfo.schema() instanceof PlainSchema).
                map(schemaInfo -> schemaInfo.schema().getType()).
                orElse(AttrSchemaType.String);
    }

    private static void logMapping(
            final Item item,
            final IntAttrName intAttrName,
            final AttrSchemaType schemaType,
            final List<PlainAttrValue> values) {

        logDebug(
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

        List<Object> result = new ArrayList<>();
        for (PlainAttrValue value : values) {
            result.add(toObjectValue(intAttrName, schemaType, value));
        }
        return result;
    }

    private Object toObjectValue(
            final IntAttrName intAttrName,
            final AttrSchemaType schemaType,
            final PlainAttrValue value) {

        if (schemaType == AttrSchemaType.Encrypted
                && intAttrName.getSchemaInfo().schema() instanceof PlainSchema schema) {

            return decodedValueOrOriginal(intAttrName, schema, value);
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

    private Object decodedValueOrOriginal(
            final IntAttrName intAttrName,
            final PlainSchema schema,
            final PlainAttrValue value) {

        String decoded = null;
        try {
            decoded = encryptorManager.getInstance(schema.getSecretKey()).
                    decode(value.getStringValue(), schema.getCipherAlgorithm());
        } catch (NoSuchAlgorithmException
                | NoSuchPaddingException
                | InvalidKeyException
                | IllegalBlockSizeException
                | BadPaddingException
                | RuntimeException e) {

            logWarn("Could not decode value for {} with algorithm {}",
                    intAttrName.getSchemaInfo(), schema.getCipherAlgorithm(), e);
        }
        return Optional.ofNullable(decoded).orElse(value.getStringValue());
    }

    private PreparedAttr preparedAttrForAny(
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
        return preparedConnectorAttribute(item, objValues);
    }

    private static PreparedAttr preparedAttrForRealm(final Item item, final List<Object> objValues) {
        if (item.isConnObjectKey()) {
            return new PreparedAttr(objValues.isEmpty() ? null : objValues.getFirst().toString(), null);
        }
        return preparedConnectorAttribute(item, objValues);
    }

    private static PreparedAttr preparedConnectorAttribute(final Item item, final List<Object> objValues) {
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

    private record ReferenceResolution(
            List<Any> references,
            Relationship<?, ?> relationship,
            Membership<?> membership) {
    }

    private ReferenceResolution resolveReferences(final IntAttrName intAttrName, final Any any) {
        if (intAttrName.getExternalUser() != null) {
            return new ReferenceResolution(externalUserReferences(intAttrName), null, null);
        }
        if (intAttrName.getExternalGroup() != null) {
            return new ReferenceResolution(externalGroupReferences(intAttrName), null, null);
        }
        if (intAttrName.getExternalAnyObject() != null) {
            return new ReferenceResolution(
                    new ArrayList<>(anyObjectDAO.findByName(intAttrName.getExternalAnyObject())),
                    null,
                    null);
        }

        List<Any> references = new ArrayList<>();
        references.add(any);

        if (intAttrName.getMembership() != null && any instanceof Groupable<?, ?, ?> groupable) {
            Membership<?> membership = groupDAO.findByName(intAttrName.getMembership()).
                    flatMap(group -> groupable.getMembership(group.getKey())).
                    orElse(null);
            return new ReferenceResolution(references, null, membership);
        }
        if (intAttrName.getRelationshipInfo() != null && any instanceof Relatable<?, ?> relatable) {
            return new ReferenceResolution(references, resolveRelationship(intAttrName, relatable), null);
        }
        return new ReferenceResolution(references, null, null);
    }

    private List<Any> externalUserReferences(final IntAttrName intAttrName) {
        List<Any> references = new ArrayList<>();
        userDAO.findByUsername(intAttrName.getExternalUser()).ifPresentOrElse(
                references::add,
                () -> logWarn("Could not find user {}, ignoring", intAttrName.getExternalUser()));
        return references;
    }

    private List<Any> externalGroupReferences(final IntAttrName intAttrName) {
        List<Any> references = new ArrayList<>();
        groupDAO.findByName(intAttrName.getExternalGroup()).ifPresentOrElse(
                references::add,
                () -> logWarn("Could not find group {}, ignoring", intAttrName.getExternalGroup()));
        return references;
    }


    private Relationship<?, ?> resolveRelationship(
            final IntAttrName intAttrName,
            final Relatable<?, ?> relatable) {

        RelationshipType relationshipType =
                relationshipTypeDAO.findById(intAttrName.getRelationshipInfo().type()).orElse(null);
        if (relationshipType == null) {
            logWarn("Could not find relationship type {}, ignoring", intAttrName.getRelationshipInfo().type());
            return null;
        }

        return anyObjectDAO.findByName(
                relationshipType.getRightEndAnyType().getKey(),
                intAttrName.getRelationshipInfo().anyObject()).
                flatMap(otherEnd -> relatable.getRelationship(relationshipType, otherEnd.getKey())).
                orElse(null);
    }

    private void appendReferenceValues(
            final ExternalResource resource,
            final Provision provision,
            final IntAttrName intAttrName,
            final Any ref,
            final AccountGetter usernameAccountGetter,
            final PlainAttrGetter plainAttrGetter,
            final Relationship<?, ?> relationship,
            final Membership<?> membership,
            final List<PlainAttrValue> values) {

        if (intAttrName.getField() != null) {
            fieldValue(resource, provision, intAttrName.getField(), ref, usernameAccountGetter).
                    ifPresent(values::add);
        } else if (intAttrName.getSchemaInfo() != null) {
            appendSchemaValues(intAttrName, ref, plainAttrGetter, relationship, membership, values);
        }
    }

    private Optional<PlainAttrValue> fieldValue(
            final ExternalResource resource,
            final Provision provision,
            final String field,
            final Any ref,
            final AccountGetter usernameAccountGetter) {

        return switch (field) {
            case "key" -> plainStringValue(ref.getKey());
            case "realm" -> plainStringValue(ref.getRealm().getFullPath());
            case "password" -> Optional.empty();
            case "uManager", "gManager" -> managerValue(resource, provision, ref);
            case "username", "suspended", "mustChangePassword" ->
                accountFieldValue(field, ref, usernameAccountGetter);
            default -> reflectedFieldValue(field, ref);
        };
    }

    private Optional<PlainAttrValue> accountFieldValue(
            final String field,
            final Any ref,
            final AccountGetter usernameAccountGetter) {

        if ("username".equals(field)) {
            return ref instanceof Account account
                    ? plainStringValue(usernameAccountGetter.apply(account).getUsername())
                    : Optional.empty();
        }
        if (!(ref instanceof User user)) {
            return Optional.empty();
        }

        PlainAttrValue value = new PlainAttrValue();
        if ("suspended".equals(field)) {
            value.setBooleanValue(user.isSuspended());
        } else {
            value.setBooleanValue(user.isMustChangePassword());
        }
        return Optional.of(value);
    }

    private Optional<PlainAttrValue> managerValue(
            final ExternalResource resource,
            final Provision provision,
            final Any ref) {

        Mapping uMappingTO = provision.getAnyType().equals(AnyTypeKind.USER.name())
                ? provision.getMapping()
                : null;
        Mapping gMappingTO = provision.getAnyType().equals(AnyTypeKind.GROUP.name())
                ? provision.getMapping()
                : null;

        String managerValue = null;
        if (ref.getUManager() != null && uMappingTO != null) {
            managerValue = getManagerValue(resource, provision, ref.getUManager());
        }
        if (ref.getGManager() != null && gMappingTO != null) {
            managerValue = getManagerValue(resource, provision, ref.getGManager());
        }

        return StringUtils.isNotBlank(managerValue)
                ? plainStringValue(managerValue)
                : Optional.empty();
    }

    private Optional<PlainAttrValue> reflectedFieldValue(final String field, final Any ref) {
        try {
            Object fieldValue = FieldUtils.readField(ref, field, true);
            PlainAttrValue attrValue = new PlainAttrValue();

            if (fieldValue instanceof TemporalAccessor temporalAccessor) {
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
            return Optional.of(attrValue);
        } catch (IllegalAccessException | RuntimeException e) {
            logError("Could not read value of '{}' from {}", field, ref, e);
            return Optional.empty();
        }
    }

    private static Optional<PlainAttrValue> plainStringValue(final String value) {
        PlainAttrValue attrValue = new PlainAttrValue();
        attrValue.setStringValue(value);
        return Optional.of(attrValue);
    }

    private void appendSchemaValues(
            final IntAttrName intAttrName,
            final Any ref,
            final PlainAttrGetter plainAttrGetter,
            final Relationship<?, ?> relationship,
            final Membership<?> membership,
            final List<PlainAttrValue> values) {

        switch (intAttrName.getSchemaInfo().type()) {
            case PLAIN -> appendPlainValues(
                    plainAttr(intAttrName, ref, plainAttrGetter, relationship, membership), values);

            case DERIVED -> appendDerivedValue(intAttrName, ref, relationship, membership, values);

            default -> {
            }
        }
    }

    private static PlainAttr plainAttr(
            final IntAttrName intAttrName,
            final Any ref,
            final PlainAttrGetter plainAttrGetter,
            final Relationship<?, ?> relationship,
            final Membership<?> membership) {

        String schema = intAttrName.getSchemaInfo().schema().getKey();
        if (membership == null && relationship == null) {
            return plainAttrGetter.apply(ref, schema);
        }
        if (membership == null) {
            return ((Relatable<?, ?>) ref).getPlainAttr(schema, relationship).orElse(null);
        }
        return ((Groupable<?, ?, ?>) ref).getPlainAttr(schema, membership).orElse(null);
    }

    private static void appendPlainValues(final PlainAttr attr, final List<PlainAttrValue> values) {
        if (attr == null) {
            return;
        }
        if (attr.getUniqueValue() != null) {
            values.add(clonePlainAttrValue(attr.getUniqueValue()));
        } else if (attr.getValues() != null) {
            attr.getValues().forEach(value -> values.add(clonePlainAttrValue(value)));
        }
    }

    private void appendDerivedValue(
            final IntAttrName intAttrName,
            final Any ref,
            final Relationship<?, ?> relationship,
            final Membership<?> membership,
            final List<PlainAttrValue> values) {

        DerSchema derSchema = (DerSchema) intAttrName.getSchemaInfo().schema();
        String derValue;
        if (membership == null && relationship == null) {
            derValue = derAttrHandler.getValue(ref, derSchema);
        } else if (membership == null) {
            derValue = derAttrHandler.getValue((Relatable<?, ?>) ref, relationship, derSchema);
        } else {
            derValue = derAttrHandler.getValue((Groupable<?, ?, ?>) ref, membership, derSchema);
        }
        if (derValue != null) {
            plainStringValue(derValue).ifPresent(values::add);
        }
    }

    private IntValues transformBeforePropagation(
            final Item item,
            final Attributable attributable,
            final AttrSchemaType schemaType,
            final List<PlainAttrValue> values) {

        IntValues transformed = new IntValues(schemaType, values);
        for (ItemTransformer transformer
                : MappingUtils.getItemTransformers(AuthContextUtils.getDomain(), item, getTransformers(item))) {

            transformed = transformer.beforePropagation(
                    item, attributable, transformed.attrSchemaType(), transformed.values());
        }
        logDebug("Transformed values: {}", values);
        return transformed;
    }

    private void appendRealmValues(
            final IntAttrName intAttrName,
            final Realm realm,
            final List<PlainAttrValue> values) {

        if (intAttrName.getField() != null) {
            realmFieldValue(intAttrName.getField(), realm).ifPresent(values::add);
        } else if (intAttrName.getSchemaInfo() != null) {
            appendRealmSchemaValues(intAttrName, realm, values);
        }
    }

    private static Optional<PlainAttrValue> realmFieldValue(final String field, final Realm realm) {
        return switch (field) {
            case "key" -> plainStringValue(realm.getKey());
            case "name" -> plainStringValue(realm.getName());
            case "fullPath" -> plainStringValue(realm.getFullPath());
            default -> Optional.empty();
        };
    }

    private void appendRealmSchemaValues(
            final IntAttrName intAttrName,
            final Realm realm,
            final List<PlainAttrValue> values) {

        switch (intAttrName.getSchemaInfo().type()) {
            case PLAIN ->
                realm.getPlainAttr(intAttrName.getSchemaInfo().schema().getKey()).
                        ifPresent(attr -> appendPlainValues(attr, values));

            case DERIVED -> Optional.ofNullable(
                    derAttrHandler.getValue(realm, (DerSchema) intAttrName.getSchemaInfo().schema())).
                    flatMap(DefaultMappingManager::plainStringValue).
                    ifPresent(values::add);

            default -> {
            }
        }
    }

    private List<Object> beforePullValues(
            final Item item,
            final Attribute attr,
            final EntityTO entityTO) {

        List<Object> values = null;
        if (attr != null) {
            values = attr.getValue();
            for (ItemTransformer transformer
                    : MappingUtils.getItemTransformers(AuthContextUtils.getDomain(), item, getTransformers(item))) {

                values = transformer.beforePull(item, entityTO, values);
            }
        }
        return Optional.ofNullable(values).orElseGet(List::of);
    }

    private static boolean hasUsableFieldValue(final IntAttrName intAttrName, final List<Object> values) {
        return intAttrName.getField() != null && !values.isEmpty() && values.getFirst() != null;
    }

    private static void setAnyFieldValue(final String field, final Object value, final AnyTO anyTO) {
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

    private record MembershipContext(GroupableRelatableTO groupableTO, Group group) {
    }

    private Optional<MembershipContext> membershipContext(
            final IntAttrName intAttrName,
            final AnyTO anyTO) {

        if (!(anyTO instanceof GroupableRelatableTO groupableTO) || intAttrName.getMembership() == null) {
            return Optional.empty();
        }
        return groupDAO.findByName(intAttrName.getMembership()).
                map(group -> new MembershipContext(groupableTO, group));
    }

    private void setAnySchemaValue(
            final IntAttrName intAttrName,
            final List<Object> values,
            final AnyTO anyTO) {

        Optional<MembershipContext> context = membershipContext(intAttrName, anyTO);
        switch (intAttrName.getSchemaInfo().type()) {
            case PLAIN -> addPlainAttr(anyTO, context, plainAttrTO(intAttrName, values));
            case DERIVED -> addDerivedAttr(anyTO, context, derivedAttrTO(intAttrName));
            default -> {
            }
        }
    }

    private static Attr plainAttrTO(final IntAttrName intAttrName, final List<Object> values) {
        Attr attrTO = new Attr();
        attrTO.setSchema(intAttrName.getSchemaInfo().schema().getKey());

        PlainSchema schema = (PlainSchema) intAttrName.getSchemaInfo().schema();
        for (Object value : values) {
            addPlainTOValue(attrTO, schema, value);
        }
        return attrTO;
    }

    private static void addPlainTOValue(final Attr attrTO, final PlainSchema schema, final Object value) {
        if (value == null) {
            return;
        }

        AttrSchemaType schemaType = schema == null ? AttrSchemaType.String : schema.getType();
        if (schemaType == AttrSchemaType.Binary) {
            attrTO.getValues().add(Base64.getEncoder().encodeToString((byte[]) value));
        } else {
            attrTO.getValues().add(value.toString());
        }
    }

    private static Attr derivedAttrTO(final IntAttrName intAttrName) {
        Attr attrTO = new Attr();
        attrTO.setSchema(intAttrName.getSchemaInfo().schema().getKey());
        return attrTO;
    }

    private static void addPlainAttr(
            final AnyTO anyTO,
            final Optional<MembershipContext> context,
            final Attr attrTO) {

        if (context.isEmpty()) {
            anyTO.getPlainAttrs().add(attrTO);
        } else {
            membership(context.get()).getPlainAttrs().add(attrTO);
        }
    }

    private static void addDerivedAttr(
            final AnyTO anyTO,
            final Optional<MembershipContext> context,
            final Attr attrTO) {

        if (context.isEmpty()) {
            anyTO.getDerAttrs().add(attrTO);
        } else {
            membership(context.get()).getDerAttrs().add(attrTO);
        }
    }

    private static MembershipTO membership(final MembershipContext context) {
        return context.groupableTO().getMembership(context.group().getKey()).orElseGet(() -> {
            MembershipTO newMemb = new MembershipTO.Builder(context.group().getKey()).build();
            context.groupableTO().getMemberships().add(newMemb);
            return newMemb;
        });
    }

    private void setRealmFieldValue(
            final String field,
            final List<Object> values,
            final RealmTO realmTO) {

        switch (field) {
            case "name" -> realmTO.setName(values.isEmpty() || values.getFirst() == null
                    ? null
                    : values.getFirst().toString());

            case "fullpath" -> updateRealmParent(values, realmTO);

            default -> {
            }
        }
    }

    private void updateRealmParent(final List<Object> values, final RealmTO realmTO) {
        String parentFullPath = StringUtils.substringBeforeLast(values.getFirst().toString(), "/");
        realmSearchDAO.findByFullPath(parentFullPath).ifPresentOrElse(
                parent -> realmTO.setParent(parent.getFullPath()),
                () -> logWarn("Could not find Realm with path {}, ignoring", parentFullPath));
    }

    private static void setRealmSchemaValue(
            final IntAttrName intAttrName,
            final List<Object> values,
            final RealmTO realmTO) {

        switch (intAttrName.getSchemaInfo().type()) {
            case PLAIN -> realmTO.getPlainAttrs().add(plainAttrTO(intAttrName, values));
            case DERIVED -> realmTO.getDerAttrs().add(derivedAttrTO(intAttrName));
            default -> {
            }
        }
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
            logError("Unable to locate conn object key item for {}", any.getType().getKey());
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
            logError(INVALID_INT_ATTR_NAME, item.getIntAttrName(), e);
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
            logError("No mapping configured for Realms");
            return Optional.empty();
        }

        Optional<Item> connObjectKeyItem = resource.getOrgUnit().getConnObjectKeyItem();
        if (connObjectKeyItem.isEmpty()) {
            logError("Unable to locate conn object key item for Realms");
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
            logError(INVALID_INT_ATTR_NAME, item.getIntAttrName(), e);
            intValues = new IntValues(AttrSchemaType.String, List.of());
        }
        return intValues.values().isEmpty()
                ? Optional.empty()
                : Optional.of(intValues.values().getFirst().getValueAsString());
    }

    @Transactional(readOnly = true)
    @Override
    public void setIntValues(final Item item, final Attribute attr, final AnyTO anyTO) {
        List<Object> values = beforePullValues(item, attr, anyTO);

        Optional<IntAttrName> parsed = parseIntAttrName(item, AnyTypeKind.fromTOClass(anyTO.getClass()));
        if (parsed.isEmpty()) {
            return;
        }

        IntAttrName intAttrName = parsed.get();
        if (hasUsableFieldValue(intAttrName, values)) {
            setAnyFieldValue(intAttrName.getField(), values.getFirst(), anyTO);
        } else if (intAttrName.getSchemaInfo() != null && attr != null) {
            setAnySchemaValue(intAttrName, values, anyTO);
        }
    }

    @Override
    public void setIntValues(final Item item, final Attribute attr, final RealmTO realmTO) {
        List<Object> values = beforePullValues(item, attr, realmTO);

        Optional<IntAttrName> parsed = parseIntAttrName(item);
        if (parsed.isEmpty()) {
            return;
        }

        IntAttrName intAttrName = parsed.get();
        if (intAttrName.getField() != null) {
            setRealmFieldValue(intAttrName.getField(), values, realmTO);
        } else if (intAttrName.getSchemaInfo() != null && attr != null) {
            setRealmSchemaValue(intAttrName, values, realmTO);
        }
    }

    @Override
    public boolean hasMustChangePassword(final Provision provision) {
        return Optional.ofNullable(provision.getMapping()).
                map(mapping -> mapping.getItems().stream().
                anyMatch(item -> "mustChangePassword".equals(item.getIntAttrName()))).
                orElse(false);
    }
}
