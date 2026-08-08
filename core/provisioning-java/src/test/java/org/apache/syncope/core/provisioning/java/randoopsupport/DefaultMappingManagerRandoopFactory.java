package org.apache.syncope.core.provisioning.java.randoopsupport;

import java.text.ParseException;
import java.util.List;
import java.util.Optional;

import org.apache.syncope.common.lib.to.AnyObjectTO;
import org.apache.syncope.common.lib.to.GroupTO;
import org.apache.syncope.common.lib.to.Item;
import org.apache.syncope.common.lib.to.Mapping;
import org.apache.syncope.common.lib.to.Provision;
import org.apache.syncope.common.lib.to.RealmTO;
import org.apache.syncope.common.lib.to.UserTO;
import org.apache.syncope.common.lib.types.AnyTypeKind;
import org.apache.syncope.common.lib.types.AttrSchemaType;
import org.apache.syncope.common.lib.types.MappingPurpose;
import org.apache.syncope.common.lib.types.SchemaType;
import org.apache.syncope.core.persistence.api.EncryptorManager;
import org.apache.syncope.core.persistence.api.dao.AnyObjectDAO;
import org.apache.syncope.core.persistence.api.dao.GroupDAO;
import org.apache.syncope.core.persistence.api.dao.ImplementationDAO;
import org.apache.syncope.core.persistence.api.dao.RealmSearchDAO;
import org.apache.syncope.core.persistence.api.dao.RelationshipTypeDAO;
import org.apache.syncope.core.persistence.api.dao.UserDAO;
import org.apache.syncope.core.persistence.api.entity.Any;
import org.apache.syncope.core.persistence.api.entity.AnyType;
import org.apache.syncope.core.persistence.api.entity.ExternalResource;
import org.apache.syncope.core.persistence.api.entity.PlainAttr;
import org.apache.syncope.core.persistence.api.entity.PlainAttrValue;
import org.apache.syncope.core.persistence.api.entity.PlainSchema;
import org.apache.syncope.core.persistence.api.entity.Realm;
import org.apache.syncope.core.provisioning.api.AccountGetter;
import org.apache.syncope.core.provisioning.api.DerAttrHandler;
import org.apache.syncope.core.provisioning.api.IntAttrName;
import org.apache.syncope.core.provisioning.api.IntAttrNameParser;
import org.apache.syncope.core.provisioning.api.PlainAttrGetter;
import org.apache.syncope.core.provisioning.api.jexl.JexlTools;
import org.apache.syncope.core.provisioning.java.DefaultMappingManager;
import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.AttributeBuilder;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Support factory per Randoop.
 * Non contiene test né oracle: costruisce soltanto fixture pubbliche
 * che Randoop può combinare durante la generazione.
 */
public final class DefaultMappingManagerRandoopFactory {

    private DefaultMappingManagerRandoopFactory() {
    }

    public static DefaultMappingManager manager() {
        IntAttrNameParser parser = mock(IntAttrNameParser.class);

        try {
            when(parser.parse(anyString(), any(AnyTypeKind.class)))
                    .thenAnswer(invocation -> intAttrName(invocation.getArgument(0)));
            when(parser.parse(anyString()))
                    .thenAnswer(invocation -> intAttrName(invocation.getArgument(0)));
        } catch (ParseException e) {
            throw new IllegalStateException(e);
        }

        return new DefaultMappingManager(
                mock(UserDAO.class),
                mock(AnyObjectDAO.class),
                mock(GroupDAO.class),
                mock(RelationshipTypeDAO.class),
                mock(RealmSearchDAO.class),
                mock(ImplementationDAO.class),
                mock(DerAttrHandler.class),
                parser,
                mock(EncryptorManager.class),
                mock(JexlTools.class));
    }

    public static Any anyUser() {
        Any any = mock(Any.class);
        AnyType type = mock(AnyType.class);

        when(type.getKind()).thenReturn(AnyTypeKind.USER);
        when(type.getKey()).thenReturn("USER");
        when(any.getType()).thenReturn(type);
        when(any.getKey()).thenReturn("user-key");

        PlainAttr firstname = plainAttr("firstname", "Mario");
        when(any.getPlainAttrs()).thenReturn(List.of(firstname));
        when(any.getPlainAttr(anyString())).thenAnswer(invocation -> {
            String schema = invocation.getArgument(0);
            return "missing".equals(schema)
                    ? Optional.empty()
                    : Optional.of(plainAttr(schema, "value-" + schema));
        });

        return any;
    }

    public static Realm realm() {
        Realm realm = mock(Realm.class);
        when(realm.getKey()).thenReturn("realm-key");
        when(realm.getName()).thenReturn("child");
        when(realm.getFullPath()).thenReturn("/parent/child");

        PlainAttr department = plainAttr("department", "Engineering");
        when(realm.getPlainAttrs()).thenReturn(List.of(department));
        when(realm.getPlainAttr(anyString())).thenAnswer(invocation -> {
            String schema = invocation.getArgument(0);
            return "missing".equals(schema)
                    ? Optional.empty()
                    : Optional.of(plainAttr(schema, "value-" + schema));
        });

        return realm;
    }

    public static ExternalResource resource() {
        return mock(ExternalResource.class);
    }

    public static Provision emptyProvision() {
        Provision provision = new Provision();
        provision.setMapping(new Mapping());
        return provision;
    }

