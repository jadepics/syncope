/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0.
 */
package org.apache.syncope.core.provisioning.java;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import org.apache.syncope.common.lib.to.Item;
import org.apache.syncope.common.lib.to.Mapping;
import org.apache.syncope.common.lib.to.Provision;
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
import org.apache.syncope.core.persistence.api.entity.DerSchema;
import org.apache.syncope.core.persistence.api.entity.ExternalResource;
import org.apache.syncope.core.persistence.api.entity.PlainAttr;
import org.apache.syncope.core.persistence.api.entity.PlainAttrValue;
import org.apache.syncope.core.persistence.api.entity.PlainSchema;
import org.apache.syncope.core.persistence.api.entity.RelationshipType;
import org.apache.syncope.core.persistence.api.entity.anyobject.AnyObject;
import org.apache.syncope.core.persistence.api.entity.group.Group;
import org.apache.syncope.core.persistence.api.entity.user.Account;
import org.apache.syncope.core.persistence.api.entity.user.UMembership;
import org.apache.syncope.core.persistence.api.entity.user.URelationship;
import org.apache.syncope.core.persistence.api.entity.user.User;
import org.apache.syncope.core.provisioning.api.AccountGetter;
import org.apache.syncope.core.provisioning.api.DerAttrHandler;
import org.apache.syncope.core.provisioning.api.IntAttrName;
import org.apache.syncope.core.provisioning.api.IntAttrNameParser;
import org.apache.syncope.core.provisioning.api.MappingManager;
import org.apache.syncope.core.provisioning.api.PlainAttrGetter;
import org.apache.syncope.core.provisioning.api.jexl.JexlTools;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DefaultMappingManagerGetIntValuesMidLevelTest {

    private static final String SCHEMA = "value";

    /*
     * Oggetto reale sottoposto a test.
     */
    private DefaultMappingManager mappingManager;

    /*
     * DAO simulati e configurati soltanto nei test
     * che richiedono la ricerca di altre entità.
     */
    private UserDAO userDAO;

    private AnyObjectDAO anyObjectDAO;

    private GroupDAO groupDAO;

    private RelationshipTypeDAO relationshipTypeDAO;

    /*
     * Handler simulato per i valori derivati.
     */
    private DerAttrHandler derAttrHandler;

    /*
     * Contesto comune della chiamata.
     */
    private ExternalResource resource;

    private Provision provision;

    private Item item;

    @BeforeEach
    void setUp() {
        userDAO = mock(UserDAO.class);
        anyObjectDAO = mock(AnyObjectDAO.class);
        groupDAO = mock(GroupDAO.class);
        relationshipTypeDAO = mock(RelationshipTypeDAO.class);
        derAttrHandler = mock(DerAttrHandler.class);

        resource = mock(ExternalResource.class);
        provision = mock(Provision.class);
        item = mock(Item.class);

        /*
         * Nessun transformer: in questa sezione vogliamo verificare
         * soltanto il recupero dei valori interni.
         */
        when(item.getIntAttrName()).thenReturn(SCHEMA);
        when(item.getTransformers()).thenReturn(List.of());

        mappingManager = new DefaultMappingManager(
                userDAO,
                anyObjectDAO,
                groupDAO,
                relationshipTypeDAO,
                mock(RealmSearchDAO.class),
                mock(ImplementationDAO.class),
                derAttrHandler,
                mock(IntAttrNameParser.class),
                mock(EncryptorManager.class),
                mock(JexlTools.class));
    }

    /**
     * Verifica il recupero di un attributo plain Boolean.
     */
    @Test
    void plainBooleanAttribute() {
        Any source = mock(Any.class);

        MappingManager.IntValues result = execute(
                plainIntAttrName(SCHEMA, AttrSchemaType.Boolean),
                AttrSchemaType.Boolean,
                source,
                AccountGetter.DEFAULT,
                getterReturning(plainAttr(
                        SCHEMA,
                        booleanValue(true))));

        assertEquals(AttrSchemaType.Boolean, result.attrSchemaType());
        assertEquals(Boolean.TRUE, onlyValue(result).getBooleanValue());
    }

    /**
     * Verifica il recupero di un attributo plain Long.
     */
    @Test
    void plainLongAttribute() {
        Any source = mock(Any.class);

        MappingManager.IntValues result = execute(
                plainIntAttrName(SCHEMA, AttrSchemaType.Long),
                AttrSchemaType.Long,
                source,
                AccountGetter.DEFAULT,
                getterReturning(plainAttr(
                        SCHEMA,
                        longValue(42L))));

        assertEquals(AttrSchemaType.Long, result.attrSchemaType());
        assertEquals(42L, onlyValue(result).getLongValue());
    }

    /**
     * Verifica il recupero di un attributo plain Date.
     *
     * Syncope rappresenta internamente la data mediante OffsetDateTime.
     */
    @Test
    void plainDateAttribute() {
        Any source = mock(Any.class);

        OffsetDateTime expected =
                OffsetDateTime.parse("2026-08-04T10:30:00+02:00");

        MappingManager.IntValues result = execute(
                plainIntAttrName(SCHEMA, AttrSchemaType.Date),
                AttrSchemaType.Date,
                source,
                AccountGetter.DEFAULT,
                getterReturning(plainAttr(
                        SCHEMA,
                        dateValue(expected))));

        assertEquals(AttrSchemaType.Date, result.attrSchemaType());
        assertEquals(expected, onlyValue(result).getDateValue());
    }

    /**
     * Verifica il trattamento di un attributo derivato fullname.
     *
     * Non stiamo testando il motore dell'espressione:
     * DerAttrHandler viene simulato e restituisce il risultato
     * già calcolato "Mario Rossi".
     */
    @Test
    void derivedFullnameAttribute() {
        Any source = mock(Any.class);
        DerSchema fullname = mock(DerSchema.class);

        when(fullname.getKey()).thenReturn("fullname");
        when(fullname.getExpression()).
                thenReturn("firstname + ' ' + surname");

        when(derAttrHandler.getValue(source, fullname)).
                thenReturn("Mario Rossi");

        MappingManager.IntValues result = execute(
                derivedIntAttrName(fullname),
                AttrSchemaType.String,
                source,
                AccountGetter.DEFAULT,
                getterReturning(null));

        assertEquals(
                "Mario Rossi",
                onlyValue(result).getStringValue());
    }

    /**
     * Simula un attributo derivato che non può essere calcolato,
     * per esempio perché uno dei valori sorgente è assente.
     *
     * DerAttrHandler restituisce null e il manager non deve
     * produrre alcun valore.
     */
    @Test
    void derivedAttributeWithMissingSourceValue() {
        Any source = mock(Any.class);
        DerSchema fullname = mock(DerSchema.class);

        when(fullname.getKey()).thenReturn("fullname");
        when(fullname.getExpression()).
                thenReturn("firstname + ' ' + surname");

        when(derAttrHandler.getValue(source, fullname)).
                thenReturn(null);

        MappingManager.IntValues result = execute(
                derivedIntAttrName(fullname),
                AttrSchemaType.String,
                source,
                AccountGetter.DEFAULT,
                getterReturning(null));

        assertTrue(result.values().isEmpty());
    }

    /**
     * Rappresenta uno schema inesistente o non risolto.
     *
     * IntAttrName non contiene né un campo speciale
     * né informazioni relative a uno schema.
     */
    @Test
    void nonexistentSchema() {
        Any source = mock(Any.class);
        IntAttrName unresolved = mock(IntAttrName.class);

        MappingManager.IntValues result = execute(
                unresolved,
                AttrSchemaType.String,
                source,
                AccountGetter.DEFAULT,
                getterReturning(null));

        assertTrue(result.values().isEmpty());
    }

    /**
     * Verifica groups[developers].value quando il gruppo esiste.
     *
     * Il DAO restituisce il gruppo e il PlainAttrGetter
     * restituisce il suo attributo.
     */
    @Test
    void externalGroupExists() {
        Any originalSource = mock(Any.class);
        Group group = mock(Group.class);

        doReturn(Optional.of(group)).
                when(groupDAO).
                findByName("developers");

        PlainAttr groupAttribute = plainAttr(
                SCHEMA,
                stringValue("group-value"));

        IntAttrName intAttrName =
                plainIntAttrName(SCHEMA, AttrSchemaType.String);

        when(intAttrName.getExternalGroup()).
                thenReturn("developers");

        PlainAttrGetter getter = (reference, schema) ->
                reference == group && SCHEMA.equals(schema)
                        ? groupAttribute
                        : null;

        MappingManager.IntValues result = execute(
                intAttrName,
                AttrSchemaType.String,
                originalSource,
                AccountGetter.DEFAULT,
                getter);

        assertEquals(
                "group-value",
                onlyValue(result).getStringValue());
    }

    /**
     * Verifica groups[missing-group].value quando il gruppo
     * non viene trovato.
     */
    @Test
    void externalGroupDoesNotExist() {
        Any originalSource = mock(Any.class);

        doReturn(Optional.empty()).
                when(groupDAO).
                findByName("missing-group");

        IntAttrName intAttrName =
                plainIntAttrName(SCHEMA, AttrSchemaType.String);

        when(intAttrName.getExternalGroup()).
                thenReturn("missing-group");

        MappingManager.IntValues result = execute(
                intAttrName,
                AttrSchemaType.String,
                originalSource,
                AccountGetter.DEFAULT,
                getterReturning(null));

        assertTrue(result.values().isEmpty());
    }

    /**
     * Verifica users[mrossi].value quando l'utente esiste.
     */
    @Test
    void externalUserExists() {
        Any originalSource = mock(Any.class);
        User externalUser = mock(User.class);

        doReturn(Optional.of(externalUser)).
                when(userDAO).
                findByUsername("mrossi");

        PlainAttr userAttribute = plainAttr(
                SCHEMA,
                stringValue("user-value"));

        IntAttrName intAttrName =
                plainIntAttrName(SCHEMA, AttrSchemaType.String);

        when(intAttrName.getExternalUser()).
                thenReturn("mrossi");

        PlainAttrGetter getter = (reference, schema) ->
                reference == externalUser && SCHEMA.equals(schema)
                        ? userAttribute
                        : null;

        MappingManager.IntValues result = execute(
                intAttrName,
                AttrSchemaType.String,
                originalSource,
                AccountGetter.DEFAULT,
                getter);

        assertEquals(
                "user-value",
                onlyValue(result).getStringValue());
    }

    /**
     * Verifica anyObjects[printer01].value quando
     * l'Any Object richiesto esiste.
     */
    @Test
    void externalAnyObjectExists() {
        Any originalSource = mock(Any.class);
        AnyObject printer = mock(AnyObject.class);

        when(anyObjectDAO.findByName("printer01")).
                thenReturn(List.of(printer));

        PlainAttr anyObjectAttribute = plainAttr(
                SCHEMA,
                stringValue("printer-value"));

        IntAttrName intAttrName =
                plainIntAttrName(SCHEMA, AttrSchemaType.String);

        when(intAttrName.getExternalAnyObject()).
                thenReturn("printer01");

        PlainAttrGetter getter = (reference, schema) ->
                reference == printer && SCHEMA.equals(schema)
                        ? anyObjectAttribute
                        : null;

        MappingManager.IntValues result = execute(
                intAttrName,
                AttrSchemaType.String,
                originalSource,
                AccountGetter.DEFAULT,
                getter);

        assertEquals(
                "printer-value",
                onlyValue(result).getStringValue());
    }

    /**
     * Verifica memberships[developers].value quando
     * la membership esiste.
     */
    @Test
    void membershipExists() {
        User user = mock(User.class);
        Group group = mock(Group.class);
        UMembership membership = mock(UMembership.class);

        when(group.getKey()).thenReturn("group-key");

        doReturn(Optional.of(group)).
                when(groupDAO).
                findByName("developers");

        doReturn(Optional.of(membership)).
                when(user).
                getMembership("group-key");

        PlainAttr membershipAttribute = plainAttr(
                SCHEMA,
                stringValue("membership-value"));

        when(user.getPlainAttr(SCHEMA, membership)).
                thenReturn(Optional.of(membershipAttribute));

        MappingManager.IntValues result = execute(
                membershipIntAttrName("developers", SCHEMA),
                AttrSchemaType.String,
                user,
                AccountGetter.DEFAULT,
                getterReturning(null));

        assertEquals(
                "membership-value",
                onlyValue(result).getStringValue());
    }

    /**
     * Verifica memberships[developers].value quando
     * l'utente non possiede la membership.
     */
    @Test
    void membershipDoesNotExist() {
        User user = mock(User.class);
        Group group = mock(Group.class);

        when(group.getKey()).thenReturn("group-key");

        doReturn(Optional.of(group)).
                when(groupDAO).
                findByName("developers");

        doReturn(Optional.empty()).
                when(user).
                getMembership("group-key");

        MappingManager.IntValues result = execute(
                membershipIntAttrName("developers", SCHEMA),
                AttrSchemaType.String,
                user,
                AccountGetter.DEFAULT,
                getterReturning(null));

        assertTrue(result.values().isEmpty());
    }

    /**
     * Verifica relationships[friend][printer01].value
     * quando la relationship esiste.
     */
    @Test
    void relationshipExists() {
        User user = mock(User.class);
        RelationshipType relationshipType =
                mock(RelationshipType.class);

        AnyType rightEndType = mock(AnyType.class);
        AnyObject printer = mock(AnyObject.class);
        URelationship relationship = mock(URelationship.class);

        when(rightEndType.getKey()).thenReturn("PRINTER");
        when(relationshipType.getRightEndAnyType()).
                thenReturn(rightEndType);
        when(printer.getKey()).thenReturn("printer-key");

        doReturn(Optional.of(relationshipType)).
                when(relationshipTypeDAO).
                findById("friend");

        doReturn(Optional.of(printer)).
                when(anyObjectDAO).
                findByName("PRINTER", "printer01");

        doReturn(Optional.of(relationship)).
                when(user).
                getRelationship(
                        relationshipType,
                        "printer-key");

        PlainAttr relationshipAttribute = plainAttr(
                SCHEMA,
                stringValue("relationship-value"));

        when(user.getPlainAttr(SCHEMA, relationship)).
                thenReturn(Optional.of(relationshipAttribute));

        MappingManager.IntValues result = execute(
                relationshipIntAttrName(
                        "friend",
                        "printer01",
                        SCHEMA),
                AttrSchemaType.String,
                user,
                AccountGetter.DEFAULT,
                getterReturning(null));

        assertEquals(
                "relationship-value",
                onlyValue(result).getStringValue());
    }

    /**
     * Verifica una relationship configurata ma non presente
     * sull'entità sorgente.
     */
    @Test
    void relationshipDoesNotExist() {
        User user = mock(User.class);
        RelationshipType relationshipType =
                mock(RelationshipType.class);

        AnyType rightEndType = mock(AnyType.class);
        AnyObject printer = mock(AnyObject.class);

        when(rightEndType.getKey()).thenReturn("PRINTER");
        when(relationshipType.getRightEndAnyType()).
                thenReturn(rightEndType);
        when(printer.getKey()).thenReturn("printer-key");

        doReturn(Optional.of(relationshipType)).
                when(relationshipTypeDAO).
                findById("friend");

        doReturn(Optional.of(printer)).
                when(anyObjectDAO).
                findByName("PRINTER", "printer01");

        doReturn(Optional.empty()).
                when(user).
                getRelationship(
                        relationshipType,
                        "printer-key");

        MappingManager.IntValues result = execute(
                relationshipIntAttrName(
                        "friend",
                        "printer01",
                        SCHEMA),
                AttrSchemaType.String,
                user,
                AccountGetter.DEFAULT,
                getterReturning(null));

        assertTrue(result.values().isEmpty());
    }

    /**
     * Verifica il campo speciale username quando AccountGetter
     * riesce a fornire l'account effettivo.
     */
    @Test
    void accountGetterRetrievesAccount() {
        User source = mock(User.class);
        Account effectiveAccount = mock(Account.class);

        when(effectiveAccount.getUsername()).
                thenReturn("resolved.username");

        AccountGetter getter =
                ignored -> effectiveAccount;

        MappingManager.IntValues result = execute(
                fieldIntAttrName("username"),
                AttrSchemaType.String,
                source,
                getter,
                getterReturning(null));

        assertEquals(
                "resolved.username",
                onlyValue(result).getStringValue());
    }

    /**
     * Caso di robustezza: AccountGetter non restituisce
     * alcun account.
     *
     * Il contratto non definisce un valore di fallback;
     * l'implementazione corrente genera NullPointerException.
     */
    @Test
    void accountGetterDoesNotRetrieveAccount() {
        User source = mock(User.class);

        AccountGetter getter =
                ignored -> null;

        assertThrows(
                NullPointerException.class,
                () -> execute(
                        fieldIntAttrName("username"),
                        AttrSchemaType.String,
                        source,
                        getter,
                        getterReturning(null)));
    }

    /**
     * Verifica esplicitamente un PlainAttrGetter che trova
     * l'attributo richiesto.
     */
    @Test
    void plainAttrGetterRetrievesAttribute() {
        Any source = mock(Any.class);

        PlainAttr expected = plainAttr(
                SCHEMA,
                stringValue("retrieved-value"));

        MappingManager.IntValues result = execute(
                plainIntAttrName(SCHEMA, AttrSchemaType.String),
                AttrSchemaType.String,
                source,
                AccountGetter.DEFAULT,
                getterReturning(expected));

        assertEquals(
                "retrieved-value",
                onlyValue(result).getStringValue());
    }

    /**
     * Verifica un PlainAttrGetter che non trova
     * l'attributo richiesto.
     */
    @Test
    void plainAttrGetterDoesNotRetrieveAttribute() {
        Any source = mock(Any.class);

        MappingManager.IntValues result = execute(
                plainIntAttrName(SCHEMA, AttrSchemaType.String),
                AttrSchemaType.String,
                source,
                AccountGetter.DEFAULT,
                getterReturning(null));

        assertTrue(result.values().isEmpty());
    }

    /**
     * Verifica una Provision di tipo GROUP utilizzata mentre
     * si tenta di recuperare l'uManager di un User.
     *
     * La configurazione non è coerente con il manager richiesto
     * e non viene prodotto alcun valore.
     */
    @Test
    void provisionNotCoherentWithEntityType() {
        User source = mock(User.class);
        User manager = mock(User.class);

        when(source.getUManager()).thenReturn(manager);

        Provision mismatchedProvision = mock(Provision.class);
        Mapping groupMapping = mock(Mapping.class);

        when(mismatchedProvision.getAnyType()).
                thenReturn(AnyTypeKind.GROUP.name());
        when(mismatchedProvision.getMapping()).
                thenReturn(groupMapping);

        MappingManager.IntValues result = execute(
                mismatchedProvision,
                fieldIntAttrName("uManager"),
                AttrSchemaType.String,
                source,
                AccountGetter.DEFAULT,
                getterReturning(null));

        assertTrue(result.values().isEmpty());
    }

    /**
     * Verifica in modo separato alcuni parametri essenziali null.
     *
     * Non si usa un prodotto cartesiano: ogni invocazione modifica
     * soltanto un parametro necessario per il percorso selezionato.
     */
    @Test
    void nullMainParametersAreRejected() {
        Any source = mock(Any.class);
        User user = mock(User.class);

        IntAttrName keyField =
                fieldIntAttrName("key");

        IntAttrName usernameField =
                fieldIntAttrName("username");

        IntAttrName plainName =
                plainIntAttrName(
                        SCHEMA,
                        AttrSchemaType.String);

        assertAll(
                /*
                 * Item necessario per identificare il mapping.
                 */
                () -> assertThrows(
                        NullPointerException.class,
                        () -> mappingManager.getIntValues(
                                resource,
                                provision,
                                null,
                                keyField,
                                AttrSchemaType.String,
                                source,
                                AccountGetter.DEFAULT,
                                getterReturning(null))),

                /*
                 * IntAttrName necessario per determinare
                 * campo o schema da leggere.
                 */
                () -> assertThrows(
                        NullPointerException.class,
                        () -> mappingManager.getIntValues(
                                resource,
                                provision,
                                item,
                                null,
                                AttrSchemaType.String,
                                source,
                                AccountGetter.DEFAULT,
                                getterReturning(null))),

                /*
                 * Entità sorgente necessaria per leggere la key.
                 */
                () -> assertThrows(
                        NullPointerException.class,
                        () -> mappingManager.getIntValues(
                                resource,
                                provision,
                                item,
                                keyField,
                                AttrSchemaType.String,
                                null,
                                AccountGetter.DEFAULT,
                                getterReturning(null))),

                /*
                 * AccountGetter necessario per lo username.
                 */
                () -> assertThrows(
                        NullPointerException.class,
                        () -> mappingManager.getIntValues(
                                resource,
                                provision,
                                item,
                                usernameField,
                                AttrSchemaType.String,
                                user,
                                null,
                                getterReturning(null))),

                /*
                 * PlainAttrGetter necessario per uno schema plain.
                 */
                () -> assertThrows(
                        NullPointerException.class,
                        () -> mappingManager.getIntValues(
                                resource,
                                provision,
                                item,
                                plainName,
                                AttrSchemaType.String,
                                source,
                                AccountGetter.DEFAULT,
                                null)));
    }

    /**
     * Invoca il vero getIntValues() con la Provision comune.
     */
    private MappingManager.IntValues execute(
            final IntAttrName intAttrName,
            final AttrSchemaType schemaType,
            final Any source,
            final AccountGetter accountGetter,
            final PlainAttrGetter plainAttrGetter) {

        return execute(
                provision,
                intAttrName,
                schemaType,
                source,
                accountGetter,
                plainAttrGetter);
    }

    /**
     * Variante usata quando il test deve fornire
     * una Provision specifica.
     */
    private MappingManager.IntValues execute(
            final Provision selectedProvision,
            final IntAttrName intAttrName,
            final AttrSchemaType schemaType,
            final Any source,
            final AccountGetter accountGetter,
            final PlainAttrGetter plainAttrGetter) {

        return mappingManager.getIntValues(
                resource,
                selectedProvision,
                item,
                intAttrName,
                schemaType,
                source,
                accountGetter,
                plainAttrGetter);
    }

    /**
     * Crea un IntAttrName che rappresenta uno schema plain.
     */
    private IntAttrName plainIntAttrName(
            final String schemaName,
            final AttrSchemaType schemaType) {

        PlainSchema schema = mock(PlainSchema.class);

        when(schema.getKey()).thenReturn(schemaName);
        when(schema.getType()).thenReturn(schemaType);

        IntAttrName intAttrName = mock(IntAttrName.class);

        when(intAttrName.getSchemaInfo()).thenReturn(
                new IntAttrName.SchemaInfo(
                        schema,
                        SchemaType.PLAIN));

        return intAttrName;
    }

    /**
     * Crea un IntAttrName relativo a uno schema derivato.
     */
    private IntAttrName derivedIntAttrName(
            final DerSchema schema) {

        IntAttrName intAttrName = mock(IntAttrName.class);

        when(intAttrName.getSchemaInfo()).thenReturn(
                new IntAttrName.SchemaInfo(
                        schema,
                        SchemaType.DERIVED));

        return intAttrName;
    }

    /**
     * Crea un IntAttrName relativo a un campo speciale.
     */
    private IntAttrName fieldIntAttrName(
            final String field) {

        IntAttrName intAttrName = mock(IntAttrName.class);
        when(intAttrName.getField()).thenReturn(field);

        return intAttrName;
    }

    /**
     * Crea una forma memberships[group].schema.
     */
    private IntAttrName membershipIntAttrName(
            final String group,
            final String schemaName) {

        IntAttrName intAttrName = plainIntAttrName(
                schemaName,
                AttrSchemaType.String);

        when(intAttrName.getMembership()).thenReturn(group);

        return intAttrName;
    }

    /**
     * Crea una forma relationships[type][anyObject].schema.
     */
    private IntAttrName relationshipIntAttrName(
            final String type,
            final String anyObject,
            final String schemaName) {

        IntAttrName intAttrName = plainIntAttrName(
                schemaName,
                AttrSchemaType.String);

        when(intAttrName.getRelationshipInfo()).thenReturn(
                new IntAttrName.RelationshipInfo(
                        type,
                        anyObject));

        return intAttrName;
    }

    /**
     * Crea un PlainAttrGetter minimale che restituisce
     * sempre l'attributo indicato.
     */
    private PlainAttrGetter getterReturning(
            final PlainAttr attribute) {

        return (source, schema) -> attribute;
    }

    /**
     * Crea un PlainAttr reale con i valori indicati.
     */
    private PlainAttr plainAttr(
            final String schema,
            final PlainAttrValue... values) {

        PlainAttr attribute = new PlainAttr();
        attribute.setSchema(schema);

        for (PlainAttrValue value : values) {
            attribute.add(value);
        }

        return attribute;
    }

    private PlainAttrValue stringValue(
            final String value) {

        PlainAttrValue result = new PlainAttrValue();
        result.setStringValue(value);

        return result;
    }

    private PlainAttrValue booleanValue(
            final boolean value) {

        PlainAttrValue result = new PlainAttrValue();
        result.setBooleanValue(value);

        return result;
    }

    private PlainAttrValue longValue(
            final long value) {

        PlainAttrValue result = new PlainAttrValue();
        result.setLongValue(value);

        return result;
    }

    private PlainAttrValue dateValue(
            final OffsetDateTime value) {

        PlainAttrValue result = new PlainAttrValue();
        result.setDateValue(value);

        return result;
    }

    /**
     * Riduce la ripetizione delle asserzioni:
     * verifica che esista un solo valore e lo restituisce.
     */
    private PlainAttrValue onlyValue(
            final MappingManager.IntValues result) {

        assertEquals(1, result.values().size());
        return result.values().getFirst();
    }
}
//
