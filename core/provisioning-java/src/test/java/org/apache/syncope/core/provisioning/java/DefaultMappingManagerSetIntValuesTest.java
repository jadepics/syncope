/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0.
 */
package org.apache.syncope.core.provisioning.java;

import java.util.List;
import org.apache.syncope.common.lib.Attr;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DefaultMappingManagerSetIntValuesTest {

    private static final String SCHEMA = "email";

    private static final String EXTERNAL_ATTRIBUTE = "mail";

    private DefaultMappingManager mappingManager;

    private IntAttrNameParser intAttrNameParser;

    @BeforeEach
    void setUp() throws Exception {
        intAttrNameParser = mock(IntAttrNameParser.class);

        /*
         * Schema plain di tipo String usato dai test su AnyTO e RealmTO.
         */
        PlainSchema plainSchema = mock(PlainSchema.class);
        when(plainSchema.getKey()).thenReturn(SCHEMA);
        when(plainSchema.getType()).thenReturn(AttrSchemaType.String);

        IntAttrName plainIntAttrName = mock(IntAttrName.class);
        when(plainIntAttrName.getSchemaInfo()).thenReturn(
                new IntAttrName.SchemaInfo(
                        plainSchema,
                        SchemaType.PLAIN));

        /*
         * Durante l'overload AnyTO il parser riceve anche il tipo USER.
         */
        when(intAttrNameParser.parse(SCHEMA, AnyTypeKind.USER)).
                thenReturn(plainIntAttrName);

        /*
         * Durante l'overload RealmTO il parser riceve soltanto
         * il nome dell'attributo interno.
         */
        when(intAttrNameParser.parse(SCHEMA)).
                thenReturn(plainIntAttrName);

        /*
         * IntAttrName dedicato al campo speciale username.
         */
        IntAttrName usernameIntAttrName = mock(IntAttrName.class);
        when(usernameIntAttrName.getField()).thenReturn("username");

        when(intAttrNameParser.parse("username", AnyTypeKind.USER)).
                thenReturn(usernameIntAttrName);

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

    @Test
    void insertSingleValueIntoAnyTO() {
        UserTO anyTO = new UserTO();

        Attribute externalAttribute = AttributeBuilder.build(
                EXTERNAL_ATTRIBUTE,
                "mario.rossi@example.org");

        mappingManager.setIntValues(
                item(SCHEMA),
                externalAttribute,
                anyTO);

        Attr internalAttribute = anyTO.getPlainAttr(SCHEMA).
                orElseThrow();

        assertEquals(1, anyTO.getPlainAttrs().size());
        assertEquals(
                List.of("mario.rossi@example.org"),
                internalAttribute.getValues());
    }

    @Test
    void insertMultipleValuesIntoAnyTO() {
        UserTO anyTO = new UserTO();

        Attribute externalAttribute = AttributeBuilder.build(
                EXTERNAL_ATTRIBUTE,
                "mario.rossi@example.org",
                "mrossi@example.com");

        mappingManager.setIntValues(
                item(SCHEMA),
                externalAttribute,
                anyTO);

        Attr internalAttribute = anyTO.getPlainAttr(SCHEMA).
                orElseThrow();

        assertEquals(1, anyTO.getPlainAttrs().size());
        assertEquals(
                List.of(
                        "mario.rossi@example.org",
                        "mrossi@example.com"),
                internalAttribute.getValues());
    }

    @Test
    void replaceExistingUsernameValue() {
        UserTO anyTO = new UserTO();
        anyTO.setUsername("old.username");

        Attribute externalAttribute = AttributeBuilder.build(
                "uid",
                "new.username");

        mappingManager.setIntValues(
                item("username"),
                externalAttribute,
                anyTO);

        assertEquals("new.username", anyTO.getUsername());
    }

    @Test
    void externalAttributeWithoutValues() {
        UserTO anyTO = new UserTO();

        Attribute externalAttribute = AttributeBuilder.build(
                EXTERNAL_ATTRIBUTE);

        mappingManager.setIntValues(
                item(SCHEMA),
                externalAttribute,
                anyTO);

        Attr internalAttribute = anyTO.getPlainAttr(SCHEMA).
                orElseThrow();

        assertEquals(1, anyTO.getPlainAttrs().size());
        assertTrue(internalAttribute.getValues().isEmpty());
    }

    @Test
    void insertSingleValueIntoRealmTO() {
        RealmTO realmTO = new RealmTO();

        Attribute externalAttribute = AttributeBuilder.build(
                EXTERNAL_ATTRIBUTE,
                "realm-value");

        mappingManager.setIntValues(
                item(SCHEMA),
                externalAttribute,
                realmTO);

        Attr internalAttribute = realmTO.getPlainAttr(SCHEMA).
                orElseThrow();

        assertEquals(1, realmTO.getPlainAttrs().size());
        assertEquals(
                List.of("realm-value"),
                internalAttribute.getValues());
    }

    @Test
    void insertMultipleValuesIntoRealmTO() {
        RealmTO realmTO = new RealmTO();

        Attribute externalAttribute = AttributeBuilder.build(
                EXTERNAL_ATTRIBUTE,
                "realm-value-1",
                "realm-value-2");

        mappingManager.setIntValues(
                item(SCHEMA),
                externalAttribute,
                realmTO);

        Attr internalAttribute = realmTO.getPlainAttr(SCHEMA).
                orElseThrow();

        assertEquals(1, realmTO.getPlainAttrs().size());
        assertEquals(
                List.of(
                        "realm-value-1",
                        "realm-value-2"),
                internalAttribute.getValues());
    }

    /**
     * Crea un mapping item reale e minimale.
     *
     * Non sono configurati transformer perché questi test verificano
     * esclusivamente il trasferimento diretto dei valori.
     */
    private Item item(final String intAttrName) {
        Item item = new Item();
        item.setIntAttrName(intAttrName);
        return item;
    }
}
//