package org.apache.syncope.core.provisioning.java;

import org.apache.syncope.common.lib.Attr;
import org.apache.syncope.common.lib.to.AnyTO;
import org.apache.syncope.common.lib.to.Item;
import org.apache.syncope.common.lib.to.RealmTO;
import org.apache.syncope.common.lib.to.UserTO;
import org.apache.syncope.common.lib.types.AnyTypeKind;
import org.apache.syncope.common.lib.types.AttrSchemaType;
import org.apache.syncope.common.lib.types.SchemaType;
import org.apache.syncope.core.persistence.api.EncryptorManager;
import org.apache.syncope.core.persistence.api.dao.AnyObjectDAO;
import org.apache.syncope.core.persistence.api.dao.GroupDAO;
import org.apache.syncope.core.persistence.api.dao.ImplementationDAO;
import org.apache.syncope.core.persistence.api.dao.RealmSearchDAO;
import org.apache.syncope.core.persistence.api.dao.RelationshipTypeDAO;
import org.apache.syncope.core.persistence.api.dao.UserDAO;
import org.apache.syncope.core.persistence.api.entity.PlainSchema;
import org.apache.syncope.core.provisioning.api.DerAttrHandler;
import org.apache.syncope.core.provisioning.api.IntAttrName;
import org.apache.syncope.core.provisioning.api.IntAttrNameParser;
import org.apache.syncope.core.provisioning.api.jexl.JexlTools;
import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.AttributeBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test manuali di livello intermedio per i due overload
 * di DefaultMappingManager.setIntValues().
 *
 * I test modificano una sola condizione alla volta e condividono
 * soltanto la configurazione indispensabile.
 */
class DefaultMappingManagerSetIntValuesMidLevelTest {

    /**
     * Parser simulato.
     *
     * setIntValues() riceve nell'Item il nome interno come stringa
     * e utilizza questo componente per trasformarlo in IntAttrName.
     */
    private IntAttrNameParser intAttrNameParser;

    /**
     * Vera istanza della classe sottoposta a test.
     */
    private DefaultMappingManager mappingManager;

    @BeforeEach
    void setUp() {
        intAttrNameParser = mock(IntAttrNameParser.class);

        /*
         * Le dipendenze infrastrutturali non sono necessarie
         * per i casi di questa suite e vengono quindi simulate.
         */
        mappingManager = new DefaultMappingManager(
                mock(UserDAO.class),
                mock(AnyObjectDAO.class),
                mock(GroupDAO.class),
                mock(RelationshipTypeDAO.class),
                mock(RealmSearchDAO.class),
                mock(ImplementationDAO.class),
                mock(DerAttrHandler.class),
                intAttrNameParser,
                mock(EncryptorManager.class),
                mock(JexlTools.class));
    }

    /**
     * Verifica l'inserimento di un valore Boolean
     * in un attributo plain di UserTO.
     *
     * Il valore ConnId Boolean viene trasferito nel Transfer Object
     * usando la sua rappresentazione testuale "true".
     */
    @Test
    void insertBooleanValue() throws Exception {
        UserTO userTO = new UserTO();

        Item item = plainItem(
                "active",
                "active",
                AttrSchemaType.Boolean);

        Attribute attribute =
                AttributeBuilder.build("active", true);

        mappingManager.setIntValues(item, attribute, userTO);

        Attr inserted = findPlainAttr(userTO, "active");

        assertEquals(1, inserted.getValues().size());
        assertEquals("true", inserted.getValues().getFirst());
    }

    /**
     * Verifica l'inserimento di un valore Long.
     *
     * Anche in questo caso il valore viene memorizzato
     * nel Transfer Object sotto forma di stringa.
     */
    @Test
    void insertLongValue() throws Exception {
        UserTO userTO = new UserTO();

        Item item = plainItem(
                "employeeNumber",
                "employeeNumber",
                AttrSchemaType.Long);

        Attribute attribute =
                AttributeBuilder.build("employeeNumber", 42L);

        mappingManager.setIntValues(item, attribute, userTO);

        Attr inserted =
                findPlainAttr(userTO, "employeeNumber");

        assertEquals(1, inserted.getValues().size());
        assertEquals("42", inserted.getValues().getFirst());
    }

