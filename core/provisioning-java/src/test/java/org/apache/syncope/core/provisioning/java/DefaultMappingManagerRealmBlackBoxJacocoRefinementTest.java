package org.apache.syncope.core.provisioning.java;

import java.util.List;
import java.util.Optional;
import org.apache.syncope.common.lib.to.Item;
import org.apache.syncope.common.lib.types.AttrSchemaType;
import org.apache.syncope.common.lib.types.SchemaType;
import org.apache.syncope.core.persistence.api.EncryptorManager;
import org.apache.syncope.core.persistence.api.dao.AnyObjectDAO;
import org.apache.syncope.core.persistence.api.dao.GroupDAO;
import org.apache.syncope.core.persistence.api.dao.ImplementationDAO;
import org.apache.syncope.core.persistence.api.dao.RealmSearchDAO;
import org.apache.syncope.core.persistence.api.dao.RelationshipTypeDAO;
import org.apache.syncope.core.persistence.api.dao.UserDAO;
import org.apache.syncope.core.persistence.api.entity.DerSchema;
import org.apache.syncope.core.persistence.api.entity.ExternalResource;
import org.apache.syncope.core.persistence.api.entity.PlainAttr;
import org.apache.syncope.core.persistence.api.entity.PlainAttrValue;
import org.apache.syncope.core.persistence.api.entity.PlainSchema;
import org.apache.syncope.core.persistence.api.entity.Realm;
import org.apache.syncope.core.provisioning.api.DerAttrHandler;
import org.apache.syncope.core.provisioning.api.IntAttrName;
import org.apache.syncope.core.provisioning.api.IntAttrNameParser;
import org.apache.syncope.core.provisioning.api.MappingManager;
import org.apache.syncope.core.provisioning.api.jexl.JexlTools;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Raffinamento black-box dei test di DefaultMappingManager
 * dedicato agli overload pubblici che ricevono un Realm.
 *
 * I casi sono ricavati dalle classi di equivalenza:
 *
 * - name del Realm;
 * - fullPath del Realm;
 * - attributo plain multivalore;
 * - attributo derivato;
 * - item ordinario;
 * - item connObjectKey.
 *
 * Non vengono utilizzati spy e non vengono verificate
 * chiamate o condizioni interne della classe.
 */
class DefaultMappingManagerRealmBlackBoxRefinementTest {

    private DefaultMappingManager mappingManager;

    private IntAttrNameParser intAttrNameParser;

    private DerAttrHandler derAttrHandler;

    private ExternalResource resource;

    @BeforeEach
    void setUp() {
        intAttrNameParser = mock(IntAttrNameParser.class);
        derAttrHandler = mock(DerAttrHandler.class);
        resource = mock(ExternalResource.class);

        /*
         * Le dipendenze infrastrutturali vengono simulate.
         * La vera istanza di DefaultMappingManager viene
         * invece eseguita in tutti i test.
         */
        mappingManager = new DefaultMappingManager(
                mock(UserDAO.class),
                mock(AnyObjectDAO.class),
                mock(GroupDAO.class),
                mock(RelationshipTypeDAO.class),
                mock(RealmSearchDAO.class),
                mock(ImplementationDAO.class),
                derAttrHandler,
                intAttrNameParser,
                mock(EncryptorManager.class),
                mock(JexlTools.class));
    }

    /**
     * Classe di equivalenza:
     * campo pubblico "name" di un Realm.
     *
     * L'oracolo controlla soltanto il valore restituito.
     */
    @Test
    void getRealmName() {
        Realm realm = mock(Realm.class);
        when(realm.getName()).thenReturn("engineering");

        Item item = item("name", "name");

        IntAttrName intAttrName = fieldIntAttrName("name");

        MappingManager.IntValues result =
                mappingManager.getIntValues(
                        resource,
                        item,
                        intAttrName,
                        AttrSchemaType.String,
                        realm);

        assertEquals(AttrSchemaType.String, result.attrSchemaType());
        assertEquals(1, result.values().size());
        assertEquals(
                "engineering",
                result.values().getFirst().getStringValue());
    }

    /**
     * Classe di equivalenza:
     * fullPath di un Realm figlio.
     */
    @Test
    void getRealmFullPath() {
        Realm realm = mock(Realm.class);
        when(realm.getFullPath()).
                thenReturn("/company/engineering");

        Item item = item("fullPath", "path");

        IntAttrName intAttrName =
                fieldIntAttrName("fullPath");

        MappingManager.IntValues result =
                mappingManager.getIntValues(
                        resource,
                        item,
                        intAttrName,
                        AttrSchemaType.String,
                        realm);

        assertEquals(1, result.values().size());
        assertEquals(
                "/company/engineering",
                result.values().getFirst().getStringValue());
    }

    /**
     * Classi di equivalenza:
     *
     * - schema plain;
     * - Realm con attributi propri;
     * - dato presente con più valori.
     */
    @Test
    void getRealmPlainAttributeWithMultipleValues() {
        Realm realm = mock(Realm.class);

        PlainSchema schema = mock(PlainSchema.class);
        when(schema.getKey()).thenReturn("allowedRegion");
        when(schema.getType()).thenReturn(AttrSchemaType.String);

        PlainAttr plainAttr = new PlainAttr();
        plainAttr.setSchema("allowedRegion");
        plainAttr.add(stringValue("north"));
        plainAttr.add(stringValue("south"));

        when(realm.getPlainAttr("allowedRegion")).
                thenReturn(Optional.of(plainAttr));

        Item item = item(
                "allowedRegion",
                "externalRegion");

        IntAttrName intAttrName =
                schemaIntAttrName(
                        schema,
                        SchemaType.PLAIN);

        MappingManager.IntValues result =
                mappingManager.getIntValues(
                        resource,
                        item,
                        intAttrName,
                        AttrSchemaType.String,
                        realm);

        assertEquals(2, result.values().size());

        List<String> values = result.values().stream().
                map(PlainAttrValue::getStringValue).
                toList();

        assertEquals(
                List.of("north", "south"),
                values);
    }

