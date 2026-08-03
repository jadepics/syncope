/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0.
 */
package org.apache.syncope.core.provisioning.java;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.apache.syncope.common.lib.to.Item;
import org.apache.syncope.common.lib.to.Mapping;
import org.apache.syncope.common.lib.to.Provision;
import org.apache.syncope.common.lib.types.AnyTypeKind;
import org.apache.syncope.common.lib.types.AttrSchemaType;
import org.apache.syncope.common.lib.types.MappingPurpose;
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
import org.apache.syncope.core.persistence.api.entity.user.User;
import org.apache.syncope.core.provisioning.api.DerAttrHandler;
import org.apache.syncope.core.provisioning.api.IntAttrName;
import org.apache.syncope.core.provisioning.api.IntAttrNameParser;
import org.apache.syncope.core.provisioning.api.MappingManager;
import org.apache.syncope.core.provisioning.api.jexl.JexlTools;
import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.AttributeUtil;
import org.identityconnectors.framework.common.objects.OperationalAttributes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DefaultMappingManagerPrepareAttrsFromAnyTest {

    private static final String EMAIL_SCHEMA = "email";

    private static final String MAIL_ATTRIBUTE = "mail";

    /*
     * Oggetto reale sottoposto a test.
     */
    private DefaultMappingManager mappingManager;

    /*
     * Parser simulato utilizzato da prepareAttr(), che viene richiamato
     * internamente da prepareAttrsFromAny().
     */
    private IntAttrNameParser intAttrNameParser;

    /*
     * Risorsa esterna simulata.
     *
     * In questi test non sono necessari un vero connector,
     * una connessione LDAP o altre configurazioni infrastrutturali.
     */
    private ExternalResource resource;

    /*
     * Tipo USER condiviso dagli oggetti Any e User creati nei test.
     */
    private AnyType userType;

    @BeforeEach
    void setUp() throws Exception {
        intAttrNameParser = mock(IntAttrNameParser.class);
        resource = mock(ExternalResource.class);
        userType = mock(AnyType.class);

        when(userType.getKind()).thenReturn(AnyTypeKind.USER);
        when(userType.getKey()).thenReturn(AnyTypeKind.USER.name());

        /*
         * Configurazione dinamica del parser.
         *
         * I nomi "username" e "password" vengono interpretati
         * come campi speciali dell'entità.
         *
         * Qualunque altro nome viene interpretato come schema
         * plain di tipo String.
         */
        when(intAttrNameParser.parse(
                anyString(),
                eq(AnyTypeKind.USER))).
                thenAnswer(invocation -> {

                    String intAttrName = invocation.getArgument(0);

                    IntAttrName parsed = mock(IntAttrName.class);

                    if ("username".equals(intAttrName)
                            || "password".equals(intAttrName)) {

                        when(parsed.getField()).
                                thenReturn(intAttrName);
                    } else {
                        PlainSchema schema = mock(PlainSchema.class);

                        when(schema.getKey()).
                                thenReturn(intAttrName);

                        when(schema.getType()).
                                thenReturn(AttrSchemaType.String);

                        when(parsed.getSchemaInfo()).thenReturn(
                                new IntAttrName.SchemaInfo(
                                        schema,
                                        SchemaType.PLAIN));
                    }

                    return parsed;
                });

        /*
         * DefaultMappingManager è reale.
         *
         * Sono simulati soltanto i DAO e i servizi infrastrutturali
         * che non partecipano direttamente ai cinque casi base.
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
     * Verifica una Mapping valida ma priva di item.
     *
     * Non essendoci item, connObjectKey o attributo enable,
     * il metodo deve restituire un insieme vuoto.
     */
    @Test
    void emptyMapping() {
        Any any = emptyAny();

        MappingManager.PreparedAttrs result =
                mappingManager.prepareAttrsFromAny(
                        any,
                        null,
                        true,
                        null,
                        resource,
                        provision());

        assertNotNull(result);
        assertNull(result.connObjectLink());
        assertTrue(result.attributes().isEmpty());
    }

    /**
     * Verifica una Mapping contenente esattamente un item plain:
     *
     * attributo interno: email
     * attributo esterno: mail
     *
     * enable=true viene fornito per distinguere questo caso
     * dal successivo test dedicato all'assenza di enable.
     */
    @Test
    void mappingWithSinglePlainItem() {
        PlainAttr email = plainAttr(
                EMAIL_SCHEMA,
                "mario.rossi@example.org");

        Any any = anyWithPlainAttr(email);

        MappingManager.PreparedAttrs result =
                mappingManager.prepareAttrsFromAny(
                        any,
                        null,
                        true,
                        true,
                        resource,
                        provision(plainItem(
                                EMAIL_SCHEMA,
                                MAIL_ATTRIBUTE)));

        Attribute mail = AttributeUtil.find(
                MAIL_ATTRIBUTE,
                result.attributes());

        Attribute enable = AttributeUtil.find(
                OperationalAttributes.ENABLE_NAME,
                result.attributes());

        assertNotNull(result);
        assertNull(result.connObjectLink());

        /*
         * Deve essere presente l'unico attributo derivante
         * dalla Mapping.
         */
        assertNotNull(mail);
        assertEquals(
                List.of("mario.rossi@example.org"),
                mail.getValue());

        /*
         * enable non deriva dalla Mapping, ma dal parametro
         * Boolean enable passato al metodo.
         */
        assertNotNull(enable);

        assertEquals(2, result.attributes().size());
    }

    /**
     * Verifica esplicitamente la preparazione degli attributi
     * partendo da un'entità User.
     *
     * L'item usa il campo speciale username e lo mappa
     * nell'attributo esterno uid.
     */
    @Test
    void prepareAttributesFromUser() {
        User user = user("mrossi");

        MappingManager.PreparedAttrs result =
                mappingManager.prepareAttrsFromAny(
                        user,
                        null,
                        true,
                        null,
                        resource,
                        provision(fieldItem(
                                "username",
                                "uid",
                                false)));

        Attribute uid = AttributeUtil.find(
                "uid",
                result.attributes());

        assertNotNull(result);
        assertNull(result.connObjectLink());

        assertNotNull(uid);
        assertEquals(
                List.of("mrossi"),
                uid.getValue());

        assertEquals(1, result.attributes().size());
    }

    /**
     * Verifica il comportamento quando nella Mapping è presente
     * un item password, ma non è disponibile alcuna password.
     *
     * changePwd=true è intenzionale: in questo modo l'assenza
     * di __PASSWORD__ dipende dalla mancanza del valore e non
     * dalla successiva rimozione eseguita con changePwd=false.
     */
    @Test
    void prepareWithoutPassword() {
        PlainAttr email = plainAttr(
                EMAIL_SCHEMA,
                "mario.rossi@example.org");

        User user = userWithPlainAttr(
                "mrossi",
                email);

        /*
         * Il sistema non può recuperare o decodificare
         * una password memorizzata per questo User.
         */
        when(user.canDecodeSecrets()).thenReturn(false);

        Item emailItem = plainItem(
                EMAIL_SCHEMA,
                MAIL_ATTRIBUTE);

        Item passwordItem = fieldItem(
                "password",
                OperationalAttributes.PASSWORD_NAME,
                true);

        MappingManager.PreparedAttrs result =
                mappingManager.prepareAttrsFromAny(
                        user,
                        null,
                        true,
                        null,
                        resource,
                        provision(emailItem, passwordItem));

        Attribute mail = AttributeUtil.find(
                MAIL_ATTRIBUTE,
                result.attributes());

        Attribute password = AttributeUtil.find(
                OperationalAttributes.PASSWORD_NAME,
                result.attributes());

        /*
         * L'attributo ordinario viene comunque preparato.
         */
        assertNotNull(mail);
        assertEquals(
                List.of("mario.rossi@example.org"),
                mail.getValue());

        /*
         * Non essendoci una password esplicita né decodificabile,
         * __PASSWORD__ non deve essere presente.
         */
        assertNull(password);
        assertEquals(1, result.attributes().size());
    }

    /**
     * Verifica che enable=null non generi l'attributo
     * operativo ConnId __ENABLE__.
     *
     * Viene mantenuto un item plain valido per dimostrare
     * che il metodo continua a preparare gli altri attributi.
     */
    @Test
    void prepareWithoutEnableAttribute() {
        PlainAttr email = plainAttr(
                EMAIL_SCHEMA,
                "mario.rossi@example.org");

        Any any = anyWithPlainAttr(email);

        MappingManager.PreparedAttrs result =
                mappingManager.prepareAttrsFromAny(
                        any,
                        null,
                        true,
                        null,
                        resource,
                        provision(plainItem(
                                EMAIL_SCHEMA,
                                MAIL_ATTRIBUTE)));

        Attribute mail = AttributeUtil.find(
                MAIL_ATTRIBUTE,
                result.attributes());

        Attribute enable = AttributeUtil.find(
                OperationalAttributes.ENABLE_NAME,
                result.attributes());

        /*
         * L'attributo ordinario deve essere presente.
         */
        assertNotNull(mail);
        assertEquals(
                List.of("mario.rossi@example.org"),
                mail.getValue());

        /*
         * enable=null significa che non deve essere aggiunto
         * alcun attributo operativo __ENABLE__.
         */
        assertNull(enable);
        assertEquals(1, result.attributes().size());
    }

    /**
     * Crea una Provision simulata con una Mapping contenente
     * gli item passati al metodo.
     *
     * La Mapping non contiene connObjectKey perché questi test
     * base non verificano ancora la costruzione di __NAME__.
     */
    private Provision provision(final Item... items) {
        Mapping mapping = mock(Mapping.class);

        when(mapping.getItems()).
                thenReturn(Arrays.asList(items));

        when(mapping.getConnObjectKeyItem()).
                thenReturn(Optional.empty());

        Provision provision = mock(Provision.class);
        when(provision.getMapping()).
                thenReturn(mapping);

        return provision;
    }

    /**
     * Crea un item plain reale destinato alla propagazione.
     *
     * MappingUtils considera, durante prepareAttrsFromAny(),
     * soltanto item con purpose PROPAGATION oppure BOTH.
     */
    private Item plainItem(
            final String intAttrName,
            final String extAttrName) {

        Item item = new Item();
        item.setIntAttrName(intAttrName);
        item.setExtAttrName(extAttrName);
        item.setPurpose(MappingPurpose.PROPAGATION);
        item.setConnObjectKey(false);
        item.setPassword(false);

        return item;
    }

    /**
     * Crea un item relativo a un campo speciale, per esempio
     * username oppure password.
     */
    private Item fieldItem(
            final String intAttrName,
            final String extAttrName,
            final boolean password) {

        Item item = new Item();
        item.setIntAttrName(intAttrName);
        item.setExtAttrName(extAttrName);
        item.setPurpose(MappingPurpose.PROPAGATION);
        item.setConnObjectKey(false);
        item.setPassword(password);

        return item;
    }

    /**
     * Crea un Any simulato senza attributi plain.
     */
    private Any emptyAny() {
        Any any = mock(Any.class);

        when(any.getType()).thenReturn(userType);
        when(any.getPlainAttrs()).thenReturn(List.of());
        when(any.getPlainAttr(anyString())).
                thenReturn(Optional.empty());

        return any;
    }

    /**
     * Crea un Any simulato che possiede un singolo
     * attributo plain reale.
     */
    private Any anyWithPlainAttr(final PlainAttr plainAttr) {
        Any any = mock(Any.class);

        when(any.getType()).thenReturn(userType);
        when(any.getPlainAttrs()).
                thenReturn(List.of(plainAttr));

        when(any.getPlainAttr(plainAttr.getSchema())).
                thenReturn(Optional.of(plainAttr));

        return any;
    }

    /**
     * Crea uno User simulato con username e senza
     * attributi plain.
     */
    private User user(final String username) {
        User user = mock(User.class);

        when(user.getType()).thenReturn(userType);
        when(user.getUsername()).thenReturn(username);
        when(user.getPlainAttrs()).thenReturn(List.of());
        when(user.getPlainAttr(anyString())).
                thenReturn(Optional.empty());

        return user;
    }

    /**
     * Crea uno User simulato con username e con un
     * attributo plain reale.
     */
    private User userWithPlainAttr(
            final String username,
            final PlainAttr plainAttr) {

        User user = mock(User.class);

        when(user.getType()).thenReturn(userType);
        when(user.getUsername()).thenReturn(username);

        when(user.getPlainAttrs()).
                thenReturn(List.of(plainAttr));

        when(user.getPlainAttr(plainAttr.getSchema())).
                thenReturn(Optional.of(plainAttr));

        return user;
    }

    /**
     * Crea un vero PlainAttr con uno o più valori String.
     *
     * I dati principali del test non sono mock.
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