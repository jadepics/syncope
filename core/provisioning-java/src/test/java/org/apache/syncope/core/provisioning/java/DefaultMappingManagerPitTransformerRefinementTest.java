package org.apache.syncope.core.provisioning.java;

import java.util.List;
import java.util.Map;
import org.apache.commons.jexl3.JexlBuilder;
import org.apache.syncope.common.lib.to.Item;
import org.apache.syncope.common.lib.types.AnyTypeKind;
import org.apache.syncope.common.lib.types.AttrSchemaType;
import org.apache.syncope.common.lib.types.SchemaType;
import org.apache.syncope.core.persistence.api.ApplicationContextProvider;
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
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Raffinamento black-box guidato dai risultati di mutation testing.
 *
 * <p>Il test confronta due configurazioni pubbliche dello stesso mapping:
 * transformer assente e transformer JEXL presente. Tutte le altre
 * caratteristiche dell'input restano invariate.</p>
 *
 * <p>Il contesto Spring inizializzato dalla fixture serve esclusivamente a
 * rendere disponibile l'infrastruttura necessaria alla creazione del
 * transformer reale. Il caso di test e l'oracolo osservano soltanto l'input
 * pubblico {@link Item} e il risultato pubblico di
 * {@link DefaultMappingManager#prepareAttr}.</p>
 */
class DefaultMappingManagerPitTransformerRefinementTest {

    private static final String INTERNAL_SCHEMA = "department";

    private static final String EXTERNAL_ATTRIBUTE = "ou";

    private static final String ORIGINAL_VALUE = "Engineering";

    private static final String TRANSFORMED_VALUE = "Engineering-external";

    private DefaultMappingManager mappingManager;

    private IntAttrNameParser intAttrNameParser;

    private ExternalResource resource;

    private org.apache.syncope.common.lib.to.Provision provision;

    private Any any;

    private AnnotationConfigApplicationContext testApplicationContext;

    private ConfigurableApplicationContext previousApplicationContext;

    private DefaultListableBeanFactory previousBeanFactory;

    @BeforeEach
    void setUp() throws Exception {
        /*
         * Preserva l'eventuale contesto già presente, così la classe resta
         * indipendente e non lascia stato statico dopo l'esecuzione.
         */
        previousApplicationContext =
                ApplicationContextProvider.getApplicationContext();
        previousBeanFactory =
                ApplicationContextProvider.getBeanFactory();

        intAttrNameParser = mock(IntAttrNameParser.class);
        resource = mock(ExternalResource.class);
        provision = mock(
                org.apache.syncope.common.lib.to.Provision.class);
        any = mock(Any.class);

        AnyType anyType = mock(AnyType.class);
        when(anyType.getKind()).thenReturn(AnyTypeKind.USER);
        when(any.getType()).thenReturn(anyType);
        when(any.getPlainAttrs()).thenReturn(List.of());

        when(intAttrNameParser.parse(
                anyString(),
                eq(AnyTypeKind.USER))).
                thenAnswer(invocation -> {
                    String schemaName = invocation.getArgument(0);

                    PlainSchema schema = mock(PlainSchema.class);
                    when(schema.getKey()).thenReturn(schemaName);
                    when(schema.getType()).thenReturn(
                            AttrSchemaType.String);

                    IntAttrName intAttrName =
                            mock(IntAttrName.class);
                    when(intAttrName.getSchemaInfo()).thenReturn(
                            new IntAttrName.SchemaInfo(
                                    schema,
                                    SchemaType.PLAIN));

                    return intAttrName;
                });

        DerAttrHandler derAttrHandler =
                mock(DerAttrHandler.class);
        when(derAttrHandler.getValues(any)).
                thenReturn(Map.of());

        JexlTools jexlTools = new JexlTools(
                new JexlBuilder().create());

        /*
         * Il transformer JEXL di Syncope viene creato come componente Spring.
         * La fixture prepara un contesto minimo reale con i soli collaboratori
         * richiesti dal transformer.
         */
        testApplicationContext =
                new AnnotationConfigApplicationContext();

        testApplicationContext.registerBean(
                JexlTools.class,
                () -> jexlTools);

        testApplicationContext.registerBean(
                DerAttrHandler.class,
                () -> derAttrHandler);

        testApplicationContext.refresh();

        /*
         * Elimina un eventuale BeanFactory statico precedente, in modo che
         * ApplicationContextProvider utilizzi quello del contesto di test.
         */
        ApplicationContextProvider.setBeanFactory(null);
        ApplicationContextProvider.setApplicationContext(
                testApplicationContext);

        mappingManager = new DefaultMappingManager(
                mock(UserDAO.class),
                mock(AnyObjectDAO.class),
                mock(GroupDAO.class),
                mock(RelationshipTypeDAO.class),
                mock(RealmSearchDAO.class),
                mock(ImplementationDAO.class),
                derAttrHandler,
                intAttrNameParser,
                mock(EncryptorManager.class),
                jexlTools);
    }

    @AfterEach
    void tearDown() {
        /*
         * Ripristina lo stato statico precedente per non influenzare altre
         * classi di test eseguite nella stessa JVM.
         */
        ApplicationContextProvider.setApplicationContext(
                previousApplicationContext);
        ApplicationContextProvider.setBeanFactory(
                previousBeanFactory);

        if (testApplicationContext != null) {
            testApplicationContext.close();
        }
    }

    /**
     * Confronta due classi della Category Partition:
     *
     * <ul>
     *   <li>transformer di propagazione assente;</li>
     *   <li>transformer JEXL di propagazione presente e con effetto
     *       osservabile.</li>
     * </ul>
     *
     * <p>Schema, valore interno, nome esterno, cardinalità e tipo dell'entità
     * rimangono uguali. Il solo fattore modificato è la configurazione
     * pubblica del transformer. L'oracolo verifica il valore completo
     * prodotto nei due casi.</p>
     */
    @Test
    void propagationJexlTransformerProducesObservableDifferentValue() {
        Item itemWithoutTransformer = item();

        Item itemWithTransformer = item();
        itemWithTransformer.setPropagationJEXLTransformer(
                "value + '-external'");

        MappingManager.PreparedAttr untransformed = prepare(
                itemWithoutTransformer,
                plainAttr(ORIGINAL_VALUE));

        MappingManager.PreparedAttr transformed = prepare(
                itemWithTransformer,
                plainAttr(ORIGINAL_VALUE));

        assertPreparedAttribute(
                untransformed,
                ORIGINAL_VALUE);

        assertPreparedAttribute(
                transformed,
                TRANSFORMED_VALUE);
    }

    private void assertPreparedAttribute(
            final MappingManager.PreparedAttr result,
            final String expectedValue) {

        assertNotNull(result);
        assertNull(result.connObjectLink());
        assertNotNull(result.attribute());
        assertEquals(
                EXTERNAL_ATTRIBUTE,
                result.attribute().getName());
        assertEquals(
                List.of(expectedValue),
                result.attribute().getValue());
    }

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

    private Item item() {
        Item item = new Item();
        item.setIntAttrName(INTERNAL_SCHEMA);
        item.setExtAttrName(EXTERNAL_ATTRIBUTE);
        item.setConnObjectKey(false);
        item.setPassword(false);

        return item;
    }

    private PlainAttr plainAttr(final String value) {
        PlainAttr attr = new PlainAttr();
        attr.setSchema(INTERNAL_SCHEMA);

        PlainAttrValue attrValue = new PlainAttrValue();
        attrValue.setStringValue(value);
        attr.add(attrValue);

        return attr;
    }
}