/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0.
 */
package org.apache.syncope.core.provisioning.java;

import org.apache.syncope.common.lib.to.Item;
import org.apache.syncope.common.lib.to.UserTO;
import org.apache.syncope.common.lib.types.AnyTypeKind;
import org.apache.syncope.core.persistence.api.EncryptorManager;
import org.apache.syncope.core.persistence.api.dao.AnyObjectDAO;
import org.apache.syncope.core.persistence.api.dao.GroupDAO;
import org.apache.syncope.core.persistence.api.dao.ImplementationDAO;
import org.apache.syncope.core.persistence.api.dao.RealmSearchDAO;
import org.apache.syncope.core.persistence.api.dao.RelationshipTypeDAO;
import org.apache.syncope.core.persistence.api.dao.UserDAO;
import org.apache.syncope.core.provisioning.api.DerAttrHandler;
import org.apache.syncope.core.provisioning.api.IntAttrName;
import org.apache.syncope.core.provisioning.api.IntAttrNameParser;
import org.apache.syncope.core.provisioning.api.jexl.JexlTools;
import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.AttributeBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Raffinamento black-box degli oracoli relativi ai campi pubblici speciali di
 * {@link UserTO}.
 *
 * <p>I test non verificano chiamate interne e non derivano casi dalle righe
 * mutate da PIT. Rafforzano invece l'osservazione del risultato pubblico:
 * oltre all'effetto atteso sul campo dedicato, verificano l'assenza dell'effetto
 * alternativo incompatibile, cioè la creazione di un attributo plain omonimo.</p>
 */
class DefaultMappingManagerPitNegativeOraclesRefinementTest {

    private DefaultMappingManager mappingManager;

    private IntAttrNameParser intAttrNameParser;

    @BeforeEach
    void setUp() {
        intAttrNameParser = mock(IntAttrNameParser.class);

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
     * Verifica la categoria pubblica "campo speciale uManager".
     *
     * <p>L'oracolo positivo controlla il nuovo valore del campo dedicato;
     * l'oracolo negativo controlla che lo stesso dato non venga rappresentato
     * come attributo plain denominato uManager.</p>
     */
    @Test
    void userManagerIsUpdatedOnlyThroughItsDedicatedField() throws Exception {
        UserTO userTO = new UserTO();
        userTO.setUManager("old-manager");

        Item item = userFieldItem("uManager");
        Attribute attribute = AttributeBuilder.build(
                "uManager",
                "new-manager");

        mappingManager.setIntValues(item, attribute, userTO);

        assertEquals("new-manager", userTO.getUManager());
        assertTrue(userTO.getPlainAttr("uManager").isEmpty());
    }

    /**
     * Verifica la categoria pubblica "campo speciale mustChangePassword".
     *
     * <p>Rispetto allo stato iniziale false, l'oracolo positivo richiede il
     * valore true nel campo booleano dedicato. L'oracolo negativo esclude la
     * creazione di un attributo plain omonimo, rendendo distinguibile il campo
     * speciale da uno schema plain ordinario.</p>
     */
    @Test
    void mustChangePasswordIsUpdatedOnlyThroughItsDedicatedField() throws Exception {
        UserTO userTO = new UserTO();
        userTO.setMustChangePassword(false);

        Item item = userFieldItem("mustChangePassword");
        Attribute attribute = AttributeBuilder.build(
                "mustChangePassword",
                true);

        assertFalse(userTO.isMustChangePassword());

        mappingManager.setIntValues(item, attribute, userTO);

        assertTrue(userTO.isMustChangePassword());
        assertTrue(userTO.getPlainAttr("mustChangePassword").isEmpty());
    }

    private Item userFieldItem(final String field) throws Exception {
        Item item = new Item();
        item.setIntAttrName(field);

        IntAttrName intAttrName = mock(IntAttrName.class);
        when(intAttrName.getField()).thenReturn(field);

        when(intAttrNameParser.parse(field, AnyTypeKind.USER)).
                thenReturn(intAttrName);

        return item;
    }
}
