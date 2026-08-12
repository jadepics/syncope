/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0.
 */
package org.apache.syncope.core.provisioning.java.LLM;

import java.util.List;
import java.util.Map;
import org.apache.commons.jexl3.JexlContext;
import org.apache.syncope.common.lib.to.Item;
import org.apache.syncope.common.lib.to.Mapping;
import org.apache.syncope.common.lib.to.Provision;
import org.apache.syncope.common.lib.types.AnyTypeKind;
import org.apache.syncope.core.persistence.api.EncryptorManager;
import org.apache.syncope.core.persistence.api.dao.AnyObjectDAO;
import org.apache.syncope.core.persistence.api.dao.GroupDAO;
import org.apache.syncope.core.persistence.api.dao.ImplementationDAO;
import org.apache.syncope.core.persistence.api.dao.RealmSearchDAO;
import org.apache.syncope.core.persistence.api.dao.RelationshipTypeDAO;
import org.apache.syncope.core.persistence.api.dao.UserDAO;
import org.apache.syncope.core.persistence.api.entity.Any;
import org.apache.syncope.core.persistence.api.entity.AnyType;
import org.apache.syncope.core.provisioning.api.DerAttrHandler;
import org.apache.syncope.core.provisioning.api.IntAttrNameParser;
import org.apache.syncope.core.provisioning.api.jexl.JexlTools;
import org.apache.syncope.core.provisioning.java.DefaultMappingManager;
import org.identityconnectors.framework.common.objects.Name;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

/**
 * Tests for various utility and evaluation methods in DefaultMappingManager.
 */
class TestLLMFewShotDefaultMappingManager {

    /**
     * Test-Specific Subclass to cleanly expose protected methods for testing
     * without breaking encapsulation rules in the main source tree.
     */
    static class TestableDefaultMappingManager extends DefaultMappingManager {

        public TestableDefaultMappingManager(
                UserDAO userDAO, AnyObjectDAO anyObjectDAO, GroupDAO groupDAO,
                RelationshipTypeDAO relationshipTypeDAO, RealmSearchDAO realmSearchDAO,
                ImplementationDAO implementationDAO, DerAttrHandler derAttrHandler,
                IntAttrNameParser intAttrNameParser, EncryptorManager encryptorManager,
                JexlTools jexlTools) {
            super(userDAO, anyObjectDAO, groupDAO, relationshipTypeDAO, realmSearchDAO,
                    implementationDAO, derAttrHandler, intAttrNameParser, encryptorManager, jexlTools);
        }

        public static Name publicGetName(String evalConnObjectLink, String connObjectKey) {
            return getName(evalConnObjectLink, connObjectKey);
        }

        public Name publicEvaluateNAME(Any any, Provision provision, String connObjectKey) {
            return evaluateNAME(any, provision, connObjectKey);
        }
    }

    private TestableDefaultMappingManager mappingManager;
    private DerAttrHandler derAttrHandler;
    private JexlTools jexlTools;

    @BeforeEach
    void setUp() {
        derAttrHandler = mock(DerAttrHandler.class);
        jexlTools = mock(JexlTools.class);

        mappingManager = spy(new TestableDefaultMappingManager(
                mock(UserDAO.class),
                mock(AnyObjectDAO.class),
                mock(GroupDAO.class),
                mock(RelationshipTypeDAO.class),
                mock(RealmSearchDAO.class),
                mock(ImplementationDAO.class),
                derAttrHandler,
                mock(IntAttrNameParser.class),
                mock(EncryptorManager.class),
                jexlTools));
    }

    /**
     * Tests that hasMustChangePassword returns true when the mapping contains
     * an item with the internal attribute name "mustChangePassword".
     */
    @Test
    void testHasMustChangePasswordTrue() {
        Provision provision = mock(Provision.class);
        Mapping mapping = mock(Mapping.class);
        Item item = mock(Item.class);

        when(provision.getMapping()).thenReturn(mapping);
        when(mapping.getItems()).thenReturn(List.of(item));
        when(item.getIntAttrName()).thenReturn("mustChangePassword");

        assertTrue(mappingManager.hasMustChangePassword(provision));
    }

