/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0.
 */
package org.apache.syncope.core.provisioning.java;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import org.apache.syncope.common.lib.Attr;
import org.apache.syncope.common.lib.to.GroupTO;
import org.apache.syncope.common.lib.to.Item;
import org.apache.syncope.common.lib.to.MembershipTO;
import org.apache.syncope.common.lib.to.Provision;
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
import org.apache.syncope.core.persistence.api.entity.Any;
import org.apache.syncope.core.persistence.api.entity.AnyType;
import org.apache.syncope.core.persistence.api.entity.ExternalResource;
import org.apache.syncope.core.persistence.api.entity.PlainAttr;
import org.apache.syncope.core.persistence.api.entity.PlainAttrValue;
import org.apache.syncope.core.persistence.api.entity.PlainSchema;
import org.apache.syncope.core.persistence.api.entity.group.Group;
import org.apache.syncope.core.provisioning.api.AccountGetter;
import org.apache.syncope.core.provisioning.api.DerAttrHandler;
import org.apache.syncope.core.provisioning.api.IntAttrName;
import org.apache.syncope.core.provisioning.api.IntAttrNameParser;
import org.apache.syncope.core.provisioning.api.MappingManager;
import org.apache.syncope.core.provisioning.api.PlainAttrGetter;
import org.apache.syncope.core.provisioning.api.jexl.JexlTools;
import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.AttributeBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.doReturn;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Raffinamento gray-box guidato dal report PIT di DefaultMappingManager.
 *
 * <p>La suite black-box originale e i successivi raffinamenti black-box
 * restano separati e invariati. I casi presenti in questa classe sono stati
 * selezionati dopo l'osservazione dei mutanti sopravvissuti e delle porzioni
 * di implementazione non coperte; per questo motivo la progettazione è
 * implementation-aware e viene classificata come gray-box.</p>
 *
 * <p>I test continuano comunque a invocare esclusivamente i metodi pubblici
 * getIntValues(), prepareAttr() e setIntValues(), verificando soltanto valori,
 * DTO e record pubblicamente osservabili. Non vengono invocati metodi
 * protetti o privati e non vengono usati spy sulla classe sotto test.</p>
 */
class DefaultMappingManagerPitGreyBoxRefinementTest {

    private static final String PLAIN_SCHEMA = "employeeCode";

    private static final String BINARY_SCHEMA = "certificate";

    private static final String MEMBERSHIP_SCHEMA = "role";

    private static final String MEMBERSHIP_NAME = "engineering";

    private static final String MEMBERSHIP_KEY = "group-engineering";

    private DefaultMappingManager mappingManager;

    private IntAttrNameParser intAttrNameParser;

    private GroupDAO groupDAO;

    private ExternalResource resource;

    private Provision provision;

    @BeforeEach
    void setUp() {
        intAttrNameParser = mock(IntAttrNameParser.class);
        groupDAO = mock(GroupDAO.class);
        resource = mock(ExternalResource.class);
        provision = mock(Provision.class);

        mappingManager = new DefaultMappingManager(
                mock(UserDAO.class),
                mock(AnyObjectDAO.class),
                groupDAO,
                mock(RelationshipTypeDAO.class),
                mock(RealmSearchDAO.class),
                mock(ImplementationDAO.class),
                mock(DerAttrHandler.class),
                intAttrNameParser,
                mock(EncryptorManager.class),
                mock(JexlTools.class));
    }

