package org.apache.syncope.core.provisioning.java.LLM;

import java.util.List;

import org.apache.syncope.common.lib.to.Item;
import org.apache.syncope.common.lib.to.Provision;
import org.apache.syncope.common.lib.types.AttrSchemaType;
import org.apache.syncope.core.persistence.api.EncryptorManager;
import org.apache.syncope.core.persistence.api.dao.AnyObjectDAO;
import org.apache.syncope.core.persistence.api.dao.GroupDAO;
import org.apache.syncope.core.persistence.api.dao.ImplementationDAO;
import org.apache.syncope.core.persistence.api.dao.RealmSearchDAO;
import org.apache.syncope.core.persistence.api.dao.RelationshipTypeDAO;
import org.apache.syncope.core.persistence.api.dao.UserDAO;
import org.apache.syncope.core.persistence.api.entity.ExternalResource;
import org.apache.syncope.core.persistence.api.entity.Realm;
import org.apache.syncope.core.persistence.api.entity.user.User;
import org.apache.syncope.core.provisioning.api.AccountGetter;
import org.apache.syncope.core.provisioning.api.DerAttrHandler;
import org.apache.syncope.core.provisioning.api.IntAttrName;
import org.apache.syncope.core.provisioning.api.IntAttrNameParser;
import org.apache.syncope.core.provisioning.api.MappingManager;
import org.apache.syncope.core.provisioning.api.PlainAttrGetter;
import org.apache.syncope.core.provisioning.api.jexl.JexlTools;
import org.apache.syncope.core.provisioning.java.DefaultMappingManager;
import org.identityconnectors.framework.common.objects.Attribute;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * JUnit 5 tests for {@link DefaultMappingManager}.
 */
@ExtendWith(MockitoExtension.class)
public class DefaultMappingManagerTestZeroPrompting2 {

    /*
     * Dipendenze del costruttore di DefaultMappingManager.
     * Sono tutte dipendenze già presenti in Syncope:
     * non viene aggiunta alcuna libreria.
     */

    @Mock
    private UserDAO userDAO;

    @Mock
    private AnyObjectDAO anyObjectDAO;

    @Mock
    private GroupDAO groupDAO;

    @Mock
    private RelationshipTypeDAO relationshipTypeDAO;

    @Mock
    private RealmSearchDAO realmSearchDAO;

    @Mock
    private ImplementationDAO implementationDAO;

    @Mock
    private DerAttrHandler derAttrHandler;

    @Mock
    private IntAttrNameParser intAttrNameParser;

    @Mock
    private EncryptorManager encryptorManager;

    @Mock
    private JexlTools jexlTools;

    @InjectMocks
    private DefaultMappingManager mappingManager;

    @Mock
    private ExternalResource resource;

    @Mock
    private Item mappingItem;

    @Mock
    private Realm realm;

    @Mock
    private User user;

    @Mock
    private Provision provision;

    @Mock
    private IntAttrName intAttrName;

    /**
     * Tests prepareAttr for the "name" field of a Realm.
     */
    @Test
    public void testPrepareAttrForRealm() throws Exception {
        when(mappingItem.getExtAttrName()).thenReturn("ou");
        when(mappingItem.getIntAttrName()).thenReturn("name");
        when(mappingItem.getTransformers()).thenReturn(List.of());

        when(intAttrNameParser.parse("name")).thenReturn(intAttrName);
        when(intAttrName.getField()).thenReturn("name");

        when(realm.getName()).thenReturn("master");

        MappingManager.PreparedAttr prepared =
                mappingManager.prepareAttr(resource, mappingItem, realm);

        assertNotNull(prepared, "PreparedAttr should not be null");

        Attribute attr = prepared.attribute();

        assertNotNull(attr, "Attribute should not be null");
        assertEquals(
                "ou",
                attr.getName(),
                "The external attribute name should be correctly mapped");

        assertFalse(
                attr.getValue().isEmpty(),
                "The attribute value list should not be empty");

        assertEquals(
                "master",
                attr.getValue().get(0),
                "The realm name should be correctly extracted as the value");

        verify(mappingItem, atLeastOnce()).getExtAttrName();
        verify(realm, atLeastOnce()).getName();
    }

    /**
     * Tests prepareAttr for the "fullPath" field of a Realm.
     */
    @Test
    public void testPrepareAttrForRealmPath() throws Exception {
        when(mappingItem.getExtAttrName()).thenReturn("description");
        when(mappingItem.getIntAttrName()).thenReturn("fullPath");
        when(mappingItem.getTransformers()).thenReturn(List.of());

        when(intAttrNameParser.parse("fullPath")).thenReturn(intAttrName);
        when(intAttrName.getField()).thenReturn("fullPath");

        when(realm.getFullPath()).thenReturn("/master/subrealm");

        MappingManager.PreparedAttr prepared =
                mappingManager.prepareAttr(resource, mappingItem, realm);

        assertNotNull(prepared);

        Attribute attr = prepared.attribute();

        assertNotNull(attr);
        assertEquals("description", attr.getName());
        assertFalse(attr.getValue().isEmpty());
        assertEquals("/master/subrealm", attr.getValue().get(0));
    }

    /**
     * Tests prepareAttr when the internal attribute does not resolve
     * to either a field or a schema.
     *
     * In this case the prepared external Attribute has no values.
     */
    @Test
    public void testPrepareAttrWhenNoInternalValueIsAvailable() throws Exception {
        when(mappingItem.getExtAttrName()).thenReturn("cn");
        when(mappingItem.getIntAttrName()).thenReturn("nonExistingSchema");
        when(mappingItem.getTransformers()).thenReturn(List.of());

        /*
         * intAttrName is deliberately left without field and schema info.
         */
        when(intAttrNameParser.parse("nonExistingSchema")).thenReturn(intAttrName);

        MappingManager.PreparedAttr prepared =
                mappingManager.prepareAttr(resource, mappingItem, realm);

        assertNotNull(prepared);

        Attribute attr = prepared.attribute();

        assertNotNull(attr);
        assertEquals("cn", attr.getName());
        assertNull(
                attr.getValue(),
                "No internal value should be available");
    }

    /**
     * Tests getIntValues for the username field of a User.
     */
    @Test
    public void testGetIntValuesForAny() {
        when(mappingItem.getIntAttrName()).thenReturn("username");
        when(mappingItem.getTransformers()).thenReturn(List.of());

        when(intAttrName.getField()).thenReturn("username");

        when(user.getUsername()).thenReturn("testuser");

        MappingManager.IntValues intValues = mappingManager.getIntValues(
                resource,
                provision,
                mappingItem,
                intAttrName,
                AttrSchemaType.String,
                user,
                AccountGetter.DEFAULT,
                PlainAttrGetter.DEFAULT);

        assertNotNull(intValues);
        assertNotNull(intValues.values());

        assertEquals(1, intValues.values().size());

        assertEquals(
                "testuser",
                intValues.values().get(0).getStringValue(),
                "Internal values should contain the username");
    }
}