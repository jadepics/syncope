package org.apache.syncope.core.provisioning.java.randoop.s0;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DefaultMappingManagerRandoopRegressionS00 {

    public static boolean debug = false;

    public void assertBooleanArrayEquals(boolean[] expectedArray, boolean[] actualArray) {
        if (expectedArray.length != actualArray.length) {
            throw new AssertionError("Array lengths differ: " + expectedArray.length + " != " + actualArray.length);
        }
        for (int i = 0; i < expectedArray.length; i++) {
            if (expectedArray[i] != actualArray[i]) {
                throw new AssertionError("Arrays differ at index " + i + ": " + expectedArray[i] + " != " + actualArray[i]);
            }
        }
    }

    @Test
    public void test01() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "DefaultMappingManagerRandoopRegressionS00.test01");
        org.apache.syncope.core.persistence.api.dao.UserDAO userDAO0 = null;
        org.apache.syncope.core.persistence.api.dao.AnyObjectDAO anyObjectDAO1 = null;
        org.apache.syncope.core.persistence.api.dao.GroupDAO groupDAO2 = null;
        org.apache.syncope.core.persistence.api.dao.RelationshipTypeDAO relationshipTypeDAO3 = null;
        org.apache.syncope.core.persistence.api.dao.RealmSearchDAO realmSearchDAO4 = null;
        org.apache.syncope.core.persistence.api.dao.ImplementationDAO implementationDAO5 = null;
        org.apache.syncope.core.provisioning.api.DerAttrHandler derAttrHandler6 = null;
        org.apache.syncope.core.provisioning.api.IntAttrNameParser intAttrNameParser7 = null;
        org.apache.syncope.core.persistence.api.EncryptorManager encryptorManager8 = null;
        org.apache.syncope.core.provisioning.api.jexl.JexlTools jexlTools9 = null;
        org.apache.syncope.core.provisioning.java.DefaultMappingManager defaultMappingManager10 = new org.apache.syncope.core.provisioning.java.DefaultMappingManager(userDAO0, anyObjectDAO1, groupDAO2, relationshipTypeDAO3, realmSearchDAO4, implementationDAO5, derAttrHandler6, intAttrNameParser7, encryptorManager8, jexlTools9);
        org.apache.syncope.core.persistence.api.entity.ExternalResource externalResource11 = null;
        org.apache.syncope.common.lib.to.Provision provision12 = null;
        org.apache.syncope.common.lib.to.Item item13 = null;
        org.apache.syncope.core.persistence.api.entity.Any any14 = null;
        org.apache.syncope.core.provisioning.api.AccountGetter accountGetter16 = null;
        org.apache.syncope.core.provisioning.api.AccountGetter accountGetter17 = null;
        org.apache.syncope.core.provisioning.api.PlainAttrGetter plainAttrGetter18 = null;
        // The following exception was thrown during execution in test generation
        try {
            org.apache.syncope.core.provisioning.api.MappingManager.PreparedAttr preparedAttr19 = defaultMappingManager10.prepareAttr(externalResource11, provision12, item13, any14, "department", accountGetter16, accountGetter17, plainAttrGetter18);
            org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException; message: Cannot invoke \"org.apache.syncope.common.lib.to.Item.getIntAttrName()\" because \"item\" is null");
        } catch (java.lang.NullPointerException e) {
            // Expected exception.
        }
    }

    @Test
    public void test02() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "DefaultMappingManagerRandoopRegressionS00.test02");
        org.apache.syncope.core.persistence.api.dao.UserDAO userDAO0 = null;
        org.apache.syncope.core.persistence.api.dao.AnyObjectDAO anyObjectDAO1 = null;
        org.apache.syncope.core.persistence.api.dao.GroupDAO groupDAO2 = null;
        org.apache.syncope.core.persistence.api.dao.RelationshipTypeDAO relationshipTypeDAO3 = null;
        org.apache.syncope.core.persistence.api.dao.RealmSearchDAO realmSearchDAO4 = null;
        org.apache.syncope.core.persistence.api.dao.ImplementationDAO implementationDAO5 = null;
        org.apache.syncope.core.provisioning.api.DerAttrHandler derAttrHandler6 = null;
        org.apache.syncope.core.provisioning.api.IntAttrNameParser intAttrNameParser7 = null;
        org.apache.syncope.core.persistence.api.EncryptorManager encryptorManager8 = null;
        org.apache.syncope.core.provisioning.api.jexl.JexlTools jexlTools9 = null;
        org.apache.syncope.core.provisioning.java.DefaultMappingManager defaultMappingManager10 = new org.apache.syncope.core.provisioning.java.DefaultMappingManager(userDAO0, anyObjectDAO1, groupDAO2, relationshipTypeDAO3, realmSearchDAO4, implementationDAO5, derAttrHandler6, intAttrNameParser7, encryptorManager8, jexlTools9);
        org.apache.syncope.common.lib.to.Item item11 = null;
        org.identityconnectors.framework.common.objects.Attribute attribute13 = org.identityconnectors.framework.common.objects.AttributeBuilder.buildEnableDate((long) (short) -1);
        org.apache.syncope.common.lib.to.GroupTO groupTO14 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.groupTO();
        java.time.OffsetDateTime offsetDateTime15 = groupTO14.getLastChangeDate();
        // The following exception was thrown during execution in test generation
        try {
            defaultMappingManager10.setIntValues(item11, attribute13, (org.apache.syncope.common.lib.to.AnyTO) groupTO14);
            org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException; message: Cannot invoke \"org.apache.syncope.common.lib.to.Item.getTransformers()\" because \"item\" is null");
        } catch (java.lang.NullPointerException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(attribute13);
        org.junit.Assert.assertNotNull(groupTO14);
        org.junit.Assert.assertNull(offsetDateTime15);
    }

    @Test
    public void test03() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "DefaultMappingManagerRandoopRegressionS00.test03");
        org.apache.syncope.core.persistence.api.dao.UserDAO userDAO0 = null;
        org.apache.syncope.core.persistence.api.dao.AnyObjectDAO anyObjectDAO1 = null;
        org.apache.syncope.core.persistence.api.dao.GroupDAO groupDAO2 = null;
        org.apache.syncope.core.persistence.api.dao.RelationshipTypeDAO relationshipTypeDAO3 = null;
        org.apache.syncope.core.persistence.api.dao.RealmSearchDAO realmSearchDAO4 = null;
        org.apache.syncope.core.persistence.api.dao.ImplementationDAO implementationDAO5 = null;
        org.apache.syncope.core.provisioning.api.DerAttrHandler derAttrHandler6 = null;
        org.apache.syncope.core.provisioning.api.IntAttrNameParser intAttrNameParser7 = null;
        org.apache.syncope.core.persistence.api.EncryptorManager encryptorManager8 = null;
        org.apache.syncope.core.provisioning.api.jexl.JexlTools jexlTools9 = null;
        org.apache.syncope.core.provisioning.java.DefaultMappingManager defaultMappingManager10 = new org.apache.syncope.core.provisioning.java.DefaultMappingManager(userDAO0, anyObjectDAO1, groupDAO2, relationshipTypeDAO3, realmSearchDAO4, implementationDAO5, derAttrHandler6, intAttrNameParser7, encryptorManager8, jexlTools9);
        org.apache.syncope.common.lib.to.Item item11 = null;
        org.identityconnectors.framework.common.objects.Attribute attribute13 = org.identityconnectors.framework.common.objects.AttributeBuilder.buildPasswordExpired(false);
        org.apache.syncope.common.lib.to.RealmTO realmTO14 = null;
        // The following exception was thrown during execution in test generation
        try {
            defaultMappingManager10.setIntValues(item11, attribute13, realmTO14);
            org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException; message: Cannot invoke \"org.apache.syncope.common.lib.to.Item.getTransformers()\" because \"item\" is null");
        } catch (java.lang.NullPointerException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(attribute13);
    }

    @Test
    public void test04() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "DefaultMappingManagerRandoopRegressionS00.test04");
        org.apache.syncope.core.persistence.api.dao.UserDAO userDAO0 = null;
        org.apache.syncope.core.persistence.api.dao.AnyObjectDAO anyObjectDAO1 = null;
        org.apache.syncope.core.persistence.api.dao.GroupDAO groupDAO2 = null;
        org.apache.syncope.core.persistence.api.dao.RelationshipTypeDAO relationshipTypeDAO3 = null;
        org.apache.syncope.core.persistence.api.dao.RealmSearchDAO realmSearchDAO4 = null;
        org.apache.syncope.core.persistence.api.dao.ImplementationDAO implementationDAO5 = null;
        org.apache.syncope.core.provisioning.api.DerAttrHandler derAttrHandler6 = null;
        org.apache.syncope.core.provisioning.api.IntAttrNameParser intAttrNameParser7 = null;
        org.apache.syncope.core.persistence.api.EncryptorManager encryptorManager8 = null;
        org.apache.syncope.core.provisioning.api.jexl.JexlTools jexlTools9 = null;
        org.apache.syncope.core.provisioning.java.DefaultMappingManager defaultMappingManager10 = new org.apache.syncope.core.provisioning.java.DefaultMappingManager(userDAO0, anyObjectDAO1, groupDAO2, relationshipTypeDAO3, realmSearchDAO4, implementationDAO5, derAttrHandler6, intAttrNameParser7, encryptorManager8, jexlTools9);
        org.apache.syncope.core.persistence.api.entity.ExternalResource externalResource11 = null;
        org.apache.syncope.common.lib.to.Item item12 = null;
        org.apache.syncope.core.persistence.api.entity.Realm realm13 = null;
        // The following exception was thrown during execution in test generation
        try {
            org.apache.syncope.core.provisioning.api.MappingManager.PreparedAttr preparedAttr14 = defaultMappingManager10.prepareAttr(externalResource11, item12, realm13);
            org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException; message: Cannot invoke \"org.apache.syncope.common.lib.to.Item.getIntAttrName()\" because \"item\" is null");
        } catch (java.lang.NullPointerException e) {
            // Expected exception.
        }
    }

    @Test
    public void test05() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "DefaultMappingManagerRandoopRegressionS00.test05");
        org.apache.syncope.core.provisioning.java.DefaultMappingManager defaultMappingManager0 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.manager();
        org.apache.syncope.core.persistence.api.entity.ExternalResource externalResource1 = null;
        org.apache.syncope.common.lib.to.Item item2 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.mustChangePasswordItem();
        org.apache.syncope.core.provisioning.api.IntAttrName intAttrName3 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.nameIntAttrName();
        org.apache.syncope.common.lib.types.AttrSchemaType attrSchemaType4 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.stringSchemaType();
        org.apache.syncope.core.persistence.api.entity.Realm realm5 = null;
        // The following exception was thrown during execution in test generation
        try {
            org.apache.syncope.core.provisioning.api.MappingManager.IntValues intValues6 = defaultMappingManager0.getIntValues(externalResource1, item2, intAttrName3, attrSchemaType4, realm5);
            org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException; message: Cannot invoke \"org.apache.syncope.core.persistence.api.entity.Realm.getName()\" because \"realm\" is null");
        } catch (java.lang.NullPointerException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(defaultMappingManager0);
        org.junit.Assert.assertNotNull(item2);
        org.junit.Assert.assertNotNull(intAttrName3);
        org.junit.Assert.assertTrue("'" + attrSchemaType4 + "' != '" + org.apache.syncope.common.lib.types.AttrSchemaType.String + "'", attrSchemaType4.equals(org.apache.syncope.common.lib.types.AttrSchemaType.String));
    }

    @Test
    public void test06() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "DefaultMappingManagerRandoopRegressionS00.test06");
        org.apache.syncope.core.persistence.api.dao.UserDAO userDAO0 = null;
        org.apache.syncope.core.persistence.api.dao.AnyObjectDAO anyObjectDAO1 = null;
        org.apache.syncope.core.persistence.api.dao.GroupDAO groupDAO2 = null;
        org.apache.syncope.core.persistence.api.dao.RelationshipTypeDAO relationshipTypeDAO3 = null;
        org.apache.syncope.core.persistence.api.dao.RealmSearchDAO realmSearchDAO4 = null;
        org.apache.syncope.core.persistence.api.dao.ImplementationDAO implementationDAO5 = null;
        org.apache.syncope.core.provisioning.api.DerAttrHandler derAttrHandler6 = null;
        org.apache.syncope.core.provisioning.api.IntAttrNameParser intAttrNameParser7 = null;
        org.apache.syncope.core.persistence.api.EncryptorManager encryptorManager8 = null;
        org.apache.syncope.core.provisioning.api.jexl.JexlTools jexlTools9 = null;
        org.apache.syncope.core.provisioning.java.DefaultMappingManager defaultMappingManager10 = new org.apache.syncope.core.provisioning.java.DefaultMappingManager(userDAO0, anyObjectDAO1, groupDAO2, relationshipTypeDAO3, realmSearchDAO4, implementationDAO5, derAttrHandler6, intAttrNameParser7, encryptorManager8, jexlTools9);
        org.apache.syncope.core.persistence.api.entity.ExternalResource externalResource11 = null;
        org.apache.syncope.common.lib.to.Item item12 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.keyItem();
        org.apache.syncope.common.lib.types.MappingPurpose mappingPurpose13 = null;
        item12.setPurpose(mappingPurpose13);
        org.apache.syncope.core.provisioning.api.IntAttrName intAttrName15 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.plainStringIntAttrName();
        org.apache.syncope.common.lib.types.AttrSchemaType attrSchemaType16 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.stringSchemaType();
        org.apache.syncope.core.persistence.api.entity.Realm realm17 = null;
        // The following exception was thrown during execution in test generation
        try {
            org.apache.syncope.core.provisioning.api.MappingManager.IntValues intValues18 = defaultMappingManager10.getIntValues(externalResource11, item12, intAttrName15, attrSchemaType16, realm17);
            org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException; message: Cannot invoke \"org.apache.syncope.core.persistence.api.entity.Realm.getPlainAttr(String)\" because \"realm\" is null");
        } catch (java.lang.NullPointerException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(item12);
        org.junit.Assert.assertNotNull(intAttrName15);
        org.junit.Assert.assertTrue("'" + attrSchemaType16 + "' != '" + org.apache.syncope.common.lib.types.AttrSchemaType.String + "'", attrSchemaType16.equals(org.apache.syncope.common.lib.types.AttrSchemaType.String));
    }

    @Test
    public void test07() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "DefaultMappingManagerRandoopRegressionS00.test07");
        org.apache.syncope.core.persistence.api.dao.UserDAO userDAO0 = null;
        org.apache.syncope.core.persistence.api.dao.AnyObjectDAO anyObjectDAO1 = null;
        org.apache.syncope.core.persistence.api.dao.GroupDAO groupDAO2 = null;
        org.apache.syncope.core.persistence.api.dao.RelationshipTypeDAO relationshipTypeDAO3 = null;
        org.apache.syncope.core.persistence.api.dao.RealmSearchDAO realmSearchDAO4 = null;
        org.apache.syncope.core.persistence.api.dao.ImplementationDAO implementationDAO5 = null;
        org.apache.syncope.core.provisioning.api.DerAttrHandler derAttrHandler6 = null;
        org.apache.syncope.core.provisioning.api.IntAttrNameParser intAttrNameParser7 = null;
        org.apache.syncope.core.persistence.api.EncryptorManager encryptorManager8 = null;
        org.apache.syncope.core.provisioning.api.jexl.JexlTools jexlTools9 = null;
        org.apache.syncope.core.provisioning.java.DefaultMappingManager defaultMappingManager10 = new org.apache.syncope.core.provisioning.java.DefaultMappingManager(userDAO0, anyObjectDAO1, groupDAO2, relationshipTypeDAO3, realmSearchDAO4, implementationDAO5, derAttrHandler6, intAttrNameParser7, encryptorManager8, jexlTools9);
        org.apache.syncope.core.persistence.api.entity.ExternalResource externalResource11 = null;
        org.apache.syncope.common.lib.to.Provision provision12 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.plainAndKeyProvision();
        provision12.setUidOnCreate("mailAlternateAddress");
        org.apache.syncope.common.lib.to.Mapping mapping15 = provision12.getMapping();
        java.lang.String str16 = provision12.getSyncToken();
        org.apache.syncope.common.lib.to.Item item17 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.keyItem();
        java.lang.String str18 = item17.getMandatoryCondition();
        org.apache.syncope.core.persistence.api.entity.Any any19 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.anyUser();
        org.apache.syncope.core.provisioning.api.AccountGetter accountGetter21 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.defaultAccountGetter();
        org.apache.syncope.core.provisioning.api.AccountGetter accountGetter22 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.defaultAccountGetter();
        org.apache.syncope.core.provisioning.api.PlainAttrGetter plainAttrGetter23 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.plainAttrGetter();
        // The following exception was thrown during execution in test generation
        try {
            org.apache.syncope.core.provisioning.api.MappingManager.PreparedAttr preparedAttr24 = defaultMappingManager10.prepareAttr(externalResource11, provision12, item17, any19, "realm-key", accountGetter21, accountGetter22, plainAttrGetter23);
            org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException; message: Cannot invoke \"org.apache.syncope.core.provisioning.api.IntAttrNameParser.parse(String, org.apache.syncope.common.lib.types.AnyTypeKind)\" because \"this.intAttrNameParser\" is null");
        } catch (java.lang.NullPointerException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(provision12);
        org.junit.Assert.assertNotNull(mapping15);
        org.junit.Assert.assertNull(str16);
        org.junit.Assert.assertNotNull(item17);
        org.junit.Assert.assertEquals("'" + str18 + "' != '" + "false" + "'", str18, "false");
        org.junit.Assert.assertNotNull(any19);
        org.junit.Assert.assertNotNull(accountGetter21);
        org.junit.Assert.assertNotNull(accountGetter22);
        org.junit.Assert.assertNotNull(plainAttrGetter23);
    }

    @Test
    public void test08() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "DefaultMappingManagerRandoopRegressionS00.test08");
        org.apache.syncope.core.persistence.api.dao.UserDAO userDAO0 = null;
        org.apache.syncope.core.persistence.api.dao.AnyObjectDAO anyObjectDAO1 = null;
        org.apache.syncope.core.persistence.api.dao.GroupDAO groupDAO2 = null;
        org.apache.syncope.core.persistence.api.dao.RelationshipTypeDAO relationshipTypeDAO3 = null;
        org.apache.syncope.core.persistence.api.dao.RealmSearchDAO realmSearchDAO4 = null;
        org.apache.syncope.core.persistence.api.dao.ImplementationDAO implementationDAO5 = null;
        org.apache.syncope.core.provisioning.api.DerAttrHandler derAttrHandler6 = null;
        org.apache.syncope.core.provisioning.api.IntAttrNameParser intAttrNameParser7 = null;
        org.apache.syncope.core.persistence.api.EncryptorManager encryptorManager8 = null;
        org.apache.syncope.core.provisioning.api.jexl.JexlTools jexlTools9 = null;
        org.apache.syncope.core.provisioning.java.DefaultMappingManager defaultMappingManager10 = new org.apache.syncope.core.provisioning.java.DefaultMappingManager(userDAO0, anyObjectDAO1, groupDAO2, relationshipTypeDAO3, realmSearchDAO4, implementationDAO5, derAttrHandler6, intAttrNameParser7, encryptorManager8, jexlTools9);
        org.apache.syncope.core.persistence.api.entity.ExternalResource externalResource11 = null;
        org.apache.syncope.common.lib.to.Item item12 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.keyItem();
        java.lang.String str13 = item12.getIntAttrName();
        org.apache.syncope.core.provisioning.api.IntAttrName intAttrName14 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.plainStringIntAttrName();
        org.apache.syncope.common.lib.types.AttrSchemaType attrSchemaType15 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.stringSchemaType();
        org.apache.syncope.core.persistence.api.entity.Realm realm16 = null;
        // The following exception was thrown during execution in test generation
        try {
            org.apache.syncope.core.provisioning.api.MappingManager.IntValues intValues17 = defaultMappingManager10.getIntValues(externalResource11, item12, intAttrName14, attrSchemaType15, realm16);
            org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException; message: Cannot invoke \"org.apache.syncope.core.persistence.api.entity.Realm.getPlainAttr(String)\" because \"realm\" is null");
        } catch (java.lang.NullPointerException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(item12);
        org.junit.Assert.assertEquals("'" + str13 + "' != '" + "key" + "'", str13, "key");
        org.junit.Assert.assertNotNull(intAttrName14);
        org.junit.Assert.assertTrue("'" + attrSchemaType15 + "' != '" + org.apache.syncope.common.lib.types.AttrSchemaType.String + "'", attrSchemaType15.equals(org.apache.syncope.common.lib.types.AttrSchemaType.String));
    }

    @Test
    public void test09() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "DefaultMappingManagerRandoopRegressionS00.test09");
        org.apache.syncope.core.persistence.api.dao.UserDAO userDAO0 = null;
        org.apache.syncope.core.persistence.api.dao.AnyObjectDAO anyObjectDAO1 = null;
        org.apache.syncope.core.persistence.api.dao.GroupDAO groupDAO2 = null;
        org.apache.syncope.core.persistence.api.dao.RelationshipTypeDAO relationshipTypeDAO3 = null;
        org.apache.syncope.core.persistence.api.dao.RealmSearchDAO realmSearchDAO4 = null;
        org.apache.syncope.core.persistence.api.dao.ImplementationDAO implementationDAO5 = null;
        org.apache.syncope.core.provisioning.api.DerAttrHandler derAttrHandler6 = null;
        org.apache.syncope.core.provisioning.api.IntAttrNameParser intAttrNameParser7 = null;
        org.apache.syncope.core.persistence.api.EncryptorManager encryptorManager8 = null;
        org.apache.syncope.core.provisioning.api.jexl.JexlTools jexlTools9 = null;
        org.apache.syncope.core.provisioning.java.DefaultMappingManager defaultMappingManager10 = new org.apache.syncope.core.provisioning.java.DefaultMappingManager(userDAO0, anyObjectDAO1, groupDAO2, relationshipTypeDAO3, realmSearchDAO4, implementationDAO5, derAttrHandler6, intAttrNameParser7, encryptorManager8, jexlTools9);
        org.apache.syncope.core.persistence.api.entity.Any any11 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.anyUser();
        org.apache.syncope.core.persistence.api.entity.ExternalResource externalResource15 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.resource();
        org.apache.syncope.common.lib.to.Provision provision16 = null;
        // The following exception was thrown during execution in test generation
        try {
            org.apache.syncope.core.provisioning.api.MappingManager.PreparedAttrs preparedAttrs17 = defaultMappingManager10.prepareAttrsFromAny(any11, "hi!", true, (java.lang.Boolean) false, externalResource15, provision16);
            org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException; message: Cannot invoke \"org.apache.syncope.common.lib.to.Provision.getMapping()\" because \"provision\" is null");
        } catch (java.lang.NullPointerException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(any11);
        org.junit.Assert.assertNotNull(externalResource15);
    }

    @Test
    public void test10() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "DefaultMappingManagerRandoopRegressionS00.test10");
        org.apache.syncope.core.persistence.api.dao.UserDAO userDAO0 = null;
        org.apache.syncope.core.persistence.api.dao.AnyObjectDAO anyObjectDAO1 = null;
        org.apache.syncope.core.persistence.api.dao.GroupDAO groupDAO2 = null;
        org.apache.syncope.core.persistence.api.dao.RelationshipTypeDAO relationshipTypeDAO3 = null;
        org.apache.syncope.core.persistence.api.dao.RealmSearchDAO realmSearchDAO4 = null;
        org.apache.syncope.core.persistence.api.dao.ImplementationDAO implementationDAO5 = null;
        org.apache.syncope.core.provisioning.api.DerAttrHandler derAttrHandler6 = null;
        org.apache.syncope.core.provisioning.api.IntAttrNameParser intAttrNameParser7 = null;
        org.apache.syncope.core.persistence.api.EncryptorManager encryptorManager8 = null;
        org.apache.syncope.core.provisioning.api.jexl.JexlTools jexlTools9 = null;
        org.apache.syncope.core.provisioning.java.DefaultMappingManager defaultMappingManager10 = new org.apache.syncope.core.provisioning.java.DefaultMappingManager(userDAO0, anyObjectDAO1, groupDAO2, relationshipTypeDAO3, realmSearchDAO4, implementationDAO5, derAttrHandler6, intAttrNameParser7, encryptorManager8, jexlTools9);
        org.apache.syncope.core.persistence.api.entity.ExternalResource externalResource11 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.resource();
        org.apache.syncope.common.lib.to.Item item12 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.optionalMissingPlainItem();
        java.lang.String str13 = item12.toString();
        java.lang.String str14 = item12.toString();
        org.apache.syncope.core.persistence.api.entity.Realm realm15 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.realm();
        // The following exception was thrown during execution in test generation
        try {
            org.apache.syncope.core.provisioning.api.MappingManager.PreparedAttr preparedAttr16 = defaultMappingManager10.prepareAttr(externalResource11, item12, realm15);
            org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException; message: Cannot invoke \"org.apache.syncope.core.provisioning.api.IntAttrNameParser.parse(String)\" because \"this.intAttrNameParser\" is null");
        } catch (java.lang.NullPointerException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(externalResource11);
        org.junit.Assert.assertNotNull(item12);
        org.junit.Assert.assertNotNull(realm15);
    }

    @Test
    public void test11() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "DefaultMappingManagerRandoopRegressionS00.test11");
        org.apache.syncope.core.provisioning.java.DefaultMappingManager defaultMappingManager0 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.manager();
        org.apache.syncope.core.provisioning.java.DefaultMappingManager defaultMappingManager1 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.manager();
        org.apache.syncope.core.persistence.api.entity.ExternalResource externalResource2 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.resource();
        org.apache.syncope.common.lib.to.Provision provision3 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.plainAndKeyProvision();
        provision3.setUidOnCreate("mailAlternateAddress");
        org.apache.syncope.common.lib.to.Mapping mapping6 = provision3.getMapping();
        org.apache.syncope.common.lib.to.Mapping mapping7 = provision3.getMapping();
        provision3.setUidOnCreate("USER");
        org.apache.syncope.common.lib.to.Item item10 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.usernameItem();
        boolean boolean12 = item10.equals((java.lang.Object) "name");
        org.apache.syncope.core.provisioning.api.IntAttrName intAttrName13 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.missingIntAttrName();
        org.apache.syncope.common.lib.types.AttrSchemaType attrSchemaType14 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.stringSchemaType();
        org.apache.syncope.core.persistence.api.entity.Any any15 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.anyUser();
        org.apache.syncope.core.provisioning.api.AccountGetter accountGetter16 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.defaultAccountGetter();
        org.apache.syncope.core.provisioning.api.PlainAttrGetter plainAttrGetter17 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.missingPlainAttrGetter();
        org.apache.syncope.core.provisioning.api.MappingManager.IntValues intValues18 = defaultMappingManager1.getIntValues(externalResource2, provision3, item10, intAttrName13, attrSchemaType14, any15, accountGetter16, plainAttrGetter17);
        org.apache.syncope.common.lib.to.Item item19 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.usernameItem();
        item19.setPassword(false);
        boolean boolean22 = item19.isPassword();
        org.apache.syncope.core.persistence.api.entity.Realm realm23 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.realm();
        org.apache.syncope.core.provisioning.api.MappingManager.PreparedAttr preparedAttr24 = defaultMappingManager0.prepareAttr(externalResource2, item19, realm23);
        org.junit.Assert.assertNotNull(defaultMappingManager0);
        org.junit.Assert.assertNotNull(defaultMappingManager1);
        org.junit.Assert.assertNotNull(externalResource2);
        org.junit.Assert.assertNotNull(provision3);
        org.junit.Assert.assertNotNull(mapping6);
        org.junit.Assert.assertNotNull(mapping7);
        org.junit.Assert.assertNotNull(item10);
        org.junit.Assert.assertTrue("'" + boolean12 + "' != '" + false + "'", boolean12 == false);
        org.junit.Assert.assertNotNull(intAttrName13);
        org.junit.Assert.assertTrue("'" + attrSchemaType14 + "' != '" + org.apache.syncope.common.lib.types.AttrSchemaType.String + "'", attrSchemaType14.equals(org.apache.syncope.common.lib.types.AttrSchemaType.String));
        org.junit.Assert.assertNotNull(any15);
        org.junit.Assert.assertNotNull(accountGetter16);
        org.junit.Assert.assertNotNull(plainAttrGetter17);
        org.junit.Assert.assertNotNull(intValues18);
        org.junit.Assert.assertNotNull(item19);
        org.junit.Assert.assertTrue("'" + boolean22 + "' != '" + false + "'", boolean22 == false);
        org.junit.Assert.assertNotNull(realm23);
        org.junit.Assert.assertNotNull(preparedAttr24);
    }

    @Test
    public void test12() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "DefaultMappingManagerRandoopRegressionS00.test12");
        org.apache.syncope.core.provisioning.java.DefaultMappingManager defaultMappingManager0 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.manager();
        org.apache.syncope.core.persistence.api.entity.ExternalResource externalResource1 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.resource();
        org.apache.syncope.common.lib.to.Provision provision2 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.plainAndKeyProvision();
        provision2.setUidOnCreate("mailAlternateAddress");
        org.apache.syncope.common.lib.to.Mapping mapping5 = provision2.getMapping();
        org.apache.syncope.common.lib.to.Mapping mapping6 = provision2.getMapping();
        provision2.setUidOnCreate("USER");
        org.apache.syncope.common.lib.to.Item item9 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.usernameItem();
        boolean boolean11 = item9.equals((java.lang.Object) "name");
        org.apache.syncope.core.provisioning.api.IntAttrName intAttrName12 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.missingIntAttrName();
        org.apache.syncope.common.lib.types.AttrSchemaType attrSchemaType13 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.stringSchemaType();
        org.apache.syncope.core.persistence.api.entity.Any any14 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.anyUser();
        org.apache.syncope.core.provisioning.api.AccountGetter accountGetter15 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.defaultAccountGetter();
        org.apache.syncope.core.provisioning.api.PlainAttrGetter plainAttrGetter16 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.missingPlainAttrGetter();
        org.apache.syncope.core.provisioning.api.MappingManager.IntValues intValues17 = defaultMappingManager0.getIntValues(externalResource1, provision2, item9, intAttrName12, attrSchemaType13, any14, accountGetter15, plainAttrGetter16);
        java.lang.String str18 = item9.getMandatoryCondition();
        org.junit.Assert.assertNotNull(defaultMappingManager0);
        org.junit.Assert.assertNotNull(externalResource1);
        org.junit.Assert.assertNotNull(provision2);
        org.junit.Assert.assertNotNull(mapping5);
        org.junit.Assert.assertNotNull(mapping6);
        org.junit.Assert.assertNotNull(item9);
        org.junit.Assert.assertTrue("'" + boolean11 + "' != '" + false + "'", boolean11 == false);
        org.junit.Assert.assertNotNull(intAttrName12);
        org.junit.Assert.assertTrue("'" + attrSchemaType13 + "' != '" + org.apache.syncope.common.lib.types.AttrSchemaType.String + "'", attrSchemaType13.equals(org.apache.syncope.common.lib.types.AttrSchemaType.String));
        org.junit.Assert.assertNotNull(any14);
        org.junit.Assert.assertNotNull(accountGetter15);
        org.junit.Assert.assertNotNull(plainAttrGetter16);
        org.junit.Assert.assertNotNull(intValues17);
        org.junit.Assert.assertEquals("'" + str18 + "' != '" + "false" + "'", str18, "false");
    }

    @Test
    public void test13() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "DefaultMappingManagerRandoopRegressionS00.test13");
        org.apache.syncope.core.persistence.api.entity.PlainAttrValue plainAttrValue0 = new org.apache.syncope.core.persistence.api.entity.PlainAttrValue();
        org.apache.syncope.core.provisioning.java.DefaultMappingManager defaultMappingManager1 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.manager();
        org.apache.syncope.core.persistence.api.entity.ExternalResource externalResource2 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.resource();
        org.apache.syncope.common.lib.to.Provision provision3 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.plainAndKeyProvision();
        provision3.setUidOnCreate("mailAlternateAddress");
        org.apache.syncope.common.lib.to.Mapping mapping6 = provision3.getMapping();
        org.apache.syncope.common.lib.to.Mapping mapping7 = provision3.getMapping();
        provision3.setUidOnCreate("USER");
        org.apache.syncope.common.lib.to.Item item10 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.usernameItem();
        boolean boolean12 = item10.equals((java.lang.Object) "name");
        org.apache.syncope.core.provisioning.api.IntAttrName intAttrName13 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.missingIntAttrName();
        org.apache.syncope.common.lib.types.AttrSchemaType attrSchemaType14 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.stringSchemaType();
        org.apache.syncope.core.persistence.api.entity.Any any15 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.anyUser();
        org.apache.syncope.core.provisioning.api.AccountGetter accountGetter16 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.defaultAccountGetter();
        org.apache.syncope.core.provisioning.api.PlainAttrGetter plainAttrGetter17 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.missingPlainAttrGetter();
        org.apache.syncope.core.provisioning.api.MappingManager.IntValues intValues18 = defaultMappingManager1.getIntValues(externalResource2, provision3, item10, intAttrName13, attrSchemaType14, any15, accountGetter16, plainAttrGetter17);
        java.lang.String str19 = plainAttrValue0.getValueAsString(attrSchemaType14);
        org.apache.syncope.core.persistence.api.entity.PlainSchema plainSchema20 = null;
        // The following exception was thrown during execution in test generation
        try {
            plainAttrValue0.parseValue(plainSchema20, "newUsername");
            org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException; message: Cannot invoke \"org.apache.syncope.core.persistence.api.entity.PlainSchema.getType()\" because \"schema\" is null");
        } catch (java.lang.NullPointerException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(defaultMappingManager1);
        org.junit.Assert.assertNotNull(externalResource2);
        org.junit.Assert.assertNotNull(provision3);
        org.junit.Assert.assertNotNull(mapping6);
        org.junit.Assert.assertNotNull(mapping7);
        org.junit.Assert.assertNotNull(item10);
        org.junit.Assert.assertTrue("'" + boolean12 + "' != '" + false + "'", boolean12 == false);
        org.junit.Assert.assertNotNull(intAttrName13);
        org.junit.Assert.assertTrue("'" + attrSchemaType14 + "' != '" + org.apache.syncope.common.lib.types.AttrSchemaType.String + "'", attrSchemaType14.equals(org.apache.syncope.common.lib.types.AttrSchemaType.String));
        org.junit.Assert.assertNotNull(any15);
        org.junit.Assert.assertNotNull(accountGetter16);
        org.junit.Assert.assertNotNull(plainAttrGetter17);
        org.junit.Assert.assertNotNull(intValues18);
        org.junit.Assert.assertEquals("'" + str19 + "' != '" + "" + "'", str19, "");
    }

    @Test
    public void test14() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "DefaultMappingManagerRandoopRegressionS00.test14");
        org.apache.syncope.core.provisioning.java.DefaultMappingManager defaultMappingManager0 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.manager();
        org.apache.syncope.core.persistence.api.entity.ExternalResource externalResource1 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.resource();
        org.apache.syncope.common.lib.to.Provision provision2 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.plainAndKeyProvision();
        provision2.setUidOnCreate("mailAlternateAddress");
        org.apache.syncope.common.lib.to.Mapping mapping5 = provision2.getMapping();
        org.apache.syncope.common.lib.to.Mapping mapping6 = provision2.getMapping();
        provision2.setUidOnCreate("USER");
        org.apache.syncope.common.lib.to.Item item9 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.usernameItem();
        boolean boolean11 = item9.equals((java.lang.Object) "name");
        org.apache.syncope.core.provisioning.api.IntAttrName intAttrName12 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.missingIntAttrName();
        org.apache.syncope.common.lib.types.AttrSchemaType attrSchemaType13 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.stringSchemaType();
        org.apache.syncope.core.persistence.api.entity.Any any14 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.anyUser();
        org.apache.syncope.core.provisioning.api.AccountGetter accountGetter15 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.defaultAccountGetter();
        org.apache.syncope.core.provisioning.api.PlainAttrGetter plainAttrGetter16 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.missingPlainAttrGetter();
        org.apache.syncope.core.provisioning.api.MappingManager.IntValues intValues17 = defaultMappingManager0.getIntValues(externalResource1, provision2, item9, intAttrName12, attrSchemaType13, any14, accountGetter15, plainAttrGetter16);
        java.lang.String str18 = item9.getPropagationJEXLTransformer();
        item9.setPullJEXLTransformer("/parent/child");
        java.lang.String str21 = item9.getPullJEXLTransformer();
        org.junit.Assert.assertNotNull(defaultMappingManager0);
        org.junit.Assert.assertNotNull(externalResource1);
        org.junit.Assert.assertNotNull(provision2);
        org.junit.Assert.assertNotNull(mapping5);
        org.junit.Assert.assertNotNull(mapping6);
        org.junit.Assert.assertNotNull(item9);
        org.junit.Assert.assertTrue("'" + boolean11 + "' != '" + false + "'", boolean11 == false);
        org.junit.Assert.assertNotNull(intAttrName12);
        org.junit.Assert.assertTrue("'" + attrSchemaType13 + "' != '" + org.apache.syncope.common.lib.types.AttrSchemaType.String + "'", attrSchemaType13.equals(org.apache.syncope.common.lib.types.AttrSchemaType.String));
        org.junit.Assert.assertNotNull(any14);
        org.junit.Assert.assertNotNull(accountGetter15);
        org.junit.Assert.assertNotNull(plainAttrGetter16);
        org.junit.Assert.assertNotNull(intValues17);
        org.junit.Assert.assertNull(str18);
        org.junit.Assert.assertEquals("'" + str21 + "' != '" + "/parent/child" + "'", str21, "/parent/child");
    }

    @Test
    public void test15() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "DefaultMappingManagerRandoopRegressionS00.test15");
        org.apache.syncope.core.persistence.api.dao.UserDAO userDAO0 = null;
        org.apache.syncope.core.persistence.api.dao.AnyObjectDAO anyObjectDAO1 = null;
        org.apache.syncope.core.persistence.api.dao.GroupDAO groupDAO2 = null;
        org.apache.syncope.core.persistence.api.dao.RelationshipTypeDAO relationshipTypeDAO3 = null;
        org.apache.syncope.core.persistence.api.dao.RealmSearchDAO realmSearchDAO4 = null;
        org.apache.syncope.core.persistence.api.dao.ImplementationDAO implementationDAO5 = null;
        org.apache.syncope.core.provisioning.api.DerAttrHandler derAttrHandler6 = null;
        org.apache.syncope.core.provisioning.api.IntAttrNameParser intAttrNameParser7 = null;
        org.apache.syncope.core.persistence.api.EncryptorManager encryptorManager8 = null;
        org.apache.syncope.core.provisioning.api.jexl.JexlTools jexlTools9 = null;
        org.apache.syncope.core.provisioning.java.DefaultMappingManager defaultMappingManager10 = new org.apache.syncope.core.provisioning.java.DefaultMappingManager(userDAO0, anyObjectDAO1, groupDAO2, relationshipTypeDAO3, realmSearchDAO4, implementationDAO5, derAttrHandler6, intAttrNameParser7, encryptorManager8, jexlTools9);
        org.apache.syncope.core.persistence.api.entity.Any any11 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.anyUser();
        org.apache.syncope.core.persistence.api.entity.ExternalResource externalResource15 = null;
        org.apache.syncope.common.lib.to.Provision provision16 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.plainAndKeyProvision();
        provision16.setUidOnCreate("mailAlternateAddress");
        java.lang.String str19 = provision16.getAnyType();
        org.apache.syncope.core.provisioning.api.MappingManager.PreparedAttrs preparedAttrs20 = defaultMappingManager10.prepareAttrsFromAny(any11, "mario.rossi@example.org", true, (java.lang.Boolean) false, externalResource15, provision16);
        provision16.setSyncToken("");
        provision16.setUidOnCreate("org.apache.syncope.common.lib.to.GroupTO");
        org.junit.Assert.assertNotNull(any11);
        org.junit.Assert.assertNotNull(provision16);
        org.junit.Assert.assertNull(str19);
        org.junit.Assert.assertNotNull(preparedAttrs20);
    }

    @Test
    public void test16() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "DefaultMappingManagerRandoopRegressionS00.test16");
        org.apache.syncope.core.persistence.api.dao.UserDAO userDAO0 = null;
        org.apache.syncope.core.persistence.api.dao.AnyObjectDAO anyObjectDAO1 = null;
        org.apache.syncope.core.persistence.api.dao.GroupDAO groupDAO2 = null;
        org.apache.syncope.core.persistence.api.dao.RelationshipTypeDAO relationshipTypeDAO3 = null;
        org.apache.syncope.core.persistence.api.dao.RealmSearchDAO realmSearchDAO4 = null;
        org.apache.syncope.core.persistence.api.dao.ImplementationDAO implementationDAO5 = null;
        org.apache.syncope.core.provisioning.api.DerAttrHandler derAttrHandler6 = null;
        org.apache.syncope.core.provisioning.api.IntAttrNameParser intAttrNameParser7 = null;
        org.apache.syncope.core.persistence.api.EncryptorManager encryptorManager8 = null;
        org.apache.syncope.core.provisioning.api.jexl.JexlTools jexlTools9 = null;
        org.apache.syncope.core.provisioning.java.DefaultMappingManager defaultMappingManager10 = new org.apache.syncope.core.provisioning.java.DefaultMappingManager(userDAO0, anyObjectDAO1, groupDAO2, relationshipTypeDAO3, realmSearchDAO4, implementationDAO5, derAttrHandler6, intAttrNameParser7, encryptorManager8, jexlTools9);
        org.apache.syncope.core.persistence.api.entity.Any any11 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.anyUser();
        org.apache.syncope.core.persistence.api.entity.ExternalResource externalResource15 = null;
        org.apache.syncope.common.lib.to.Provision provision16 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.plainAndKeyProvision();
        provision16.setUidOnCreate("mailAlternateAddress");
        java.lang.String str19 = provision16.getAnyType();
        org.apache.syncope.core.provisioning.api.MappingManager.PreparedAttrs preparedAttrs20 = defaultMappingManager10.prepareAttrsFromAny(any11, "mario.rossi@example.org", true, (java.lang.Boolean) false, externalResource15, provision16);
        org.apache.syncope.common.lib.to.Mapping mapping21 = provision16.getMapping();
        provision16.setAnyType("true");
        java.lang.String str24 = provision16.getSyncToken();
        org.junit.Assert.assertNotNull(any11);
        org.junit.Assert.assertNotNull(provision16);
        org.junit.Assert.assertNull(str19);
        org.junit.Assert.assertNotNull(preparedAttrs20);
        org.junit.Assert.assertNotNull(mapping21);
        org.junit.Assert.assertNull(str24);
    }

    @Test
    public void test17() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "DefaultMappingManagerRandoopRegressionS00.test17");
        org.apache.syncope.core.persistence.api.dao.UserDAO userDAO0 = null;
        org.apache.syncope.core.persistence.api.dao.AnyObjectDAO anyObjectDAO1 = null;
        org.apache.syncope.core.persistence.api.dao.GroupDAO groupDAO2 = null;
        org.apache.syncope.core.persistence.api.dao.RelationshipTypeDAO relationshipTypeDAO3 = null;
        org.apache.syncope.core.persistence.api.dao.RealmSearchDAO realmSearchDAO4 = null;
        org.apache.syncope.core.persistence.api.dao.ImplementationDAO implementationDAO5 = null;
        org.apache.syncope.core.provisioning.api.DerAttrHandler derAttrHandler6 = null;
        org.apache.syncope.core.provisioning.api.IntAttrNameParser intAttrNameParser7 = null;
        org.apache.syncope.core.persistence.api.EncryptorManager encryptorManager8 = null;
        org.apache.syncope.core.provisioning.api.jexl.JexlTools jexlTools9 = null;
        org.apache.syncope.core.provisioning.java.DefaultMappingManager defaultMappingManager10 = new org.apache.syncope.core.provisioning.java.DefaultMappingManager(userDAO0, anyObjectDAO1, groupDAO2, relationshipTypeDAO3, realmSearchDAO4, implementationDAO5, derAttrHandler6, intAttrNameParser7, encryptorManager8, jexlTools9);
        org.apache.syncope.core.persistence.api.entity.Any any11 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.anyUser();
        org.apache.syncope.core.persistence.api.entity.ExternalResource externalResource15 = null;
        org.apache.syncope.common.lib.to.Provision provision16 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.plainAndKeyProvision();
        provision16.setUidOnCreate("mailAlternateAddress");
        java.lang.String str19 = provision16.getAnyType();
        org.apache.syncope.core.provisioning.api.MappingManager.PreparedAttrs preparedAttrs20 = defaultMappingManager10.prepareAttrsFromAny(any11, "mario.rossi@example.org", true, (java.lang.Boolean) false, externalResource15, provision16);
        provision16.setSyncToken("");
        java.lang.String str23 = provision16.getUidOnCreate();
        org.junit.Assert.assertNotNull(any11);
        org.junit.Assert.assertNotNull(provision16);
        org.junit.Assert.assertNull(str19);
        org.junit.Assert.assertNotNull(preparedAttrs20);
        org.junit.Assert.assertEquals("'" + str23 + "' != '" + "mailAlternateAddress" + "'", str23, "mailAlternateAddress");
    }

    @Test
    public void test18() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "DefaultMappingManagerRandoopRegressionS00.test18");
        org.apache.syncope.core.provisioning.java.DefaultMappingManager defaultMappingManager0 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.manager();
        org.apache.syncope.common.lib.to.Item item1 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.keyItem();
        org.apache.syncope.common.lib.types.MappingPurpose mappingPurpose2 = null;
        item1.setPurpose(mappingPurpose2);
        item1.setPullJEXLTransformer("org.apache.syncope.common.lib.to.GroupTO");
        org.identityconnectors.framework.common.objects.Attribute attribute6 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.emptyAttribute();
        org.apache.syncope.common.lib.to.UserTO userTO7 = new org.apache.syncope.common.lib.to.UserTO();
        boolean boolean8 = userTO7.isSuspended();
        userTO7.setRealm("username");
        userTO7.setSuspended(true);
        userTO7.setLastChangeContext("org.apache.syncope.common.lib.to.AnyObjectTO");
        java.lang.String str15 = userTO7.getDiscriminator();
        java.lang.String str16 = userTO7.getLastModifier();
        java.util.List<java.lang.String> strList17 = userTO7.getDelegatingDelegations();
        java.lang.String str18 = userTO7.getDiscriminator();
        // The following exception was thrown during execution in test generation
        try {
            defaultMappingManager0.setIntValues(item1, attribute6, (org.apache.syncope.common.lib.to.AnyTO) userTO7);
            org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException; message: Cannot invoke \"org.springframework.beans.factory.support.DefaultListableBeanFactory.createBean(java.lang.Class)\" because the return value of \"org.apache.syncope.core.persistence.api.ApplicationContextProvider.getBeanFactory()\" is null");
        } catch (java.lang.NullPointerException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(defaultMappingManager0);
        org.junit.Assert.assertNotNull(item1);
        org.junit.Assert.assertNotNull(attribute6);
        org.junit.Assert.assertTrue("'" + boolean8 + "' != '" + false + "'", boolean8 == false);
        org.junit.Assert.assertEquals("'" + str15 + "' != '" + "org.apache.syncope.common.lib.to.UserTO" + "'", str15, "org.apache.syncope.common.lib.to.UserTO");
        org.junit.Assert.assertNull(str16);
        org.junit.Assert.assertNotNull(strList17);
        org.junit.Assert.assertEquals("'" + str18 + "' != '" + "org.apache.syncope.common.lib.to.UserTO" + "'", str18, "org.apache.syncope.common.lib.to.UserTO");
    }

    @Test
    public void test19() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "DefaultMappingManagerRandoopRegressionS00.test19");
        org.apache.syncope.core.provisioning.java.DefaultMappingManager defaultMappingManager0 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.manager();
        org.apache.syncope.core.persistence.api.entity.ExternalResource externalResource1 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.resource();
        org.apache.syncope.common.lib.to.Provision provision2 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.plainAndKeyProvision();
        provision2.setUidOnCreate("mailAlternateAddress");
        org.apache.syncope.common.lib.to.Mapping mapping5 = provision2.getMapping();
        org.apache.syncope.common.lib.to.Mapping mapping6 = provision2.getMapping();
        provision2.setUidOnCreate("USER");
        org.apache.syncope.common.lib.to.Item item9 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.usernameItem();
        boolean boolean11 = item9.equals((java.lang.Object) "name");
        org.apache.syncope.core.provisioning.api.IntAttrName intAttrName12 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.missingIntAttrName();
        org.apache.syncope.common.lib.types.AttrSchemaType attrSchemaType13 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.stringSchemaType();
        org.apache.syncope.core.persistence.api.entity.Any any14 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.anyUser();
        org.apache.syncope.core.provisioning.api.AccountGetter accountGetter15 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.defaultAccountGetter();
        org.apache.syncope.core.provisioning.api.PlainAttrGetter plainAttrGetter16 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.missingPlainAttrGetter();
        org.apache.syncope.core.provisioning.api.MappingManager.IntValues intValues17 = defaultMappingManager0.getIntValues(externalResource1, provision2, item9, intAttrName12, attrSchemaType13, any14, accountGetter15, plainAttrGetter16);
        java.lang.String str18 = item9.getPropagationJEXLTransformer();
        item9.setPullJEXLTransformer("username");
        org.junit.Assert.assertNotNull(defaultMappingManager0);
        org.junit.Assert.assertNotNull(externalResource1);
        org.junit.Assert.assertNotNull(provision2);
        org.junit.Assert.assertNotNull(mapping5);
        org.junit.Assert.assertNotNull(mapping6);
        org.junit.Assert.assertNotNull(item9);
        org.junit.Assert.assertTrue("'" + boolean11 + "' != '" + false + "'", boolean11 == false);
        org.junit.Assert.assertNotNull(intAttrName12);
        org.junit.Assert.assertTrue("'" + attrSchemaType13 + "' != '" + org.apache.syncope.common.lib.types.AttrSchemaType.String + "'", attrSchemaType13.equals(org.apache.syncope.common.lib.types.AttrSchemaType.String));
        org.junit.Assert.assertNotNull(any14);
        org.junit.Assert.assertNotNull(accountGetter15);
        org.junit.Assert.assertNotNull(plainAttrGetter16);
        org.junit.Assert.assertNotNull(intValues17);
        org.junit.Assert.assertNull(str18);
    }

    @Test
    public void test20() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "DefaultMappingManagerRandoopRegressionS00.test20");
        org.apache.syncope.core.provisioning.java.DefaultMappingManager defaultMappingManager0 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.manager();
        org.apache.syncope.common.lib.to.Provision provision1 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.plainAndKeyProvision();
        provision1.setUidOnCreate("mailAlternateAddress");
        org.apache.syncope.common.lib.to.Mapping mapping4 = provision1.getMapping();
        org.apache.syncope.common.lib.to.Provision provision5 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.plainAndKeyProvision();
        provision5.setUidOnCreate("mailAlternateAddress");
        org.apache.syncope.common.lib.to.Mapping mapping8 = provision5.getMapping();
        org.apache.syncope.common.lib.to.Mapping mapping9 = provision5.getMapping();
        provision1.setMapping(mapping9);
        mapping9.setConnObjectLink("mrossi@example.com");
        java.util.Optional<org.apache.syncope.common.lib.to.Item> itemOptional13 = mapping9.getConnObjectKeyItem();
        org.apache.syncope.common.lib.to.Item item14 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.keyItem();
        java.lang.String str15 = item14.getMandatoryCondition();
        java.lang.String str16 = item14.getIntAttrName();
        boolean boolean17 = mapping9.add(item14);
        org.identityconnectors.framework.common.objects.Attribute attribute19 = org.identityconnectors.framework.common.objects.AttributeBuilder.buildPasswordExpirationDate((long) (short) -1);
        org.apache.syncope.common.lib.to.AnyObjectTO anyObjectTO20 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.anyObjectTO();
        java.util.Optional<org.apache.syncope.common.lib.to.MembershipTO> membershipTOOptional22 = anyObjectTO20.getMembership("email");
        defaultMappingManager0.setIntValues(item14, attribute19, (org.apache.syncope.common.lib.to.AnyTO) anyObjectTO20);
        java.lang.String str24 = anyObjectTO20.getDiscriminator();
        org.junit.Assert.assertNotNull(defaultMappingManager0);
        org.junit.Assert.assertNotNull(provision1);
        org.junit.Assert.assertNotNull(mapping4);
        org.junit.Assert.assertNotNull(provision5);
        org.junit.Assert.assertNotNull(mapping8);
        org.junit.Assert.assertNotNull(mapping9);
        org.junit.Assert.assertNotNull(itemOptional13);
        org.junit.Assert.assertNotNull(item14);
        org.junit.Assert.assertEquals("'" + str15 + "' != '" + "false" + "'", str15, "false");
        org.junit.Assert.assertEquals("'" + str16 + "' != '" + "key" + "'", str16, "key");
        org.junit.Assert.assertTrue("'" + boolean17 + "' != '" + true + "'", boolean17 == true);
        org.junit.Assert.assertNotNull(attribute19);
        org.junit.Assert.assertNotNull(anyObjectTO20);
        org.junit.Assert.assertNotNull(membershipTOOptional22);
        org.junit.Assert.assertEquals("'" + str24 + "' != '" + "org.apache.syncope.common.lib.to.AnyObjectTO" + "'", str24, "org.apache.syncope.common.lib.to.AnyObjectTO");
    }

    @Test
    public void test21() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "DefaultMappingManagerRandoopRegressionS00.test21");
        org.apache.syncope.core.provisioning.java.DefaultMappingManager defaultMappingManager0 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.manager();
        org.apache.syncope.core.persistence.api.entity.ExternalResource externalResource1 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.resource();
        org.apache.syncope.common.lib.to.Provision provision2 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.plainAndKeyProvision();
        provision2.setUidOnCreate("mailAlternateAddress");
        org.apache.syncope.common.lib.to.Mapping mapping5 = provision2.getMapping();
        org.apache.syncope.common.lib.to.Mapping mapping6 = provision2.getMapping();
        provision2.setUidOnCreate("USER");
        org.apache.syncope.common.lib.to.Item item9 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.usernameItem();
        boolean boolean11 = item9.equals((java.lang.Object) "name");
        org.apache.syncope.core.provisioning.api.IntAttrName intAttrName12 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.missingIntAttrName();
        org.apache.syncope.common.lib.types.AttrSchemaType attrSchemaType13 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.stringSchemaType();
        org.apache.syncope.core.persistence.api.entity.Any any14 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.anyUser();
        org.apache.syncope.core.provisioning.api.AccountGetter accountGetter15 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.defaultAccountGetter();
        org.apache.syncope.core.provisioning.api.PlainAttrGetter plainAttrGetter16 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.missingPlainAttrGetter();
        org.apache.syncope.core.provisioning.api.MappingManager.IntValues intValues17 = defaultMappingManager0.getIntValues(externalResource1, provision2, item9, intAttrName12, attrSchemaType13, any14, accountGetter15, plainAttrGetter16);
        provision2.setIgnoreCaseMatch(false);
        org.junit.Assert.assertNotNull(defaultMappingManager0);
        org.junit.Assert.assertNotNull(externalResource1);
        org.junit.Assert.assertNotNull(provision2);
        org.junit.Assert.assertNotNull(mapping5);
        org.junit.Assert.assertNotNull(mapping6);
        org.junit.Assert.assertNotNull(item9);
        org.junit.Assert.assertTrue("'" + boolean11 + "' != '" + false + "'", boolean11 == false);
        org.junit.Assert.assertNotNull(intAttrName12);
        org.junit.Assert.assertTrue("'" + attrSchemaType13 + "' != '" + org.apache.syncope.common.lib.types.AttrSchemaType.String + "'", attrSchemaType13.equals(org.apache.syncope.common.lib.types.AttrSchemaType.String));
        org.junit.Assert.assertNotNull(any14);
        org.junit.Assert.assertNotNull(accountGetter15);
        org.junit.Assert.assertNotNull(plainAttrGetter16);
        org.junit.Assert.assertNotNull(intValues17);
    }

    @Test
    public void test22() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "DefaultMappingManagerRandoopRegressionS00.test22");
        org.apache.syncope.core.provisioning.java.DefaultMappingManager defaultMappingManager0 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.manager();
        org.apache.syncope.common.lib.to.Item item1 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.keyItem();
        java.lang.String str2 = item1.getIntAttrName();
        java.util.List<java.lang.String> strList3 = item1.getTransformers();
        org.identityconnectors.framework.common.objects.Attribute attribute5 = org.identityconnectors.framework.common.objects.AttributeBuilder.build("initial-group");
        org.apache.syncope.common.lib.to.Provision provision6 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.plainAndKeyProvision();
        provision6.setUidOnCreate("mailAlternateAddress");
        org.apache.syncope.common.lib.to.Mapping mapping9 = provision6.getMapping();
        org.apache.syncope.common.lib.to.Mapping mapping10 = provision6.getMapping();
        org.apache.syncope.common.lib.to.RealmTO realmTO11 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.realmTO();
        java.util.Optional<org.apache.syncope.common.lib.Attr> attrOptional13 = realmTO11.getPlainAttr("");
        java.util.Set<org.apache.syncope.common.lib.Attr> attrSet14 = realmTO11.getDerAttrs();
        java.util.Set<org.apache.syncope.common.lib.Attr> attrSet15 = realmTO11.getPlainAttrs();
        boolean boolean16 = mapping10.equals((java.lang.Object) realmTO11);
        java.util.Optional<org.apache.syncope.common.lib.Attr> attrOptional18 = realmTO11.getDerAttr("GROUP");
        java.lang.String str19 = realmTO11.getTicketExpirationPolicy();
        defaultMappingManager0.setIntValues(item1, attribute5, realmTO11);
        java.util.List<java.lang.String> strList21 = item1.getTransformers();
        java.lang.String str22 = item1.toString();
        java.lang.String str23 = item1.toString();
        org.junit.Assert.assertNotNull(defaultMappingManager0);
        org.junit.Assert.assertNotNull(item1);
        org.junit.Assert.assertEquals("'" + str2 + "' != '" + "key" + "'", str2, "key");
        org.junit.Assert.assertNotNull(strList3);
        org.junit.Assert.assertNotNull(attribute5);
        org.junit.Assert.assertNotNull(provision6);
        org.junit.Assert.assertNotNull(mapping9);
        org.junit.Assert.assertNotNull(mapping10);
        org.junit.Assert.assertNotNull(realmTO11);
        org.junit.Assert.assertNotNull(attrOptional13);
        org.junit.Assert.assertNotNull(attrSet14);
        org.junit.Assert.assertNotNull(attrSet15);
        org.junit.Assert.assertTrue("'" + boolean16 + "' != '" + false + "'", boolean16 == false);
        org.junit.Assert.assertNotNull(attrOptional18);
        org.junit.Assert.assertNull(str19);
        org.junit.Assert.assertNotNull(strList21);
    }

    @Test
    public void test23() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "DefaultMappingManagerRandoopRegressionS00.test23");
        org.apache.syncope.core.persistence.api.dao.UserDAO userDAO0 = null;
        org.apache.syncope.core.persistence.api.dao.AnyObjectDAO anyObjectDAO1 = null;
        org.apache.syncope.core.persistence.api.dao.GroupDAO groupDAO2 = null;
        org.apache.syncope.core.persistence.api.dao.RelationshipTypeDAO relationshipTypeDAO3 = null;
        org.apache.syncope.core.persistence.api.dao.RealmSearchDAO realmSearchDAO4 = null;
        org.apache.syncope.core.persistence.api.dao.ImplementationDAO implementationDAO5 = null;
        org.apache.syncope.core.provisioning.api.DerAttrHandler derAttrHandler6 = null;
        org.apache.syncope.core.provisioning.api.IntAttrNameParser intAttrNameParser7 = null;
        org.apache.syncope.core.persistence.api.EncryptorManager encryptorManager8 = null;
        org.apache.syncope.core.provisioning.api.jexl.JexlTools jexlTools9 = null;
        org.apache.syncope.core.provisioning.java.DefaultMappingManager defaultMappingManager10 = new org.apache.syncope.core.provisioning.java.DefaultMappingManager(userDAO0, anyObjectDAO1, groupDAO2, relationshipTypeDAO3, realmSearchDAO4, implementationDAO5, derAttrHandler6, intAttrNameParser7, encryptorManager8, jexlTools9);
        org.apache.syncope.core.persistence.api.entity.Any any11 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.anyUser();
        org.apache.syncope.core.persistence.api.entity.ExternalResource externalResource15 = null;
        org.apache.syncope.common.lib.to.Provision provision16 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.plainAndKeyProvision();
        provision16.setUidOnCreate("mailAlternateAddress");
        java.lang.String str19 = provision16.getAnyType();
        org.apache.syncope.core.provisioning.api.MappingManager.PreparedAttrs preparedAttrs20 = defaultMappingManager10.prepareAttrsFromAny(any11, "mario.rossi@example.org", true, (java.lang.Boolean) false, externalResource15, provision16);
        provision16.setUidOnCreate("org.apache.syncope.common.lib.to.UserTO");
        org.junit.Assert.assertNotNull(any11);
        org.junit.Assert.assertNotNull(provision16);
        org.junit.Assert.assertNull(str19);
        org.junit.Assert.assertNotNull(preparedAttrs20);
    }

    @Test
    public void test24() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "DefaultMappingManagerRandoopRegressionS00.test24");
        org.apache.syncope.core.persistence.api.dao.UserDAO userDAO0 = null;
        org.apache.syncope.core.persistence.api.dao.AnyObjectDAO anyObjectDAO1 = null;
        org.apache.syncope.core.persistence.api.dao.GroupDAO groupDAO2 = null;
        org.apache.syncope.core.persistence.api.dao.RelationshipTypeDAO relationshipTypeDAO3 = null;
        org.apache.syncope.core.persistence.api.dao.RealmSearchDAO realmSearchDAO4 = null;
        org.apache.syncope.core.persistence.api.dao.ImplementationDAO implementationDAO5 = null;
        org.apache.syncope.core.provisioning.api.DerAttrHandler derAttrHandler6 = null;
        org.apache.syncope.core.provisioning.api.IntAttrNameParser intAttrNameParser7 = null;
        org.apache.syncope.core.persistence.api.EncryptorManager encryptorManager8 = null;
        org.apache.syncope.core.provisioning.api.jexl.JexlTools jexlTools9 = null;
        org.apache.syncope.core.provisioning.java.DefaultMappingManager defaultMappingManager10 = new org.apache.syncope.core.provisioning.java.DefaultMappingManager(userDAO0, anyObjectDAO1, groupDAO2, relationshipTypeDAO3, realmSearchDAO4, implementationDAO5, derAttrHandler6, intAttrNameParser7, encryptorManager8, jexlTools9);
        org.apache.syncope.core.persistence.api.entity.Any any11 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.anyUser();
        org.apache.syncope.core.persistence.api.entity.ExternalResource externalResource15 = null;
        org.apache.syncope.common.lib.to.Provision provision16 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.plainAndKeyProvision();
        provision16.setUidOnCreate("mailAlternateAddress");
        java.lang.String str19 = provision16.getAnyType();
        org.apache.syncope.core.provisioning.api.MappingManager.PreparedAttrs preparedAttrs20 = defaultMappingManager10.prepareAttrsFromAny(any11, "mario.rossi@example.org", true, (java.lang.Boolean) false, externalResource15, provision16);
        org.apache.syncope.common.lib.to.Mapping mapping21 = provision16.getMapping();
        mapping21.setConnObjectLink("mustChangePassword");
        java.util.List<org.apache.syncope.common.lib.to.Item> itemList24 = mapping21.getItems();
        org.junit.Assert.assertNotNull(any11);
        org.junit.Assert.assertNotNull(provision16);
        org.junit.Assert.assertNull(str19);
        org.junit.Assert.assertNotNull(preparedAttrs20);
        org.junit.Assert.assertNotNull(mapping21);
        org.junit.Assert.assertNotNull(itemList24);
    }

    @Test
    public void test25() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "DefaultMappingManagerRandoopRegressionS00.test25");
        org.apache.syncope.core.provisioning.java.DefaultMappingManager defaultMappingManager0 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.manager();
        org.apache.syncope.common.lib.to.Item item1 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.keyItem();
        java.lang.String str2 = item1.getIntAttrName();
        java.util.List<java.lang.String> strList3 = item1.getTransformers();
        org.identityconnectors.framework.common.objects.Attribute attribute5 = org.identityconnectors.framework.common.objects.AttributeBuilder.build("initial-group");
        org.apache.syncope.common.lib.to.Provision provision6 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.plainAndKeyProvision();
        provision6.setUidOnCreate("mailAlternateAddress");
        org.apache.syncope.common.lib.to.Mapping mapping9 = provision6.getMapping();
        org.apache.syncope.common.lib.to.Mapping mapping10 = provision6.getMapping();
        org.apache.syncope.common.lib.to.RealmTO realmTO11 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.realmTO();
        java.util.Optional<org.apache.syncope.common.lib.Attr> attrOptional13 = realmTO11.getPlainAttr("");
        java.util.Set<org.apache.syncope.common.lib.Attr> attrSet14 = realmTO11.getDerAttrs();
        java.util.Set<org.apache.syncope.common.lib.Attr> attrSet15 = realmTO11.getPlainAttrs();
        boolean boolean16 = mapping10.equals((java.lang.Object) realmTO11);
        java.util.Optional<org.apache.syncope.common.lib.Attr> attrOptional18 = realmTO11.getDerAttr("GROUP");
        java.lang.String str19 = realmTO11.getTicketExpirationPolicy();
        defaultMappingManager0.setIntValues(item1, attribute5, realmTO11);
        java.util.List<java.lang.String> strList21 = realmTO11.getAnyTypeClasses();
        java.util.List<java.lang.String> strList22 = realmTO11.getActions();
        org.junit.Assert.assertNotNull(defaultMappingManager0);
        org.junit.Assert.assertNotNull(item1);
        org.junit.Assert.assertEquals("'" + str2 + "' != '" + "key" + "'", str2, "key");
        org.junit.Assert.assertNotNull(strList3);
        org.junit.Assert.assertNotNull(attribute5);
        org.junit.Assert.assertNotNull(provision6);
        org.junit.Assert.assertNotNull(mapping9);
        org.junit.Assert.assertNotNull(mapping10);
        org.junit.Assert.assertNotNull(realmTO11);
        org.junit.Assert.assertNotNull(attrOptional13);
        org.junit.Assert.assertNotNull(attrSet14);
        org.junit.Assert.assertNotNull(attrSet15);
        org.junit.Assert.assertTrue("'" + boolean16 + "' != '" + false + "'", boolean16 == false);
        org.junit.Assert.assertNotNull(attrOptional18);
        org.junit.Assert.assertNull(str19);
        org.junit.Assert.assertNotNull(strList21);
        org.junit.Assert.assertNotNull(strList22);
    }

    @Test
    public void test26() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "DefaultMappingManagerRandoopRegressionS00.test26");
        org.apache.syncope.core.persistence.api.entity.PlainAttrValue plainAttrValue0 = new org.apache.syncope.core.persistence.api.entity.PlainAttrValue();
        org.apache.syncope.core.provisioning.java.DefaultMappingManager defaultMappingManager1 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.manager();
        org.apache.syncope.core.persistence.api.entity.ExternalResource externalResource2 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.resource();
        org.apache.syncope.common.lib.to.Provision provision3 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.plainAndKeyProvision();
        provision3.setUidOnCreate("mailAlternateAddress");
        org.apache.syncope.common.lib.to.Mapping mapping6 = provision3.getMapping();
        org.apache.syncope.common.lib.to.Mapping mapping7 = provision3.getMapping();
        provision3.setUidOnCreate("USER");
        org.apache.syncope.common.lib.to.Item item10 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.usernameItem();
        boolean boolean12 = item10.equals((java.lang.Object) "name");
        org.apache.syncope.core.provisioning.api.IntAttrName intAttrName13 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.missingIntAttrName();
        org.apache.syncope.common.lib.types.AttrSchemaType attrSchemaType14 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.stringSchemaType();
        org.apache.syncope.core.persistence.api.entity.Any any15 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.anyUser();
        org.apache.syncope.core.provisioning.api.AccountGetter accountGetter16 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.defaultAccountGetter();
        org.apache.syncope.core.provisioning.api.PlainAttrGetter plainAttrGetter17 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.missingPlainAttrGetter();
        org.apache.syncope.core.provisioning.api.MappingManager.IntValues intValues18 = defaultMappingManager1.getIntValues(externalResource2, provision3, item10, intAttrName13, attrSchemaType14, any15, accountGetter16, plainAttrGetter17);
        java.lang.String str19 = plainAttrValue0.getValueAsString(attrSchemaType14);
        plainAttrValue0.setStringValue("mobile");
        // The following exception was thrown during execution in test generation
        try {
            org.apache.syncope.common.lib.to.RelatableTO relatableTO22 = plainAttrValue0.getValue();
            org.junit.Assert.fail("Expected exception of type java.lang.ClassCastException; message: Cannot cast java.lang.String to org.apache.syncope.common.lib.to.RelatableTO");
        } catch (java.lang.ClassCastException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(defaultMappingManager1);
        org.junit.Assert.assertNotNull(externalResource2);
        org.junit.Assert.assertNotNull(provision3);
        org.junit.Assert.assertNotNull(mapping6);
        org.junit.Assert.assertNotNull(mapping7);
        org.junit.Assert.assertNotNull(item10);
        org.junit.Assert.assertTrue("'" + boolean12 + "' != '" + false + "'", boolean12 == false);
        org.junit.Assert.assertNotNull(intAttrName13);
        org.junit.Assert.assertTrue("'" + attrSchemaType14 + "' != '" + org.apache.syncope.common.lib.types.AttrSchemaType.String + "'", attrSchemaType14.equals(org.apache.syncope.common.lib.types.AttrSchemaType.String));
        org.junit.Assert.assertNotNull(any15);
        org.junit.Assert.assertNotNull(accountGetter16);
        org.junit.Assert.assertNotNull(plainAttrGetter17);
        org.junit.Assert.assertNotNull(intValues18);
        org.junit.Assert.assertEquals("'" + str19 + "' != '" + "" + "'", str19, "");
    }

    @Test
    public void test27() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "DefaultMappingManagerRandoopRegressionS00.test27");
        org.apache.syncope.core.provisioning.java.DefaultMappingManager defaultMappingManager0 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.manager();
        org.apache.syncope.core.persistence.api.entity.ExternalResource externalResource1 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.resource();
        org.apache.syncope.common.lib.to.Provision provision2 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.plainAndKeyProvision();
        provision2.setUidOnCreate("mailAlternateAddress");
        org.apache.syncope.common.lib.to.Mapping mapping5 = provision2.getMapping();
        org.apache.syncope.common.lib.to.Mapping mapping6 = provision2.getMapping();
        provision2.setUidOnCreate("USER");
        org.apache.syncope.common.lib.to.Item item9 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.usernameItem();
        boolean boolean11 = item9.equals((java.lang.Object) "name");
        org.apache.syncope.core.provisioning.api.IntAttrName intAttrName12 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.missingIntAttrName();
        org.apache.syncope.common.lib.types.AttrSchemaType attrSchemaType13 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.stringSchemaType();
        org.apache.syncope.core.persistence.api.entity.Any any14 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.anyUser();
        org.apache.syncope.core.provisioning.api.AccountGetter accountGetter15 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.defaultAccountGetter();
        org.apache.syncope.core.provisioning.api.PlainAttrGetter plainAttrGetter16 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.missingPlainAttrGetter();
        org.apache.syncope.core.provisioning.api.MappingManager.IntValues intValues17 = defaultMappingManager0.getIntValues(externalResource1, provision2, item9, intAttrName12, attrSchemaType13, any14, accountGetter15, plainAttrGetter16);
        item9.setExtAttrName("uid");
        org.junit.Assert.assertNotNull(defaultMappingManager0);
        org.junit.Assert.assertNotNull(externalResource1);
        org.junit.Assert.assertNotNull(provision2);
        org.junit.Assert.assertNotNull(mapping5);
        org.junit.Assert.assertNotNull(mapping6);
        org.junit.Assert.assertNotNull(item9);
        org.junit.Assert.assertTrue("'" + boolean11 + "' != '" + false + "'", boolean11 == false);
        org.junit.Assert.assertNotNull(intAttrName12);
        org.junit.Assert.assertTrue("'" + attrSchemaType13 + "' != '" + org.apache.syncope.common.lib.types.AttrSchemaType.String + "'", attrSchemaType13.equals(org.apache.syncope.common.lib.types.AttrSchemaType.String));
        org.junit.Assert.assertNotNull(any14);
        org.junit.Assert.assertNotNull(accountGetter15);
        org.junit.Assert.assertNotNull(plainAttrGetter16);
        org.junit.Assert.assertNotNull(intValues17);
    }

    @Test
    public void test28() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "DefaultMappingManagerRandoopRegressionS00.test28");
        org.apache.syncope.core.provisioning.java.DefaultMappingManager defaultMappingManager0 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.manager();
        org.apache.syncope.common.lib.to.Item item1 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.keyItem();
        java.lang.String str2 = item1.getIntAttrName();
        java.util.List<java.lang.String> strList3 = item1.getTransformers();
        org.identityconnectors.framework.common.objects.Attribute attribute5 = org.identityconnectors.framework.common.objects.AttributeBuilder.build("initial-group");
        org.apache.syncope.common.lib.to.Provision provision6 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.plainAndKeyProvision();
        provision6.setUidOnCreate("mailAlternateAddress");
        org.apache.syncope.common.lib.to.Mapping mapping9 = provision6.getMapping();
        org.apache.syncope.common.lib.to.Mapping mapping10 = provision6.getMapping();
        org.apache.syncope.common.lib.to.RealmTO realmTO11 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.realmTO();
        java.util.Optional<org.apache.syncope.common.lib.Attr> attrOptional13 = realmTO11.getPlainAttr("");
        java.util.Set<org.apache.syncope.common.lib.Attr> attrSet14 = realmTO11.getDerAttrs();
        java.util.Set<org.apache.syncope.common.lib.Attr> attrSet15 = realmTO11.getPlainAttrs();
        boolean boolean16 = mapping10.equals((java.lang.Object) realmTO11);
        java.util.Optional<org.apache.syncope.common.lib.Attr> attrOptional18 = realmTO11.getDerAttr("GROUP");
        java.lang.String str19 = realmTO11.getTicketExpirationPolicy();
        defaultMappingManager0.setIntValues(item1, attribute5, realmTO11);
        java.util.List<java.lang.String> strList21 = realmTO11.getResources();
        realmTO11.setPasswordPolicy("username");
        org.junit.Assert.assertNotNull(defaultMappingManager0);
        org.junit.Assert.assertNotNull(item1);
        org.junit.Assert.assertEquals("'" + str2 + "' != '" + "key" + "'", str2, "key");
        org.junit.Assert.assertNotNull(strList3);
        org.junit.Assert.assertNotNull(attribute5);
        org.junit.Assert.assertNotNull(provision6);
        org.junit.Assert.assertNotNull(mapping9);
        org.junit.Assert.assertNotNull(mapping10);
        org.junit.Assert.assertNotNull(realmTO11);
        org.junit.Assert.assertNotNull(attrOptional13);
        org.junit.Assert.assertNotNull(attrSet14);
        org.junit.Assert.assertNotNull(attrSet15);
        org.junit.Assert.assertTrue("'" + boolean16 + "' != '" + false + "'", boolean16 == false);
        org.junit.Assert.assertNotNull(attrOptional18);
        org.junit.Assert.assertNull(str19);
        org.junit.Assert.assertNotNull(strList21);
    }

    @Test
    public void test29() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "DefaultMappingManagerRandoopRegressionS00.test29");
        org.apache.syncope.core.provisioning.java.DefaultMappingManager defaultMappingManager0 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.manager();
        org.apache.syncope.core.persistence.api.entity.ExternalResource externalResource1 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.resource();
        org.apache.syncope.common.lib.to.Provision provision2 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.plainAndKeyProvision();
        provision2.setUidOnCreate("mailAlternateAddress");
        org.apache.syncope.common.lib.to.Mapping mapping5 = provision2.getMapping();
        org.apache.syncope.common.lib.to.Mapping mapping6 = provision2.getMapping();
        org.apache.syncope.common.lib.to.Item item7 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.optionalMissingPlainItem();
        java.lang.String str8 = item7.toString();
        java.lang.String str9 = item7.toString();
        item7.setPassword(true);
        org.apache.syncope.core.provisioning.api.IntAttrName intAttrName12 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.usernameIntAttrName();
        org.apache.syncope.common.lib.types.AttrSchemaType attrSchemaType13 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.stringSchemaType();
        org.apache.syncope.core.persistence.api.entity.Any any14 = null;
        org.apache.syncope.core.provisioning.api.AccountGetter accountGetter15 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.defaultAccountGetter();
        org.apache.syncope.core.provisioning.api.PlainAttrGetter plainAttrGetter16 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.missingPlainAttrGetter();
        org.apache.syncope.core.provisioning.api.MappingManager.IntValues intValues17 = defaultMappingManager0.getIntValues(externalResource1, provision2, item7, intAttrName12, attrSchemaType13, any14, accountGetter15, plainAttrGetter16);
        java.lang.Class<?> wildcardClass18 = intAttrName12.getClass();
        org.junit.Assert.assertNotNull(defaultMappingManager0);
        org.junit.Assert.assertNotNull(externalResource1);
        org.junit.Assert.assertNotNull(provision2);
        org.junit.Assert.assertNotNull(mapping5);
        org.junit.Assert.assertNotNull(mapping6);
        org.junit.Assert.assertNotNull(item7);
        org.junit.Assert.assertNotNull(intAttrName12);
        org.junit.Assert.assertTrue("'" + attrSchemaType13 + "' != '" + org.apache.syncope.common.lib.types.AttrSchemaType.String + "'", attrSchemaType13.equals(org.apache.syncope.common.lib.types.AttrSchemaType.String));
        org.junit.Assert.assertNotNull(accountGetter15);
        org.junit.Assert.assertNotNull(plainAttrGetter16);
        org.junit.Assert.assertNotNull(intValues17);
        org.junit.Assert.assertNotNull(wildcardClass18);
    }

    @Test
    public void test30() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "DefaultMappingManagerRandoopRegressionS00.test30");
        org.apache.syncope.core.provisioning.java.DefaultMappingManager defaultMappingManager0 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.manager();
        org.apache.syncope.common.lib.to.Item item1 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.keyItem();
        java.lang.String str2 = item1.getIntAttrName();
        java.util.List<java.lang.String> strList3 = item1.getTransformers();
        org.identityconnectors.framework.common.objects.Attribute attribute5 = org.identityconnectors.framework.common.objects.AttributeBuilder.build("initial-group");
        org.apache.syncope.common.lib.to.Provision provision6 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.plainAndKeyProvision();
        provision6.setUidOnCreate("mailAlternateAddress");
        org.apache.syncope.common.lib.to.Mapping mapping9 = provision6.getMapping();
        org.apache.syncope.common.lib.to.Mapping mapping10 = provision6.getMapping();
        org.apache.syncope.common.lib.to.RealmTO realmTO11 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.realmTO();
        java.util.Optional<org.apache.syncope.common.lib.Attr> attrOptional13 = realmTO11.getPlainAttr("");
        java.util.Set<org.apache.syncope.common.lib.Attr> attrSet14 = realmTO11.getDerAttrs();
        java.util.Set<org.apache.syncope.common.lib.Attr> attrSet15 = realmTO11.getPlainAttrs();
        boolean boolean16 = mapping10.equals((java.lang.Object) realmTO11);
        java.util.Optional<org.apache.syncope.common.lib.Attr> attrOptional18 = realmTO11.getDerAttr("GROUP");
        java.lang.String str19 = realmTO11.getTicketExpirationPolicy();
        defaultMappingManager0.setIntValues(item1, attribute5, realmTO11);
        java.util.List<java.lang.String> strList21 = realmTO11.getAnyTypeClasses();
        java.lang.String str22 = realmTO11.getFullPath();
        java.lang.String str23 = realmTO11.getAttrReleasePolicy();
        org.junit.Assert.assertNotNull(defaultMappingManager0);
        org.junit.Assert.assertNotNull(item1);
        org.junit.Assert.assertEquals("'" + str2 + "' != '" + "key" + "'", str2, "key");
        org.junit.Assert.assertNotNull(strList3);
        org.junit.Assert.assertNotNull(attribute5);
        org.junit.Assert.assertNotNull(provision6);
        org.junit.Assert.assertNotNull(mapping9);
        org.junit.Assert.assertNotNull(mapping10);
        org.junit.Assert.assertNotNull(realmTO11);
        org.junit.Assert.assertNotNull(attrOptional13);
        org.junit.Assert.assertNotNull(attrSet14);
        org.junit.Assert.assertNotNull(attrSet15);
        org.junit.Assert.assertTrue("'" + boolean16 + "' != '" + false + "'", boolean16 == false);
        org.junit.Assert.assertNotNull(attrOptional18);
        org.junit.Assert.assertNull(str19);
        org.junit.Assert.assertNotNull(strList21);
        org.junit.Assert.assertNull(str22);
        org.junit.Assert.assertNull(str23);
    }

    @Test
    public void test31() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "DefaultMappingManagerRandoopRegressionS00.test31");
        org.apache.syncope.core.persistence.api.dao.UserDAO userDAO0 = null;
        org.apache.syncope.core.persistence.api.dao.AnyObjectDAO anyObjectDAO1 = null;
        org.apache.syncope.core.persistence.api.dao.GroupDAO groupDAO2 = null;
        org.apache.syncope.core.persistence.api.dao.RelationshipTypeDAO relationshipTypeDAO3 = null;
        org.apache.syncope.core.persistence.api.dao.RealmSearchDAO realmSearchDAO4 = null;
        org.apache.syncope.core.persistence.api.dao.ImplementationDAO implementationDAO5 = null;
        org.apache.syncope.core.provisioning.api.DerAttrHandler derAttrHandler6 = null;
        org.apache.syncope.core.provisioning.api.IntAttrNameParser intAttrNameParser7 = null;
        org.apache.syncope.core.persistence.api.EncryptorManager encryptorManager8 = null;
        org.apache.syncope.core.provisioning.api.jexl.JexlTools jexlTools9 = null;
        org.apache.syncope.core.provisioning.java.DefaultMappingManager defaultMappingManager10 = new org.apache.syncope.core.provisioning.java.DefaultMappingManager(userDAO0, anyObjectDAO1, groupDAO2, relationshipTypeDAO3, realmSearchDAO4, implementationDAO5, derAttrHandler6, intAttrNameParser7, encryptorManager8, jexlTools9);
        org.apache.syncope.core.persistence.api.entity.Any any11 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.anyUser();
        org.apache.syncope.core.persistence.api.entity.ExternalResource externalResource15 = null;
        org.apache.syncope.common.lib.to.Provision provision16 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.plainAndKeyProvision();
        provision16.setUidOnCreate("mailAlternateAddress");
        java.lang.String str19 = provision16.getAnyType();
        org.apache.syncope.core.provisioning.api.MappingManager.PreparedAttrs preparedAttrs20 = defaultMappingManager10.prepareAttrsFromAny(any11, "mario.rossi@example.org", true, (java.lang.Boolean) false, externalResource15, provision16);
        provision16.setUidOnCreate("Mario");
        provision16.setIgnoreCaseMatch(false);
        org.junit.Assert.assertNotNull(any11);
        org.junit.Assert.assertNotNull(provision16);
        org.junit.Assert.assertNull(str19);
        org.junit.Assert.assertNotNull(preparedAttrs20);
    }

    @Test
    public void test32() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "DefaultMappingManagerRandoopRegressionS00.test32");
        org.apache.syncope.core.provisioning.java.DefaultMappingManager defaultMappingManager0 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.manager();
        org.apache.syncope.common.lib.to.Item item1 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.keyItem();
        java.lang.String str2 = item1.getIntAttrName();
        java.util.List<java.lang.String> strList3 = item1.getTransformers();
        org.identityconnectors.framework.common.objects.Attribute attribute5 = org.identityconnectors.framework.common.objects.AttributeBuilder.build("initial-group");
        org.apache.syncope.common.lib.to.Provision provision6 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.plainAndKeyProvision();
        provision6.setUidOnCreate("mailAlternateAddress");
        org.apache.syncope.common.lib.to.Mapping mapping9 = provision6.getMapping();
        org.apache.syncope.common.lib.to.Mapping mapping10 = provision6.getMapping();
        org.apache.syncope.common.lib.to.RealmTO realmTO11 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.realmTO();
        java.util.Optional<org.apache.syncope.common.lib.Attr> attrOptional13 = realmTO11.getPlainAttr("");
        java.util.Set<org.apache.syncope.common.lib.Attr> attrSet14 = realmTO11.getDerAttrs();
        java.util.Set<org.apache.syncope.common.lib.Attr> attrSet15 = realmTO11.getPlainAttrs();
        boolean boolean16 = mapping10.equals((java.lang.Object) realmTO11);
        java.util.Optional<org.apache.syncope.common.lib.Attr> attrOptional18 = realmTO11.getDerAttr("GROUP");
        java.lang.String str19 = realmTO11.getTicketExpirationPolicy();
        defaultMappingManager0.setIntValues(item1, attribute5, realmTO11);
        java.util.List<java.lang.String> strList21 = item1.getTransformers();
        java.lang.String str22 = item1.getExtAttrName();
        org.apache.syncope.common.lib.types.MappingPurpose mappingPurpose23 = item1.getPurpose();
        org.junit.Assert.assertNotNull(defaultMappingManager0);
        org.junit.Assert.assertNotNull(item1);
        org.junit.Assert.assertEquals("'" + str2 + "' != '" + "key" + "'", str2, "key");
        org.junit.Assert.assertNotNull(strList3);
        org.junit.Assert.assertNotNull(attribute5);
        org.junit.Assert.assertNotNull(provision6);
        org.junit.Assert.assertNotNull(mapping9);
        org.junit.Assert.assertNotNull(mapping10);
        org.junit.Assert.assertNotNull(realmTO11);
        org.junit.Assert.assertNotNull(attrOptional13);
        org.junit.Assert.assertNotNull(attrSet14);
        org.junit.Assert.assertNotNull(attrSet15);
        org.junit.Assert.assertTrue("'" + boolean16 + "' != '" + false + "'", boolean16 == false);
        org.junit.Assert.assertNotNull(attrOptional18);
        org.junit.Assert.assertNull(str19);
        org.junit.Assert.assertNotNull(strList21);
        org.junit.Assert.assertEquals("'" + str22 + "' != '" + "uid" + "'", str22, "uid");
        org.junit.Assert.assertTrue("'" + mappingPurpose23 + "' != '" + org.apache.syncope.common.lib.types.MappingPurpose.PROPAGATION + "'", mappingPurpose23.equals(org.apache.syncope.common.lib.types.MappingPurpose.PROPAGATION));
    }

    @Test
    public void test33() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "DefaultMappingManagerRandoopRegressionS00.test33");
        org.apache.syncope.core.provisioning.java.DefaultMappingManager defaultMappingManager0 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.manager();
        org.apache.syncope.core.persistence.api.entity.ExternalResource externalResource1 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.resource();
        org.apache.syncope.common.lib.to.Provision provision2 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.plainAndKeyProvision();
        provision2.setUidOnCreate("mailAlternateAddress");
        org.apache.syncope.common.lib.to.Mapping mapping5 = provision2.getMapping();
        org.apache.syncope.common.lib.to.Mapping mapping6 = provision2.getMapping();
        org.apache.syncope.common.lib.to.Item item7 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.optionalMissingPlainItem();
        java.lang.String str8 = item7.toString();
        java.lang.String str9 = item7.toString();
        item7.setPassword(true);
        org.apache.syncope.core.provisioning.api.IntAttrName intAttrName12 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.usernameIntAttrName();
        org.apache.syncope.common.lib.types.AttrSchemaType attrSchemaType13 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.stringSchemaType();
        org.apache.syncope.core.persistence.api.entity.Any any14 = null;
        org.apache.syncope.core.provisioning.api.AccountGetter accountGetter15 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.defaultAccountGetter();
        org.apache.syncope.core.provisioning.api.PlainAttrGetter plainAttrGetter16 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.missingPlainAttrGetter();
        org.apache.syncope.core.provisioning.api.MappingManager.IntValues intValues17 = defaultMappingManager0.getIntValues(externalResource1, provision2, item7, intAttrName12, attrSchemaType13, any14, accountGetter15, plainAttrGetter16);
        java.util.List<java.lang.String> strList18 = provision2.getAuxClasses();
        org.junit.Assert.assertNotNull(defaultMappingManager0);
        org.junit.Assert.assertNotNull(externalResource1);
        org.junit.Assert.assertNotNull(provision2);
        org.junit.Assert.assertNotNull(mapping5);
        org.junit.Assert.assertNotNull(mapping6);
        org.junit.Assert.assertNotNull(item7);
        org.junit.Assert.assertNotNull(intAttrName12);
        org.junit.Assert.assertTrue("'" + attrSchemaType13 + "' != '" + org.apache.syncope.common.lib.types.AttrSchemaType.String + "'", attrSchemaType13.equals(org.apache.syncope.common.lib.types.AttrSchemaType.String));
        org.junit.Assert.assertNotNull(accountGetter15);
        org.junit.Assert.assertNotNull(plainAttrGetter16);
        org.junit.Assert.assertNotNull(intValues17);
        org.junit.Assert.assertNotNull(strList18);
    }

    @Test
    public void test34() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "DefaultMappingManagerRandoopRegressionS00.test34");
        org.apache.syncope.core.provisioning.java.DefaultMappingManager defaultMappingManager0 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.manager();
        org.apache.syncope.common.lib.to.Provision provision1 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.plainAndKeyProvision();
        provision1.setUidOnCreate("mailAlternateAddress");
        org.apache.syncope.common.lib.to.Mapping mapping4 = provision1.getMapping();
        org.apache.syncope.common.lib.to.Provision provision5 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.plainAndKeyProvision();
        provision5.setUidOnCreate("mailAlternateAddress");
        org.apache.syncope.common.lib.to.Mapping mapping8 = provision5.getMapping();
        org.apache.syncope.common.lib.to.Mapping mapping9 = provision5.getMapping();
        provision1.setMapping(mapping9);
        mapping9.setConnObjectLink("mrossi@example.com");
        java.util.Optional<org.apache.syncope.common.lib.to.Item> itemOptional13 = mapping9.getConnObjectKeyItem();
        org.apache.syncope.common.lib.to.Item item14 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.keyItem();
        java.lang.String str15 = item14.getMandatoryCondition();
        java.lang.String str16 = item14.getIntAttrName();
        boolean boolean17 = mapping9.add(item14);
        org.identityconnectors.framework.common.objects.Attribute attribute19 = org.identityconnectors.framework.common.objects.AttributeBuilder.buildPasswordExpirationDate((long) (short) -1);
        org.apache.syncope.common.lib.to.AnyObjectTO anyObjectTO20 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.anyObjectTO();
        java.util.Optional<org.apache.syncope.common.lib.to.MembershipTO> membershipTOOptional22 = anyObjectTO20.getMembership("email");
        defaultMappingManager0.setIntValues(item14, attribute19, (org.apache.syncope.common.lib.to.AnyTO) anyObjectTO20);
        java.lang.String str24 = anyObjectTO20.getType();
        org.junit.Assert.assertNotNull(defaultMappingManager0);
        org.junit.Assert.assertNotNull(provision1);
        org.junit.Assert.assertNotNull(mapping4);
        org.junit.Assert.assertNotNull(provision5);
        org.junit.Assert.assertNotNull(mapping8);
        org.junit.Assert.assertNotNull(mapping9);
        org.junit.Assert.assertNotNull(itemOptional13);
        org.junit.Assert.assertNotNull(item14);
        org.junit.Assert.assertEquals("'" + str15 + "' != '" + "false" + "'", str15, "false");
        org.junit.Assert.assertEquals("'" + str16 + "' != '" + "key" + "'", str16, "key");
        org.junit.Assert.assertTrue("'" + boolean17 + "' != '" + true + "'", boolean17 == true);
        org.junit.Assert.assertNotNull(attribute19);
        org.junit.Assert.assertNotNull(anyObjectTO20);
        org.junit.Assert.assertNotNull(membershipTOOptional22);
        org.junit.Assert.assertNull(str24);
    }

    @Test
    public void test35() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "DefaultMappingManagerRandoopRegressionS00.test35");
        org.apache.syncope.core.persistence.api.entity.PlainAttrValue plainAttrValue0 = new org.apache.syncope.core.persistence.api.entity.PlainAttrValue();
        org.apache.syncope.core.provisioning.java.DefaultMappingManager defaultMappingManager1 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.manager();
        org.apache.syncope.core.persistence.api.entity.ExternalResource externalResource2 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.resource();
        org.apache.syncope.common.lib.to.Provision provision3 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.plainAndKeyProvision();
        provision3.setUidOnCreate("mailAlternateAddress");
        org.apache.syncope.common.lib.to.Mapping mapping6 = provision3.getMapping();
        org.apache.syncope.common.lib.to.Mapping mapping7 = provision3.getMapping();
        provision3.setUidOnCreate("USER");
        org.apache.syncope.common.lib.to.Item item10 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.usernameItem();
        boolean boolean12 = item10.equals((java.lang.Object) "name");
        org.apache.syncope.core.provisioning.api.IntAttrName intAttrName13 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.missingIntAttrName();
        org.apache.syncope.common.lib.types.AttrSchemaType attrSchemaType14 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.stringSchemaType();
        org.apache.syncope.core.persistence.api.entity.Any any15 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.anyUser();
        org.apache.syncope.core.provisioning.api.AccountGetter accountGetter16 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.defaultAccountGetter();
        org.apache.syncope.core.provisioning.api.PlainAttrGetter plainAttrGetter17 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.missingPlainAttrGetter();
        org.apache.syncope.core.provisioning.api.MappingManager.IntValues intValues18 = defaultMappingManager1.getIntValues(externalResource2, provision3, item10, intAttrName13, attrSchemaType14, any15, accountGetter16, plainAttrGetter17);
        java.lang.String str19 = plainAttrValue0.getValueAsString(attrSchemaType14);
        plainAttrValue0.setDoubleValue((java.lang.Double) 1.0d);
        java.lang.Double double22 = plainAttrValue0.getDoubleValue();
        org.junit.Assert.assertNotNull(defaultMappingManager1);
        org.junit.Assert.assertNotNull(externalResource2);
        org.junit.Assert.assertNotNull(provision3);
        org.junit.Assert.assertNotNull(mapping6);
        org.junit.Assert.assertNotNull(mapping7);
        org.junit.Assert.assertNotNull(item10);
        org.junit.Assert.assertTrue("'" + boolean12 + "' != '" + false + "'", boolean12 == false);
        org.junit.Assert.assertNotNull(intAttrName13);
        org.junit.Assert.assertTrue("'" + attrSchemaType14 + "' != '" + org.apache.syncope.common.lib.types.AttrSchemaType.String + "'", attrSchemaType14.equals(org.apache.syncope.common.lib.types.AttrSchemaType.String));
        org.junit.Assert.assertNotNull(any15);
        org.junit.Assert.assertNotNull(accountGetter16);
        org.junit.Assert.assertNotNull(plainAttrGetter17);
        org.junit.Assert.assertNotNull(intValues18);
        org.junit.Assert.assertEquals("'" + str19 + "' != '" + "" + "'", str19, "");
        org.junit.Assert.assertTrue("'" + double22 + "' != '" + 1.0d + "'", double22 == 1.0d);
    }

    @Test
    public void test36() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "DefaultMappingManagerRandoopRegressionS00.test36");
        org.apache.syncope.core.persistence.api.entity.PlainAttrValue plainAttrValue0 = new org.apache.syncope.core.persistence.api.entity.PlainAttrValue();
        java.lang.Long long1 = plainAttrValue0.getLongValue();
        org.apache.syncope.core.persistence.api.entity.PlainAttrValue plainAttrValue2 = new org.apache.syncope.core.persistence.api.entity.PlainAttrValue();
        org.apache.syncope.core.provisioning.java.DefaultMappingManager defaultMappingManager3 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.manager();
        org.apache.syncope.core.persistence.api.entity.ExternalResource externalResource4 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.resource();
        org.apache.syncope.common.lib.to.Provision provision5 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.plainAndKeyProvision();
        provision5.setUidOnCreate("mailAlternateAddress");
        org.apache.syncope.common.lib.to.Mapping mapping8 = provision5.getMapping();
        org.apache.syncope.common.lib.to.Mapping mapping9 = provision5.getMapping();
        provision5.setUidOnCreate("USER");
        org.apache.syncope.common.lib.to.Item item12 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.usernameItem();
        boolean boolean14 = item12.equals((java.lang.Object) "name");
        org.apache.syncope.core.provisioning.api.IntAttrName intAttrName15 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.missingIntAttrName();
        org.apache.syncope.common.lib.types.AttrSchemaType attrSchemaType16 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.stringSchemaType();
        org.apache.syncope.core.persistence.api.entity.Any any17 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.anyUser();
        org.apache.syncope.core.provisioning.api.AccountGetter accountGetter18 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.defaultAccountGetter();
        org.apache.syncope.core.provisioning.api.PlainAttrGetter plainAttrGetter19 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.missingPlainAttrGetter();
        org.apache.syncope.core.provisioning.api.MappingManager.IntValues intValues20 = defaultMappingManager3.getIntValues(externalResource4, provision5, item12, intAttrName15, attrSchemaType16, any17, accountGetter18, plainAttrGetter19);
        java.lang.String str21 = plainAttrValue2.getValueAsString(attrSchemaType16);
        java.lang.String str22 = plainAttrValue0.getValueAsString(attrSchemaType16);
        org.junit.Assert.assertNull(long1);
        org.junit.Assert.assertNotNull(defaultMappingManager3);
        org.junit.Assert.assertNotNull(externalResource4);
        org.junit.Assert.assertNotNull(provision5);
        org.junit.Assert.assertNotNull(mapping8);
        org.junit.Assert.assertNotNull(mapping9);
        org.junit.Assert.assertNotNull(item12);
        org.junit.Assert.assertTrue("'" + boolean14 + "' != '" + false + "'", boolean14 == false);
        org.junit.Assert.assertNotNull(intAttrName15);
        org.junit.Assert.assertTrue("'" + attrSchemaType16 + "' != '" + org.apache.syncope.common.lib.types.AttrSchemaType.String + "'", attrSchemaType16.equals(org.apache.syncope.common.lib.types.AttrSchemaType.String));
        org.junit.Assert.assertNotNull(any17);
        org.junit.Assert.assertNotNull(accountGetter18);
        org.junit.Assert.assertNotNull(plainAttrGetter19);
        org.junit.Assert.assertNotNull(intValues20);
        org.junit.Assert.assertEquals("'" + str21 + "' != '" + "" + "'", str21, "");
        org.junit.Assert.assertEquals("'" + str22 + "' != '" + "" + "'", str22, "");
    }

    @Test
    public void test37() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "DefaultMappingManagerRandoopRegressionS00.test37");
        org.apache.syncope.core.provisioning.java.DefaultMappingManager defaultMappingManager0 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.manager();
        org.apache.syncope.common.lib.to.Provision provision1 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.plainAndKeyProvision();
        provision1.setUidOnCreate("mailAlternateAddress");
        org.apache.syncope.common.lib.to.Mapping mapping4 = provision1.getMapping();
        org.apache.syncope.common.lib.to.Provision provision5 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.plainAndKeyProvision();
        provision5.setUidOnCreate("mailAlternateAddress");
        org.apache.syncope.common.lib.to.Mapping mapping8 = provision5.getMapping();
        org.apache.syncope.common.lib.to.Mapping mapping9 = provision5.getMapping();
        provision1.setMapping(mapping9);
        mapping9.setConnObjectLink("mrossi@example.com");
        java.util.Optional<org.apache.syncope.common.lib.to.Item> itemOptional13 = mapping9.getConnObjectKeyItem();
        org.apache.syncope.common.lib.to.Item item14 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.keyItem();
        java.lang.String str15 = item14.getMandatoryCondition();
        java.lang.String str16 = item14.getIntAttrName();
        boolean boolean17 = mapping9.add(item14);
        org.identityconnectors.framework.common.objects.Attribute attribute19 = org.identityconnectors.framework.common.objects.AttributeBuilder.buildPasswordExpirationDate((long) (short) -1);
        org.apache.syncope.common.lib.to.AnyObjectTO anyObjectTO20 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.anyObjectTO();
        java.util.Optional<org.apache.syncope.common.lib.to.MembershipTO> membershipTOOptional22 = anyObjectTO20.getMembership("email");
        defaultMappingManager0.setIntValues(item14, attribute19, (org.apache.syncope.common.lib.to.AnyTO) anyObjectTO20);
        java.lang.String str24 = anyObjectTO20.getRealm();
        org.junit.Assert.assertNotNull(defaultMappingManager0);
        org.junit.Assert.assertNotNull(provision1);
        org.junit.Assert.assertNotNull(mapping4);
        org.junit.Assert.assertNotNull(provision5);
        org.junit.Assert.assertNotNull(mapping8);
        org.junit.Assert.assertNotNull(mapping9);
        org.junit.Assert.assertNotNull(itemOptional13);
        org.junit.Assert.assertNotNull(item14);
        org.junit.Assert.assertEquals("'" + str15 + "' != '" + "false" + "'", str15, "false");
        org.junit.Assert.assertEquals("'" + str16 + "' != '" + "key" + "'", str16, "key");
        org.junit.Assert.assertTrue("'" + boolean17 + "' != '" + true + "'", boolean17 == true);
        org.junit.Assert.assertNotNull(attribute19);
        org.junit.Assert.assertNotNull(anyObjectTO20);
        org.junit.Assert.assertNotNull(membershipTOOptional22);
        org.junit.Assert.assertNull(str24);
    }

    @Test
    public void test38() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "DefaultMappingManagerRandoopRegressionS00.test38");
        org.apache.syncope.core.provisioning.java.DefaultMappingManager defaultMappingManager0 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.manager();
        org.apache.syncope.common.lib.to.Item item1 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.keyItem();
        java.lang.String str2 = item1.getIntAttrName();
        java.util.List<java.lang.String> strList3 = item1.getTransformers();
        org.identityconnectors.framework.common.objects.Attribute attribute5 = org.identityconnectors.framework.common.objects.AttributeBuilder.build("initial-group");
        org.apache.syncope.common.lib.to.Provision provision6 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.plainAndKeyProvision();
        provision6.setUidOnCreate("mailAlternateAddress");
        org.apache.syncope.common.lib.to.Mapping mapping9 = provision6.getMapping();
        org.apache.syncope.common.lib.to.Mapping mapping10 = provision6.getMapping();
        org.apache.syncope.common.lib.to.RealmTO realmTO11 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.realmTO();
        java.util.Optional<org.apache.syncope.common.lib.Attr> attrOptional13 = realmTO11.getPlainAttr("");
        java.util.Set<org.apache.syncope.common.lib.Attr> attrSet14 = realmTO11.getDerAttrs();
        java.util.Set<org.apache.syncope.common.lib.Attr> attrSet15 = realmTO11.getPlainAttrs();
        boolean boolean16 = mapping10.equals((java.lang.Object) realmTO11);
        java.util.Optional<org.apache.syncope.common.lib.Attr> attrOptional18 = realmTO11.getDerAttr("GROUP");
        java.lang.String str19 = realmTO11.getTicketExpirationPolicy();
        defaultMappingManager0.setIntValues(item1, attribute5, realmTO11);
        java.util.List<java.lang.String> strList21 = realmTO11.getAnyTypeClasses();
        java.lang.String str22 = realmTO11.getFullPath();
        realmTO11.setAttrReleasePolicy("Engineering");
        org.junit.Assert.assertNotNull(defaultMappingManager0);
        org.junit.Assert.assertNotNull(item1);
        org.junit.Assert.assertEquals("'" + str2 + "' != '" + "key" + "'", str2, "key");
        org.junit.Assert.assertNotNull(strList3);
        org.junit.Assert.assertNotNull(attribute5);
        org.junit.Assert.assertNotNull(provision6);
        org.junit.Assert.assertNotNull(mapping9);
        org.junit.Assert.assertNotNull(mapping10);
        org.junit.Assert.assertNotNull(realmTO11);
        org.junit.Assert.assertNotNull(attrOptional13);
        org.junit.Assert.assertNotNull(attrSet14);
        org.junit.Assert.assertNotNull(attrSet15);
        org.junit.Assert.assertTrue("'" + boolean16 + "' != '" + false + "'", boolean16 == false);
        org.junit.Assert.assertNotNull(attrOptional18);
        org.junit.Assert.assertNull(str19);
        org.junit.Assert.assertNotNull(strList21);
        org.junit.Assert.assertNull(str22);
    }

    @Test
    public void test39() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "DefaultMappingManagerRandoopRegressionS00.test39");
        org.apache.syncope.core.provisioning.java.DefaultMappingManager defaultMappingManager0 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.manager();
        org.apache.syncope.core.persistence.api.entity.ExternalResource externalResource1 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.resource();
        org.apache.syncope.common.lib.to.Provision provision2 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.plainAndKeyProvision();
        provision2.setUidOnCreate("mailAlternateAddress");
        org.apache.syncope.common.lib.to.Mapping mapping5 = provision2.getMapping();
        org.apache.syncope.common.lib.to.Mapping mapping6 = provision2.getMapping();
        org.apache.syncope.common.lib.to.Item item7 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.optionalMissingPlainItem();
        java.lang.String str8 = item7.toString();
        java.lang.String str9 = item7.toString();
        item7.setPassword(true);
        org.apache.syncope.core.provisioning.api.IntAttrName intAttrName12 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.usernameIntAttrName();
        org.apache.syncope.common.lib.types.AttrSchemaType attrSchemaType13 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.stringSchemaType();
        org.apache.syncope.core.persistence.api.entity.Any any14 = null;
        org.apache.syncope.core.provisioning.api.AccountGetter accountGetter15 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.defaultAccountGetter();
        org.apache.syncope.core.provisioning.api.PlainAttrGetter plainAttrGetter16 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.missingPlainAttrGetter();
        org.apache.syncope.core.provisioning.api.MappingManager.IntValues intValues17 = defaultMappingManager0.getIntValues(externalResource1, provision2, item7, intAttrName12, attrSchemaType13, any14, accountGetter15, plainAttrGetter16);
        provision2.setObjectClass("Software Engineer");
        org.junit.Assert.assertNotNull(defaultMappingManager0);
        org.junit.Assert.assertNotNull(externalResource1);
        org.junit.Assert.assertNotNull(provision2);
        org.junit.Assert.assertNotNull(mapping5);
        org.junit.Assert.assertNotNull(mapping6);
        org.junit.Assert.assertNotNull(item7);
        org.junit.Assert.assertNotNull(intAttrName12);
        org.junit.Assert.assertTrue("'" + attrSchemaType13 + "' != '" + org.apache.syncope.common.lib.types.AttrSchemaType.String + "'", attrSchemaType13.equals(org.apache.syncope.common.lib.types.AttrSchemaType.String));
        org.junit.Assert.assertNotNull(accountGetter15);
        org.junit.Assert.assertNotNull(plainAttrGetter16);
        org.junit.Assert.assertNotNull(intValues17);
    }

    @Test
    public void test40() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "DefaultMappingManagerRandoopRegressionS00.test40");
        org.apache.syncope.core.provisioning.java.DefaultMappingManager defaultMappingManager1 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.manager();
        org.apache.syncope.common.lib.to.Item item2 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.keyItem();
        java.lang.String str3 = item2.getIntAttrName();
        java.util.List<java.lang.String> strList4 = item2.getTransformers();
        org.identityconnectors.framework.common.objects.Attribute attribute6 = org.identityconnectors.framework.common.objects.AttributeBuilder.build("initial-group");
        org.apache.syncope.common.lib.to.Provision provision7 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.plainAndKeyProvision();
        provision7.setUidOnCreate("mailAlternateAddress");
        org.apache.syncope.common.lib.to.Mapping mapping10 = provision7.getMapping();
        org.apache.syncope.common.lib.to.Mapping mapping11 = provision7.getMapping();
        org.apache.syncope.common.lib.to.RealmTO realmTO12 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.realmTO();
        java.util.Optional<org.apache.syncope.common.lib.Attr> attrOptional14 = realmTO12.getPlainAttr("");
        java.util.Set<org.apache.syncope.common.lib.Attr> attrSet15 = realmTO12.getDerAttrs();
        java.util.Set<org.apache.syncope.common.lib.Attr> attrSet16 = realmTO12.getPlainAttrs();
        boolean boolean17 = mapping11.equals((java.lang.Object) realmTO12);
        java.util.Optional<org.apache.syncope.common.lib.Attr> attrOptional19 = realmTO12.getDerAttr("GROUP");
        java.lang.String str20 = realmTO12.getTicketExpirationPolicy();
        defaultMappingManager1.setIntValues(item2, attribute6, realmTO12);
        java.util.List<java.lang.String> strList22 = realmTO12.getResources();
        org.identityconnectors.framework.common.objects.Attribute attribute23 = org.identityconnectors.framework.common.objects.AttributeBuilder.build("ou", (java.util.Collection<java.lang.String>) strList22);
        org.junit.Assert.assertNotNull(defaultMappingManager1);
        org.junit.Assert.assertNotNull(item2);
        org.junit.Assert.assertEquals("'" + str3 + "' != '" + "key" + "'", str3, "key");
        org.junit.Assert.assertNotNull(strList4);
        org.junit.Assert.assertNotNull(attribute6);
        org.junit.Assert.assertNotNull(provision7);
        org.junit.Assert.assertNotNull(mapping10);
        org.junit.Assert.assertNotNull(mapping11);
        org.junit.Assert.assertNotNull(realmTO12);
        org.junit.Assert.assertNotNull(attrOptional14);
        org.junit.Assert.assertNotNull(attrSet15);
        org.junit.Assert.assertNotNull(attrSet16);
        org.junit.Assert.assertTrue("'" + boolean17 + "' != '" + false + "'", boolean17 == false);
        org.junit.Assert.assertNotNull(attrOptional19);
        org.junit.Assert.assertNull(str20);
        org.junit.Assert.assertNotNull(strList22);
        org.junit.Assert.assertNotNull(attribute23);
    }

    @Test
    public void test41() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "DefaultMappingManagerRandoopRegressionS00.test41");
        org.apache.syncope.core.provisioning.java.DefaultMappingManager defaultMappingManager0 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.manager();
        org.apache.syncope.common.lib.to.Item item1 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.keyItem();
        java.lang.String str2 = item1.getIntAttrName();
        java.util.List<java.lang.String> strList3 = item1.getTransformers();
        org.identityconnectors.framework.common.objects.Attribute attribute5 = org.identityconnectors.framework.common.objects.AttributeBuilder.build("initial-group");
        org.apache.syncope.common.lib.to.Provision provision6 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.plainAndKeyProvision();
        provision6.setUidOnCreate("mailAlternateAddress");
        org.apache.syncope.common.lib.to.Mapping mapping9 = provision6.getMapping();
        org.apache.syncope.common.lib.to.Mapping mapping10 = provision6.getMapping();
        org.apache.syncope.common.lib.to.RealmTO realmTO11 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.realmTO();
        java.util.Optional<org.apache.syncope.common.lib.Attr> attrOptional13 = realmTO11.getPlainAttr("");
        java.util.Set<org.apache.syncope.common.lib.Attr> attrSet14 = realmTO11.getDerAttrs();
        java.util.Set<org.apache.syncope.common.lib.Attr> attrSet15 = realmTO11.getPlainAttrs();
        boolean boolean16 = mapping10.equals((java.lang.Object) realmTO11);
        java.util.Optional<org.apache.syncope.common.lib.Attr> attrOptional18 = realmTO11.getDerAttr("GROUP");
        java.lang.String str19 = realmTO11.getTicketExpirationPolicy();
        defaultMappingManager0.setIntValues(item1, attribute5, realmTO11);
        java.lang.String str21 = realmTO11.getFullPath();
        org.junit.Assert.assertNotNull(defaultMappingManager0);
        org.junit.Assert.assertNotNull(item1);
        org.junit.Assert.assertEquals("'" + str2 + "' != '" + "key" + "'", str2, "key");
        org.junit.Assert.assertNotNull(strList3);
        org.junit.Assert.assertNotNull(attribute5);
        org.junit.Assert.assertNotNull(provision6);
        org.junit.Assert.assertNotNull(mapping9);
        org.junit.Assert.assertNotNull(mapping10);
        org.junit.Assert.assertNotNull(realmTO11);
        org.junit.Assert.assertNotNull(attrOptional13);
        org.junit.Assert.assertNotNull(attrSet14);
        org.junit.Assert.assertNotNull(attrSet15);
        org.junit.Assert.assertTrue("'" + boolean16 + "' != '" + false + "'", boolean16 == false);
        org.junit.Assert.assertNotNull(attrOptional18);
        org.junit.Assert.assertNull(str19);
        org.junit.Assert.assertNull(str21);
    }

    @Test
    public void test42() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "DefaultMappingManagerRandoopRegressionS00.test42");
        org.apache.syncope.core.persistence.api.dao.UserDAO userDAO0 = null;
        org.apache.syncope.core.persistence.api.dao.AnyObjectDAO anyObjectDAO1 = null;
        org.apache.syncope.core.persistence.api.dao.GroupDAO groupDAO2 = null;
        org.apache.syncope.core.persistence.api.dao.RelationshipTypeDAO relationshipTypeDAO3 = null;
        org.apache.syncope.core.persistence.api.dao.RealmSearchDAO realmSearchDAO4 = null;
        org.apache.syncope.core.persistence.api.dao.ImplementationDAO implementationDAO5 = null;
        org.apache.syncope.core.provisioning.api.DerAttrHandler derAttrHandler6 = null;
        org.apache.syncope.core.provisioning.api.IntAttrNameParser intAttrNameParser7 = null;
        org.apache.syncope.core.persistence.api.EncryptorManager encryptorManager8 = null;
        org.apache.syncope.core.provisioning.api.jexl.JexlTools jexlTools9 = null;
        org.apache.syncope.core.provisioning.java.DefaultMappingManager defaultMappingManager10 = new org.apache.syncope.core.provisioning.java.DefaultMappingManager(userDAO0, anyObjectDAO1, groupDAO2, relationshipTypeDAO3, realmSearchDAO4, implementationDAO5, derAttrHandler6, intAttrNameParser7, encryptorManager8, jexlTools9);
        org.apache.syncope.core.persistence.api.entity.Any any11 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.anyUser();
        org.apache.syncope.core.persistence.api.entity.ExternalResource externalResource15 = null;
        org.apache.syncope.common.lib.to.Provision provision16 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.plainAndKeyProvision();
        provision16.setUidOnCreate("mailAlternateAddress");
        java.lang.String str19 = provision16.getAnyType();
        org.apache.syncope.core.provisioning.api.MappingManager.PreparedAttrs preparedAttrs20 = defaultMappingManager10.prepareAttrsFromAny(any11, "mario.rossi@example.org", true, (java.lang.Boolean) false, externalResource15, provision16);
        org.apache.syncope.common.lib.to.Mapping mapping21 = provision16.getMapping();
        provision16.setAnyType("aliases");
        org.junit.Assert.assertNotNull(any11);
        org.junit.Assert.assertNotNull(provision16);
        org.junit.Assert.assertNull(str19);
        org.junit.Assert.assertNotNull(preparedAttrs20);
        org.junit.Assert.assertNotNull(mapping21);
    }

    @Test
    public void test43() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "DefaultMappingManagerRandoopRegressionS00.test43");
        org.apache.syncope.core.provisioning.java.DefaultMappingManager defaultMappingManager0 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.manager();
        org.apache.syncope.common.lib.to.Item item1 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.keyItem();
        org.apache.syncope.common.lib.types.MappingPurpose mappingPurpose2 = null;
        item1.setPurpose(mappingPurpose2);
        java.lang.String str4 = item1.getMandatoryCondition();
        item1.setMandatoryCondition("key");
        boolean boolean7 = item1.isPassword();
        boolean boolean8 = item1.isPassword();
        org.identityconnectors.framework.common.objects.Attribute attribute10 = org.identityconnectors.framework.common.objects.AttributeBuilder.buildLastPasswordChangeDate((long) (byte) 0);
        org.apache.syncope.common.lib.to.RealmTO realmTO11 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.realmTO();
        java.util.Optional<org.apache.syncope.common.lib.Attr> attrOptional13 = realmTO11.getPlainAttr("");
        java.util.Set<org.apache.syncope.common.lib.Attr> attrSet14 = realmTO11.getDerAttrs();
        java.util.Set<org.apache.syncope.common.lib.Attr> attrSet15 = realmTO11.getPlainAttrs();
        realmTO11.setName("username");
        realmTO11.setName("realm-key");
        java.util.Set<org.apache.syncope.common.lib.Attr> attrSet20 = realmTO11.getPlainAttrs();
        defaultMappingManager0.setIntValues(item1, attribute10, realmTO11);
        org.junit.Assert.assertNotNull(defaultMappingManager0);
        org.junit.Assert.assertNotNull(item1);
        org.junit.Assert.assertEquals("'" + str4 + "' != '" + "false" + "'", str4, "false");
        org.junit.Assert.assertTrue("'" + boolean7 + "' != '" + false + "'", boolean7 == false);
        org.junit.Assert.assertTrue("'" + boolean8 + "' != '" + false + "'", boolean8 == false);
        org.junit.Assert.assertNotNull(attribute10);
        org.junit.Assert.assertNotNull(realmTO11);
        org.junit.Assert.assertNotNull(attrOptional13);
        org.junit.Assert.assertNotNull(attrSet14);
        org.junit.Assert.assertNotNull(attrSet15);
        org.junit.Assert.assertNotNull(attrSet20);
    }

    @Test
    public void test44() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "DefaultMappingManagerRandoopRegressionS00.test44");
        org.apache.syncope.core.provisioning.java.DefaultMappingManager defaultMappingManager0 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.manager();
        org.apache.syncope.common.lib.to.Item item1 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.keyItem();
        java.lang.String str2 = item1.getIntAttrName();
        java.util.List<java.lang.String> strList3 = item1.getTransformers();
        org.identityconnectors.framework.common.objects.Attribute attribute5 = org.identityconnectors.framework.common.objects.AttributeBuilder.build("initial-group");
        org.apache.syncope.common.lib.to.Provision provision6 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.plainAndKeyProvision();
        provision6.setUidOnCreate("mailAlternateAddress");
        org.apache.syncope.common.lib.to.Mapping mapping9 = provision6.getMapping();
        org.apache.syncope.common.lib.to.Mapping mapping10 = provision6.getMapping();
        org.apache.syncope.common.lib.to.RealmTO realmTO11 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.realmTO();
        java.util.Optional<org.apache.syncope.common.lib.Attr> attrOptional13 = realmTO11.getPlainAttr("");
        java.util.Set<org.apache.syncope.common.lib.Attr> attrSet14 = realmTO11.getDerAttrs();
        java.util.Set<org.apache.syncope.common.lib.Attr> attrSet15 = realmTO11.getPlainAttrs();
        boolean boolean16 = mapping10.equals((java.lang.Object) realmTO11);
        java.util.Optional<org.apache.syncope.common.lib.Attr> attrOptional18 = realmTO11.getDerAttr("GROUP");
        java.lang.String str19 = realmTO11.getTicketExpirationPolicy();
        defaultMappingManager0.setIntValues(item1, attribute5, realmTO11);
        java.util.List<java.lang.String> strList21 = item1.getTransformers();
        java.lang.String str22 = item1.getExtAttrName();
        java.lang.String str23 = item1.toString();
        org.junit.Assert.assertNotNull(defaultMappingManager0);
        org.junit.Assert.assertNotNull(item1);
        org.junit.Assert.assertEquals("'" + str2 + "' != '" + "key" + "'", str2, "key");
        org.junit.Assert.assertNotNull(strList3);
        org.junit.Assert.assertNotNull(attribute5);
        org.junit.Assert.assertNotNull(provision6);
        org.junit.Assert.assertNotNull(mapping9);
        org.junit.Assert.assertNotNull(mapping10);
        org.junit.Assert.assertNotNull(realmTO11);
        org.junit.Assert.assertNotNull(attrOptional13);
        org.junit.Assert.assertNotNull(attrSet14);
        org.junit.Assert.assertNotNull(attrSet15);
        org.junit.Assert.assertTrue("'" + boolean16 + "' != '" + false + "'", boolean16 == false);
        org.junit.Assert.assertNotNull(attrOptional18);
        org.junit.Assert.assertNull(str19);
        org.junit.Assert.assertNotNull(strList21);
        org.junit.Assert.assertEquals("'" + str22 + "' != '" + "uid" + "'", str22, "uid");
    }

    @Test
    public void test45() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "DefaultMappingManagerRandoopRegressionS00.test45");
        org.apache.syncope.core.provisioning.java.DefaultMappingManager defaultMappingManager0 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.manager();
        org.apache.syncope.common.lib.to.Item item1 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.keyItem();
        java.lang.String str2 = item1.getIntAttrName();
        java.util.List<java.lang.String> strList3 = item1.getTransformers();
        org.identityconnectors.framework.common.objects.Attribute attribute5 = org.identityconnectors.framework.common.objects.AttributeBuilder.build("initial-group");
        org.apache.syncope.common.lib.to.Provision provision6 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.plainAndKeyProvision();
        provision6.setUidOnCreate("mailAlternateAddress");
        org.apache.syncope.common.lib.to.Mapping mapping9 = provision6.getMapping();
        org.apache.syncope.common.lib.to.Mapping mapping10 = provision6.getMapping();
        org.apache.syncope.common.lib.to.RealmTO realmTO11 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.realmTO();
        java.util.Optional<org.apache.syncope.common.lib.Attr> attrOptional13 = realmTO11.getPlainAttr("");
        java.util.Set<org.apache.syncope.common.lib.Attr> attrSet14 = realmTO11.getDerAttrs();
        java.util.Set<org.apache.syncope.common.lib.Attr> attrSet15 = realmTO11.getPlainAttrs();
        boolean boolean16 = mapping10.equals((java.lang.Object) realmTO11);
        java.util.Optional<org.apache.syncope.common.lib.Attr> attrOptional18 = realmTO11.getDerAttr("GROUP");
        java.lang.String str19 = realmTO11.getTicketExpirationPolicy();
        defaultMappingManager0.setIntValues(item1, attribute5, realmTO11);
        java.util.List<java.lang.String> strList21 = realmTO11.getAnyTypeClasses();
        realmTO11.setAttrReleasePolicy("secret");
        org.junit.Assert.assertNotNull(defaultMappingManager0);
        org.junit.Assert.assertNotNull(item1);
        org.junit.Assert.assertEquals("'" + str2 + "' != '" + "key" + "'", str2, "key");
        org.junit.Assert.assertNotNull(strList3);
        org.junit.Assert.assertNotNull(attribute5);
        org.junit.Assert.assertNotNull(provision6);
        org.junit.Assert.assertNotNull(mapping9);
        org.junit.Assert.assertNotNull(mapping10);
        org.junit.Assert.assertNotNull(realmTO11);
        org.junit.Assert.assertNotNull(attrOptional13);
        org.junit.Assert.assertNotNull(attrSet14);
        org.junit.Assert.assertNotNull(attrSet15);
        org.junit.Assert.assertTrue("'" + boolean16 + "' != '" + false + "'", boolean16 == false);
        org.junit.Assert.assertNotNull(attrOptional18);
        org.junit.Assert.assertNull(str19);
        org.junit.Assert.assertNotNull(strList21);
    }

    @Test
    public void test46() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "DefaultMappingManagerRandoopRegressionS00.test46");
        org.apache.syncope.core.provisioning.java.DefaultMappingManager defaultMappingManager0 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.manager();
        org.apache.syncope.common.lib.to.Provision provision1 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.plainAndKeyProvision();
        provision1.setUidOnCreate("mailAlternateAddress");
        org.apache.syncope.common.lib.to.Mapping mapping4 = provision1.getMapping();
        org.apache.syncope.common.lib.to.Provision provision5 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.plainAndKeyProvision();
        provision5.setUidOnCreate("mailAlternateAddress");
        org.apache.syncope.common.lib.to.Mapping mapping8 = provision5.getMapping();
        org.apache.syncope.common.lib.to.Mapping mapping9 = provision5.getMapping();
        provision1.setMapping(mapping9);
        mapping9.setConnObjectLink("mrossi@example.com");
        java.util.Optional<org.apache.syncope.common.lib.to.Item> itemOptional13 = mapping9.getConnObjectKeyItem();
        org.apache.syncope.common.lib.to.Item item14 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.keyItem();
        java.lang.String str15 = item14.getMandatoryCondition();
        java.lang.String str16 = item14.getIntAttrName();
        boolean boolean17 = mapping9.add(item14);
        org.identityconnectors.framework.common.objects.Attribute attribute19 = org.identityconnectors.framework.common.objects.AttributeBuilder.buildPasswordExpirationDate((long) (short) -1);
        org.apache.syncope.common.lib.to.AnyObjectTO anyObjectTO20 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.anyObjectTO();
        java.util.Optional<org.apache.syncope.common.lib.to.MembershipTO> membershipTOOptional22 = anyObjectTO20.getMembership("email");
        defaultMappingManager0.setIntValues(item14, attribute19, (org.apache.syncope.common.lib.to.AnyTO) anyObjectTO20);
        java.util.List<java.lang.String> strList24 = item14.getTransformers();
        org.junit.Assert.assertNotNull(defaultMappingManager0);
        org.junit.Assert.assertNotNull(provision1);
        org.junit.Assert.assertNotNull(mapping4);
        org.junit.Assert.assertNotNull(provision5);
        org.junit.Assert.assertNotNull(mapping8);
        org.junit.Assert.assertNotNull(mapping9);
        org.junit.Assert.assertNotNull(itemOptional13);
        org.junit.Assert.assertNotNull(item14);
        org.junit.Assert.assertEquals("'" + str15 + "' != '" + "false" + "'", str15, "false");
        org.junit.Assert.assertEquals("'" + str16 + "' != '" + "key" + "'", str16, "key");
        org.junit.Assert.assertTrue("'" + boolean17 + "' != '" + true + "'", boolean17 == true);
        org.junit.Assert.assertNotNull(attribute19);
        org.junit.Assert.assertNotNull(anyObjectTO20);
        org.junit.Assert.assertNotNull(membershipTOOptional22);
        org.junit.Assert.assertNotNull(strList24);
    }

    @Test
    public void test47() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "DefaultMappingManagerRandoopRegressionS00.test47");
        org.apache.syncope.core.provisioning.java.DefaultMappingManager defaultMappingManager0 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.manager();
        org.apache.syncope.common.lib.to.Provision provision1 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.plainAndKeyProvision();
        provision1.setUidOnCreate("mailAlternateAddress");
        org.apache.syncope.common.lib.to.Mapping mapping4 = provision1.getMapping();
        org.apache.syncope.common.lib.to.Provision provision5 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.plainAndKeyProvision();
        provision5.setUidOnCreate("mailAlternateAddress");
        org.apache.syncope.common.lib.to.Mapping mapping8 = provision5.getMapping();
        org.apache.syncope.common.lib.to.Mapping mapping9 = provision5.getMapping();
        provision1.setMapping(mapping9);
        mapping9.setConnObjectLink("mrossi@example.com");
        java.util.Optional<org.apache.syncope.common.lib.to.Item> itemOptional13 = mapping9.getConnObjectKeyItem();
        org.apache.syncope.common.lib.to.Item item14 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.keyItem();
        java.lang.String str15 = item14.getMandatoryCondition();
        java.lang.String str16 = item14.getIntAttrName();
        boolean boolean17 = mapping9.add(item14);
        org.identityconnectors.framework.common.objects.Attribute attribute19 = org.identityconnectors.framework.common.objects.AttributeBuilder.buildPasswordExpirationDate((long) (short) -1);
        org.apache.syncope.common.lib.to.AnyObjectTO anyObjectTO20 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.anyObjectTO();
        java.util.Optional<org.apache.syncope.common.lib.to.MembershipTO> membershipTOOptional22 = anyObjectTO20.getMembership("email");
        defaultMappingManager0.setIntValues(item14, attribute19, (org.apache.syncope.common.lib.to.AnyTO) anyObjectTO20);
        java.lang.String str24 = item14.toString();
        org.junit.Assert.assertNotNull(defaultMappingManager0);
        org.junit.Assert.assertNotNull(provision1);
        org.junit.Assert.assertNotNull(mapping4);
        org.junit.Assert.assertNotNull(provision5);
        org.junit.Assert.assertNotNull(mapping8);
        org.junit.Assert.assertNotNull(mapping9);
        org.junit.Assert.assertNotNull(itemOptional13);
        org.junit.Assert.assertNotNull(item14);
        org.junit.Assert.assertEquals("'" + str15 + "' != '" + "false" + "'", str15, "false");
        org.junit.Assert.assertEquals("'" + str16 + "' != '" + "key" + "'", str16, "key");
        org.junit.Assert.assertTrue("'" + boolean17 + "' != '" + true + "'", boolean17 == true);
        org.junit.Assert.assertNotNull(attribute19);
        org.junit.Assert.assertNotNull(anyObjectTO20);
        org.junit.Assert.assertNotNull(membershipTOOptional22);
    }

    @Test
    public void test48() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "DefaultMappingManagerRandoopRegressionS00.test48");
        org.apache.syncope.core.provisioning.java.DefaultMappingManager defaultMappingManager0 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.manager();
        org.apache.syncope.core.persistence.api.entity.ExternalResource externalResource1 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.resource();
        org.apache.syncope.common.lib.to.Provision provision2 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.plainAndKeyProvision();
        provision2.setUidOnCreate("mailAlternateAddress");
        org.apache.syncope.common.lib.to.Mapping mapping5 = provision2.getMapping();
        org.apache.syncope.common.lib.to.Mapping mapping6 = provision2.getMapping();
        provision2.setUidOnCreate("USER");
        org.apache.syncope.common.lib.to.Item item9 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.usernameItem();
        boolean boolean11 = item9.equals((java.lang.Object) "name");
        org.apache.syncope.core.provisioning.api.IntAttrName intAttrName12 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.missingIntAttrName();
        org.apache.syncope.common.lib.types.AttrSchemaType attrSchemaType13 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.stringSchemaType();
        org.apache.syncope.core.persistence.api.entity.Any any14 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.anyUser();
        org.apache.syncope.core.provisioning.api.AccountGetter accountGetter15 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.defaultAccountGetter();
        org.apache.syncope.core.provisioning.api.PlainAttrGetter plainAttrGetter16 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.missingPlainAttrGetter();
        org.apache.syncope.core.provisioning.api.MappingManager.IntValues intValues17 = defaultMappingManager0.getIntValues(externalResource1, provision2, item9, intAttrName12, attrSchemaType13, any14, accountGetter15, plainAttrGetter16);
        java.lang.String str18 = provision2.getUidOnCreate();
        provision2.setIgnoreCaseMatch(false);
        java.lang.String str21 = provision2.getSyncToken();
        org.junit.Assert.assertNotNull(defaultMappingManager0);
        org.junit.Assert.assertNotNull(externalResource1);
        org.junit.Assert.assertNotNull(provision2);
        org.junit.Assert.assertNotNull(mapping5);
        org.junit.Assert.assertNotNull(mapping6);
        org.junit.Assert.assertNotNull(item9);
        org.junit.Assert.assertTrue("'" + boolean11 + "' != '" + false + "'", boolean11 == false);
        org.junit.Assert.assertNotNull(intAttrName12);
        org.junit.Assert.assertTrue("'" + attrSchemaType13 + "' != '" + org.apache.syncope.common.lib.types.AttrSchemaType.String + "'", attrSchemaType13.equals(org.apache.syncope.common.lib.types.AttrSchemaType.String));
        org.junit.Assert.assertNotNull(any14);
        org.junit.Assert.assertNotNull(accountGetter15);
        org.junit.Assert.assertNotNull(plainAttrGetter16);
        org.junit.Assert.assertNotNull(intValues17);
        org.junit.Assert.assertEquals("'" + str18 + "' != '" + "USER" + "'", str18, "USER");
        org.junit.Assert.assertNull(str21);
    }

    @Test
    public void test49() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "DefaultMappingManagerRandoopRegressionS00.test49");
        org.apache.syncope.core.provisioning.java.DefaultMappingManager defaultMappingManager0 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.manager();
        org.apache.syncope.core.persistence.api.entity.ExternalResource externalResource1 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.resource();
        org.apache.syncope.common.lib.to.Provision provision2 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.plainAndKeyProvision();
        provision2.setUidOnCreate("mailAlternateAddress");
        org.apache.syncope.common.lib.to.Mapping mapping5 = provision2.getMapping();
        org.apache.syncope.common.lib.to.Mapping mapping6 = provision2.getMapping();
        provision2.setUidOnCreate("USER");
        org.apache.syncope.common.lib.to.Item item9 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.usernameItem();
        boolean boolean11 = item9.equals((java.lang.Object) "name");
        org.apache.syncope.core.provisioning.api.IntAttrName intAttrName12 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.missingIntAttrName();
        org.apache.syncope.common.lib.types.AttrSchemaType attrSchemaType13 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.stringSchemaType();
        org.apache.syncope.core.persistence.api.entity.Any any14 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.anyUser();
        org.apache.syncope.core.provisioning.api.AccountGetter accountGetter15 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.defaultAccountGetter();
        org.apache.syncope.core.provisioning.api.PlainAttrGetter plainAttrGetter16 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.missingPlainAttrGetter();
        org.apache.syncope.core.provisioning.api.MappingManager.IntValues intValues17 = defaultMappingManager0.getIntValues(externalResource1, provision2, item9, intAttrName12, attrSchemaType13, any14, accountGetter15, plainAttrGetter16);
        java.lang.String str18 = item9.getPropagationJEXLTransformer();
        java.util.List<java.lang.String> strList19 = item9.getTransformers();
        java.lang.String str20 = item9.getMandatoryCondition();
        org.junit.Assert.assertNotNull(defaultMappingManager0);
        org.junit.Assert.assertNotNull(externalResource1);
        org.junit.Assert.assertNotNull(provision2);
        org.junit.Assert.assertNotNull(mapping5);
        org.junit.Assert.assertNotNull(mapping6);
        org.junit.Assert.assertNotNull(item9);
        org.junit.Assert.assertTrue("'" + boolean11 + "' != '" + false + "'", boolean11 == false);
        org.junit.Assert.assertNotNull(intAttrName12);
        org.junit.Assert.assertTrue("'" + attrSchemaType13 + "' != '" + org.apache.syncope.common.lib.types.AttrSchemaType.String + "'", attrSchemaType13.equals(org.apache.syncope.common.lib.types.AttrSchemaType.String));
        org.junit.Assert.assertNotNull(any14);
        org.junit.Assert.assertNotNull(accountGetter15);
        org.junit.Assert.assertNotNull(plainAttrGetter16);
        org.junit.Assert.assertNotNull(intValues17);
        org.junit.Assert.assertNull(str18);
        org.junit.Assert.assertNotNull(strList19);
        org.junit.Assert.assertEquals("'" + str20 + "' != '" + "false" + "'", str20, "false");
    }

    @Test
    public void test50() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "DefaultMappingManagerRandoopRegressionS00.test50");
        org.apache.syncope.core.persistence.api.entity.PlainAttrValue plainAttrValue0 = new org.apache.syncope.core.persistence.api.entity.PlainAttrValue();
        plainAttrValue0.setLongValue((java.lang.Long) (-1L));
        java.time.OffsetDateTime offsetDateTime3 = null;
        plainAttrValue0.setDateValue(offsetDateTime3);
        org.apache.syncope.core.provisioning.java.DefaultMappingManager defaultMappingManager5 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.manager();
        org.apache.syncope.core.persistence.api.entity.ExternalResource externalResource6 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.resource();
        org.apache.syncope.common.lib.to.Provision provision7 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.plainAndKeyProvision();
        provision7.setUidOnCreate("mailAlternateAddress");
        org.apache.syncope.common.lib.to.Mapping mapping10 = provision7.getMapping();
        org.apache.syncope.common.lib.to.Mapping mapping11 = provision7.getMapping();
        provision7.setUidOnCreate("USER");
        org.apache.syncope.common.lib.to.Item item14 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.usernameItem();
        boolean boolean16 = item14.equals((java.lang.Object) "name");
        org.apache.syncope.core.provisioning.api.IntAttrName intAttrName17 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.missingIntAttrName();
        org.apache.syncope.common.lib.types.AttrSchemaType attrSchemaType18 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.stringSchemaType();
        org.apache.syncope.core.persistence.api.entity.Any any19 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.anyUser();
        org.apache.syncope.core.provisioning.api.AccountGetter accountGetter20 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.defaultAccountGetter();
        org.apache.syncope.core.provisioning.api.PlainAttrGetter plainAttrGetter21 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.missingPlainAttrGetter();
        org.apache.syncope.core.provisioning.api.MappingManager.IntValues intValues22 = defaultMappingManager5.getIntValues(externalResource6, provision7, item14, intAttrName17, attrSchemaType18, any19, accountGetter20, plainAttrGetter21);
        java.lang.String str23 = plainAttrValue0.getValueAsString(attrSchemaType18);
        org.junit.Assert.assertNotNull(defaultMappingManager5);
        org.junit.Assert.assertNotNull(externalResource6);
        org.junit.Assert.assertNotNull(provision7);
        org.junit.Assert.assertNotNull(mapping10);
        org.junit.Assert.assertNotNull(mapping11);
        org.junit.Assert.assertNotNull(item14);
        org.junit.Assert.assertTrue("'" + boolean16 + "' != '" + false + "'", boolean16 == false);
        org.junit.Assert.assertNotNull(intAttrName17);
        org.junit.Assert.assertTrue("'" + attrSchemaType18 + "' != '" + org.apache.syncope.common.lib.types.AttrSchemaType.String + "'", attrSchemaType18.equals(org.apache.syncope.common.lib.types.AttrSchemaType.String));
        org.junit.Assert.assertNotNull(any19);
        org.junit.Assert.assertNotNull(accountGetter20);
        org.junit.Assert.assertNotNull(plainAttrGetter21);
        org.junit.Assert.assertNotNull(intValues22);
        org.junit.Assert.assertEquals("'" + str23 + "' != '" + "-1" + "'", str23, "-1");
    }

    @Test
    public void test51() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "DefaultMappingManagerRandoopRegressionS00.test51");
        org.apache.syncope.core.provisioning.java.DefaultMappingManager defaultMappingManager0 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.manager();
        org.apache.syncope.core.persistence.api.entity.ExternalResource externalResource1 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.resource();
        org.apache.syncope.common.lib.to.Provision provision2 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.plainAndKeyProvision();
        provision2.setUidOnCreate("mailAlternateAddress");
        org.apache.syncope.common.lib.to.Mapping mapping5 = provision2.getMapping();
        org.apache.syncope.common.lib.to.Mapping mapping6 = provision2.getMapping();
        org.apache.syncope.common.lib.to.Item item7 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.optionalMissingPlainItem();
        java.lang.String str8 = item7.toString();
        java.lang.String str9 = item7.toString();
        item7.setPassword(true);
        org.apache.syncope.core.provisioning.api.IntAttrName intAttrName12 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.usernameIntAttrName();
        org.apache.syncope.common.lib.types.AttrSchemaType attrSchemaType13 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.stringSchemaType();
        org.apache.syncope.core.persistence.api.entity.Any any14 = null;
        org.apache.syncope.core.provisioning.api.AccountGetter accountGetter15 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.defaultAccountGetter();
        org.apache.syncope.core.provisioning.api.PlainAttrGetter plainAttrGetter16 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.missingPlainAttrGetter();
        org.apache.syncope.core.provisioning.api.MappingManager.IntValues intValues17 = defaultMappingManager0.getIntValues(externalResource1, provision2, item7, intAttrName12, attrSchemaType13, any14, accountGetter15, plainAttrGetter16);
        java.lang.String str18 = provision2.getSyncToken();
        provision2.setIgnoreCaseMatch(true);
        java.util.List<java.lang.String> strList21 = provision2.getAuxClasses();
        org.apache.syncope.common.lib.to.Mapping mapping22 = null;
        provision2.setMapping(mapping22);
        org.junit.Assert.assertNotNull(defaultMappingManager0);
        org.junit.Assert.assertNotNull(externalResource1);
        org.junit.Assert.assertNotNull(provision2);
        org.junit.Assert.assertNotNull(mapping5);
        org.junit.Assert.assertNotNull(mapping6);
        org.junit.Assert.assertNotNull(item7);
        org.junit.Assert.assertNotNull(intAttrName12);
        org.junit.Assert.assertTrue("'" + attrSchemaType13 + "' != '" + org.apache.syncope.common.lib.types.AttrSchemaType.String + "'", attrSchemaType13.equals(org.apache.syncope.common.lib.types.AttrSchemaType.String));
        org.junit.Assert.assertNotNull(accountGetter15);
        org.junit.Assert.assertNotNull(plainAttrGetter16);
        org.junit.Assert.assertNotNull(intValues17);
        org.junit.Assert.assertNull(str18);
        org.junit.Assert.assertNotNull(strList21);
    }

    @Test
    public void test52() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "DefaultMappingManagerRandoopRegressionS00.test52");
        org.apache.syncope.core.provisioning.java.DefaultMappingManager defaultMappingManager0 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.manager();
        org.apache.syncope.core.persistence.api.entity.ExternalResource externalResource1 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.resource();
        org.apache.syncope.common.lib.to.Provision provision2 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.plainAndKeyProvision();
        provision2.setUidOnCreate("mailAlternateAddress");
        org.apache.syncope.common.lib.to.Mapping mapping5 = provision2.getMapping();
        org.apache.syncope.common.lib.to.Mapping mapping6 = provision2.getMapping();
        org.apache.syncope.common.lib.to.Item item7 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.optionalMissingPlainItem();
        java.lang.String str8 = item7.toString();
        java.lang.String str9 = item7.toString();
        item7.setPassword(true);
        org.apache.syncope.core.provisioning.api.IntAttrName intAttrName12 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.usernameIntAttrName();
        org.apache.syncope.common.lib.types.AttrSchemaType attrSchemaType13 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.stringSchemaType();
        org.apache.syncope.core.persistence.api.entity.Any any14 = null;
        org.apache.syncope.core.provisioning.api.AccountGetter accountGetter15 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.defaultAccountGetter();
        org.apache.syncope.core.provisioning.api.PlainAttrGetter plainAttrGetter16 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.missingPlainAttrGetter();
        org.apache.syncope.core.provisioning.api.MappingManager.IntValues intValues17 = defaultMappingManager0.getIntValues(externalResource1, provision2, item7, intAttrName12, attrSchemaType13, any14, accountGetter15, plainAttrGetter16);
        provision2.setIgnoreCaseMatch(true);
        provision2.setSyncToken("Software Engineer");
        org.junit.Assert.assertNotNull(defaultMappingManager0);
        org.junit.Assert.assertNotNull(externalResource1);
        org.junit.Assert.assertNotNull(provision2);
        org.junit.Assert.assertNotNull(mapping5);
        org.junit.Assert.assertNotNull(mapping6);
        org.junit.Assert.assertNotNull(item7);
        org.junit.Assert.assertNotNull(intAttrName12);
        org.junit.Assert.assertTrue("'" + attrSchemaType13 + "' != '" + org.apache.syncope.common.lib.types.AttrSchemaType.String + "'", attrSchemaType13.equals(org.apache.syncope.common.lib.types.AttrSchemaType.String));
        org.junit.Assert.assertNotNull(accountGetter15);
        org.junit.Assert.assertNotNull(plainAttrGetter16);
        org.junit.Assert.assertNotNull(intValues17);
    }

    @Test
    public void test53() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "DefaultMappingManagerRandoopRegressionS00.test53");
        org.apache.syncope.core.provisioning.java.DefaultMappingManager defaultMappingManager0 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.manager();
        org.apache.syncope.core.persistence.api.entity.ExternalResource externalResource1 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.resource();
        org.apache.syncope.common.lib.to.Provision provision2 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.plainAndKeyProvision();
        provision2.setUidOnCreate("mailAlternateAddress");
        org.apache.syncope.common.lib.to.Mapping mapping5 = provision2.getMapping();
        org.apache.syncope.common.lib.to.Mapping mapping6 = provision2.getMapping();
        provision2.setUidOnCreate("USER");
        org.apache.syncope.common.lib.to.Item item9 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.usernameItem();
        boolean boolean11 = item9.equals((java.lang.Object) "name");
        org.apache.syncope.core.provisioning.api.IntAttrName intAttrName12 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.missingIntAttrName();
        org.apache.syncope.common.lib.types.AttrSchemaType attrSchemaType13 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.stringSchemaType();
        org.apache.syncope.core.persistence.api.entity.Any any14 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.anyUser();
        org.apache.syncope.core.provisioning.api.AccountGetter accountGetter15 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.defaultAccountGetter();
        org.apache.syncope.core.provisioning.api.PlainAttrGetter plainAttrGetter16 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.missingPlainAttrGetter();
        org.apache.syncope.core.provisioning.api.MappingManager.IntValues intValues17 = defaultMappingManager0.getIntValues(externalResource1, provision2, item9, intAttrName12, attrSchemaType13, any14, accountGetter15, plainAttrGetter16);
        java.lang.String str18 = item9.getPropagationJEXLTransformer();
        item9.setPullJEXLTransformer("/parent/child");
        org.apache.syncope.common.lib.types.MappingPurpose mappingPurpose21 = item9.getPurpose();
        org.junit.Assert.assertNotNull(defaultMappingManager0);
        org.junit.Assert.assertNotNull(externalResource1);
        org.junit.Assert.assertNotNull(provision2);
        org.junit.Assert.assertNotNull(mapping5);
        org.junit.Assert.assertNotNull(mapping6);
        org.junit.Assert.assertNotNull(item9);
        org.junit.Assert.assertTrue("'" + boolean11 + "' != '" + false + "'", boolean11 == false);
        org.junit.Assert.assertNotNull(intAttrName12);
        org.junit.Assert.assertTrue("'" + attrSchemaType13 + "' != '" + org.apache.syncope.common.lib.types.AttrSchemaType.String + "'", attrSchemaType13.equals(org.apache.syncope.common.lib.types.AttrSchemaType.String));
        org.junit.Assert.assertNotNull(any14);
        org.junit.Assert.assertNotNull(accountGetter15);
        org.junit.Assert.assertNotNull(plainAttrGetter16);
        org.junit.Assert.assertNotNull(intValues17);
        org.junit.Assert.assertNull(str18);
        org.junit.Assert.assertTrue("'" + mappingPurpose21 + "' != '" + org.apache.syncope.common.lib.types.MappingPurpose.PROPAGATION + "'", mappingPurpose21.equals(org.apache.syncope.common.lib.types.MappingPurpose.PROPAGATION));
    }

    @Test
    public void test54() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "DefaultMappingManagerRandoopRegressionS00.test54");
        org.apache.syncope.core.persistence.api.entity.PlainAttrValue plainAttrValue0 = new org.apache.syncope.core.persistence.api.entity.PlainAttrValue();
        java.lang.String str1 = plainAttrValue0.getStringValue();
        org.apache.syncope.core.persistence.api.entity.PlainAttr plainAttr2 = plainAttrValue0.getAttr();
        org.apache.syncope.core.provisioning.java.DefaultMappingManager defaultMappingManager3 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.manager();
        org.apache.syncope.core.persistence.api.entity.ExternalResource externalResource4 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.resource();
        org.apache.syncope.common.lib.to.Provision provision5 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.plainAndKeyProvision();
        provision5.setUidOnCreate("mailAlternateAddress");
        org.apache.syncope.common.lib.to.Mapping mapping8 = provision5.getMapping();
        org.apache.syncope.common.lib.to.Mapping mapping9 = provision5.getMapping();
        provision5.setUidOnCreate("USER");
        org.apache.syncope.common.lib.to.Item item12 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.usernameItem();
        boolean boolean14 = item12.equals((java.lang.Object) "name");
        org.apache.syncope.core.provisioning.api.IntAttrName intAttrName15 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.missingIntAttrName();
        org.apache.syncope.common.lib.types.AttrSchemaType attrSchemaType16 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.stringSchemaType();
        org.apache.syncope.core.persistence.api.entity.Any any17 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.anyUser();
        org.apache.syncope.core.provisioning.api.AccountGetter accountGetter18 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.defaultAccountGetter();
        org.apache.syncope.core.provisioning.api.PlainAttrGetter plainAttrGetter19 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.missingPlainAttrGetter();
        org.apache.syncope.core.provisioning.api.MappingManager.IntValues intValues20 = defaultMappingManager3.getIntValues(externalResource4, provision5, item12, intAttrName15, attrSchemaType16, any17, accountGetter18, plainAttrGetter19);
        java.lang.String str21 = plainAttrValue0.getValueAsString(attrSchemaType16);
        org.apache.syncope.core.persistence.api.entity.PlainSchema plainSchema22 = null;
        // The following exception was thrown during execution in test generation
        try {
            plainAttrValue0.parseValue(plainSchema22, "-1");
            org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException; message: Cannot invoke \"org.apache.syncope.core.persistence.api.entity.PlainSchema.getType()\" because \"schema\" is null");
        } catch (java.lang.NullPointerException e) {
            // Expected exception.
        }
        org.junit.Assert.assertEquals("'" + str1 + "' != '" + "" + "'", str1, "");
        org.junit.Assert.assertNull(plainAttr2);
        org.junit.Assert.assertNotNull(defaultMappingManager3);
        org.junit.Assert.assertNotNull(externalResource4);
        org.junit.Assert.assertNotNull(provision5);
        org.junit.Assert.assertNotNull(mapping8);
        org.junit.Assert.assertNotNull(mapping9);
        org.junit.Assert.assertNotNull(item12);
        org.junit.Assert.assertTrue("'" + boolean14 + "' != '" + false + "'", boolean14 == false);
        org.junit.Assert.assertNotNull(intAttrName15);
        org.junit.Assert.assertTrue("'" + attrSchemaType16 + "' != '" + org.apache.syncope.common.lib.types.AttrSchemaType.String + "'", attrSchemaType16.equals(org.apache.syncope.common.lib.types.AttrSchemaType.String));
        org.junit.Assert.assertNotNull(any17);
        org.junit.Assert.assertNotNull(accountGetter18);
        org.junit.Assert.assertNotNull(plainAttrGetter19);
        org.junit.Assert.assertNotNull(intValues20);
        org.junit.Assert.assertEquals("'" + str21 + "' != '" + "" + "'", str21, "");
    }

    @Test
    public void test55() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "DefaultMappingManagerRandoopRegressionS00.test55");
        org.apache.syncope.core.provisioning.java.DefaultMappingManager defaultMappingManager0 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.manager();
        org.apache.syncope.common.lib.to.Item item1 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.optionalMissingPlainItem();
        java.lang.String str2 = item1.toString();
        org.identityconnectors.framework.common.objects.Attribute attribute4 = org.identityconnectors.framework.common.objects.AttributeBuilder.buildEnableDate((long) (short) 100);
        org.apache.syncope.common.lib.to.AnyObjectTO anyObjectTO5 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.anyObjectTO();
        java.util.Optional<org.apache.syncope.common.lib.to.MembershipTO> membershipTOOptional7 = anyObjectTO5.getMembership("email");
        java.lang.String str8 = anyObjectTO5.getDiscriminator();
        java.lang.String str9 = anyObjectTO5.getDiscriminator();
        defaultMappingManager0.setIntValues(item1, attribute4, (org.apache.syncope.common.lib.to.AnyTO) anyObjectTO5);
        java.lang.String str11 = anyObjectTO5.getDiscriminator();
        java.util.List<org.apache.syncope.common.lib.to.MembershipTO> membershipTOList12 = anyObjectTO5.getMemberships();
        org.junit.Assert.assertNotNull(defaultMappingManager0);
        org.junit.Assert.assertNotNull(item1);
        org.junit.Assert.assertNotNull(attribute4);
        org.junit.Assert.assertNotNull(anyObjectTO5);
        org.junit.Assert.assertNotNull(membershipTOOptional7);
        org.junit.Assert.assertEquals("'" + str8 + "' != '" + "org.apache.syncope.common.lib.to.AnyObjectTO" + "'", str8, "org.apache.syncope.common.lib.to.AnyObjectTO");
        org.junit.Assert.assertEquals("'" + str9 + "' != '" + "org.apache.syncope.common.lib.to.AnyObjectTO" + "'", str9, "org.apache.syncope.common.lib.to.AnyObjectTO");
        org.junit.Assert.assertEquals("'" + str11 + "' != '" + "org.apache.syncope.common.lib.to.AnyObjectTO" + "'", str11, "org.apache.syncope.common.lib.to.AnyObjectTO");
        org.junit.Assert.assertNotNull(membershipTOList12);
    }

    @Test
    public void test56() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "DefaultMappingManagerRandoopRegressionS00.test56");
        org.apache.syncope.core.provisioning.java.DefaultMappingManager defaultMappingManager0 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.manager();
        org.apache.syncope.common.lib.to.Item item1 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.keyItem();
        java.lang.String str2 = item1.getIntAttrName();
        java.util.List<java.lang.String> strList3 = item1.getTransformers();
        org.identityconnectors.framework.common.objects.Attribute attribute5 = org.identityconnectors.framework.common.objects.AttributeBuilder.build("initial-group");
        org.apache.syncope.common.lib.to.Provision provision6 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.plainAndKeyProvision();
        provision6.setUidOnCreate("mailAlternateAddress");
        org.apache.syncope.common.lib.to.Mapping mapping9 = provision6.getMapping();
        org.apache.syncope.common.lib.to.Mapping mapping10 = provision6.getMapping();
        org.apache.syncope.common.lib.to.RealmTO realmTO11 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.realmTO();
        java.util.Optional<org.apache.syncope.common.lib.Attr> attrOptional13 = realmTO11.getPlainAttr("");
        java.util.Set<org.apache.syncope.common.lib.Attr> attrSet14 = realmTO11.getDerAttrs();
        java.util.Set<org.apache.syncope.common.lib.Attr> attrSet15 = realmTO11.getPlainAttrs();
        boolean boolean16 = mapping10.equals((java.lang.Object) realmTO11);
        java.util.Optional<org.apache.syncope.common.lib.Attr> attrOptional18 = realmTO11.getDerAttr("GROUP");
        java.lang.String str19 = realmTO11.getTicketExpirationPolicy();
        defaultMappingManager0.setIntValues(item1, attribute5, realmTO11);
        boolean boolean21 = item1.isConnObjectKey();
        org.junit.Assert.assertNotNull(defaultMappingManager0);
        org.junit.Assert.assertNotNull(item1);
        org.junit.Assert.assertEquals("'" + str2 + "' != '" + "key" + "'", str2, "key");
        org.junit.Assert.assertNotNull(strList3);
        org.junit.Assert.assertNotNull(attribute5);
        org.junit.Assert.assertNotNull(provision6);
        org.junit.Assert.assertNotNull(mapping9);
        org.junit.Assert.assertNotNull(mapping10);
        org.junit.Assert.assertNotNull(realmTO11);
        org.junit.Assert.assertNotNull(attrOptional13);
        org.junit.Assert.assertNotNull(attrSet14);
        org.junit.Assert.assertNotNull(attrSet15);
        org.junit.Assert.assertTrue("'" + boolean16 + "' != '" + false + "'", boolean16 == false);
        org.junit.Assert.assertNotNull(attrOptional18);
        org.junit.Assert.assertNull(str19);
        org.junit.Assert.assertTrue("'" + boolean21 + "' != '" + true + "'", boolean21 == true);
    }

    @Test
    public void test57() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "DefaultMappingManagerRandoopRegressionS00.test57");
        org.apache.syncope.core.persistence.api.dao.UserDAO userDAO0 = null;
        org.apache.syncope.core.persistence.api.dao.AnyObjectDAO anyObjectDAO1 = null;
        org.apache.syncope.core.persistence.api.dao.GroupDAO groupDAO2 = null;
        org.apache.syncope.core.persistence.api.dao.RelationshipTypeDAO relationshipTypeDAO3 = null;
        org.apache.syncope.core.persistence.api.dao.RealmSearchDAO realmSearchDAO4 = null;
        org.apache.syncope.core.persistence.api.dao.ImplementationDAO implementationDAO5 = null;
        org.apache.syncope.core.provisioning.api.DerAttrHandler derAttrHandler6 = null;
        org.apache.syncope.core.provisioning.api.IntAttrNameParser intAttrNameParser7 = null;
        org.apache.syncope.core.persistence.api.EncryptorManager encryptorManager8 = null;
        org.apache.syncope.core.provisioning.api.jexl.JexlTools jexlTools9 = null;
        org.apache.syncope.core.provisioning.java.DefaultMappingManager defaultMappingManager10 = new org.apache.syncope.core.provisioning.java.DefaultMappingManager(userDAO0, anyObjectDAO1, groupDAO2, relationshipTypeDAO3, realmSearchDAO4, implementationDAO5, derAttrHandler6, intAttrNameParser7, encryptorManager8, jexlTools9);
        org.apache.syncope.core.persistence.api.entity.Any any11 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.anyUser();
        org.apache.syncope.core.persistence.api.entity.ExternalResource externalResource15 = null;
        org.apache.syncope.common.lib.to.Provision provision16 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.plainAndKeyProvision();
        provision16.setUidOnCreate("mailAlternateAddress");
        java.lang.String str19 = provision16.getAnyType();
        org.apache.syncope.core.provisioning.api.MappingManager.PreparedAttrs preparedAttrs20 = defaultMappingManager10.prepareAttrsFromAny(any11, "mario.rossi@example.org", true, (java.lang.Boolean) false, externalResource15, provision16);
        org.apache.syncope.common.lib.to.Mapping mapping21 = provision16.getMapping();
        java.lang.String str22 = provision16.getObjectClass();
        java.lang.String str23 = provision16.getAnyType();
        org.junit.Assert.assertNotNull(any11);
        org.junit.Assert.assertNotNull(provision16);
        org.junit.Assert.assertNull(str19);
        org.junit.Assert.assertNotNull(preparedAttrs20);
        org.junit.Assert.assertNotNull(mapping21);
        org.junit.Assert.assertNull(str22);
        org.junit.Assert.assertNull(str23);
    }

    @Test
    public void test58() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "DefaultMappingManagerRandoopRegressionS00.test58");
        org.apache.syncope.core.provisioning.java.DefaultMappingManager defaultMappingManager0 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.manager();
        org.apache.syncope.core.persistence.api.entity.ExternalResource externalResource1 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.resource();
        org.apache.syncope.common.lib.to.Provision provision2 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.plainAndKeyProvision();
        provision2.setUidOnCreate("mailAlternateAddress");
        org.apache.syncope.common.lib.to.Mapping mapping5 = provision2.getMapping();
        org.apache.syncope.common.lib.to.Mapping mapping6 = provision2.getMapping();
        provision2.setUidOnCreate("USER");
        org.apache.syncope.common.lib.to.Item item9 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.usernameItem();
        boolean boolean11 = item9.equals((java.lang.Object) "name");
        org.apache.syncope.core.provisioning.api.IntAttrName intAttrName12 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.missingIntAttrName();
        org.apache.syncope.common.lib.types.AttrSchemaType attrSchemaType13 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.stringSchemaType();
        org.apache.syncope.core.persistence.api.entity.Any any14 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.anyUser();
        org.apache.syncope.core.provisioning.api.AccountGetter accountGetter15 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.defaultAccountGetter();
        org.apache.syncope.core.provisioning.api.PlainAttrGetter plainAttrGetter16 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.missingPlainAttrGetter();
        org.apache.syncope.core.provisioning.api.MappingManager.IntValues intValues17 = defaultMappingManager0.getIntValues(externalResource1, provision2, item9, intAttrName12, attrSchemaType13, any14, accountGetter15, plainAttrGetter16);
        java.lang.String str18 = provision2.getSyncToken();
        provision2.setAnyType("title");
        org.junit.Assert.assertNotNull(defaultMappingManager0);
        org.junit.Assert.assertNotNull(externalResource1);
        org.junit.Assert.assertNotNull(provision2);
        org.junit.Assert.assertNotNull(mapping5);
        org.junit.Assert.assertNotNull(mapping6);
        org.junit.Assert.assertNotNull(item9);
        org.junit.Assert.assertTrue("'" + boolean11 + "' != '" + false + "'", boolean11 == false);
        org.junit.Assert.assertNotNull(intAttrName12);
        org.junit.Assert.assertTrue("'" + attrSchemaType13 + "' != '" + org.apache.syncope.common.lib.types.AttrSchemaType.String + "'", attrSchemaType13.equals(org.apache.syncope.common.lib.types.AttrSchemaType.String));
        org.junit.Assert.assertNotNull(any14);
        org.junit.Assert.assertNotNull(accountGetter15);
        org.junit.Assert.assertNotNull(plainAttrGetter16);
        org.junit.Assert.assertNotNull(intValues17);
        org.junit.Assert.assertNull(str18);
    }

    @Test
    public void test59() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "DefaultMappingManagerRandoopRegressionS00.test59");
        org.apache.syncope.core.provisioning.java.DefaultMappingManager defaultMappingManager0 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.manager();
        org.apache.syncope.common.lib.to.Item item1 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.keyItem();
        java.lang.String str2 = item1.getIntAttrName();
        java.util.List<java.lang.String> strList3 = item1.getTransformers();
        org.identityconnectors.framework.common.objects.Attribute attribute5 = org.identityconnectors.framework.common.objects.AttributeBuilder.build("initial-group");
        org.apache.syncope.common.lib.to.Provision provision6 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.plainAndKeyProvision();
        provision6.setUidOnCreate("mailAlternateAddress");
        org.apache.syncope.common.lib.to.Mapping mapping9 = provision6.getMapping();
        org.apache.syncope.common.lib.to.Mapping mapping10 = provision6.getMapping();
        org.apache.syncope.common.lib.to.RealmTO realmTO11 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.realmTO();
        java.util.Optional<org.apache.syncope.common.lib.Attr> attrOptional13 = realmTO11.getPlainAttr("");
        java.util.Set<org.apache.syncope.common.lib.Attr> attrSet14 = realmTO11.getDerAttrs();
        java.util.Set<org.apache.syncope.common.lib.Attr> attrSet15 = realmTO11.getPlainAttrs();
        boolean boolean16 = mapping10.equals((java.lang.Object) realmTO11);
        java.util.Optional<org.apache.syncope.common.lib.Attr> attrOptional18 = realmTO11.getDerAttr("GROUP");
        java.lang.String str19 = realmTO11.getTicketExpirationPolicy();
        defaultMappingManager0.setIntValues(item1, attribute5, realmTO11);
        java.util.List<java.lang.String> strList21 = item1.getTransformers();
        java.lang.String str22 = item1.toString();
        item1.setPassword(true);
        org.junit.Assert.assertNotNull(defaultMappingManager0);
        org.junit.Assert.assertNotNull(item1);
        org.junit.Assert.assertEquals("'" + str2 + "' != '" + "key" + "'", str2, "key");
        org.junit.Assert.assertNotNull(strList3);
        org.junit.Assert.assertNotNull(attribute5);
        org.junit.Assert.assertNotNull(provision6);
        org.junit.Assert.assertNotNull(mapping9);
        org.junit.Assert.assertNotNull(mapping10);
        org.junit.Assert.assertNotNull(realmTO11);
        org.junit.Assert.assertNotNull(attrOptional13);
        org.junit.Assert.assertNotNull(attrSet14);
        org.junit.Assert.assertNotNull(attrSet15);
        org.junit.Assert.assertTrue("'" + boolean16 + "' != '" + false + "'", boolean16 == false);
        org.junit.Assert.assertNotNull(attrOptional18);
        org.junit.Assert.assertNull(str19);
        org.junit.Assert.assertNotNull(strList21);
    }

    @Test
    public void test60() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "DefaultMappingManagerRandoopRegressionS00.test60");
        org.apache.syncope.core.provisioning.java.DefaultMappingManager defaultMappingManager0 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.manager();
        org.apache.syncope.common.lib.to.Item item1 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.keyItem();
        java.lang.String str2 = item1.getIntAttrName();
        java.util.List<java.lang.String> strList3 = item1.getTransformers();
        org.identityconnectors.framework.common.objects.Attribute attribute5 = org.identityconnectors.framework.common.objects.AttributeBuilder.build("initial-group");
        org.apache.syncope.common.lib.to.Provision provision6 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.plainAndKeyProvision();
        provision6.setUidOnCreate("mailAlternateAddress");
        org.apache.syncope.common.lib.to.Mapping mapping9 = provision6.getMapping();
        org.apache.syncope.common.lib.to.Mapping mapping10 = provision6.getMapping();
        org.apache.syncope.common.lib.to.RealmTO realmTO11 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.realmTO();
        java.util.Optional<org.apache.syncope.common.lib.Attr> attrOptional13 = realmTO11.getPlainAttr("");
        java.util.Set<org.apache.syncope.common.lib.Attr> attrSet14 = realmTO11.getDerAttrs();
        java.util.Set<org.apache.syncope.common.lib.Attr> attrSet15 = realmTO11.getPlainAttrs();
        boolean boolean16 = mapping10.equals((java.lang.Object) realmTO11);
        java.util.Optional<org.apache.syncope.common.lib.Attr> attrOptional18 = realmTO11.getDerAttr("GROUP");
        java.lang.String str19 = realmTO11.getTicketExpirationPolicy();
        defaultMappingManager0.setIntValues(item1, attribute5, realmTO11);
        java.util.List<java.lang.String> strList21 = item1.getTransformers();
        java.lang.String str22 = item1.getExtAttrName();
        java.lang.String str23 = item1.getPullJEXLTransformer();
        org.junit.Assert.assertNotNull(defaultMappingManager0);
        org.junit.Assert.assertNotNull(item1);
        org.junit.Assert.assertEquals("'" + str2 + "' != '" + "key" + "'", str2, "key");
        org.junit.Assert.assertNotNull(strList3);
        org.junit.Assert.assertNotNull(attribute5);
        org.junit.Assert.assertNotNull(provision6);
        org.junit.Assert.assertNotNull(mapping9);
        org.junit.Assert.assertNotNull(mapping10);
        org.junit.Assert.assertNotNull(realmTO11);
        org.junit.Assert.assertNotNull(attrOptional13);
        org.junit.Assert.assertNotNull(attrSet14);
        org.junit.Assert.assertNotNull(attrSet15);
        org.junit.Assert.assertTrue("'" + boolean16 + "' != '" + false + "'", boolean16 == false);
        org.junit.Assert.assertNotNull(attrOptional18);
        org.junit.Assert.assertNull(str19);
        org.junit.Assert.assertNotNull(strList21);
        org.junit.Assert.assertEquals("'" + str22 + "' != '" + "uid" + "'", str22, "uid");
        org.junit.Assert.assertNull(str23);
    }

    @Test
    public void test61() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "DefaultMappingManagerRandoopRegressionS00.test61");
        org.apache.syncope.core.provisioning.java.DefaultMappingManager defaultMappingManager0 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.manager();
        org.apache.syncope.common.lib.to.Item item1 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.keyItem();
        java.lang.String str2 = item1.getIntAttrName();
        java.util.List<java.lang.String> strList3 = item1.getTransformers();
        org.identityconnectors.framework.common.objects.Attribute attribute5 = org.identityconnectors.framework.common.objects.AttributeBuilder.build("initial-group");
        org.apache.syncope.common.lib.to.Provision provision6 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.plainAndKeyProvision();
        provision6.setUidOnCreate("mailAlternateAddress");
        org.apache.syncope.common.lib.to.Mapping mapping9 = provision6.getMapping();
        org.apache.syncope.common.lib.to.Mapping mapping10 = provision6.getMapping();
        org.apache.syncope.common.lib.to.RealmTO realmTO11 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.realmTO();
        java.util.Optional<org.apache.syncope.common.lib.Attr> attrOptional13 = realmTO11.getPlainAttr("");
        java.util.Set<org.apache.syncope.common.lib.Attr> attrSet14 = realmTO11.getDerAttrs();
        java.util.Set<org.apache.syncope.common.lib.Attr> attrSet15 = realmTO11.getPlainAttrs();
        boolean boolean16 = mapping10.equals((java.lang.Object) realmTO11);
        java.util.Optional<org.apache.syncope.common.lib.Attr> attrOptional18 = realmTO11.getDerAttr("GROUP");
        java.lang.String str19 = realmTO11.getTicketExpirationPolicy();
        defaultMappingManager0.setIntValues(item1, attribute5, realmTO11);
        java.util.List<java.lang.String> strList21 = realmTO11.getAnyTypeClasses();
        java.lang.String str22 = realmTO11.getAccountPolicy();
        org.junit.Assert.assertNotNull(defaultMappingManager0);
        org.junit.Assert.assertNotNull(item1);
        org.junit.Assert.assertEquals("'" + str2 + "' != '" + "key" + "'", str2, "key");
        org.junit.Assert.assertNotNull(strList3);
        org.junit.Assert.assertNotNull(attribute5);
        org.junit.Assert.assertNotNull(provision6);
        org.junit.Assert.assertNotNull(mapping9);
        org.junit.Assert.assertNotNull(mapping10);
        org.junit.Assert.assertNotNull(realmTO11);
        org.junit.Assert.assertNotNull(attrOptional13);
        org.junit.Assert.assertNotNull(attrSet14);
        org.junit.Assert.assertNotNull(attrSet15);
        org.junit.Assert.assertTrue("'" + boolean16 + "' != '" + false + "'", boolean16 == false);
        org.junit.Assert.assertNotNull(attrOptional18);
        org.junit.Assert.assertNull(str19);
        org.junit.Assert.assertNotNull(strList21);
        org.junit.Assert.assertNull(str22);
    }

    @Test
    public void test62() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "DefaultMappingManagerRandoopRegressionS00.test62");
        org.apache.syncope.core.provisioning.java.DefaultMappingManager defaultMappingManager0 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.manager();
        org.apache.syncope.core.persistence.api.entity.ExternalResource externalResource1 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.resource();
        org.apache.syncope.common.lib.to.Provision provision2 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.plainAndKeyProvision();
        provision2.setUidOnCreate("mailAlternateAddress");
        org.apache.syncope.common.lib.to.Mapping mapping5 = provision2.getMapping();
        org.apache.syncope.common.lib.to.Mapping mapping6 = provision2.getMapping();
        org.apache.syncope.common.lib.to.Item item7 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.optionalMissingPlainItem();
        java.lang.String str8 = item7.toString();
        java.lang.String str9 = item7.toString();
        item7.setPassword(true);
        org.apache.syncope.core.provisioning.api.IntAttrName intAttrName12 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.usernameIntAttrName();
        org.apache.syncope.common.lib.types.AttrSchemaType attrSchemaType13 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.stringSchemaType();
        org.apache.syncope.core.persistence.api.entity.Any any14 = null;
        org.apache.syncope.core.provisioning.api.AccountGetter accountGetter15 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.defaultAccountGetter();
        org.apache.syncope.core.provisioning.api.PlainAttrGetter plainAttrGetter16 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.missingPlainAttrGetter();
        org.apache.syncope.core.provisioning.api.MappingManager.IntValues intValues17 = defaultMappingManager0.getIntValues(externalResource1, provision2, item7, intAttrName12, attrSchemaType13, any14, accountGetter15, plainAttrGetter16);
        provision2.setAnyType("department");
        provision2.setIgnoreCaseMatch(true);
        provision2.setAnyType("firstname");
        java.lang.String str24 = provision2.getAnyType();
        org.junit.Assert.assertNotNull(defaultMappingManager0);
        org.junit.Assert.assertNotNull(externalResource1);
        org.junit.Assert.assertNotNull(provision2);
        org.junit.Assert.assertNotNull(mapping5);
        org.junit.Assert.assertNotNull(mapping6);
        org.junit.Assert.assertNotNull(item7);
        org.junit.Assert.assertNotNull(intAttrName12);
        org.junit.Assert.assertTrue("'" + attrSchemaType13 + "' != '" + org.apache.syncope.common.lib.types.AttrSchemaType.String + "'", attrSchemaType13.equals(org.apache.syncope.common.lib.types.AttrSchemaType.String));
        org.junit.Assert.assertNotNull(accountGetter15);
        org.junit.Assert.assertNotNull(plainAttrGetter16);
        org.junit.Assert.assertNotNull(intValues17);
        org.junit.Assert.assertEquals("'" + str24 + "' != '" + "firstname" + "'", str24, "firstname");
    }

    @Test
    public void test63() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "DefaultMappingManagerRandoopRegressionS00.test63");
        org.apache.syncope.core.provisioning.java.DefaultMappingManager defaultMappingManager0 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.manager();
        org.apache.syncope.core.persistence.api.entity.ExternalResource externalResource1 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.resource();
        org.apache.syncope.common.lib.to.Provision provision2 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.plainAndKeyProvision();
        provision2.setUidOnCreate("mailAlternateAddress");
        org.apache.syncope.common.lib.to.Mapping mapping5 = provision2.getMapping();
        org.apache.syncope.common.lib.to.Mapping mapping6 = provision2.getMapping();
        org.apache.syncope.common.lib.to.Item item7 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.optionalMissingPlainItem();
        java.lang.String str8 = item7.toString();
        java.lang.String str9 = item7.toString();
        item7.setPassword(true);
        org.apache.syncope.core.provisioning.api.IntAttrName intAttrName12 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.usernameIntAttrName();
        org.apache.syncope.common.lib.types.AttrSchemaType attrSchemaType13 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.stringSchemaType();
        org.apache.syncope.core.persistence.api.entity.Any any14 = null;
        org.apache.syncope.core.provisioning.api.AccountGetter accountGetter15 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.defaultAccountGetter();
        org.apache.syncope.core.provisioning.api.PlainAttrGetter plainAttrGetter16 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.missingPlainAttrGetter();
        org.apache.syncope.core.provisioning.api.MappingManager.IntValues intValues17 = defaultMappingManager0.getIntValues(externalResource1, provision2, item7, intAttrName12, attrSchemaType13, any14, accountGetter15, plainAttrGetter16);
        java.lang.String str18 = provision2.getSyncToken();
        provision2.setIgnoreCaseMatch(true);
        provision2.setIgnoreCaseMatch(false);
        org.junit.Assert.assertNotNull(defaultMappingManager0);
        org.junit.Assert.assertNotNull(externalResource1);
        org.junit.Assert.assertNotNull(provision2);
        org.junit.Assert.assertNotNull(mapping5);
        org.junit.Assert.assertNotNull(mapping6);
        org.junit.Assert.assertNotNull(item7);
        org.junit.Assert.assertNotNull(intAttrName12);
        org.junit.Assert.assertTrue("'" + attrSchemaType13 + "' != '" + org.apache.syncope.common.lib.types.AttrSchemaType.String + "'", attrSchemaType13.equals(org.apache.syncope.common.lib.types.AttrSchemaType.String));
        org.junit.Assert.assertNotNull(accountGetter15);
        org.junit.Assert.assertNotNull(plainAttrGetter16);
        org.junit.Assert.assertNotNull(intValues17);
        org.junit.Assert.assertNull(str18);
    }

    @Test
    public void test64() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "DefaultMappingManagerRandoopRegressionS00.test64");
        org.apache.syncope.core.provisioning.java.DefaultMappingManager defaultMappingManager0 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.manager();
        org.apache.syncope.common.lib.to.Item item1 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.keyItem();
        java.lang.String str2 = item1.getIntAttrName();
        java.util.List<java.lang.String> strList3 = item1.getTransformers();
        org.identityconnectors.framework.common.objects.Attribute attribute5 = org.identityconnectors.framework.common.objects.AttributeBuilder.build("initial-group");
        org.apache.syncope.common.lib.to.Provision provision6 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.plainAndKeyProvision();
        provision6.setUidOnCreate("mailAlternateAddress");
        org.apache.syncope.common.lib.to.Mapping mapping9 = provision6.getMapping();
        org.apache.syncope.common.lib.to.Mapping mapping10 = provision6.getMapping();
        org.apache.syncope.common.lib.to.RealmTO realmTO11 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.realmTO();
        java.util.Optional<org.apache.syncope.common.lib.Attr> attrOptional13 = realmTO11.getPlainAttr("");
        java.util.Set<org.apache.syncope.common.lib.Attr> attrSet14 = realmTO11.getDerAttrs();
        java.util.Set<org.apache.syncope.common.lib.Attr> attrSet15 = realmTO11.getPlainAttrs();
        boolean boolean16 = mapping10.equals((java.lang.Object) realmTO11);
        java.util.Optional<org.apache.syncope.common.lib.Attr> attrOptional18 = realmTO11.getDerAttr("GROUP");
        java.lang.String str19 = realmTO11.getTicketExpirationPolicy();
        defaultMappingManager0.setIntValues(item1, attribute5, realmTO11);
        java.util.List<java.lang.String> strList21 = realmTO11.getAnyTypeClasses();
        java.lang.String str22 = realmTO11.getFullPath();
        realmTO11.setAccountPolicy("department");
        org.junit.Assert.assertNotNull(defaultMappingManager0);
        org.junit.Assert.assertNotNull(item1);
        org.junit.Assert.assertEquals("'" + str2 + "' != '" + "key" + "'", str2, "key");
        org.junit.Assert.assertNotNull(strList3);
        org.junit.Assert.assertNotNull(attribute5);
        org.junit.Assert.assertNotNull(provision6);
        org.junit.Assert.assertNotNull(mapping9);
        org.junit.Assert.assertNotNull(mapping10);
        org.junit.Assert.assertNotNull(realmTO11);
        org.junit.Assert.assertNotNull(attrOptional13);
        org.junit.Assert.assertNotNull(attrSet14);
        org.junit.Assert.assertNotNull(attrSet15);
        org.junit.Assert.assertTrue("'" + boolean16 + "' != '" + false + "'", boolean16 == false);
        org.junit.Assert.assertNotNull(attrOptional18);
        org.junit.Assert.assertNull(str19);
        org.junit.Assert.assertNotNull(strList21);
        org.junit.Assert.assertNull(str22);
    }

    @Test
    public void test65() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "DefaultMappingManagerRandoopRegressionS00.test65");
        org.apache.syncope.core.provisioning.java.DefaultMappingManager defaultMappingManager0 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.manager();
        org.apache.syncope.common.lib.to.Item item1 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.keyItem();
        java.lang.String str2 = item1.getIntAttrName();
        java.util.List<java.lang.String> strList3 = item1.getTransformers();
        org.identityconnectors.framework.common.objects.Attribute attribute5 = org.identityconnectors.framework.common.objects.AttributeBuilder.build("initial-group");
        org.apache.syncope.common.lib.to.Provision provision6 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.plainAndKeyProvision();
        provision6.setUidOnCreate("mailAlternateAddress");
        org.apache.syncope.common.lib.to.Mapping mapping9 = provision6.getMapping();
        org.apache.syncope.common.lib.to.Mapping mapping10 = provision6.getMapping();
        org.apache.syncope.common.lib.to.RealmTO realmTO11 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.realmTO();
        java.util.Optional<org.apache.syncope.common.lib.Attr> attrOptional13 = realmTO11.getPlainAttr("");
        java.util.Set<org.apache.syncope.common.lib.Attr> attrSet14 = realmTO11.getDerAttrs();
        java.util.Set<org.apache.syncope.common.lib.Attr> attrSet15 = realmTO11.getPlainAttrs();
        boolean boolean16 = mapping10.equals((java.lang.Object) realmTO11);
        java.util.Optional<org.apache.syncope.common.lib.Attr> attrOptional18 = realmTO11.getDerAttr("GROUP");
        java.lang.String str19 = realmTO11.getTicketExpirationPolicy();
        defaultMappingManager0.setIntValues(item1, attribute5, realmTO11);
        java.util.List<java.lang.String> strList21 = realmTO11.getResources();
        java.util.Optional<org.apache.syncope.common.lib.Attr> attrOptional23 = realmTO11.getPlainAttr("Engineering");
        java.lang.String str24 = realmTO11.getFullPath();
        org.junit.Assert.assertNotNull(defaultMappingManager0);
        org.junit.Assert.assertNotNull(item1);
        org.junit.Assert.assertEquals("'" + str2 + "' != '" + "key" + "'", str2, "key");
        org.junit.Assert.assertNotNull(strList3);
        org.junit.Assert.assertNotNull(attribute5);
        org.junit.Assert.assertNotNull(provision6);
        org.junit.Assert.assertNotNull(mapping9);
        org.junit.Assert.assertNotNull(mapping10);
        org.junit.Assert.assertNotNull(realmTO11);
        org.junit.Assert.assertNotNull(attrOptional13);
        org.junit.Assert.assertNotNull(attrSet14);
        org.junit.Assert.assertNotNull(attrSet15);
        org.junit.Assert.assertTrue("'" + boolean16 + "' != '" + false + "'", boolean16 == false);
        org.junit.Assert.assertNotNull(attrOptional18);
        org.junit.Assert.assertNull(str19);
        org.junit.Assert.assertNotNull(strList21);
        org.junit.Assert.assertNotNull(attrOptional23);
        org.junit.Assert.assertNull(str24);
    }

    @Test
    public void test66() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "DefaultMappingManagerRandoopRegressionS00.test66");
        org.apache.syncope.core.provisioning.java.DefaultMappingManager defaultMappingManager0 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.manager();
        org.apache.syncope.common.lib.to.Item item1 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.optionalMissingPlainItem();
        java.lang.String str2 = item1.toString();
        org.identityconnectors.framework.common.objects.Attribute attribute4 = org.identityconnectors.framework.common.objects.AttributeBuilder.buildEnableDate((long) (short) 100);
        org.apache.syncope.common.lib.to.AnyObjectTO anyObjectTO5 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.anyObjectTO();
        java.util.Optional<org.apache.syncope.common.lib.to.MembershipTO> membershipTOOptional7 = anyObjectTO5.getMembership("email");
        java.lang.String str8 = anyObjectTO5.getDiscriminator();
        java.lang.String str9 = anyObjectTO5.getDiscriminator();
        defaultMappingManager0.setIntValues(item1, attribute4, (org.apache.syncope.common.lib.to.AnyTO) anyObjectTO5);
        org.apache.syncope.core.persistence.api.entity.ExternalResource externalResource11 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.resource();
        org.apache.syncope.common.lib.to.Item item12 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.usernameItem();
        java.lang.String str13 = item12.getPullJEXLTransformer();
        item12.setIntAttrName("initial-group");
        item12.setIntAttrName("USER");
        item12.setExtAttrName("initial-realm");
        org.apache.syncope.core.persistence.api.entity.Realm realm20 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.realm();
        org.apache.syncope.core.provisioning.api.MappingManager.PreparedAttr preparedAttr21 = defaultMappingManager0.prepareAttr(externalResource11, item12, realm20);
        java.lang.String str22 = item12.getIntAttrName();
        org.junit.Assert.assertNotNull(defaultMappingManager0);
        org.junit.Assert.assertNotNull(item1);
        org.junit.Assert.assertNotNull(attribute4);
        org.junit.Assert.assertNotNull(anyObjectTO5);
        org.junit.Assert.assertNotNull(membershipTOOptional7);
        org.junit.Assert.assertEquals("'" + str8 + "' != '" + "org.apache.syncope.common.lib.to.AnyObjectTO" + "'", str8, "org.apache.syncope.common.lib.to.AnyObjectTO");
        org.junit.Assert.assertEquals("'" + str9 + "' != '" + "org.apache.syncope.common.lib.to.AnyObjectTO" + "'", str9, "org.apache.syncope.common.lib.to.AnyObjectTO");
        org.junit.Assert.assertNotNull(externalResource11);
        org.junit.Assert.assertNotNull(item12);
        org.junit.Assert.assertNull(str13);
        org.junit.Assert.assertNotNull(realm20);
        org.junit.Assert.assertNotNull(preparedAttr21);
        org.junit.Assert.assertEquals("'" + str22 + "' != '" + "USER" + "'", str22, "USER");
    }

    @Test
    public void test67() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "DefaultMappingManagerRandoopRegressionS00.test67");
        org.apache.syncope.core.persistence.api.entity.PlainAttrValue plainAttrValue0 = new org.apache.syncope.core.persistence.api.entity.PlainAttrValue();
        org.apache.syncope.core.provisioning.java.DefaultMappingManager defaultMappingManager1 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.manager();
        org.apache.syncope.core.persistence.api.entity.ExternalResource externalResource2 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.resource();
        org.apache.syncope.common.lib.to.Provision provision3 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.plainAndKeyProvision();
        provision3.setUidOnCreate("mailAlternateAddress");
        org.apache.syncope.common.lib.to.Mapping mapping6 = provision3.getMapping();
        org.apache.syncope.common.lib.to.Mapping mapping7 = provision3.getMapping();
        provision3.setUidOnCreate("USER");
        org.apache.syncope.common.lib.to.Item item10 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.usernameItem();
        boolean boolean12 = item10.equals((java.lang.Object) "name");
        org.apache.syncope.core.provisioning.api.IntAttrName intAttrName13 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.missingIntAttrName();
        org.apache.syncope.common.lib.types.AttrSchemaType attrSchemaType14 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.stringSchemaType();
        org.apache.syncope.core.persistence.api.entity.Any any15 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.anyUser();
        org.apache.syncope.core.provisioning.api.AccountGetter accountGetter16 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.defaultAccountGetter();
        org.apache.syncope.core.provisioning.api.PlainAttrGetter plainAttrGetter17 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.missingPlainAttrGetter();
        org.apache.syncope.core.provisioning.api.MappingManager.IntValues intValues18 = defaultMappingManager1.getIntValues(externalResource2, provision3, item10, intAttrName13, attrSchemaType14, any15, accountGetter16, plainAttrGetter17);
        java.lang.String str19 = plainAttrValue0.getValueAsString(attrSchemaType14);
        plainAttrValue0.setDoubleValue((java.lang.Double) 1.0d);
        java.lang.String str22 = plainAttrValue0.getStringValue();
        byte[] byteArray23 = plainAttrValue0.getBinaryValue();
        org.junit.Assert.assertNotNull(defaultMappingManager1);
        org.junit.Assert.assertNotNull(externalResource2);
        org.junit.Assert.assertNotNull(provision3);
        org.junit.Assert.assertNotNull(mapping6);
        org.junit.Assert.assertNotNull(mapping7);
        org.junit.Assert.assertNotNull(item10);
        org.junit.Assert.assertTrue("'" + boolean12 + "' != '" + false + "'", boolean12 == false);
        org.junit.Assert.assertNotNull(intAttrName13);
        org.junit.Assert.assertTrue("'" + attrSchemaType14 + "' != '" + org.apache.syncope.common.lib.types.AttrSchemaType.String + "'", attrSchemaType14.equals(org.apache.syncope.common.lib.types.AttrSchemaType.String));
        org.junit.Assert.assertNotNull(any15);
        org.junit.Assert.assertNotNull(accountGetter16);
        org.junit.Assert.assertNotNull(plainAttrGetter17);
        org.junit.Assert.assertNotNull(intValues18);
        org.junit.Assert.assertEquals("'" + str19 + "' != '" + "" + "'", str19, "");
        org.junit.Assert.assertNull(str22);
        org.junit.Assert.assertNull(byteArray23);
    }

    @Test
    public void test68() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "DefaultMappingManagerRandoopRegressionS00.test68");
        org.apache.syncope.core.provisioning.java.DefaultMappingManager defaultMappingManager0 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.manager();
        org.apache.syncope.common.lib.to.Item item1 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.usernameItem();
        boolean boolean3 = item1.equals((java.lang.Object) "name");
        item1.setExtAttrName("false");
        item1.setExtAttrName("");
        org.identityconnectors.framework.common.objects.Attribute attribute9 = org.identityconnectors.framework.common.objects.AttributeBuilder.buildLastPasswordChangeDate((long) (byte) 10);
        org.apache.syncope.common.lib.to.RealmTO realmTO10 = null;
        defaultMappingManager0.setIntValues(item1, attribute9, realmTO10);
        org.junit.Assert.assertNotNull(defaultMappingManager0);
        org.junit.Assert.assertNotNull(item1);
        org.junit.Assert.assertTrue("'" + boolean3 + "' != '" + false + "'", boolean3 == false);
        org.junit.Assert.assertNotNull(attribute9);
    }

    @Test
    public void test69() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "DefaultMappingManagerRandoopRegressionS00.test69");
        org.apache.syncope.core.provisioning.java.DefaultMappingManager defaultMappingManager0 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.manager();
        org.apache.syncope.common.lib.to.Item item1 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.optionalMissingPlainItem();
        java.lang.String str2 = item1.toString();
        org.identityconnectors.framework.common.objects.Attribute attribute4 = org.identityconnectors.framework.common.objects.AttributeBuilder.buildEnableDate((long) (short) 100);
        org.apache.syncope.common.lib.to.AnyObjectTO anyObjectTO5 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.anyObjectTO();
        java.util.Optional<org.apache.syncope.common.lib.to.MembershipTO> membershipTOOptional7 = anyObjectTO5.getMembership("email");
        java.lang.String str8 = anyObjectTO5.getDiscriminator();
        java.lang.String str9 = anyObjectTO5.getDiscriminator();
        defaultMappingManager0.setIntValues(item1, attribute4, (org.apache.syncope.common.lib.to.AnyTO) anyObjectTO5);
        java.lang.String str11 = anyObjectTO5.getCreator();
        org.junit.Assert.assertNotNull(defaultMappingManager0);
        org.junit.Assert.assertNotNull(item1);
        org.junit.Assert.assertNotNull(attribute4);
        org.junit.Assert.assertNotNull(anyObjectTO5);
        org.junit.Assert.assertNotNull(membershipTOOptional7);
        org.junit.Assert.assertEquals("'" + str8 + "' != '" + "org.apache.syncope.common.lib.to.AnyObjectTO" + "'", str8, "org.apache.syncope.common.lib.to.AnyObjectTO");
        org.junit.Assert.assertEquals("'" + str9 + "' != '" + "org.apache.syncope.common.lib.to.AnyObjectTO" + "'", str9, "org.apache.syncope.common.lib.to.AnyObjectTO");
        org.junit.Assert.assertNull(str11);
    }

    @Test
    public void test70() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "DefaultMappingManagerRandoopRegressionS00.test70");
        org.apache.syncope.core.persistence.api.entity.PlainAttrValue plainAttrValue0 = new org.apache.syncope.core.persistence.api.entity.PlainAttrValue();
        org.apache.syncope.core.provisioning.java.DefaultMappingManager defaultMappingManager1 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.manager();
        org.apache.syncope.core.persistence.api.entity.ExternalResource externalResource2 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.resource();
        org.apache.syncope.common.lib.to.Provision provision3 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.plainAndKeyProvision();
        provision3.setUidOnCreate("mailAlternateAddress");
        org.apache.syncope.common.lib.to.Mapping mapping6 = provision3.getMapping();
        org.apache.syncope.common.lib.to.Mapping mapping7 = provision3.getMapping();
        provision3.setUidOnCreate("USER");
        org.apache.syncope.common.lib.to.Item item10 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.usernameItem();
        boolean boolean12 = item10.equals((java.lang.Object) "name");
        org.apache.syncope.core.provisioning.api.IntAttrName intAttrName13 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.missingIntAttrName();
        org.apache.syncope.common.lib.types.AttrSchemaType attrSchemaType14 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.stringSchemaType();
        org.apache.syncope.core.persistence.api.entity.Any any15 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.anyUser();
        org.apache.syncope.core.provisioning.api.AccountGetter accountGetter16 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.defaultAccountGetter();
        org.apache.syncope.core.provisioning.api.PlainAttrGetter plainAttrGetter17 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.missingPlainAttrGetter();
        org.apache.syncope.core.provisioning.api.MappingManager.IntValues intValues18 = defaultMappingManager1.getIntValues(externalResource2, provision3, item10, intAttrName13, attrSchemaType14, any15, accountGetter16, plainAttrGetter17);
        java.lang.String str19 = plainAttrValue0.getValueAsString(attrSchemaType14);
        plainAttrValue0.setDoubleValue((java.lang.Double) 1.0d);
        java.lang.String str22 = plainAttrValue0.getStringValue();
        org.apache.syncope.core.persistence.api.entity.PlainAttr plainAttr23 = plainAttrValue0.getAttr();
        org.junit.Assert.assertNotNull(defaultMappingManager1);
        org.junit.Assert.assertNotNull(externalResource2);
        org.junit.Assert.assertNotNull(provision3);
        org.junit.Assert.assertNotNull(mapping6);
        org.junit.Assert.assertNotNull(mapping7);
        org.junit.Assert.assertNotNull(item10);
        org.junit.Assert.assertTrue("'" + boolean12 + "' != '" + false + "'", boolean12 == false);
        org.junit.Assert.assertNotNull(intAttrName13);
        org.junit.Assert.assertTrue("'" + attrSchemaType14 + "' != '" + org.apache.syncope.common.lib.types.AttrSchemaType.String + "'", attrSchemaType14.equals(org.apache.syncope.common.lib.types.AttrSchemaType.String));
        org.junit.Assert.assertNotNull(any15);
        org.junit.Assert.assertNotNull(accountGetter16);
        org.junit.Assert.assertNotNull(plainAttrGetter17);
        org.junit.Assert.assertNotNull(intValues18);
        org.junit.Assert.assertEquals("'" + str19 + "' != '" + "" + "'", str19, "");
        org.junit.Assert.assertNull(str22);
        org.junit.Assert.assertNull(plainAttr23);
    }

    @Test
    public void test71() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "DefaultMappingManagerRandoopRegressionS00.test71");
        org.apache.syncope.core.provisioning.java.DefaultMappingManager defaultMappingManager0 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.manager();
        org.apache.syncope.common.lib.to.Item item1 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.optionalMissingPlainItem();
        java.lang.String str2 = item1.toString();
        org.identityconnectors.framework.common.objects.Attribute attribute4 = org.identityconnectors.framework.common.objects.AttributeBuilder.buildEnableDate((long) (short) 100);
        org.apache.syncope.common.lib.to.AnyObjectTO anyObjectTO5 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.anyObjectTO();
        java.util.Optional<org.apache.syncope.common.lib.to.MembershipTO> membershipTOOptional7 = anyObjectTO5.getMembership("email");
        java.lang.String str8 = anyObjectTO5.getDiscriminator();
        java.lang.String str9 = anyObjectTO5.getDiscriminator();
        defaultMappingManager0.setIntValues(item1, attribute4, (org.apache.syncope.common.lib.to.AnyTO) anyObjectTO5);
        org.apache.syncope.core.persistence.api.entity.ExternalResource externalResource11 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.resource();
        org.apache.syncope.common.lib.to.Item item12 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.keyItem();
        org.apache.syncope.common.lib.types.MappingPurpose mappingPurpose13 = null;
        item12.setPurpose(mappingPurpose13);
        boolean boolean15 = item12.isConnObjectKey();
        org.apache.syncope.core.persistence.api.entity.Realm realm16 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.realm();
        org.apache.syncope.core.provisioning.api.MappingManager.PreparedAttr preparedAttr17 = defaultMappingManager0.prepareAttr(externalResource11, item12, realm16);
        item12.setExtAttrName("org.apache.syncope.common.lib.to.AnyObjectTO");
        org.junit.Assert.assertNotNull(defaultMappingManager0);
        org.junit.Assert.assertNotNull(item1);
        org.junit.Assert.assertNotNull(attribute4);
        org.junit.Assert.assertNotNull(anyObjectTO5);
        org.junit.Assert.assertNotNull(membershipTOOptional7);
        org.junit.Assert.assertEquals("'" + str8 + "' != '" + "org.apache.syncope.common.lib.to.AnyObjectTO" + "'", str8, "org.apache.syncope.common.lib.to.AnyObjectTO");
        org.junit.Assert.assertEquals("'" + str9 + "' != '" + "org.apache.syncope.common.lib.to.AnyObjectTO" + "'", str9, "org.apache.syncope.common.lib.to.AnyObjectTO");
        org.junit.Assert.assertNotNull(externalResource11);
        org.junit.Assert.assertNotNull(item12);
        org.junit.Assert.assertTrue("'" + boolean15 + "' != '" + true + "'", boolean15 == true);
        org.junit.Assert.assertNotNull(realm16);
        org.junit.Assert.assertNotNull(preparedAttr17);
    }

    @Test
    public void test72() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "DefaultMappingManagerRandoopRegressionS00.test72");
        org.apache.syncope.core.provisioning.java.DefaultMappingManager defaultMappingManager0 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.manager();
        org.apache.syncope.core.persistence.api.entity.ExternalResource externalResource1 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.resource();
        org.apache.syncope.common.lib.to.Provision provision2 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.plainAndKeyProvision();
        provision2.setUidOnCreate("mailAlternateAddress");
        org.apache.syncope.common.lib.to.Mapping mapping5 = provision2.getMapping();
        org.apache.syncope.common.lib.to.Mapping mapping6 = provision2.getMapping();
        provision2.setUidOnCreate("USER");
        org.apache.syncope.common.lib.to.Item item9 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.usernameItem();
        boolean boolean11 = item9.equals((java.lang.Object) "name");
        org.apache.syncope.core.provisioning.api.IntAttrName intAttrName12 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.missingIntAttrName();
        org.apache.syncope.common.lib.types.AttrSchemaType attrSchemaType13 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.stringSchemaType();
        org.apache.syncope.core.persistence.api.entity.Any any14 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.anyUser();
        org.apache.syncope.core.provisioning.api.AccountGetter accountGetter15 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.defaultAccountGetter();
        org.apache.syncope.core.provisioning.api.PlainAttrGetter plainAttrGetter16 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.missingPlainAttrGetter();
        org.apache.syncope.core.provisioning.api.MappingManager.IntValues intValues17 = defaultMappingManager0.getIntValues(externalResource1, provision2, item9, intAttrName12, attrSchemaType13, any14, accountGetter15, plainAttrGetter16);
        java.lang.String str18 = provision2.getUidOnCreate();
        provision2.setIgnoreCaseMatch(true);
        org.junit.Assert.assertNotNull(defaultMappingManager0);
        org.junit.Assert.assertNotNull(externalResource1);
        org.junit.Assert.assertNotNull(provision2);
        org.junit.Assert.assertNotNull(mapping5);
        org.junit.Assert.assertNotNull(mapping6);
        org.junit.Assert.assertNotNull(item9);
        org.junit.Assert.assertTrue("'" + boolean11 + "' != '" + false + "'", boolean11 == false);
        org.junit.Assert.assertNotNull(intAttrName12);
        org.junit.Assert.assertTrue("'" + attrSchemaType13 + "' != '" + org.apache.syncope.common.lib.types.AttrSchemaType.String + "'", attrSchemaType13.equals(org.apache.syncope.common.lib.types.AttrSchemaType.String));
        org.junit.Assert.assertNotNull(any14);
        org.junit.Assert.assertNotNull(accountGetter15);
        org.junit.Assert.assertNotNull(plainAttrGetter16);
        org.junit.Assert.assertNotNull(intValues17);
        org.junit.Assert.assertEquals("'" + str18 + "' != '" + "USER" + "'", str18, "USER");
    }

    @Test
    public void test73() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "DefaultMappingManagerRandoopRegressionS00.test73");
        org.apache.syncope.core.persistence.api.entity.PlainAttrValue plainAttrValue0 = new org.apache.syncope.core.persistence.api.entity.PlainAttrValue();
        org.apache.syncope.core.provisioning.java.DefaultMappingManager defaultMappingManager1 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.manager();
        org.apache.syncope.core.persistence.api.entity.ExternalResource externalResource2 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.resource();
        org.apache.syncope.common.lib.to.Provision provision3 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.plainAndKeyProvision();
        provision3.setUidOnCreate("mailAlternateAddress");
        org.apache.syncope.common.lib.to.Mapping mapping6 = provision3.getMapping();
        org.apache.syncope.common.lib.to.Mapping mapping7 = provision3.getMapping();
        provision3.setUidOnCreate("USER");
        org.apache.syncope.common.lib.to.Item item10 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.usernameItem();
        boolean boolean12 = item10.equals((java.lang.Object) "name");
        org.apache.syncope.core.provisioning.api.IntAttrName intAttrName13 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.missingIntAttrName();
        org.apache.syncope.common.lib.types.AttrSchemaType attrSchemaType14 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.stringSchemaType();
        org.apache.syncope.core.persistence.api.entity.Any any15 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.anyUser();
        org.apache.syncope.core.provisioning.api.AccountGetter accountGetter16 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.defaultAccountGetter();
        org.apache.syncope.core.provisioning.api.PlainAttrGetter plainAttrGetter17 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.missingPlainAttrGetter();
        org.apache.syncope.core.provisioning.api.MappingManager.IntValues intValues18 = defaultMappingManager1.getIntValues(externalResource2, provision3, item10, intAttrName13, attrSchemaType14, any15, accountGetter16, plainAttrGetter17);
        java.lang.String str19 = plainAttrValue0.getValueAsString(attrSchemaType14);
        plainAttrValue0.setStringValue("mobile");
        org.apache.syncope.core.persistence.api.entity.PlainSchema plainSchema22 = null;
        // The following exception was thrown during execution in test generation
        try {
            java.lang.String str23 = plainAttrValue0.getValueAsString(plainSchema22);
            org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException; message: Cannot invoke \"org.apache.syncope.core.persistence.api.entity.PlainSchema.getType()\" because \"schema\" is null");
        } catch (java.lang.NullPointerException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(defaultMappingManager1);
        org.junit.Assert.assertNotNull(externalResource2);
        org.junit.Assert.assertNotNull(provision3);
        org.junit.Assert.assertNotNull(mapping6);
        org.junit.Assert.assertNotNull(mapping7);
        org.junit.Assert.assertNotNull(item10);
        org.junit.Assert.assertTrue("'" + boolean12 + "' != '" + false + "'", boolean12 == false);
        org.junit.Assert.assertNotNull(intAttrName13);
        org.junit.Assert.assertTrue("'" + attrSchemaType14 + "' != '" + org.apache.syncope.common.lib.types.AttrSchemaType.String + "'", attrSchemaType14.equals(org.apache.syncope.common.lib.types.AttrSchemaType.String));
        org.junit.Assert.assertNotNull(any15);
        org.junit.Assert.assertNotNull(accountGetter16);
        org.junit.Assert.assertNotNull(plainAttrGetter17);
        org.junit.Assert.assertNotNull(intValues18);
        org.junit.Assert.assertEquals("'" + str19 + "' != '" + "" + "'", str19, "");
    }

    @Test
    public void test74() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "DefaultMappingManagerRandoopRegressionS00.test74");
        org.apache.syncope.core.provisioning.java.DefaultMappingManager defaultMappingManager0 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.manager();
        org.apache.syncope.core.persistence.api.entity.ExternalResource externalResource1 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.resource();
        org.apache.syncope.common.lib.to.Provision provision2 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.plainAndKeyProvision();
        provision2.setUidOnCreate("mailAlternateAddress");
        org.apache.syncope.common.lib.to.Mapping mapping5 = provision2.getMapping();
        org.apache.syncope.common.lib.to.Mapping mapping6 = provision2.getMapping();
        org.apache.syncope.common.lib.to.Item item7 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.optionalMissingPlainItem();
        java.lang.String str8 = item7.toString();
        java.lang.String str9 = item7.toString();
        item7.setPassword(true);
        org.apache.syncope.core.provisioning.api.IntAttrName intAttrName12 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.usernameIntAttrName();
        org.apache.syncope.common.lib.types.AttrSchemaType attrSchemaType13 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.stringSchemaType();
        org.apache.syncope.core.persistence.api.entity.Any any14 = null;
        org.apache.syncope.core.provisioning.api.AccountGetter accountGetter15 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.defaultAccountGetter();
        org.apache.syncope.core.provisioning.api.PlainAttrGetter plainAttrGetter16 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.missingPlainAttrGetter();
        org.apache.syncope.core.provisioning.api.MappingManager.IntValues intValues17 = defaultMappingManager0.getIntValues(externalResource1, provision2, item7, intAttrName12, attrSchemaType13, any14, accountGetter15, plainAttrGetter16);
        provision2.setIgnoreCaseMatch(true);
        java.lang.String str20 = provision2.getUidOnCreate();
        java.lang.String str21 = provision2.getUidOnCreate();
        provision2.setAnyType("ou");
        org.junit.Assert.assertNotNull(defaultMappingManager0);
        org.junit.Assert.assertNotNull(externalResource1);
        org.junit.Assert.assertNotNull(provision2);
        org.junit.Assert.assertNotNull(mapping5);
        org.junit.Assert.assertNotNull(mapping6);
        org.junit.Assert.assertNotNull(item7);
        org.junit.Assert.assertNotNull(intAttrName12);
        org.junit.Assert.assertTrue("'" + attrSchemaType13 + "' != '" + org.apache.syncope.common.lib.types.AttrSchemaType.String + "'", attrSchemaType13.equals(org.apache.syncope.common.lib.types.AttrSchemaType.String));
        org.junit.Assert.assertNotNull(accountGetter15);
        org.junit.Assert.assertNotNull(plainAttrGetter16);
        org.junit.Assert.assertNotNull(intValues17);
        org.junit.Assert.assertEquals("'" + str20 + "' != '" + "mailAlternateAddress" + "'", str20, "mailAlternateAddress");
        org.junit.Assert.assertEquals("'" + str21 + "' != '" + "mailAlternateAddress" + "'", str21, "mailAlternateAddress");
    }

    @Test
    public void test75() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "DefaultMappingManagerRandoopRegressionS00.test75");
        org.apache.syncope.core.persistence.api.entity.PlainAttrValue plainAttrValue0 = new org.apache.syncope.core.persistence.api.entity.PlainAttrValue();
        org.apache.syncope.core.provisioning.java.DefaultMappingManager defaultMappingManager1 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.manager();
        org.apache.syncope.core.persistence.api.entity.ExternalResource externalResource2 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.resource();
        org.apache.syncope.common.lib.to.Provision provision3 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.plainAndKeyProvision();
        provision3.setUidOnCreate("mailAlternateAddress");
        org.apache.syncope.common.lib.to.Mapping mapping6 = provision3.getMapping();
        org.apache.syncope.common.lib.to.Mapping mapping7 = provision3.getMapping();
        provision3.setUidOnCreate("USER");
        org.apache.syncope.common.lib.to.Item item10 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.usernameItem();
        boolean boolean12 = item10.equals((java.lang.Object) "name");
        org.apache.syncope.core.provisioning.api.IntAttrName intAttrName13 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.missingIntAttrName();
        org.apache.syncope.common.lib.types.AttrSchemaType attrSchemaType14 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.stringSchemaType();
        org.apache.syncope.core.persistence.api.entity.Any any15 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.anyUser();
        org.apache.syncope.core.provisioning.api.AccountGetter accountGetter16 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.defaultAccountGetter();
        org.apache.syncope.core.provisioning.api.PlainAttrGetter plainAttrGetter17 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.missingPlainAttrGetter();
        org.apache.syncope.core.provisioning.api.MappingManager.IntValues intValues18 = defaultMappingManager1.getIntValues(externalResource2, provision3, item10, intAttrName13, attrSchemaType14, any15, accountGetter16, plainAttrGetter17);
        java.lang.String str19 = plainAttrValue0.getValueAsString(attrSchemaType14);
        plainAttrValue0.setDoubleValue((java.lang.Double) 1.0d);
        java.lang.String str22 = plainAttrValue0.toString();
        plainAttrValue0.setDoubleValue((java.lang.Double) 0.0d);
        org.junit.Assert.assertNotNull(defaultMappingManager1);
        org.junit.Assert.assertNotNull(externalResource2);
        org.junit.Assert.assertNotNull(provision3);
        org.junit.Assert.assertNotNull(mapping6);
        org.junit.Assert.assertNotNull(mapping7);
        org.junit.Assert.assertNotNull(item10);
        org.junit.Assert.assertTrue("'" + boolean12 + "' != '" + false + "'", boolean12 == false);
        org.junit.Assert.assertNotNull(intAttrName13);
        org.junit.Assert.assertTrue("'" + attrSchemaType14 + "' != '" + org.apache.syncope.common.lib.types.AttrSchemaType.String + "'", attrSchemaType14.equals(org.apache.syncope.common.lib.types.AttrSchemaType.String));
        org.junit.Assert.assertNotNull(any15);
        org.junit.Assert.assertNotNull(accountGetter16);
        org.junit.Assert.assertNotNull(plainAttrGetter17);
        org.junit.Assert.assertNotNull(intValues18);
        org.junit.Assert.assertEquals("'" + str19 + "' != '" + "" + "'", str19, "");
    }

    @Test
    public void test76() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "DefaultMappingManagerRandoopRegressionS00.test76");
        org.apache.syncope.core.persistence.api.entity.PlainAttrValue plainAttrValue0 = new org.apache.syncope.core.persistence.api.entity.PlainAttrValue();
        org.apache.syncope.core.provisioning.java.DefaultMappingManager defaultMappingManager1 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.manager();
        org.apache.syncope.core.persistence.api.entity.ExternalResource externalResource2 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.resource();
        org.apache.syncope.common.lib.to.Provision provision3 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.plainAndKeyProvision();
        provision3.setUidOnCreate("mailAlternateAddress");
        org.apache.syncope.common.lib.to.Mapping mapping6 = provision3.getMapping();
        org.apache.syncope.common.lib.to.Mapping mapping7 = provision3.getMapping();
        provision3.setUidOnCreate("USER");
        org.apache.syncope.common.lib.to.Item item10 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.usernameItem();
        boolean boolean12 = item10.equals((java.lang.Object) "name");
        org.apache.syncope.core.provisioning.api.IntAttrName intAttrName13 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.missingIntAttrName();
        org.apache.syncope.common.lib.types.AttrSchemaType attrSchemaType14 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.stringSchemaType();
        org.apache.syncope.core.persistence.api.entity.Any any15 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.anyUser();
        org.apache.syncope.core.provisioning.api.AccountGetter accountGetter16 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.defaultAccountGetter();
        org.apache.syncope.core.provisioning.api.PlainAttrGetter plainAttrGetter17 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.missingPlainAttrGetter();
        org.apache.syncope.core.provisioning.api.MappingManager.IntValues intValues18 = defaultMappingManager1.getIntValues(externalResource2, provision3, item10, intAttrName13, attrSchemaType14, any15, accountGetter16, plainAttrGetter17);
        java.lang.String str19 = plainAttrValue0.getValueAsString(attrSchemaType14);
        java.lang.String str20 = plainAttrValue0.toString();
        org.apache.syncope.core.persistence.api.entity.PlainSchema plainSchema21 = null;
        // The following exception was thrown during execution in test generation
        try {
            plainAttrValue0.parseValue(plainSchema21, "-1");
            org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException; message: Cannot invoke \"org.apache.syncope.core.persistence.api.entity.PlainSchema.getType()\" because \"schema\" is null");
        } catch (java.lang.NullPointerException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(defaultMappingManager1);
        org.junit.Assert.assertNotNull(externalResource2);
        org.junit.Assert.assertNotNull(provision3);
        org.junit.Assert.assertNotNull(mapping6);
        org.junit.Assert.assertNotNull(mapping7);
        org.junit.Assert.assertNotNull(item10);
        org.junit.Assert.assertTrue("'" + boolean12 + "' != '" + false + "'", boolean12 == false);
        org.junit.Assert.assertNotNull(intAttrName13);
        org.junit.Assert.assertTrue("'" + attrSchemaType14 + "' != '" + org.apache.syncope.common.lib.types.AttrSchemaType.String + "'", attrSchemaType14.equals(org.apache.syncope.common.lib.types.AttrSchemaType.String));
        org.junit.Assert.assertNotNull(any15);
        org.junit.Assert.assertNotNull(accountGetter16);
        org.junit.Assert.assertNotNull(plainAttrGetter17);
        org.junit.Assert.assertNotNull(intValues18);
        org.junit.Assert.assertEquals("'" + str19 + "' != '" + "" + "'", str19, "");
    }

    @Test
    public void test77() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "DefaultMappingManagerRandoopRegressionS00.test77");
        org.apache.syncope.core.provisioning.java.DefaultMappingManager defaultMappingManager0 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.manager();
        org.apache.syncope.core.persistence.api.entity.ExternalResource externalResource1 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.resource();
        org.apache.syncope.common.lib.to.Provision provision2 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.plainAndKeyProvision();
        provision2.setUidOnCreate("mailAlternateAddress");
        org.apache.syncope.common.lib.to.Mapping mapping5 = provision2.getMapping();
        org.apache.syncope.common.lib.to.Mapping mapping6 = provision2.getMapping();
        org.apache.syncope.common.lib.to.Item item7 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.optionalMissingPlainItem();
        java.lang.String str8 = item7.toString();
        java.lang.String str9 = item7.toString();
        item7.setPassword(true);
        org.apache.syncope.core.provisioning.api.IntAttrName intAttrName12 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.usernameIntAttrName();
        org.apache.syncope.common.lib.types.AttrSchemaType attrSchemaType13 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.stringSchemaType();
        org.apache.syncope.core.persistence.api.entity.Any any14 = null;
        org.apache.syncope.core.provisioning.api.AccountGetter accountGetter15 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.defaultAccountGetter();
        org.apache.syncope.core.provisioning.api.PlainAttrGetter plainAttrGetter16 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.missingPlainAttrGetter();
        org.apache.syncope.core.provisioning.api.MappingManager.IntValues intValues17 = defaultMappingManager0.getIntValues(externalResource1, provision2, item7, intAttrName12, attrSchemaType13, any14, accountGetter15, plainAttrGetter16);
        provision2.setIgnoreCaseMatch(true);
        java.lang.String str20 = provision2.getUidOnCreate();
        provision2.setObjectClass("secret");
        boolean boolean23 = provision2.isIgnoreCaseMatch();
        java.lang.String str24 = provision2.getObjectClass();
        org.junit.Assert.assertNotNull(defaultMappingManager0);
        org.junit.Assert.assertNotNull(externalResource1);
        org.junit.Assert.assertNotNull(provision2);
        org.junit.Assert.assertNotNull(mapping5);
        org.junit.Assert.assertNotNull(mapping6);
        org.junit.Assert.assertNotNull(item7);
        org.junit.Assert.assertNotNull(intAttrName12);
        org.junit.Assert.assertTrue("'" + attrSchemaType13 + "' != '" + org.apache.syncope.common.lib.types.AttrSchemaType.String + "'", attrSchemaType13.equals(org.apache.syncope.common.lib.types.AttrSchemaType.String));
        org.junit.Assert.assertNotNull(accountGetter15);
        org.junit.Assert.assertNotNull(plainAttrGetter16);
        org.junit.Assert.assertNotNull(intValues17);
        org.junit.Assert.assertEquals("'" + str20 + "' != '" + "mailAlternateAddress" + "'", str20, "mailAlternateAddress");
        org.junit.Assert.assertTrue("'" + boolean23 + "' != '" + true + "'", boolean23 == true);
        org.junit.Assert.assertEquals("'" + str24 + "' != '" + "secret" + "'", str24, "secret");
    }

    @Test
    public void test78() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "DefaultMappingManagerRandoopRegressionS00.test78");
        org.apache.syncope.core.provisioning.java.DefaultMappingManager defaultMappingManager0 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.manager();
        org.apache.syncope.common.lib.to.Item item1 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.keyItem();
        java.lang.String str2 = item1.getIntAttrName();
        java.util.List<java.lang.String> strList3 = item1.getTransformers();
        org.identityconnectors.framework.common.objects.Attribute attribute5 = org.identityconnectors.framework.common.objects.AttributeBuilder.build("initial-group");
        org.apache.syncope.common.lib.to.Provision provision6 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.plainAndKeyProvision();
        provision6.setUidOnCreate("mailAlternateAddress");
        org.apache.syncope.common.lib.to.Mapping mapping9 = provision6.getMapping();
        org.apache.syncope.common.lib.to.Mapping mapping10 = provision6.getMapping();
        org.apache.syncope.common.lib.to.RealmTO realmTO11 = org.apache.syncope.core.provisioning.java.randoopsupport.DefaultMappingManagerRandoopFactory.realmTO();
        java.util.Optional<org.apache.syncope.common.lib.Attr> attrOptional13 = realmTO11.getPlainAttr("");
        java.util.Set<org.apache.syncope.common.lib.Attr> attrSet14 = realmTO11.getDerAttrs();
        java.util.Set<org.apache.syncope.common.lib.Attr> attrSet15 = realmTO11.getPlainAttrs();
        boolean boolean16 = mapping10.equals((java.lang.Object) realmTO11);
        java.util.Optional<org.apache.syncope.common.lib.Attr> attrOptional18 = realmTO11.getDerAttr("GROUP");
        java.lang.String str19 = realmTO11.getTicketExpirationPolicy();
        defaultMappingManager0.setIntValues(item1, attribute5, realmTO11);
        org.apache.syncope.common.lib.types.MappingPurpose mappingPurpose21 = item1.getPurpose();
        org.junit.Assert.assertNotNull(defaultMappingManager0);
        org.junit.Assert.assertNotNull(item1);
        org.junit.Assert.assertEquals("'" + str2 + "' != '" + "key" + "'", str2, "key");
        org.junit.Assert.assertNotNull(strList3);
        org.junit.Assert.assertNotNull(attribute5);
        org.junit.Assert.assertNotNull(provision6);
        org.junit.Assert.assertNotNull(mapping9);
        org.junit.Assert.assertNotNull(mapping10);
        org.junit.Assert.assertNotNull(realmTO11);
        org.junit.Assert.assertNotNull(attrOptional13);
        org.junit.Assert.assertNotNull(attrSet14);
        org.junit.Assert.assertNotNull(attrSet15);
        org.junit.Assert.assertTrue("'" + boolean16 + "' != '" + false + "'", boolean16 == false);
        org.junit.Assert.assertNotNull(attrOptional18);
        org.junit.Assert.assertNull(str19);
        org.junit.Assert.assertTrue("'" + mappingPurpose21 + "' != '" + org.apache.syncope.common.lib.types.MappingPurpose.PROPAGATION + "'", mappingPurpose21.equals(org.apache.syncope.common.lib.types.MappingPurpose.PROPAGATION));
    }
}