    /**
     * Distingue esplicitamente un attributo plain UNIQUE da un attributo
     * multivalore ordinario.
     *
     * <p>L'oracolo controlla cardinalità, tipo e valore completo restituito.
     * Il ramo alternativo non contiene valori, quindi la mancata lettura del
     * valore unico non può essere mascherata da una seconda sorgente.</p>
     */
    @Test
    void uniquePlainValueIsPreservedByGetIntValues() {
        Item item = item(PLAIN_SCHEMA, "externalEmployeeCode");

        PlainSchema schema = plainSchema(
                PLAIN_SCHEMA,
                AttrSchemaType.String);

        IntAttrName intAttrName =
                schemaIntAttrName(schema, null);

        PlainAttr attribute = new PlainAttr();
        attribute.setSchema(PLAIN_SCHEMA);

        PlainAttrValue uniqueValue = new PlainAttrValue();
        uniqueValue.setStringValue("EMP-001");
        attribute.setUniqueValue(uniqueValue);

        MappingManager.IntValues result =
                mappingManager.getIntValues(
                        resource,
                        provision,
                        item,
                        intAttrName,
                        AttrSchemaType.String,
                        mock(Any.class),
                        AccountGetter.DEFAULT,
                        getterReturning(attribute));

        assertEquals(
                AttrSchemaType.String,
                result.attrSchemaType());
        assertEquals(1, result.values().size());
        assertEquals(
                "EMP-001",
                result.values().getFirst().getStringValue());
    }

    /**
     * Verifica che un valore plain Binary sia copiato senza perdita o
     * conversione nella fase di lettura dei valori interni.
     */
    @Test
    void binaryPlainValueIsPreservedByGetIntValues() {
        Item item = item(BINARY_SCHEMA, "externalCertificate");

        PlainSchema schema = plainSchema(
                BINARY_SCHEMA,
                AttrSchemaType.Binary);

        IntAttrName intAttrName =
                schemaIntAttrName(schema, null);

        byte[] expected = new byte[] { 1, 2, 3, 4 };

        PlainAttr attribute = new PlainAttr();
        attribute.setSchema(BINARY_SCHEMA);

        PlainAttrValue binaryValue = new PlainAttrValue();
        binaryValue.setBinaryValue(expected);
        attribute.add(binaryValue);

        MappingManager.IntValues result =
                mappingManager.getIntValues(
                        resource,
                        provision,
                        item,
                        intAttrName,
                        AttrSchemaType.Binary,
                        mock(Any.class),
                        AccountGetter.DEFAULT,
                        getterReturning(attribute));

        assertEquals(
                AttrSchemaType.Binary,
                result.attrSchemaType());
        assertEquals(1, result.values().size());

        PlainAttrValue actual =
                result.values().getFirst();

        assertArrayEquals(
                expected,
                actual.getBinaryValue());

        assertNull(actual.getStringValue());
        assertNull(actual.getDoubleValue());
        assertNull(actual.getLongValue());
    }

    /**
     * Rende distinguibili i due possibili formati pubblici di PreparedAttr:
     * attributo ordinario e chiave del connettore.
     *
     * <p>Quando l'Item è una connObjectKey, il valore deve essere esposto
     * esclusivamente tramite connObjectLink e non anche come Attribute.</p>
     */
    @Test
    void connObjectKeyProducesOnlyPublicLinkInPrepareAttr()
            throws Exception {

        Item item = item(
                PLAIN_SCHEMA,
                "uid");
        item.setConnObjectKey(true);

        PlainSchema schema = plainSchema(
                PLAIN_SCHEMA,
                AttrSchemaType.String);

        IntAttrName intAttrName =
                schemaIntAttrName(schema, null);

        Any any = mock(Any.class);
        AnyType anyType = mock(AnyType.class);

        when(anyType.getKind()).
                thenReturn(AnyTypeKind.USER);
        when(any.getType()).
                thenReturn(anyType);

        when(intAttrNameParser.parse(
                PLAIN_SCHEMA,
                AnyTypeKind.USER)).
                thenReturn(intAttrName);

        PlainAttr attribute = new PlainAttr();
        attribute.setSchema(PLAIN_SCHEMA);

        PlainAttrValue value = new PlainAttrValue();
        value.setStringValue("EMP-001");
        attribute.add(value);

        MappingManager.PreparedAttr result =
                mappingManager.prepareAttr(
                        resource,
                        provision,
                        item,
                        any,
                        null,
                        AccountGetter.DEFAULT,
                        AccountGetter.DEFAULT,
                        getterReturning(attribute));

        assertNotNull(result);
        assertEquals(
                "EMP-001",
                result.connObjectLink());

        assertNull(result.attribute());
    }

