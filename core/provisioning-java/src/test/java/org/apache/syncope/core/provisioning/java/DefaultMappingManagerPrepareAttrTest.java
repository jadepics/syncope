package org.apache.syncope.core.provisioning.java;

import java.util.List;
import org.apache.syncope.common.lib.to.Item;
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
import org.apache.syncope.core.persistence.api.entity.Any;
import org.apache.syncope.core.persistence.api.entity.AnyType;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DefaultMappingManagerPrepareAttrTest {

    /*
     * Oggetto reale sottoposto a test.
     */
    private DefaultMappingManager mappingManager;

    /*
     * Parser simulato: nel sistema reale interpreta il nome
     * dell'attributo interno, ad esempio "email".
     */
    private IntAttrNameParser intAttrNameParser;

    /*
     * Contesto minimo richiesto da prepareAttr().
     */
    private ExternalResource resource;

    private org.apache.syncope.common.lib.to.Provision provision;

    private Any any;

    //
    @BeforeEach
    void setUp() throws Exception {
        intAttrNameParser = mock(IntAttrNameParser.class);

        resource = mock(ExternalResource.class);
        provision = mock(org.apache.syncope.common.lib.to.Provision.class);
        any = mock(Any.class);

        /*
         * prepareAttr() deve conoscere il tipo dell'entità
         * per interpretare correttamente l'intAttrName.
         *
         * Nei test usiamo il tipo USER, ma non creiamo un vero User:
         * i casi non riguardano password o campi specifici dell'utente.
         */
        AnyType anyType = mock(AnyType.class);
        when(anyType.getKind()).thenReturn(AnyTypeKind.USER);
        when(any.getType()).thenReturn(anyType);

        /*
         * Configurazione dinamica del parser.
         *
         * Per ogni nome di schema ricevuto viene prodotto un IntAttrName
         * che rappresenta uno schema PLAIN di tipo String.
         *
         * Esempio:
         * "email" → schema plain String denominato "email".
         */
        when(intAttrNameParser.parse(
                anyString(),
                eq(AnyTypeKind.USER))).
                thenAnswer(invocation -> {

                    String schemaName = invocation.getArgument(0);

                    PlainSchema schema = mock(PlainSchema.class);
                    when(schema.getKey()).thenReturn(schemaName);
                    when(schema.getType()).thenReturn(AttrSchemaType.String);

                    IntAttrName intAttrName = mock(IntAttrName.class);
                    when(intAttrName.getSchemaInfo()).thenReturn(
                            new IntAttrName.SchemaInfo(
                                    schema,
                                    SchemaType.PLAIN));

                    return intAttrName;
                });

        /*
         * DefaultMappingManager è un oggetto reale.
         *
         * Vengono simulate soltanto le dipendenze infrastrutturali
         * non coinvolte in questi casi: DAO, handler, cifratura e JEXL.
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
     * Verifica il caso nominale più semplice:
     * uno schema plain String contiene un solo valore.
     *
     * Il valore interno "Mario" deve diventare il valore
     * dell'attributo ConnId esterno "givenName".
     */
    @Test
    void preparePlainAttributeWithSingleValue() {
        Item item = item("firstname", "givenName");

        MappingManager.PreparedAttr result = prepare(
                item,
                plainAttr("firstname", "Mario"));

        assertNotNull(result);
        assertNull(result.connObjectLink());
        assertNotNull(result.attribute());

        assertEquals("givenName", result.attribute().getName());
        assertEquals(
                List.of("Mario"),
                result.attribute().getValue());
    }

    /**
     * Verifica che tutti i valori di un attributo plain multivalore
     * siano trasferiti all'attributo esterno.
     *
     * Nessuno dei due valori deve essere perso.
     */
    @Test
    void preparePlainAttributeWithMultipleValues() {
        Item item = item(
                "aliases",
                "mailAlternateAddress");

        MappingManager.PreparedAttr result = prepare(
                item,
                plainAttr(
                        "aliases",
                        "mario.rossi@example.org",
                        "mrossi@example.com"));

        assertNotNull(result);
        assertNotNull(result.attribute());

        assertEquals(
                "mailAlternateAddress",
                result.attribute().getName());

        assertEquals(
                List.of(
                        "mario.rossi@example.org",
                        "mrossi@example.com"),
                result.attribute().getValue());
    }

    /**
     * Verifica esplicitamente la corrispondenza tra due nomi differenti:
     *
     * attributo interno: email
     * attributo esterno: mail
     *
     * Il contenuto non cambia, ma il nome utilizzato dal connettore
     * deve essere quello esterno configurato nell'Item.
     */
    @Test
    void mapEmailToMail() {
        Item item = item("email", "mail");

        MappingManager.PreparedAttr result = prepare(
                item,
                plainAttr(
                        "email",
                        "mario.rossi@example.org"));

        assertNotNull(result);
        assertNotNull(result.attribute());

        assertEquals("mail", result.attribute().getName());
        assertEquals(
                List.of("mario.rossi@example.org"),
                result.attribute().getValue());
    }

    /**
     * Verifica il comportamento quando l'attributo interno
     * non possiede alcun valore.
     *
     * In questo caso prepareAttr() costruisce comunque un Attribute
     * ConnId con il corretto nome esterno, ma il valore associato
     * è null, secondo il contratto di AttributeBuilder.build(name).
     */
    @Test
    void prepareOptionalAttributeWithoutValue() {
        Item item = item(
                "mobile",
                "telephoneNumber");

        item.setMandatoryCondition("false");

        /*
         * Il PlainAttrGetter non trova alcun attributo interno
         * corrispondente allo schema "mobile".
         */
        MappingManager.PreparedAttr result = prepare(
                item,
                null);

        assertNotNull(result);
        assertNull(result.connObjectLink());
        assertNotNull(result.attribute());

        assertEquals(
                "telephoneNumber",
                result.attribute().getName());

        /*
         * ConnId rappresenta un attributo creato senza valori
         * mediante getValue() == null, non mediante una lista vuota.
         */
        assertNull(result.attribute().getValue());
    }

    /**
     * Verifica che un Item senza transformer propaghi
     * il valore senza modificarlo.
     *
     * La lista dei transformer dell'Item reale è vuota.
     */
    @Test
    void prepareAttributeWithoutTransformer() {
        Item item = item(
                "department",
                "ou");

        assertTrue(item.getTransformers().isEmpty());

        MappingManager.PreparedAttr result = prepare(
                item,
                plainAttr(
                        "department",
                        "Engineering"));

        assertNotNull(result);
        assertNotNull(result.attribute());

        assertEquals("ou", result.attribute().getName());
        assertEquals(
                List.of("Engineering"),
                result.attribute().getValue());
    }

    /**
     * Verifica il ramo ordinario del metodo.
     *
     * L'Item non rappresenta:
     * - una password;
     * - una connObjectKey.
     *
     * Il risultato deve quindi essere un normale attributo ConnId,
     * non una password e non il solo valore della chiave remota.
     */
    @Test
    void prepareOrdinaryNonPasswordItem() {
        Item item = item("title", "title");

        /*
         * Le proprietà vengono controllate esplicitamente per rendere
         * evidente la classe di equivalenza del test.
         */
        assertFalse(item.isPassword());
        assertFalse(item.isConnObjectKey());

        MappingManager.PreparedAttr result = prepare(
                item,
                plainAttr(
                        "title",
                        "Software Engineer"));

        assertNotNull(result);

        /*
         * Le connObjectKey vengono restituite in connObjectLink
         * senza un normale Attribute. Qui deve accadere l'opposto.
         */
        assertNull(result.connObjectLink());
        assertNotNull(result.attribute());

        assertEquals("title", result.attribute().getName());
        assertEquals(
                List.of("Software Engineer"),
                result.attribute().getValue());
    }

    /**
     * Invoca il vero metodo prepareAttr().
     *
     * Il PlainAttrGetter è una piccola implementazione controllata:
     * restituisce l'attributo soltanto quando viene richiesto
     * lo schema configurato nell'Item.
     */
    private MappingManager.PreparedAttr prepare(
            final Item item,
            final PlainAttr plainAttr) {

        PlainAttrGetter plainAttrGetter =
                (attributable, schemaName) ->
                        item.getIntAttrName().equals(schemaName)
                                ? plainAttr
                                : null;

        return mappingManager.prepareAttr(
                resource,
                provision,
                item,
                any,
                null,
                AccountGetter.DEFAULT,
                AccountGetter.DEFAULT,
                plainAttrGetter);
    }

    /**
     * Crea un vero mapping Item minimale.
     *
     * intAttrName identifica il dato interno di Syncope.
     * extAttrName identifica il campo della risorsa esterna.
     *
     * L'Item non è né password né connObjectKey.
     * La collezione dei transformer rimane vuota.
     */
    private Item item(
            final String intAttrName,
            final String extAttrName) {

        Item item = new Item();
        item.setIntAttrName(intAttrName);
        item.setExtAttrName(extAttrName);
        item.setConnObjectKey(false);
        item.setPassword(false);

        return item;
    }

    /**
     * Crea un vero PlainAttr di Syncope con zero, uno o più valori.
     *
     * I valori principali del test non sono mock:
     * vengono utilizzati PlainAttr e PlainAttrValue reali.
     */
    private PlainAttr plainAttr(
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