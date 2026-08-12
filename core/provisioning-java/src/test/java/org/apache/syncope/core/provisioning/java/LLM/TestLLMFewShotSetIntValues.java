package org.apache.syncope.core.provisioning.java.LLM;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0.
 */


import java.text.ParseException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.apache.syncope.common.lib.Attr;
import org.apache.syncope.common.lib.to.AnyObjectTO;
import org.apache.syncope.common.lib.to.Item;
import org.apache.syncope.common.lib.to.Mapping;
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
import org.apache.syncope.core.persistence.api.entity.AnyType;
import org.apache.syncope.core.persistence.api.entity.PlainSchema;
import org.apache.syncope.core.persistence.api.entity.user.User;
import org.apache.syncope.core.provisioning.api.DerAttrHandler;
import org.apache.syncope.core.provisioning.api.IntAttrName;
import org.apache.syncope.core.provisioning.api.IntAttrNameParser;
import org.apache.syncope.core.provisioning.api.jexl.JexlTools;
import org.apache.syncope.core.provisioning.java.DefaultMappingManager;
import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.AttributeBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TestLLMFewShotSetIntValues {

    private static final String SCHEMA = "email";
    private static final String EXTERNAL_ATTRIBUTE = "mail";

    private DefaultMappingManager mappingManager;
    private IntAttrNameParser intAttrNameParser;

    @BeforeEach
    void setUp() throws Exception {
        intAttrNameParser = mock(IntAttrNameParser.class);

        // Mock PlainSchema for String types
        PlainSchema plainSchema = mock(PlainSchema.class);
        when(plainSchema.getKey()).thenReturn(SCHEMA);
        when(plainSchema.getType()).thenReturn(AttrSchemaType.String);

        IntAttrName plainIntAttrName = mock(IntAttrName.class);
        when(plainIntAttrName.getSchemaInfo()).thenReturn(
                new IntAttrName.SchemaInfo(plainSchema, SchemaType.PLAIN));

        // Parser behavior for AnyTO overload
        when(intAttrNameParser.parse(SCHEMA, AnyTypeKind.USER)).thenReturn(plainIntAttrName);
        when(intAttrNameParser.parse(SCHEMA, AnyTypeKind.ANY_OBJECT)).thenReturn(plainIntAttrName);

        // Parser behavior for RealmTO overload
        when(intAttrNameParser.parse(SCHEMA)).thenReturn(plainIntAttrName);

        // Specific fields mock (Username, Password, mustChangePassword)
        IntAttrName usernameIntAttrName = mock(IntAttrName.class);
        when(usernameIntAttrName.getField()).thenReturn("username");
        when(intAttrNameParser.parse("username", AnyTypeKind.USER)).thenReturn(usernameIntAttrName);

        IntAttrName mustChangePwdAttrName = mock(IntAttrName.class);
        when(mustChangePwdAttrName.getField()).thenReturn("mustChangePassword");
        when(intAttrNameParser.parse("mustChangePassword", AnyTypeKind.USER)).thenReturn(mustChangePwdAttrName);

        // Initialize Manager with mocked dependencies
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
    void testSetIntValuesInsertSingleValueIntoUserTO() {
        UserTO anyTO = new UserTO();
        Attribute externalAttribute = AttributeBuilder.build(EXTERNAL_ATTRIBUTE, "mario.rossi@example.org");

        mappingManager.setIntValues(item(SCHEMA), externalAttribute, anyTO);

        Attr internalAttribute = anyTO.getPlainAttr(SCHEMA).orElseThrow();
        assertEquals(1, anyTO.getPlainAttrs().size());
        assertEquals(List.of("mario.rossi@example.org"), internalAttribute.getValues());
    }

    @Test
    void testSetIntValuesInsertMultipleValuesIntoUserTO() {
        UserTO anyTO = new UserTO();
        Attribute externalAttribute = AttributeBuilder.build(
                EXTERNAL_ATTRIBUTE, "mario.rossi@example.org", "mrossi@example.com");

        mappingManager.setIntValues(item(SCHEMA), externalAttribute, anyTO);

        Attr internalAttribute = anyTO.getPlainAttr(SCHEMA).orElseThrow();
        assertEquals(1, anyTO.getPlainAttrs().size());
        assertEquals(List.of("mario.rossi@example.org", "mrossi@example.com"), internalAttribute.getValues());
    }

    @Test
    void testSetIntValuesReplaceExistingUsernameValue() {
        UserTO anyTO = new UserTO();
        anyTO.setUsername("old.username");

        Attribute externalAttribute = AttributeBuilder.build("uid", "new.username");
        mappingManager.setIntValues(item("username"), externalAttribute, anyTO);

        assertEquals("new.username", anyTO.getUsername());
    }

    @Test
    void testSetIntValuesExternalAttributeWithoutValues() {
        UserTO anyTO = new UserTO();
        Attribute externalAttribute = AttributeBuilder.build(EXTERNAL_ATTRIBUTE);

        mappingManager.setIntValues(item(SCHEMA), externalAttribute, anyTO);

        Attr internalAttribute = anyTO.getPlainAttr(SCHEMA).orElseThrow();
        assertEquals(1, anyTO.getPlainAttrs().size());
        assertTrue(internalAttribute.getValues().isEmpty());
    }

    @Test
    void testSetIntValuesMustChangePassword() {
        UserTO anyTO = new UserTO();
        Attribute externalAttribute = AttributeBuilder.build("pwdReset", "true");

        mappingManager.setIntValues(item("mustChangePassword"), externalAttribute, anyTO);

        assertTrue(anyTO.isMustChangePassword());
    }

    @Test
    void testSetIntValuesInsertSingleValueIntoRealmTO() {
        RealmTO realmTO = new RealmTO();
        Attribute externalAttribute = AttributeBuilder.build(EXTERNAL_ATTRIBUTE, "realm-value");

        mappingManager.setIntValues(item(SCHEMA), externalAttribute, realmTO);

        Attr internalAttribute = realmTO.getPlainAttr(SCHEMA).orElseThrow();
        assertEquals(1, realmTO.getPlainAttrs().size());
        assertEquals(List.of("realm-value"), internalAttribute.getValues());
    }

    @Test
    void testHasMustChangePasswordTrue() {
        Provision provision = new Provision();
        Mapping mapping = new Mapping();
        mapping.add(item("mustChangePassword"));
        provision.setMapping(mapping);

        assertTrue(mappingManager.hasMustChangePassword(provision));
    }

    @Test
    void testHasMustChangePasswordFalse() {
        Provision provision = new Provision();
        Mapping mapping = new Mapping();
        mapping.add(item("username"));
        provision.setMapping(mapping);

        assertFalse(mappingManager.hasMustChangePassword(provision));
    }

    @Test
    void testHasMustChangePasswordNullMapping() {
        Provision provision = new Provision();
        assertFalse(mappingManager.hasMustChangePassword(provision));
    }

    /**
     * Creates a minimal mapping item. No transformers are configured because these
     * tests strictly verify direct value transfer.
     */
    private Item item(final String intAttrName) {
        Item item = new Item();
        item.setIntAttrName(intAttrName);
        return item;
    }
}