    public static Provision plainProvision() {
        Mapping mapping = new Mapping();
        mapping.getItems().add(plainItem());

        Provision provision = new Provision();
        provision.setMapping(mapping);
        return provision;
    }

    public static Provision plainAndKeyProvision() {
        Mapping mapping = new Mapping();
        mapping.getItems().add(plainItem());
        mapping.getItems().add(keyItem());

        Provision provision = new Provision();
        provision.setMapping(mapping);
        return provision;
    }

    public static Item plainItem() {
        return item("firstname", "givenName", false, false);
    }

    public static Item secondPlainItem() {
        return item("department", "ou", false, false);
    }

    public static Item usernameItem() {
        return item("username", "uid", false, false);
    }

    public static Item keyItem() {
        return item("key", "uid", true, false);
    }

    public static Item nameItem() {
        return item("name", "name", false, false);
    }

    public static Item mustChangePasswordItem() {
        return item("mustChangePassword", "mustChangePassword", false, false);
    }

    public static Item optionalMissingPlainItem() {
        Item item = item("missing", "telephoneNumber", false, false);
        item.setMandatoryCondition("false");
        return item;
    }

    public static UserTO userTO() {
        UserTO user = new UserTO();
        user.setUsername("initial-user");
        return user;
    }

    public static GroupTO groupTO() {
        GroupTO group = new GroupTO();
        group.setName("initial-group");
        return group;
    }

    public static AnyObjectTO anyObjectTO() {
        AnyObjectTO anyObject = new AnyObjectTO();
        anyObject.setName("initial-any-object");
        return anyObject;
    }

    public static RealmTO realmTO() {
        RealmTO realm = new RealmTO();
        realm.setName("initial-realm");
        return realm;
    }

    public static Attribute singleStringAttribute() {
        return AttributeBuilder.build("givenName", "Mario");
    }

    public static Attribute secondStringAttribute() {
        return AttributeBuilder.build("ou", "Engineering");
    }

    public static Attribute usernameAttribute() {
        return AttributeBuilder.build("uid", "newUsername");
    }

    public static Attribute nameAttribute() {
        return AttributeBuilder.build("name", "newName");
    }

    public static Attribute booleanAttribute() {
        return AttributeBuilder.build("mustChangePassword", Boolean.TRUE);
    }

    public static Attribute multiStringAttribute() {
        return AttributeBuilder.build(
                "aliases",
                List.of("mario.rossi@example.org", "mrossi@example.com"));
    }

    public static Attribute emptyAttribute() {
        return AttributeBuilder.build("givenName");
    }

    public static IntAttrName plainStringIntAttrName() {
        return intAttrName("firstname");
    }

    public static IntAttrName secondPlainStringIntAttrName() {
        return intAttrName("department");
    }

    public static IntAttrName usernameIntAttrName() {
        return intAttrName("username");
    }

    public static IntAttrName keyIntAttrName() {
        return intAttrName("key");
    }

    public static IntAttrName nameIntAttrName() {
        return intAttrName("name");
    }

    public static IntAttrName missingIntAttrName() {
        return intAttrName("missing");
    }

    public static AttrSchemaType stringSchemaType() {
        return AttrSchemaType.String;
    }

    public static AccountGetter defaultAccountGetter() {
        return AccountGetter.DEFAULT;
    }

    public static PlainAttrGetter plainAttrGetter() {
        return (attributable, schema) ->
                "missing".equals(schema) ? null : plainAttr(schema, "value-" + schema);
    }

    public static PlainAttrGetter missingPlainAttrGetter() {
        return (attributable, schema) -> null;
    }

    public static PlainAttr plainSingleValue() {
        return plainAttr("firstname", "Mario");
    }

    public static PlainAttr plainMultipleValues() {
        return plainAttr(
                "aliases",
                "mario.rossi@example.org",
                "mrossi@example.com");
    }

    public static PlainAttr plainNoValues() {
        return plainAttr("mobile");
    }

    private static Item item(
            final String intAttrName,
            final String extAttrName,
            final boolean connObjectKey,
            final boolean password) {

        Item item = new Item();
        item.setIntAttrName(intAttrName);
        item.setExtAttrName(extAttrName);
        item.setConnObjectKey(connObjectKey);
        item.setPassword(password);
        item.setPurpose(MappingPurpose.PROPAGATION);
        item.setMandatoryCondition("false");
        return item;
    }

    private static IntAttrName intAttrName(final String name) {
        IntAttrName result = mock(IntAttrName.class);

        if ("key".equals(name)
                || "username".equals(name)
                || "name".equals(name)
                || "mustChangePassword".equals(name)) {

            when(result.getField()).thenReturn(name);
            return result;
        }

        PlainSchema schema = mock(PlainSchema.class);
        when(schema.getKey()).thenReturn(name);
        when(schema.getType()).thenReturn(AttrSchemaType.String);

        when(result.getSchemaInfo()).thenReturn(
                new IntAttrName.SchemaInfo(schema, SchemaType.PLAIN));

        return result;
    }

    private static PlainAttr plainAttr(
            final String schema,
            final String... values) {

        PlainAttr attr = new PlainAttr();
        attr.setSchema(schema);

        for (String value : values) {
            PlainAttrValue attrValue = new PlainAttrValue();
            attrValue.setStringValue(value);
            attr.add(attrValue);
        }

        return attr;
    }
}
