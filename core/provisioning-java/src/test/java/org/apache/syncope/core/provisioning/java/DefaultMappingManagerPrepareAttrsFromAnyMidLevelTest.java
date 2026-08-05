/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0.
 */
package org.apache.syncope.core.provisioning.java;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.apache.commons.jexl3.JexlContext;
import org.apache.syncope.common.lib.to.Item;
import org.apache.syncope.common.lib.to.Mapping;
import org.apache.syncope.common.lib.to.Provision;
import org.apache.syncope.common.lib.types.MappingPurpose;
import org.apache.syncope.core.persistence.api.EncryptorManager;
import org.apache.syncope.core.persistence.api.dao.AnyObjectDAO;
import org.apache.syncope.core.persistence.api.dao.GroupDAO;
import org.apache.syncope.core.persistence.api.dao.ImplementationDAO;
import org.apache.syncope.core.persistence.api.dao.RealmSearchDAO;
import org.apache.syncope.core.persistence.api.dao.RelationshipTypeDAO;
import org.apache.syncope.core.persistence.api.dao.UserDAO;
import org.apache.syncope.core.persistence.api.entity.Any;
import org.apache.syncope.core.persistence.api.entity.ExternalResource;
import org.apache.syncope.core.persistence.api.entity.anyobject.AnyObject;
import org.apache.syncope.core.persistence.api.entity.group.Group;
import org.apache.syncope.core.persistence.api.entity.user.User;
import org.apache.syncope.core.provisioning.api.AccountGetter;
import org.apache.syncope.core.provisioning.api.DerAttrHandler;
import org.apache.syncope.core.provisioning.api.IntAttrNameParser;
import org.apache.syncope.core.provisioning.api.MappingManager;
import org.apache.syncope.core.provisioning.api.PlainAttrGetter;
import org.apache.syncope.core.provisioning.api.jexl.JexlTools;
import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.AttributeBuilder;
import org.identityconnectors.framework.common.objects.AttributeUtil;
import org.identityconnectors.framework.common.objects.Name;
import org.identityconnectors.framework.common.objects.OperationalAttributes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Test manuali di livello intermedio per
 * DefaultMappingManager.prepareAttrsFromAny().
 *
 * Il metodo prepareAttrsFromAny() viene eseguito realmente.
 * prepareAttr() viene invece simulato tramite uno spy,
 * perché è già oggetto di una suite separata.
 */
class DefaultMappingManagerPrepareAttrsFromAnyMidLevelTest {

    /**
     * Spy della vera classe sottoposta a test.
     */
    private DefaultMappingManager mappingManager;

    /**
     * Oggetti comuni utilizzati dai test.
     */
    private ExternalResource resource;

    private Provision provision;

    private Mapping mapping;

    /**
     * Dipendenze mantenute come campi perché servono
     * nei test relativi al connObjectLink.
     */
    private DerAttrHandler derAttrHandler;

    private JexlTools jexlTools;

