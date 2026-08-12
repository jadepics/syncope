package org.apache.syncope.core.provisioning.java.LLM;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.apache.syncope.common.lib.to.Item;
import org.apache.syncope.common.lib.to.Mapping;
import org.apache.syncope.common.lib.to.Provision;
import org.apache.syncope.common.lib.to.RealmTO;
import org.apache.syncope.common.lib.to.UserTO;
import org.apache.syncope.common.lib.types.AnyTypeKind;
import org.apache.syncope.core.persistence.api.Encryptor;
import org.apache.syncope.core.persistence.api.EncryptorManager;
import org.apache.syncope.core.persistence.api.dao.AnyObjectDAO;
import org.apache.syncope.core.persistence.api.dao.GroupDAO;
import org.apache.syncope.core.persistence.api.dao.ImplementationDAO;
import org.apache.syncope.core.persistence.api.dao.RealmSearchDAO;
import org.apache.syncope.core.persistence.api.dao.RelationshipTypeDAO;
import org.apache.syncope.core.persistence.api.dao.UserDAO;
import org.apache.syncope.core.persistence.api.entity.Any;
import org.apache.syncope.core.persistence.api.entity.AnyType;
import org.apache.syncope.core.persistence.api.entity.Implementation;
import org.apache.syncope.core.persistence.api.entity.PlainAttrValue;
import org.apache.syncope.core.persistence.api.entity.user.Account;
import org.apache.syncope.core.persistence.api.entity.user.LinkedAccount;
import org.apache.syncope.core.persistence.api.entity.user.User;
import org.apache.syncope.core.provisioning.api.DerAttrHandler;
import org.apache.syncope.core.provisioning.api.IntAttrName;
import org.apache.syncope.core.provisioning.api.IntAttrNameParser;
import org.apache.syncope.core.provisioning.api.MappingManager;
import org.apache.syncope.core.provisioning.api.jexl.JexlTools;
import org.apache.syncope.core.provisioning.java.DefaultMappingManager;
import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.AttributeBuilder;
import org.identityconnectors.framework.common.objects.Name;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class DefaultMappingManagerTestZeroPrompting {

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

    @Mock
    private Encryptor encryptor;

    // A test wrapper class to expose protected methods for testing without reflection
    public static class TestableDefaultMappingManager extends DefaultMappingManager {

        public TestableDefaultMappingManager(UserDAO userDAO, AnyObjectDAO anyObjectDAO, GroupDAO groupDAO,
                                             RelationshipTypeDAO relationshipTypeDAO, RealmSearchDAO realmSearchDAO,
                                             ImplementationDAO implementationDAO, DerAttrHandler derAttrHandler,
                                             IntAttrNameParser intAttrNameParser, EncryptorManager encryptorManager, JexlTools jexlTools) {
            super(userDAO, anyObjectDAO, groupDAO, relationshipTypeDAO, realmSearchDAO, implementationDAO,
                    derAttrHandler, intAttrNameParser, encryptorManager, jexlTools);
        }

        public static PlainAttrValue publicClonePlainAttrValue(PlainAttrValue src) {
            return clonePlainAttrValue(src);
        }

        public static Name publicGetName(String evalConnObjectLink, String connObjectKey) {
            return getName(evalConnObjectLink, connObjectKey);
        }

        public static Optional<String> publicProcessPreparedAttr(MappingManager.PreparedAttr preparedAttr,
                                                                 Set<Attribute> attributes) {
            return processPreparedAttr(preparedAttr, attributes);
        }

        public List<Implementation> publicGetTransformers(Item item) {
            return getTransformers(item);
        }

        public Name publicEvaluateNAME(Any any, Provision provision, String connObjectKey) {
            return evaluateNAME(any, provision, connObjectKey);
        }

        public Optional<String> publicDecodePassword(Account account) {
            return decodePassword(account);
        }

        public Optional<String> publicGetPasswordAttrValue(Account account, String defaultValue) {
            return getPasswordAttrValue(account, defaultValue);
        }
    }

    @InjectMocks
    private TestableDefaultMappingManager mappingManager;

    @BeforeEach
    public void setup() {
        // Initialization if needed
    }

    @Test
    public void testClonePlainAttrValue() {
        PlainAttrValue src = new PlainAttrValue();
        src.setStringValue("testString");
        src.setBooleanValue(true);
        src.setLongValue(123L);

        PlainAttrValue dst = TestableDefaultMappingManager.publicClonePlainAttrValue(src);

        assertNotNull(dst);
        assertEquals("testString", dst.getStringValue());
        assertTrue(dst.getBooleanValue());
        assertEquals(Long.valueOf(123L), dst.getLongValue());
    }

    @Test
    public void testGetNameWithBlankEvalConnObjectLink() {
        Name name = TestableDefaultMappingManager.publicGetName("", "myConnObjectKey");
        assertNotNull(name);
        assertEquals("myConnObjectKey", name.getNameValue());
    }

    @Test
    public void testGetNameWithNotBlankEvalConnObjectLink() {
        Name name = TestableDefaultMappingManager.publicGetName("evaluatedLink", "myConnObjectKey");
        assertNotNull(name);
        assertEquals("evaluatedLink", name.getNameValue());
    }

    @Test
    public void testProcessPreparedAttrNull() {
        Optional<String> result = TestableDefaultMappingManager.publicProcessPreparedAttr(null, new HashSet<>());
        assertFalse(result.isPresent());
    }

    @Test
    public void testProcessPreparedAttrValid() {
        Attribute attr = AttributeBuilder.build("testAttr", "value1");
        MappingManager.PreparedAttr preparedAttr = new MappingManager.PreparedAttr("link123", attr);
        Set<Attribute> attributes = new HashSet<>();

        Optional<String> result = TestableDefaultMappingManager.publicProcessPreparedAttr(preparedAttr, attributes);

        assertTrue(result.isPresent());
        assertEquals("link123", result.get());
        assertEquals(1, attributes.size());
        assertEquals("value1", attributes.iterator().next().getValue().get(0));
    }

    @Test
    public void testGetTransformers() {
        Item item = mock(Item.class);
        List<String> transformerIds = new ArrayList<>();
        transformerIds.add("transformer1");
        when(item.getTransformers()).thenReturn(transformerIds);

        Implementation impl = mock(Implementation.class);
        // Use doReturn to avoid Generics-related compilation issues with thenReturn(Optional<T>)
        doReturn(Optional.of(impl)).when(implementationDAO).findById("transformer1");

        List<Implementation> transformers = mappingManager.publicGetTransformers(item);

        assertEquals(1, transformers.size());
        assertEquals(impl, transformers.get(0));
    }

    @Test
    public void testEvaluateNAMEForAny() {
        User user = mock(User.class);

        Provision provision = mock(Provision.class);
        when(provision.getMapping()).thenReturn(null);

        // connObjectKey "defaultKey" is not blank, so any.getType().getKey() will not be invoked.
        Name name = mappingManager.publicEvaluateNAME(user, provision, "defaultKey");
        assertEquals("defaultKey", name.getNameValue());
    }

    @Test
    public void testDecodePasswordSuccess() throws Exception {
        Account account = mock(Account.class);
        when(account.getPassword()).thenReturn("encodedPassword");
        when(account.getCipherAlgorithm()).thenReturn(org.apache.syncope.common.lib.types.CipherAlgorithm.AES);

        when(encryptorManager.getInstance()).thenReturn(encryptor);
        when(encryptor.decode("encodedPassword", org.apache.syncope.common.lib.types.CipherAlgorithm.AES))
                .thenReturn("decodedPassword");

        Optional<String> decoded = mappingManager.publicDecodePassword(account);
        assertTrue(decoded.isPresent());
        assertEquals("decodedPassword", decoded.get());
    }

    @Test
    public void testGetPasswordAttrValueWithLinkedAccount() throws Exception {
        LinkedAccount account = mock(LinkedAccount.class);
        when(account.getPassword()).thenReturn("encodedPwd");
        when(account.getCipherAlgorithm()).thenReturn(org.apache.syncope.common.lib.types.CipherAlgorithm.AES);

        when(encryptorManager.getInstance()).thenReturn(encryptor);
        when(encryptor.decode("encodedPwd", org.apache.syncope.common.lib.types.CipherAlgorithm.AES))
                .thenReturn("decodedPwd");

        Optional<String> pwd = mappingManager.publicGetPasswordAttrValue(account, "default");
        assertTrue(pwd.isPresent());
        assertEquals("decodedPwd", pwd.get());
    }

    @Test
    public void testGetPasswordAttrValueWithLinkedAccountNullPassword() throws Exception {
        LinkedAccount account = mock(LinkedAccount.class);
        when(account.getPassword()).thenReturn(null);

        Optional<String> pwd = mappingManager.publicGetPasswordAttrValue(account, "default");
        assertTrue(pwd.isPresent());
        assertEquals("default", pwd.get());
    }

    @Test
    public void testHasMustChangePasswordFalseNullMapping() {
        Provision provision = mock(Provision.class);
        when(provision.getMapping()).thenReturn(null);

        assertFalse(mappingManager.hasMustChangePassword(provision));
    }

    @Test
    public void testHasMustChangePasswordFalse() {
        Provision provision = mock(Provision.class);
        Mapping mapping = mock(Mapping.class);
        Item item = mock(Item.class);

        when(item.getIntAttrName()).thenReturn("username");
        when(mapping.getItems()).thenReturn(List.of(item));
        when(provision.getMapping()).thenReturn(mapping);

        assertFalse(mappingManager.hasMustChangePassword(provision));
    }

    @Test
    public void testHasMustChangePasswordTrue() {
        Provision provision = mock(Provision.class);
        Mapping mapping = mock(Mapping.class);
        Item item1 = mock(Item.class);
        Item item2 = mock(Item.class);

        when(item1.getIntAttrName()).thenReturn("username");
        when(item2.getIntAttrName()).thenReturn("mustChangePassword");
        when(mapping.getItems()).thenReturn(List.of(item1, item2));
        when(provision.getMapping()).thenReturn(mapping);

        assertTrue(mappingManager.hasMustChangePassword(provision));
    }

    @Test
    public void testSetIntValuesUserTOUsername() throws ParseException {
        Item item = mock(Item.class);
        when(item.getIntAttrName()).thenReturn("username");

        Attribute attr = AttributeBuilder.build("username", "testUser");
        UserTO userTO = new UserTO();

        IntAttrName intAttrName = mock(IntAttrName.class);
        when(intAttrName.getField()).thenReturn("username");
        when(intAttrNameParser.parse(eq("username"), any(AnyTypeKind.class))).thenReturn(intAttrName);

        mappingManager.setIntValues(item, attr, userTO);

        assertEquals("testUser", userTO.getUsername());
    }

    @Test
    public void testSetIntValuesRealmTOName() throws ParseException {
        Item item = mock(Item.class);
        when(item.getIntAttrName()).thenReturn("name");

        Attribute attr = AttributeBuilder.build("name", "testRealm");
        RealmTO realmTO = new RealmTO();

        IntAttrName intAttrName = mock(IntAttrName.class);
        when(intAttrName.getField()).thenReturn("name");
        when(intAttrNameParser.parse(eq("name"))).thenReturn(intAttrName);

        mappingManager.setIntValues(item, attr, realmTO);

        assertEquals("testRealm", realmTO.getName());
    }
}