    /**
     * Verifica l'inserimento di un valore Date.
     *
     * ConnId non gestisce direttamente OffsetDateTime come tipo
     * nativo del mapping; viene quindi utilizzata una data
     * già rappresentata in forma testuale.
     */
    @Test
    void insertDateValue() throws Exception {
        UserTO userTO = new UserTO();

        Item item = plainItem(
                "registrationDate",
                "registrationDate",
                AttrSchemaType.Date);

        String date =
                "2026-08-04T10:30:00+02:00";

        Attribute attribute =
                AttributeBuilder.build("registrationDate", date);

        mappingManager.setIntValues(item, attribute, userTO);

        Attr inserted =
                findPlainAttr(userTO, "registrationDate");

        assertEquals(1, inserted.getValues().size());
        assertEquals(date, inserted.getValues().getFirst());
    }

    /**
     * Verifica l'aggiornamento del campo speciale
     * mustChangePassword di UserTO.
     *
     * Il valore iniziale è false e deve diventare true.
     */
    @Test
    void updateMustChangePassword() throws Exception {
        UserTO userTO = new UserTO();
        userTO.setMustChangePassword(false);

        Item item = userFieldItem("mustChangePassword");

        Attribute attribute =
                AttributeBuilder.build(
                        "mustChangePassword",
                        true);

        assertFalse(userTO.isMustChangePassword());

        mappingManager.setIntValues(item, attribute, userTO);

        assertTrue(userTO.isMustChangePassword());
    }

    /**
     * Verifica l'aggiornamento di un altro campo speciale.
     *
     * È stato scelto uManager, così il test non ripete
     * il precedente caso base dedicato allo username.
     */
    @Test
    void updateSpecialField() throws Exception {
        UserTO userTO = new UserTO();
        userTO.setUManager("old-manager");

        Item item = userFieldItem("uManager");

        Attribute attribute =
                AttributeBuilder.build(
                        "uManager",
                        "new-manager");

        mappingManager.setIntValues(item, attribute, userTO);

        assertEquals(
                "new-manager",
                userTO.getUManager());
    }

    /**
     * Verifica l'azzeramento del nome di un RealmTO.
     *
     * L'attributo ConnId non contiene valori.
     * Per il campo name dell'overload RealmTO,
     * setIntValues() imposta esplicitamente null.
     */
    @Test
    void removeValueWithEmptyAttribute() throws Exception {
        RealmTO realmTO = new RealmTO();
        realmTO.setName("old-realm-name");

        Item item = realmFieldItem("name");

        /*
         * AttributeBuilder.build(nome) crea un attributo
         * senza valori.
         */
        Attribute emptyAttribute =
                AttributeBuilder.build("name");

        mappingManager.setIntValues(
                item,
                emptyAttribute,
                realmTO);

        assertNull(realmTO.getName());
    }

    /**
     * Item è indispensabile per determinare
     * quale attributo interno deve essere aggiornato.
     *
     * L'implementazione corrente genera NullPointerException.
     */
    @Test
    void nullItem() {
        UserTO userTO = new UserTO();

        Attribute attribute =
                AttributeBuilder.build("active", true);

        assertThrows(
                NullPointerException.class,
                () -> mappingManager.setIntValues(
                        null,
                        attribute,
                        userTO));
    }

    /**
     * Un Attribute null viene trattato come assenza di valori.
     *
     * Nel caso di un attributo plain di AnyTO il metodo
     * termina senza inserire alcun attributo.
     */
    @Test
    void nullAttribute() throws Exception {
        UserTO userTO = new UserTO();

        Item item = plainItem(
                "active",
                "active",
                AttrSchemaType.Boolean);

        assertDoesNotThrow(
                () -> mappingManager.setIntValues(
                        item,
                        null,
                        userTO));

        assertTrue(userTO.getPlainAttrs().isEmpty());
    }