    @BeforeEach
    void setUp() {
        resource = mock(ExternalResource.class);
        provision = mock(Provision.class);
        mapping = mock(Mapping.class);

        derAttrHandler = mock(DerAttrHandler.class);
        jexlTools = mock(JexlTools.class);

        when(provision.getMapping()).thenReturn(mapping);

        /*
         * Configurazione iniziale: mapping senza item
         * e senza connObjectKey.
         */
        when(mapping.getItems()).thenReturn(List.of());
        when(mapping.getConnObjectKeyItem()).
                thenReturn(Optional.empty());

        /*
         * La vera istanza viene avvolta in uno spy.
         * Soltanto prepareAttr() sarà simulato nei test.
         */
        mappingManager = spy(new DefaultMappingManager(
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
     * Verifica che prepareAttrsFromAny() possa aggregare
     * gli attributi appartenenti a un Group.
     */
    @Test
    void prepareAttributesFromGroup() {
        Group group = mock(Group.class);
        when(group.getPlainAttrs()).thenReturn(List.of());

        Item item = item(
                "description",
                "description",
                MappingPurpose.PROPAGATION);

        setMappingItems(item);

        stubPreparedAttr(
                resource,
                group,
                item,
                null,
                preparedAttribute(
                        "description",
                        "developers"));

        MappingManager.PreparedAttrs result =
                mappingManager.prepareAttrsFromAny(
                        group,
                        null,
                        true,
                        null,
                        resource,
                        provision);

        assertEquals(
                "developers",
                onlyValue(result, "description"));
    }

    /**
     * Verifica la preparazione degli attributi
     * appartenenti a un Any Object.
     */
    @Test
    void prepareAttributesFromAnyObject() {
        AnyObject printer = mock(AnyObject.class);
        when(printer.getPlainAttrs()).thenReturn(List.of());

        Item item = item(
                "location",
                "location",
                MappingPurpose.PROPAGATION);

        setMappingItems(item);

        stubPreparedAttr(
                resource,
                printer,
                item,
                null,
                preparedAttribute(
                        "location",
                        "room-10"));

        MappingManager.PreparedAttrs result =
                mappingManager.prepareAttrsFromAny(
                        printer,
                        null,
                        true,
                        null,
                        resource,
                        provision);

        assertEquals(
                "room-10",
                onlyValue(result, "location"));
    }

    /**
     * Con changePwd=true, una password disponibile
     * deve rimanere nell'insieme degli attributi preparati.
     */
    @Test
    void changePasswordTrueWithAvailablePassword() {
        User user = mock(User.class);
        when(user.getPlainAttrs()).thenReturn(List.of());

        Item passwordItem = passwordItem();
        setMappingItems(passwordItem);

        stubPreparedAttr(
                resource,
                user,
                passwordItem,
                "SecretPassword123!",
                preparedPassword("SecretPassword123!"));

        MappingManager.PreparedAttrs result =
                mappingManager.prepareAttrsFromAny(
                        user,
                        "SecretPassword123!",
                        true,
                        null,
                        resource,
                        provision);

        assertNotNull(
                AttributeUtil.find(
                        OperationalAttributes.PASSWORD_NAME,
                        result.attributes()));
    }

    /**
     * Con changePwd=false, la password viene prima preparata
     * ma successivamente rimossa dal risultato aggregato.
     */
    @Test
    void changePasswordFalseWithAvailablePassword() {
        User user = mock(User.class);
        when(user.getPlainAttrs()).thenReturn(List.of());

        Item passwordItem = passwordItem();
        setMappingItems(passwordItem);

        stubPreparedAttr(
                resource,
                user,
                passwordItem,
                "SecretPassword123!",
                preparedPassword("SecretPassword123!"));

        MappingManager.PreparedAttrs result =
                mappingManager.prepareAttrsFromAny(
                        user,
                        "SecretPassword123!",
                        false,
                        null,
                        resource,
                        provision);

        assertNull(
                AttributeUtil.find(
                        OperationalAttributes.PASSWORD_NAME,
                        result.attributes()));
    }

    /**
     * Con changePwd=true ma password assente,
     * prepareAttr() non produce alcun attributo password.
     */
    @Test
    void changePasswordTrueWithMissingPassword() {
        User user = mock(User.class);
        when(user.getPlainAttrs()).thenReturn(List.of());

        Item passwordItem = passwordItem();
        setMappingItems(passwordItem);

        stubPreparedAttr(
                resource,
                user,
                passwordItem,
                null,
                null);

        MappingManager.PreparedAttrs result =
                mappingManager.prepareAttrsFromAny(
                        user,
                        null,
                        true,
                        null,
                        resource,
                        provision);

        assertNull(
                AttributeUtil.find(
                        OperationalAttributes.PASSWORD_NAME,
                        result.attributes()));
    }

    /**
     * enable=true deve produrre l'attributo operativo
     * ConnId __ENABLE__ con valore true.
     */
    @Test
    void enableTrue() {
        Group group = mock(Group.class);
        when(group.getPlainAttrs()).thenReturn(List.of());

        MappingManager.PreparedAttrs result =
                mappingManager.prepareAttrsFromAny(
                        group,
                        null,
                        true,
                        true,
                        resource,
                        provision);

        assertEquals(
                Boolean.TRUE,
                onlyValue(
                        result,
                        OperationalAttributes.ENABLE_NAME));
    }

    /**
     * enable=false deve produrre __ENABLE__
     * con valore false.
     */
    @Test
    void enableFalse() {
        Group group = mock(Group.class);
        when(group.getPlainAttrs()).thenReturn(List.of());

        MappingManager.PreparedAttrs result =
                mappingManager.prepareAttrsFromAny(
                        group,
                        null,
                        true,
                        false,
                        resource,
                        provision);

        assertEquals(
                Boolean.FALSE,
                onlyValue(
                        result,
                        OperationalAttributes.ENABLE_NAME));
    }

    /**
     * Con enable=null non deve essere aggiunto
     * alcun attributo operativo __ENABLE__.
     */
    @Test
    void enableNull() {
        Group group = mock(Group.class);
        when(group.getPlainAttrs()).thenReturn(List.of());

        MappingManager.PreparedAttrs result =
                mappingManager.prepareAttrsFromAny(
                        group,
                        null,
                        true,
                        null,
                        resource,
                        provision);

        assertNull(
                AttributeUtil.find(
                        OperationalAttributes.ENABLE_NAME,
                        result.attributes()));
    }

    /**
     * Il mapping contiene:
     *
     * - un item PROPAGATION;
     * - un item PULL.
     *
     * prepareAttrsFromAny() deve elaborare soltanto
     * l'item destinato alla propagazione.
     */
    @Test
    void mappingWithPropagationAndPullItems() {
        Group group = mock(Group.class);
        when(group.getPlainAttrs()).thenReturn(List.of());

        Item propagationItem = item(
                "description",
                "description",
                MappingPurpose.PROPAGATION);

        Item pullItem = item(
                "internalCode",
                "externalCode",
                MappingPurpose.PULL);

        setMappingItems(propagationItem, pullItem);

        stubPreparedAttr(
                resource,
                group,
                propagationItem,
                null,
                preparedAttribute(
                        "description",
                        "group-description"));

        /*
         * Prepariamo anche un risultato ipotetico per PULL.
         * Se il metodo lo elaborasse per errore,
         * l'attributo comparirebbe nel risultato.
         */
        stubPreparedAttr(
                resource,
                group,
                pullItem,
                null,
                preparedAttribute(
                        "externalCode",
                        "should-not-appear"));

        MappingManager.PreparedAttrs result =
                mappingManager.prepareAttrsFromAny(
                        group,
                        null,
                        true,
                        null,
                        resource,
                        provision);

        assertEquals(
                "group-description",
                onlyValue(result, "description"));

        assertNull(
                AttributeUtil.find(
                        "externalCode",
                        result.attributes()));

        verify(mappingManager, never()).prepareAttr(
                resource,
                provision,
                pullItem,
                group,
                null,
                AccountGetter.DEFAULT,
                AccountGetter.DEFAULT,
                PlainAttrGetter.DEFAULT);
    }

    /**
     * Verifica che il risultato prodotto da un item
     * configurato con transformer venga mantenuto
     * nel risultato aggregato.
     *
     * La trasformazione vera e propria appartiene a prepareAttr()
     * e non viene duplicata in questa suite.
     */
    @Test
    void mappingWithTransformer() {
        Group group = mock(Group.class);
        when(group.getPlainAttrs()).thenReturn(List.of());

        Item item = item(
                "name",
                "displayName",
                MappingPurpose.PROPAGATION);

        item.setPropagationJEXLTransformer(
                "value.toUpperCase()");

        setMappingItems(item);

        /*
         * Simuliamo il risultato già trasformato
         * prodotto da prepareAttr().
         */
        stubPreparedAttr(
                resource,
                group,
                item,
                null,
                preparedAttribute(
                        "displayName",
                        "DEVELOPERS"));

        MappingManager.PreparedAttrs result =
                mappingManager.prepareAttrsFromAny(
                        group,
                        null,
                        true,
                        null,
                        resource,
                        provision);

        assertEquals(
                "DEVELOPERS",
                onlyValue(result, "displayName"));
    }

    /**
     * Verifica un mapping con connObjectKey.
     *
     * Il valore della chiave viene utilizzato per creare
     * l'attributo operativo ConnId __NAME__.
     */
    @Test
    void mappingWithConnObjectKey() {
        Group group = mock(Group.class);
        when(group.getPlainAttrs()).thenReturn(List.of());

        Item keyItem = item(
                "key",
                "uid",
                MappingPurpose.PROPAGATION);

        keyItem.setConnObjectKey(true);

        setMappingItems(keyItem);

        when(mapping.getConnObjectKeyItem()).
                thenReturn(Optional.of(keyItem));

        /*
         * Per un connObjectKey prepareAttr() restituisce
         * il valore nel primo campo di PreparedAttr.
         */
        stubPreparedAttr(
                resource,
                group,
                keyItem,
                null,
                new MappingManager.PreparedAttr(
                        "group-key-001",
                        null));

        MappingManager.PreparedAttrs result =
                mappingManager.prepareAttrsFromAny(
                        group,
                        null,
                        true,
                        null,
                        resource,
                        provision);

        assertEquals(
                "group-key-001",
                result.connObjectLink());

        Attribute nameAttribute =
                AttributeUtil.find(
                        Name.NAME,
                        result.attributes());

        assertNotNull(nameAttribute);
        assertTrue(nameAttribute instanceof Name);

        assertEquals(
                "group-key-001",
                ((Name) nameAttribute).getNameValue());
    }

    /**
     * Verifica un mapping con connObjectLink.
     *
     * La connObjectKey rimane "group-key-001",
     * mentre l'espressione produce il nome esterno
     * "developers-external".
     */
    @Test
    void mappingWithConnObjectLink() {
        Group group = mock(Group.class);

        when(group.getPlainAttrs()).thenReturn(List.of());
        when(derAttrHandler.getValues(group)).
                thenReturn(Map.of());

        Item keyItem = item(
                "key",
                "uid",
                MappingPurpose.PROPAGATION);

        keyItem.setConnObjectKey(true);

        setMappingItems(keyItem);

        when(mapping.getConnObjectKeyItem()).
                thenReturn(Optional.of(keyItem));

        when(mapping.getConnObjectLink()).
                thenReturn("name + '-external'");

        /*
         * Il JEXL engine viene simulato:
         * il test riguarda l'uso del risultato,
         * non il parser dell'espressione.
         */
        when(jexlTools.evaluateExpression(
                eq("name + '-external'"),
                any(JexlContext.class))).
                thenReturn("developers-external");

        stubPreparedAttr(
                resource,
                group,
                keyItem,
                null,
                new MappingManager.PreparedAttr(
                        "group-key-001",
                        null));

        MappingManager.PreparedAttrs result =
                mappingManager.prepareAttrsFromAny(
                        group,
                        null,
                        true,
                        null,
                        resource,
                        provision);

        /*
         * Il valore raccolto come connObjectKey
         * rimane quello originale.
         */
        assertEquals(
                "group-key-001",
                result.connObjectLink());

        Attribute nameAttribute =
                AttributeUtil.find(
                        Name.NAME,
                        result.attributes());

        assertNotNull(nameAttribute);
        assertTrue(nameAttribute instanceof Name);

        /*
         * __NAME__ usa invece il connObjectLink valutato.
         */
        assertEquals(
                "developers-external",
                ((Name) nameAttribute).getNameValue());

        /*
         * Poiché key e name sono differenti,
         * viene conservato anche l'attributo esterno uid.
         */
        assertEquals(
                "group-key-001",
                onlyValue(result, "uid"));
    }

    /**
     * Any è un parametro indispensabile.
     *
     * Il metodo tenta immediatamente di leggere
     * i suoi attributi plain.
     */
    @Test
    void nullAny() {
        assertThrows(
                NullPointerException.class,
                () -> mappingManager.prepareAttrsFromAny(
                        null,
                        null,
                        true,
                        null,
                        resource,
                        provision));
    }

    /**
     * prepareAttrsFromAny() non valida direttamente
     * ExternalResource.
     *
     * Il valore null viene inoltrato a prepareAttr().
     * Simulando un risultato valido, l'aggregazione
     * può comunque concludersi.
     */
    @Test
    void nullExternalResource() {
        Group group = mock(Group.class);
        when(group.getPlainAttrs()).thenReturn(List.of());

        Item item = item(
                "description",
                "description",
                MappingPurpose.PROPAGATION);

        setMappingItems(item);

        stubPreparedAttr(
                null,
                group,
                item,
                null,
                preparedAttribute(
                        "description",
                        "developers"));

        MappingManager.PreparedAttrs result =
                mappingManager.prepareAttrsFromAny(
                        group,
                        null,
                        true,
                        null,
                        null,
                        provision);

        assertEquals(
                "developers",
                onlyValue(result, "description"));
    }

    /**
     * Provision è indispensabile perché contiene
     * il Mapping e la relativa lista di Item.
     */
    @Test
    void nullProvision() {
        Group group = mock(Group.class);
        when(group.getPlainAttrs()).thenReturn(List.of());

        assertThrows(
                NullPointerException.class,
                () -> mappingManager.prepareAttrsFromAny(
                        group,
                        null,
                        true,
                        null,
                        resource,
                        null));
    }

    /**
     * Crea un normale Item con i valori essenziali.
     */
    private Item item(
            final String internalName,
            final String externalName,
            final MappingPurpose purpose) {

        Item item = new Item();
        item.setIntAttrName(internalName);
        item.setExtAttrName(externalName);
        item.setPurpose(purpose);

        return item;
    }

    /**
     * Crea l'Item speciale dedicato alla password.
     */
    private Item passwordItem() {
        Item item = item(
                "password",
                OperationalAttributes.PASSWORD_NAME,
                MappingPurpose.PROPAGATION);

        item.setPassword(true);
        return item;
    }

    /**
     * Configura la lista di Item restituita dal Mapping.
     */
    private void setMappingItems(final Item... items) {
        when(mapping.getItems()).
                thenReturn(List.of(items));
    }

    /**
     * Simula il risultato di prepareAttr().
     *
     * prepareAttrsFromAny() resta reale ed esegue
     * normalmente filtraggio e aggregazione.
     */
    private void stubPreparedAttr(
            final ExternalResource selectedResource,
            final Any entity,
            final Item item,
            final String password,
            final MappingManager.PreparedAttr preparedAttr) {

        doReturn(preparedAttr).
                when(mappingManager).
                prepareAttr(
                        selectedResource,
                        provision,
                        item,
                        entity,
                        password,
                        AccountGetter.DEFAULT,
                        AccountGetter.DEFAULT,
                        PlainAttrGetter.DEFAULT);
    }

    /**
     * Crea un PreparedAttr ordinario.
     */
    private MappingManager.PreparedAttr preparedAttribute(
            final String name,
            final Object value) {

        return new MappingManager.PreparedAttr(
                null,
                AttributeBuilder.build(name, value));
    }

    /**
     * Crea un PreparedAttr contenente una password ConnId.
     */
    private MappingManager.PreparedAttr preparedPassword(
            final String password) {

        return new MappingManager.PreparedAttr(
                null,
                AttributeBuilder.buildPassword(
                        password.toCharArray()));
    }

    /**
     * Recupera un attributo dal risultato e ne restituisce
     * l'unico valore.
     */
    private Object onlyValue(
            final MappingManager.PreparedAttrs result,
            final String attributeName) {

        Attribute attribute =
                AttributeUtil.find(
                        attributeName,
                        result.attributes());

        assertNotNull(attribute);
        assertNotNull(attribute.getValue());
        assertEquals(1, attribute.getValue().size());

        return attribute.getValue().getFirst();
    }
}