    /**
     * Classi di equivalenza:
     *
     * - schema derivato;
     * - valore derivato presente;
     * - cardinalità singola.
     *
     * Il calcolo del valore derivato appartiene al collaboratore
     * DerAttrHandler; il test osserva esclusivamente ciò che
     * getIntValues restituisce al chiamante.
     */
    @Test
    void getRealmDerivedAttribute() {
        Realm realm = mock(Realm.class);

        DerSchema schema = mock(DerSchema.class);
        when(schema.getKey()).thenReturn("displayPath");

        when(derAttrHandler.getValue(realm, schema)).
                thenReturn("Company / Engineering");

        Item item = item(
                "displayPath",
                "displayPath");

        IntAttrName intAttrName =
                schemaIntAttrName(
                        schema,
                        SchemaType.DERIVED);

        MappingManager.IntValues result =
                mappingManager.getIntValues(
                        resource,
                        item,
                        intAttrName,
                        AttrSchemaType.String,
                        realm);

        assertEquals(1, result.values().size());
        assertEquals(
                "Company / Engineering",
                result.values().getFirst().getStringValue());
    }

    /**
     * Classi di equivalenza:
     *
     * - prepareAttr su Realm;
     * - item ordinario;
     * - schema plain;
     * - rinomina tra attributo interno ed esterno;
     * - valore singolo presente.
     */
    @Test
    void prepareOrdinaryRealmAttribute() throws Exception {
        Realm realm = mock(Realm.class);

        PlainSchema schema = mock(PlainSchema.class);
        when(schema.getKey()).thenReturn("description");
        when(schema.getType()).thenReturn(AttrSchemaType.String);

        PlainAttr plainAttr = new PlainAttr();
        plainAttr.setSchema("description");
        plainAttr.add(stringValue("Engineering realm"));

        when(realm.getPlainAttr("description")).
                thenReturn(Optional.of(plainAttr));

        Item item = item(
                "description",
                "externalDescription");

        IntAttrName intAttrName =
                schemaIntAttrName(
                        schema,
                        SchemaType.PLAIN);

        /*
         * Il parser è una dipendenza esterna del metodo.
         * Configuriamo il significato pubblico dell'espressione
         * "description", senza controllare come venga analizzata.
         */
        when(intAttrNameParser.parse("description")).
                thenReturn(intAttrName);

        MappingManager.PreparedAttr result =
                mappingManager.prepareAttr(
                        resource,
                        item,
                        realm);

        assertNotNull(result);
        assertNull(result.connObjectLink());

        assertNotNull(result.attribute());
        assertEquals(
                "externalDescription",
                result.attribute().getName());

        assertEquals(
                List.of("Engineering realm"),
                result.attribute().getValue());
    }

    /**
     * Classi di equivalenza:
     *
     * - item connObjectKey;
     * - chiave ricavata dal fullPath;
     * - valore presente e non vuoto.
     *
     * L'oracolo osserva il PreparedAttr pubblico restituito.
     */
    @Test
    void prepareRealmConnObjectKey() throws Exception {
        Realm realm = mock(Realm.class);
        when(realm.getFullPath()).
                thenReturn("/company/engineering");

        Item item = item(
                "fullPath",
                "uid");

        item.setConnObjectKey(true);

        IntAttrName intAttrName =
                fieldIntAttrName("fullPath");

        when(intAttrNameParser.parse("fullPath")).
                thenReturn(intAttrName);

        MappingManager.PreparedAttr result =
                mappingManager.prepareAttr(
                        resource,
                        item,
                        realm);

        assertNotNull(result);

        /*
         * La chiave remota viene restituita tramite
         * il campo pubblico connObjectLink del record.
         */
        assertEquals(
                "/company/engineering",
                result.connObjectLink());

        /*
         * Un item usato come chiave non rappresenta
         * contemporaneamente un normale attributo esterno.
         */
        assertNull(result.attribute());
    }

    /**
     * Crea un normale mapping Item con nome interno
     * e nome utilizzato dalla risorsa esterna.
     */
    private Item item(
            final String internalName,
            final String externalName) {

        Item item = new Item();
        item.setIntAttrName(internalName);
        item.setExtAttrName(externalName);

        return item;
    }

    /**
     * Crea un IntAttrName che rappresenta un campo
     * pubblico del Realm, come name o fullPath.
     */
    private IntAttrName fieldIntAttrName(
            final String field) {

        IntAttrName intAttrName =
                mock(IntAttrName.class);

        when(intAttrName.getField()).
                thenReturn(field);

        return intAttrName;
    }

    /**
     * Crea un IntAttrName associato a uno schema
     * plain oppure derivato.
     */
    private IntAttrName schemaIntAttrName(
            final org.apache.syncope.core.persistence.api.entity.Schema schema,
            final SchemaType schemaType) {

        IntAttrName intAttrName =
                mock(IntAttrName.class);

        when(intAttrName.getSchemaInfo()).
                thenReturn(new IntAttrName.SchemaInfo(
                        schema,
                        schemaType));

        return intAttrName;
    }

    /**
     * Crea un valore plain String.
     */
    private PlainAttrValue stringValue(
            final String value) {

        PlainAttrValue attrValue =
                new PlainAttrValue();

        attrValue.setStringValue(value);
        return attrValue;
    }
}