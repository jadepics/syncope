/*
 questi test condividono quasi tutto il codice
  e coprono quattro classi di equivalenza
 attributo presente → un valore
attributo presente → più valori
attributo presente → zero valori
attributo non presente
 */
package org.apache.syncope.core.provisioning.java;

import java.util.List;
import org.apache.syncope.common.lib.to.Item;
import org.apache.syncope.common.lib.to.Provision;
import org.apache.syncope.common.lib.types.AttrSchemaType;
import org.apache.syncope.common.lib.types.SchemaType;
import org.apache.syncope.core.persistence.api.EncryptorManager;
import org.apache.syncope.core.persistence.api.dao.AnyObjectDAO;
import org.apache.syncope.core.persistence.api.dao.GroupDAO;
import org.apache.syncope.core.persistence.api.dao.ImplementationDAO;
import org.apache.syncope.core.persistence.api.dao.RealmSearchDAO;
import org.apache.syncope.core.persistence.api.dao.RelationshipTypeDAO;
import org.apache.syncope.core.persistence.api.dao.UserDAO;
import org.apache.syncope.core.persistence.api.entity.Any;
import org.apache.syncope.core.persistence.api.entity.ExternalResource;
import org.apache.syncope.core.persistence.api.entity.PlainAttr;
import org.apache.syncope.core.persistence.api.entity.PlainAttrValue;
import org.apache.syncope.core.persistence.api.entity.PlainSchema;
import org.apache.syncope.core.provisioning.api.AccountGetter;
import org.apache.syncope.core.provisioning.api.DerAttrHandler;
import org.apache.syncope.core.provisioning.api.IntAttrName;
import org.apache.syncope.core.provisioning.api.IntAttrNameParser;
import org.apache.syncope.core.provisioning.api.MappingManager;
import org.apache.syncope.core.provisioning.api.PlainAttrGetter;
import org.apache.syncope.core.provisioning.api.jexl.JexlTools;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.apache.syncope.core.persistence.api.entity.user.User;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DefaultMappingManagerGetIntValuesTest {

    private static final String SCHEMA = "surname";

    private DefaultMappingManager mappingManager;

    private ExternalResource resource;

    private Provision provision;

    private Any any;

    @BeforeEach
    void setUp() {
        mappingManager = new DefaultMappingManager(
                mock(UserDAO.class),
                mock(AnyObjectDAO.class),
                mock(GroupDAO.class),
                mock(RelationshipTypeDAO.class),
                mock(RealmSearchDAO.class),
                mock(ImplementationDAO.class),
                mock(DerAttrHandler.class),
                mock(IntAttrNameParser.class),
                mock(EncryptorManager.class),
                mock(JexlTools.class));

        resource = mock(ExternalResource.class);
        provision = mock(Provision.class);
        any = mock(Any.class);
    }

    @Test
    void plainStringWithSingleValue() {
        MappingManager.IntValues result =
                executeGetIntValues(plainAttr("Rossi"));

        assertEquals(AttrSchemaType.String, result.attrSchemaType());
        assertEquals(1, result.values().size());
        assertEquals("Rossi", result.values().getFirst().getStringValue());
    }

    @Test
    void plainStringWithMultipleValues() {
        MappingManager.IntValues result =
                executeGetIntValues(plainAttr("Rossi", "Verdi"));

        List<String> actualValues = result.values().stream().
                map(PlainAttrValue::getStringValue).
                toList();

        assertEquals(List.of("Rossi", "Verdi"), actualValues);
    }

    @Test
    void plainAttributePresentButEmpty() {
        MappingManager.IntValues result =
                executeGetIntValues(plainAttr());

        assertTrue(result.values().isEmpty());
    }

    @Test
    void plainAttributeNotPresent() {
        MappingManager.IntValues result =
                executeGetIntValues(null);

        assertTrue(result.values().isEmpty());
    }

    private MappingManager.IntValues executeGetIntValues(
            final PlainAttr plainAttr) {

        Item item = mock(Item.class);
        when(item.getIntAttrName()).thenReturn(SCHEMA);
        when(item.getTransformers()).thenReturn(List.of());

        PlainSchema schema = mock(PlainSchema.class);
        when(schema.getKey()).thenReturn(SCHEMA);

        IntAttrName intAttrName = mock(IntAttrName.class);
        when(intAttrName.getSchemaInfo()).thenReturn(
                new IntAttrName.SchemaInfo(schema, SchemaType.PLAIN));

        PlainAttrGetter plainAttrGetter = (attributable, schemaName) ->
                SCHEMA.equals(schemaName) ? plainAttr : null;

        return mappingManager.getIntValues(
                resource,
                provision,
                item,
                intAttrName,
                AttrSchemaType.String,
                any,
                AccountGetter.DEFAULT,
                plainAttrGetter);
    }
    private MappingManager.IntValues executeGetFieldValues(
            final String field,
            final Any reference) {

        Item item = mock(Item.class);
        when(item.getIntAttrName()).thenReturn(field);
        when(item.getTransformers()).thenReturn(List.of());

        IntAttrName intAttrName = mock(IntAttrName.class);
        when(intAttrName.getField()).thenReturn(field);

        return mappingManager.getIntValues(
                resource,
                provision,
                item,
                intAttrName,
                AttrSchemaType.String,
                reference,
                AccountGetter.DEFAULT,
                (attributable, schemaName) -> null);
    }

    private PlainAttr plainAttr(final String... values) {
        PlainAttr attr = new PlainAttr();
        attr.setSchema(SCHEMA);

        for (String value : values) {
            PlainAttrValue attrValue = new PlainAttrValue();
            attrValue.setStringValue(value);
            attr.add(attrValue);
        }

        return attr;
    }
/*
* nuovi 2 test
* testiamo username e key sulla base che key è un campo speciale recuperato
* tramite Any, username viene restituito solo quando l'entità implementa
* account
* */

@Test
void specialFieldUsername() {
    User user = mock(User.class);
    when(user.getUsername()).thenReturn("mrossi");

    MappingManager.IntValues result =
            executeGetFieldValues("username", user);

    assertEquals(AttrSchemaType.String, result.attrSchemaType());
    assertEquals(1, result.values().size());
    assertEquals("mrossi", result.values().getFirst().getStringValue());
}

@Test
void specialFieldKey() {
    when(any.getKey()).thenReturn("any-key-001");

    MappingManager.IntValues result =
            executeGetFieldValues("key", any);

    assertEquals(AttrSchemaType.String, result.attrSchemaType());
    assertEquals(1, result.values().size());
    assertEquals("any-key-001", result.values().getFirst().getStringValue());
}
}