    /**
     * Verifica la rappresentazione Base64 di un valore Binary inserito in
     * un UserTO.
     *
     * <p>L'oracolo controlla anche che non venga creato un attributo derivato
     * omonimo e che il risultato contenga esattamente un valore.</p>
     */
    @Test
    void binaryValueIsEncodedWhenSetOnUserTO()
            throws Exception {

        byte[] source =
                new byte[] { 10, 20, 30, 40 };

        Item item = item(
                BINARY_SCHEMA,
                "externalCertificate");

        IntAttrName intAttrName =
                schemaIntAttrName(
                        plainSchema(
                                BINARY_SCHEMA,
                                AttrSchemaType.Binary),
                        null);

        when(intAttrNameParser.parse(
                BINARY_SCHEMA,
                AnyTypeKind.USER)).
                thenReturn(intAttrName);

        Attribute externalAttribute =
                AttributeBuilder.build(
                        "externalCertificate",
                        (Object) source);

        UserTO userTO = new UserTO();

        mappingManager.setIntValues(
                item,
                externalAttribute,
                userTO);

        Attr inserted =
                userTO.getPlainAttr(BINARY_SCHEMA).
                        orElseThrow();

        assertEquals(1, inserted.getValues().size());
        assertEquals(
                Base64.getEncoder().encodeToString(source),
                inserted.getValues().getFirst());

        assertTrue(
                userTO.getDerAttr(BINARY_SCHEMA).isEmpty());
    }

    /**
     * Applica lo stesso caso Binary all'overload pubblico per RealmTO.
     */
    @Test
    void binaryValueIsEncodedWhenSetOnRealmTO()
            throws Exception {

        byte[] source =
                new byte[] { 50, 60, 70 };

        Item item = item(
                BINARY_SCHEMA,
                "externalCertificate");

        IntAttrName intAttrName =
                schemaIntAttrName(
                        plainSchema(
                                BINARY_SCHEMA,
                                AttrSchemaType.Binary),
                        null);

        when(intAttrNameParser.parse(BINARY_SCHEMA)).
                thenReturn(intAttrName);

        Attribute externalAttribute =
                AttributeBuilder.build(
                        "externalCertificate",
                        (Object) source);

        RealmTO realmTO = new RealmTO();

        mappingManager.setIntValues(
                item,
                externalAttribute,
                realmTO);

        Attr inserted =
                realmTO.getPlainAttr(BINARY_SCHEMA).
                        orElseThrow();

        assertEquals(1, inserted.getValues().size());
        assertEquals(
                Base64.getEncoder().encodeToString(source),
                inserted.getValues().getFirst());

        assertTrue(
                realmTO.getDerAttr(BINARY_SCHEMA).isEmpty());
    }

    /**
     * Verifica che un attributo associato a una membership venga inserito
     * esclusivamente nella MembershipTO corretta e non nel contenitore
     * principale UserTO.
     */
    @Test
    void membershipPlainValueIsStoredOnlyInsideMembership()
            throws Exception {

        Item item = item(
                MEMBERSHIP_SCHEMA,
                "externalRole");

        PlainSchema schema = plainSchema(
                MEMBERSHIP_SCHEMA,
                AttrSchemaType.String);

        IntAttrName intAttrName =
                schemaIntAttrName(
                        schema,
                        MEMBERSHIP_NAME);

        when(intAttrNameParser.parse(
                MEMBERSHIP_SCHEMA,
                AnyTypeKind.USER)).
                thenReturn(intAttrName);

        Group group = mock(Group.class);
        when(group.getKey()).
                thenReturn(MEMBERSHIP_KEY);

        doReturn(Optional.of(group)).
                when(groupDAO).
                findByName(MEMBERSHIP_NAME);

        UserTO userTO = new UserTO();

        Attribute externalAttribute =
                AttributeBuilder.build(
                        "externalRole",
                        "manager");

        mappingManager.setIntValues(
                item,
                externalAttribute,
                userTO);

        assertTrue(
                userTO.getPlainAttr(
                        MEMBERSHIP_SCHEMA).isEmpty());

        assertEquals(
                1,
                userTO.getMemberships().size());

        MembershipTO membership =
                userTO.getMembership(MEMBERSHIP_KEY).
                        orElseThrow();

        Attr inserted =
                membership.getPlainAttr(
                        MEMBERSHIP_SCHEMA).
                        orElseThrow();

        assertEquals(
                MEMBERSHIP_KEY,
                membership.getGroupKey());

        assertEquals(
                List.of("manager"),
                inserted.getValues());
    }

