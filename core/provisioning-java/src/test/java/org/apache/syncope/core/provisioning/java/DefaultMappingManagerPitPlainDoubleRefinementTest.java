/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0.
 */
package org.apache.syncope.core.provisioning.java;

import java.util.List;
import org.apache.syncope.common.lib.to.Item;
import org.apache.syncope.common.lib.to.Provision;
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
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Raffinamento black-box dedicato alla categoria pubblica
 * {@link AttrSchemaType#Double}.
 *
 * <p>Il caso non è derivato da una condizione interna della classe. Completa
 * invece la partizione dei tipi plain già rappresentata nella suite da String,
 * Boolean, Long e Date. Rispetto al caso nominale Long cambiano soltanto il
 * tipo pubblico dello schema e il corrispondente valore.</p>
 */
class DefaultMappingManagerPitPlainDoubleRefinementTest {

    private static final String SCHEMA = "measurement";

    private static final double EXPECTED_VALUE = 12.5D;

    private DefaultMappingManager mappingManager;

    private ExternalResource resource;

    private Provision provision;

    private Item item;

    @BeforeEach
    void setUp() {
        resource = mock(ExternalResource.class);
        provision = mock(Provision.class);
        item = mock(Item.class);

        when(item.getIntAttrName()).thenReturn(SCHEMA);
        when(item.getTransformers()).thenReturn(List.of());

        mappingManager = new DefaultMappingManager(
                mock(UserDAO.class),
                mock(AnyObjectDAO.class),
                mock(GroupDAO.class),
                mock(RelationshipTypeDAO.class),
                mock(RealmSearchDAO.class),
                mock(ImplementationDAO.class),
                mock(DerAttrHandler.class),
                mock(IntAttrNameParser.class),
                mock(EncryptorManager.class),
                mock(JexlTools.class));
    }

    /**
     * Verifica che uno schema plain Double con un unico valore presente produca:
     *
     * <ul>
     *   <li>il tipo pubblico Double nel risultato;</li>
     *   <li>esattamente un valore;</li>
     *   <li>il valore numerico atteso nel campo tipizzato Double;</li>
     *   <li>nessuna rappresentazione alternativa nel campo String.</li>
     * </ul>
     *
     * <p>L'ultima asserzione è un oracolo negativo sull'output pubblico e rende
     * distinguibile questa categoria da quella String, senza osservare chiamate
     * o decisioni interne.</p>
     */
    @Test
    void plainDoubleValueIsReturnedWithItsPublicSchemaType() {
        Any source = mock(Any.class);

        MappingManager.IntValues result = mappingManager.getIntValues(
                resource,
                provision,
                item,
                plainIntAttrName(),
                AttrSchemaType.Double,
                source,
                AccountGetter.DEFAULT,
                getterReturning(plainAttr()));

        assertEquals(AttrSchemaType.Double, result.attrSchemaType());
        assertEquals(1, result.values().size());

        PlainAttrValue actual = result.values().getFirst();

        assertEquals(Double.valueOf(EXPECTED_VALUE), actual.getDoubleValue());
        assertNull(actual.getStringValue());
    }

    private IntAttrName plainIntAttrName() {
        PlainSchema schema = mock(PlainSchema.class);
        when(schema.getKey()).thenReturn(SCHEMA);
        when(schema.getType()).thenReturn(AttrSchemaType.Double);

        IntAttrName intAttrName = mock(IntAttrName.class);
        when(intAttrName.getSchemaInfo()).thenReturn(
                new IntAttrName.SchemaInfo(schema, SchemaType.PLAIN));

        return intAttrName;
    }

    private PlainAttrGetter getterReturning(final PlainAttr attribute) {
        return (source, schemaName) -> attribute;
    }

    private PlainAttr plainAttr() {
        PlainAttr attribute = new PlainAttr();
        attribute.setSchema(SCHEMA);

        PlainAttrValue value = new PlainAttrValue();
        value.setDoubleValue(EXPECTED_VALUE);
        attribute.add(value);

        return attribute;
    }
}