    /**
     * Tests that hasMustChangePassword returns false when the mapping does not
     * contain the "mustChangePassword" item.
     */
    @Test
    void testHasMustChangePasswordFalse() {
        Provision provision = mock(Provision.class);
        Mapping mapping = mock(Mapping.class);
        Item item = mock(Item.class);

        when(provision.getMapping()).thenReturn(mapping);
        when(mapping.getItems()).thenReturn(List.of(item));
        when(item.getIntAttrName()).thenReturn("username");

        assertFalse(mappingManager.hasMustChangePassword(provision));
    }

    /**
     * Tests that hasMustChangePassword gracefully handles a null mapping.
     */
    @Test
    void testHasMustChangePasswordNullMapping() {
        Provision provision = mock(Provision.class);
        when(provision.getMapping()).thenReturn(null);

        assertFalse(mappingManager.hasMustChangePassword(provision));
    }

    /**
     * Tests the static getName method when evaluated ConnObjectLink is blank.
     * It should fallback to returning a Name built from connObjectKey.
     */
    @Test
    void testGetNameWithBlankEvalConnObjectLink() {
        Name name = TestableDefaultMappingManager.publicGetName("   ", "defaultKey");
        assertNotNull(name);
        assertEquals("defaultKey", name.getNameValue());
    }

    /**
     * Tests the static getName method when evaluated ConnObjectLink is populated.
     * It should return a Name built from the evaluated link.
     */
    @Test
    void testGetNameWithValidEvalConnObjectLink() {
        Name name = TestableDefaultMappingManager.publicGetName("evaluatedLink", "defaultKey");
        assertNotNull(name);
        assertEquals("evaluatedLink", name.getNameValue());
    }

    /**
     * Tests evaluateNAME when there is no ConnObjectLink defined in the mapping.
     * The evaluation should default to the provided connObjectKey.
     */
    @Test
    void testEvaluateNAMENoConnObjectLink() {
        Any any = mock(Any.class);
        AnyType anyType = mock(AnyType.class);
        when(any.getType()).thenReturn(anyType);
        when(anyType.getKey()).thenReturn(AnyTypeKind.USER.name());

        Provision provision = mock(Provision.class);
        Mapping mapping = mock(Mapping.class);
        when(provision.getMapping()).thenReturn(mapping);
        when(mapping.getConnObjectLink()).thenReturn(null);

        Name result = mappingManager.publicEvaluateNAME(any, provision, "fallbackKey");

        assertEquals("fallbackKey", result.getNameValue());
    }

    /**
     * Tests evaluateNAME when a ConnObjectLink is defined.
     * The JEXL expression should be evaluated and the result used as the Name.
     */
    @Test
    void testEvaluateNAMEWithConnObjectLink() {
        Any any = mock(Any.class);
        AnyType anyType = mock(AnyType.class);
        when(any.getType()).thenReturn(anyType);
        when(anyType.getKey()).thenReturn(AnyTypeKind.USER.name());
        when(any.getPlainAttrs()).thenReturn(List.of());
        when(derAttrHandler.getValues(any)).thenReturn(Map.of());

        Provision provision = mock(Provision.class);
        Mapping mapping = mock(Mapping.class);

        when(provision.getMapping()).thenReturn(mapping);
        when(mapping.getConnObjectLink()).thenReturn("username + '@domain.com'");

        when(jexlTools.evaluateExpression(anyString(), any(JexlContext.class)))
                .thenReturn("jdoe@domain.com");

        Name result = mappingManager.publicEvaluateNAME(any, provision, "fallbackKey");

        assertEquals("jdoe@domain.com", result.getNameValue());
    }
}