    /**
     * Verifica separatamente il secondo operando della decisione pubblicamente
     * osservabile relativa al nome di RealmTO: lista non vuota, ma primo valore
     * nullo.
     */
    @Test
    void nullFirstExternalValueClearsRealmName()
            throws Exception {

        Item item = item(
                "name",
                "externalName");

        IntAttrName intAttrName =
                mock(IntAttrName.class);

        when(intAttrName.getField()).
                thenReturn("name");

        when(intAttrNameParser.parse("name")).
                thenReturn(intAttrName);

        Attribute externalAttribute =
                mock(Attribute.class);

        List<Object> valuesWithNull =
                new ArrayList<>();
        valuesWithNull.add(null);

        when(externalAttribute.getValue()).
                thenReturn(valuesWithNull);

        RealmTO realmTO = new RealmTO();
        realmTO.setName("old-name");

        assertEquals(
                "old-name",
                realmTO.getName());

        mappingManager.setIntValues(
                item,
                externalAttribute,
                realmTO);

        assertNull(realmTO.getName());
    }

    /**
     * Copre il campo speciale name di GroupTO e verifica l'assenza
     * dell'effetto alternativo come attributo plain.
     */
    @Test
    void groupNameIsUpdatedWithoutPlainAttributeFallback()
            throws Exception {

        Item item = item(
                "name",
                "cn");

        IntAttrName intAttrName =
                mock(IntAttrName.class);

        when(intAttrName.getField()).
                thenReturn("name");

        when(intAttrNameParser.parse(
                "name",
                AnyTypeKind.GROUP)).
                thenReturn(intAttrName);

        GroupTO groupTO = new GroupTO();
        groupTO.setName("old-group");

        Attribute externalAttribute =
                AttributeBuilder.build(
                        "cn",
                        "new-group");

        mappingManager.setIntValues(
                item,
                externalAttribute,
                groupTO);

        assertEquals(
                "new-group",
                groupTO.getName());

        assertTrue(
                groupTO.getPlainAttr("name").isEmpty());

        assertFalse(
                groupTO.getName().isBlank());
    }

    private Item item(
            final String internalName,
            final String externalName) {

        Item item = new Item();
        item.setIntAttrName(internalName);
        item.setExtAttrName(externalName);
        return item;
    }

    private PlainSchema plainSchema(
            final String key,
            final AttrSchemaType type) {

        PlainSchema schema =
                mock(PlainSchema.class);

        when(schema.getKey()).
                thenReturn(key);
        when(schema.getType()).
                thenReturn(type);

        return schema;
    }

    private IntAttrName schemaIntAttrName(
            final PlainSchema schema,
            final String membership) {

        IntAttrName intAttrName =
                mock(IntAttrName.class);

        when(intAttrName.getSchemaInfo()).
                thenReturn(
                        new IntAttrName.SchemaInfo(
                                schema,
                                SchemaType.PLAIN));

        when(intAttrName.getMembership()).
                thenReturn(membership);

        return intAttrName;
    }

    private PlainAttrGetter getterReturning(
            final PlainAttr attribute) {

        return (source, schemaName) ->
                attribute;
    }
}