    /**
     * AnyTO è necessario per determinare il tipo di entità
     * e per applicare il valore ottenuto.
     *
     * L'implementazione corrente genera NullPointerException.
     */
    @Test
    void nullAnyTO() {
        Item item = new Item();
        item.setIntAttrName("active");

        Attribute attribute =
                AttributeBuilder.build("active", true);

        assertThrows(
                NullPointerException.class,
                () -> mappingManager.setIntValues(
                        item,
                        attribute,
                        (AnyTO) null));
    }

    /**
     * RealmTO è necessario per applicare il valore recuperato.
     *
     * Il parser riesce a riconoscere il campo name,
     * ma l'aggiornamento non può essere eseguito
     * su un oggetto target null.
     */
    @Test
    void nullRealmTO() throws Exception {
        Item item = realmFieldItem("name");

        Attribute attribute =
                AttributeBuilder.build(
                        "name",
                        "new-realm");

        assertThrows(
                NullPointerException.class,
                () -> mappingManager.setIntValues(
                        item,
                        attribute,
                        (RealmTO) null));
    }

    /**
     * Prepara un Item relativo a uno schema plain di UserTO.
     *
     * Vengono creati soltanto gli oggetti necessari
     * per far riconoscere al parser:
     *
     * - il nome dello schema;
     * - il tipo plain;
     * - il tipo del valore.
     */
    private Item plainItem(
            final String internalName,
            final String schemaName,
            final AttrSchemaType schemaType)
            throws Exception {

        Item item = new Item();
        item.setIntAttrName(internalName);

        PlainSchema schema = mock(PlainSchema.class);

        when(schema.getKey()).thenReturn(schemaName);
        when(schema.getType()).thenReturn(schemaType);

        IntAttrName intAttrName =
                mock(IntAttrName.class);

        when(intAttrName.getSchemaInfo()).thenReturn(
                new IntAttrName.SchemaInfo(
                        schema,
                        SchemaType.PLAIN));

        when(intAttrNameParser.parse(
                internalName,
                AnyTypeKind.USER)).
                thenReturn(intAttrName);

        return item;
    }

    /**
     * Prepara un Item relativo a un campo speciale di UserTO.
     *
     * Esempi:
     * - mustChangePassword;
     * - uManager;
     * - username.
     */
    private Item userFieldItem(
            final String field)
            throws Exception {

        Item item = new Item();
        item.setIntAttrName(field);

        IntAttrName intAttrName =
                mock(IntAttrName.class);

        when(intAttrName.getField()).
                thenReturn(field);

        when(intAttrNameParser.parse(
                field,
                AnyTypeKind.USER)).
                thenReturn(intAttrName);

        return item;
    }

    /**
     * Prepara un Item relativo a un campo speciale di RealmTO.
     *
     * L'overload per Realm utilizza la versione del parser
     * priva del parametro AnyTypeKind.
     */
    private Item realmFieldItem(
            final String field)
            throws Exception {

        Item item = new Item();
        item.setIntAttrName(field);

        IntAttrName intAttrName =
                mock(IntAttrName.class);

        when(intAttrName.getField()).
                thenReturn(field);

        when(intAttrNameParser.parse(field)).
                thenReturn(intAttrName);

        return item;
    }

    /**
     * Recupera dal Transfer Object l'attributo plain
     * con lo schema richiesto.
     *
     * Se l'attributo non è stato inserito, il test fallisce
     * direttamente tramite orElseThrow().
     */
    private Attr findPlainAttr(
            final AnyTO anyTO,
            final String schema) {

        return anyTO.getPlainAttr(schema).
                orElseThrow();
    }
}