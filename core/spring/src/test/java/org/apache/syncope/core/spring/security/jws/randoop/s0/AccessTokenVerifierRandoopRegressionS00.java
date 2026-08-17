package org.apache.syncope.core.spring.security.jws.randoop.s0;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AccessTokenVerifierRandoopRegressionS00 {

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
    public void test001() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test001");
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm1 = com.nimbusds.jose.JWSAlgorithm.parse("fedcba9876543210fedcba9876543210");
        // The following exception was thrown during execution in test generation
        try {
            org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier3 = new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm1, "");
            org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException; message: Unsupported JWS algorithm: fedcba9876543210fedcba9876543210");
        } catch (java.lang.IllegalArgumentException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(jWSAlgorithm1);
    }

    @Test
    public void test002() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test002");
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm0 = com.nimbusds.jose.JWSAlgorithm.Ed448;
        boolean boolean2 = jWSAlgorithm0.equals((java.lang.Object) "\ufffd\n\n\001\000");
        com.nimbusds.jose.JWSHeader.Builder builder3 = new com.nimbusds.jose.JWSHeader.Builder(jWSAlgorithm0);
        // The following exception was thrown during execution in test generation
        try {
            org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier5 = new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm0, "HS256");
            org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException; message: Unsupported JWS algorithm: Ed448");
        } catch (java.lang.IllegalArgumentException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(jWSAlgorithm0);
        org.junit.Assert.assertTrue("'" + boolean2 + "' != '" + false + "'", boolean2 == false);
    }

    @Test
    public void test003() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test003");
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm1 = com.nimbusds.jose.JWSAlgorithm.parse("\"\"");
        // The following exception was thrown during execution in test generation
        try {
            org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier3 = new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm1, "[]");
            org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException; message: Unsupported JWS algorithm: \"\"");
        } catch (java.lang.IllegalArgumentException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(jWSAlgorithm1);
    }

    @Test
    public void test004() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test004");
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm0 = com.nimbusds.jose.JWSAlgorithm.Ed448;
        // The following exception was thrown during execution in test generation
        try {
            org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier2 = new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm0, "");
            org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException; message: Unsupported JWS algorithm: Ed448");
        } catch (java.lang.IllegalArgumentException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(jWSAlgorithm0);
    }

    @Test
    public void test005() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test005");
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm0 = com.nimbusds.jose.JWSAlgorithm.PS256;
        com.nimbusds.jose.JWSHeader.Builder builder1 = new com.nimbusds.jose.JWSHeader.Builder(jWSAlgorithm0);
        // The following exception was thrown during execution in test generation
        try {
            org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier3 = new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm0, "/wH//wA=");
            org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException; message: A key pair is required, in the 'private:public' format");
        } catch (java.lang.IllegalArgumentException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(jWSAlgorithm0);
    }

    @Test
    public void test006() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test006");
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm0 = com.nimbusds.jose.JWSAlgorithm.HS384;
        java.lang.String str1 = jWSAlgorithm0.getName();
        // The following exception was thrown during execution in test generation
        try {
            org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier3 = new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm0, "PS256");
            org.junit.Assert.fail("Expected exception of type com.nimbusds.jose.KeyLengthException; message: The secret length must be at least 256 bits");
        } catch (com.nimbusds.jose.KeyLengthException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(jWSAlgorithm0);
        org.junit.Assert.assertEquals("'" + str1 + "' != '" + "HS384" + "'", str1, "HS384");
    }

    @Test
    public void test007() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test007");
        com.nimbusds.jose.JWSAlgorithm[] jWSAlgorithmArray0 = new com.nimbusds.jose.JWSAlgorithm[] {};
        com.nimbusds.jose.JWSAlgorithm.Family family1 = new com.nimbusds.jose.JWSAlgorithm.Family(jWSAlgorithmArray0);
        com.nimbusds.jose.JWSAlgorithm.Family family2 = com.nimbusds.jose.JWSAlgorithm.Family.ED;
        boolean boolean3 = family1.containsAll((java.util.Collection<com.nimbusds.jose.JWSAlgorithm>) family2);
        java.lang.Object[] objArray4 = family2.toArray();
        java.lang.Object obj5 = family2.clone();
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm6 = com.nimbusds.jose.JWSAlgorithm.HS256;
        com.nimbusds.jose.JWSAlgorithm[] jWSAlgorithmArray7 = new com.nimbusds.jose.JWSAlgorithm[] {};
        com.nimbusds.jose.JWSAlgorithm.Family family8 = new com.nimbusds.jose.JWSAlgorithm.Family(jWSAlgorithmArray7);
        com.nimbusds.jose.JWSAlgorithm.Family family9 = com.nimbusds.jose.JWSAlgorithm.Family.ED;
        boolean boolean10 = family8.containsAll((java.util.Collection<com.nimbusds.jose.JWSAlgorithm>) family9);
        boolean boolean11 = jWSAlgorithm6.equals((java.lang.Object) family8);
        family2.addFirst(jWSAlgorithm6);
        // The following exception was thrown during execution in test generation
        try {
            org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier14 = new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm6, "none");
            org.junit.Assert.fail("Expected exception of type com.nimbusds.jose.KeyLengthException; message: The secret length must be at least 256 bits");
        } catch (com.nimbusds.jose.KeyLengthException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(jWSAlgorithmArray0);
        org.junit.Assert.assertArrayEquals(jWSAlgorithmArray0, new com.nimbusds.jose.JWSAlgorithm[] {});
        org.junit.Assert.assertNotNull(family2);
        org.junit.Assert.assertTrue("'" + boolean3 + "' != '" + false + "'", boolean3 == false);
        org.junit.Assert.assertNotNull(objArray4);
// flaky "1) test007(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertEquals(java.util.Arrays.deepToString(objArray4), "[ES384, ES256, Ed25519, Ed448, HS384, HS256, EdDSA]");
// flaky "1) test007(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertEquals(java.util.Arrays.toString(objArray4), "[ES384, ES256, Ed25519, Ed448, HS384, HS256, EdDSA]");
        org.junit.Assert.assertNotNull(obj5);
// flaky "1) test007(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertEquals(obj5.toString(), "[ES384, ES256, Ed25519, Ed448, HS384, HS256, EdDSA]");
// flaky "1) test007(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertEquals(java.lang.String.valueOf(obj5), "[ES384, ES256, Ed25519, Ed448, HS384, HS256, EdDSA]");
// flaky "1) test007(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertEquals(java.util.Objects.toString(obj5), "[ES384, ES256, Ed25519, Ed448, HS384, HS256, EdDSA]");
        org.junit.Assert.assertNotNull(jWSAlgorithm6);
        org.junit.Assert.assertNotNull(jWSAlgorithmArray7);
        org.junit.Assert.assertArrayEquals(jWSAlgorithmArray7, new com.nimbusds.jose.JWSAlgorithm[] {});
        org.junit.Assert.assertNotNull(family9);
        org.junit.Assert.assertTrue("'" + boolean10 + "' != '" + false + "'", boolean10 == false);
        org.junit.Assert.assertTrue("'" + boolean11 + "' != '" + false + "'", boolean11 == false);
    }

    @Test
    public void test008() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test008");
        com.nimbusds.jose.JWSAlgorithm.Family family0 = com.nimbusds.jose.JWSAlgorithm.Family.ED;
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm1 = com.nimbusds.jose.JWSAlgorithm.EdDSA;
        family0.addLast(jWSAlgorithm1);
        byte[] byteArray8 = new byte[] { (byte) -1, (byte) 10, (byte) 10, (byte) 1, (byte) 0 };
        com.nimbusds.jose.util.Base64URL base64URL9 = com.nimbusds.jose.util.Base64URL.encode(byteArray8);
        byte[] byteArray10 = base64URL9.decode();
        boolean boolean11 = family0.equals((java.lang.Object) byteArray10);
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm12 = com.nimbusds.jose.JWSAlgorithm.ES384;
        family0.addFirst(jWSAlgorithm12);
        java.lang.String str14 = jWSAlgorithm12.toJSONString();
        java.lang.String str15 = jWSAlgorithm12.getName();
        // The following exception was thrown during execution in test generation
        try {
            org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier17 = new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm12, "\"ES384\"");
            org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException; message: Unsupported JWS algorithm: ES384");
        } catch (java.lang.IllegalArgumentException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(family0);
        org.junit.Assert.assertNotNull(jWSAlgorithm1);
        org.junit.Assert.assertNotNull(byteArray8);
        org.junit.Assert.assertArrayEquals(byteArray8, new byte[] { (byte) -1, (byte) 10, (byte) 10, (byte) 1, (byte) 0 });
        org.junit.Assert.assertNotNull(base64URL9);
        org.junit.Assert.assertNotNull(byteArray10);
        org.junit.Assert.assertArrayEquals(byteArray10, new byte[] { (byte) -1, (byte) 10, (byte) 10, (byte) 1, (byte) 0 });
        org.junit.Assert.assertTrue("'" + boolean11 + "' != '" + false + "'", boolean11 == false);
        org.junit.Assert.assertNotNull(jWSAlgorithm12);
        org.junit.Assert.assertEquals("'" + str14 + "' != '" + "\"ES384\"" + "'", str14, "\"ES384\"");
        org.junit.Assert.assertEquals("'" + str15 + "' != '" + "ES384" + "'", str15, "ES384");
    }

    @Test
    public void test009() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test009");
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm0 = com.nimbusds.jose.JWSAlgorithm.RS512;
        // The following exception was thrown during execution in test generation
        try {
            org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier2 = new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm0, "header.payload");
            org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException; message: A key pair is required, in the 'private:public' format");
        } catch (java.lang.IllegalArgumentException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(jWSAlgorithm0);
    }

    @Test
    public void test010() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test010");
        com.nimbusds.jose.JWSAlgorithm.Family family0 = com.nimbusds.jose.JWSAlgorithm.Family.HMAC_SHA;
        java.lang.Object[] objArray1 = family0.toArray();
        java.util.SequencedSet<com.nimbusds.jose.JWSAlgorithm> jWSAlgorithmSet2 = family0.reversed();
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm3 = com.nimbusds.jose.JWSAlgorithm.ES512;
        com.nimbusds.jose.JWSHeader jWSHeader4 = new com.nimbusds.jose.JWSHeader(jWSAlgorithm3);
        family0.addLast(jWSAlgorithm3);
        java.lang.String str6 = jWSAlgorithm3.toString();
        org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier8 = new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm3, "0123456789abcdef0123456789abcdef0123456789abcdef");
        // The following exception was thrown during execution in test generation
        try {
            org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier10 = new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm3, "\"\\\"\\\"\"");
            org.junit.Assert.fail("Expected exception of type com.nimbusds.jose.KeyLengthException; message: The secret length must be at least 256 bits");
        } catch (com.nimbusds.jose.KeyLengthException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(family0);
        org.junit.Assert.assertNotNull(objArray1);
// flaky "2) test010(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertEquals(java.util.Arrays.deepToString(objArray1), "[HS256, HS384, HS512, ES512]");
// flaky "2) test010(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertEquals(java.util.Arrays.toString(objArray1), "[HS256, HS384, HS512, ES512]");
        org.junit.Assert.assertNotNull(jWSAlgorithmSet2);
        org.junit.Assert.assertNotNull(jWSAlgorithm3);
        org.junit.Assert.assertEquals("'" + str6 + "' != '" + "ES512" + "'", str6, "ES512");
    }

    @Test
    public void test011() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test011");
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm1 = new com.nimbusds.jose.JWSAlgorithm("_woKAQA");
        // The following exception was thrown during execution in test generation
        try {
            org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier3 = new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm1, "\"_woKAQA\"");
            org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException; message: Unsupported JWS algorithm: _woKAQA");
        } catch (java.lang.IllegalArgumentException e) {
            // Expected exception.
        }
    }

    @Test
    public void test012() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test012");
        com.nimbusds.jose.JWSAlgorithm.Family family0 = com.nimbusds.jose.JWSAlgorithm.Family.HMAC_SHA;
        java.lang.Object[] objArray1 = family0.toArray();
        java.util.SequencedSet<com.nimbusds.jose.JWSAlgorithm> jWSAlgorithmSet2 = family0.reversed();
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm3 = com.nimbusds.jose.JWSAlgorithm.ES512;
        com.nimbusds.jose.JWSHeader jWSHeader4 = new com.nimbusds.jose.JWSHeader(jWSAlgorithm3);
        family0.addLast(jWSAlgorithm3);
        java.lang.String str6 = jWSAlgorithm3.toString();
        org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier8 = new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm3, "0123456789abcdef0123456789abcdef0123456789abcdef");
        java.util.Set<com.nimbusds.jose.JWSAlgorithm> jWSAlgorithmSet9 = accessTokenJWSVerifier8.supportedJWSAlgorithms();
        java.util.Set<com.nimbusds.jose.JWSAlgorithm> jWSAlgorithmSet10 = accessTokenJWSVerifier8.supportedJWSAlgorithms();
        java.util.Set<com.nimbusds.jose.JWSAlgorithm> jWSAlgorithmSet11 = accessTokenJWSVerifier8.supportedJWSAlgorithms();
        java.util.Set<com.nimbusds.jose.JWSAlgorithm> jWSAlgorithmSet12 = accessTokenJWSVerifier8.supportedJWSAlgorithms();
        org.junit.Assert.assertNotNull(family0);
        org.junit.Assert.assertNotNull(objArray1);
        org.junit.Assert.assertEquals(java.util.Arrays.deepToString(objArray1), "[HS256, HS384, HS512, ES512]");
        org.junit.Assert.assertEquals(java.util.Arrays.toString(objArray1), "[HS256, HS384, HS512, ES512]");
        org.junit.Assert.assertNotNull(jWSAlgorithmSet2);
        org.junit.Assert.assertNotNull(jWSAlgorithm3);
        org.junit.Assert.assertEquals("'" + str6 + "' != '" + "ES512" + "'", str6, "ES512");
        org.junit.Assert.assertNotNull(jWSAlgorithmSet9);
        org.junit.Assert.assertNotNull(jWSAlgorithmSet10);
        org.junit.Assert.assertNotNull(jWSAlgorithmSet11);
        org.junit.Assert.assertNotNull(jWSAlgorithmSet12);
    }

    @Test
    public void test013() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test013");
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm1 = com.nimbusds.jose.JWSAlgorithm.parse("0123456789abcdef0123456789abcdef0123456789abcdef");
        // The following exception was thrown during execution in test generation
        try {
            org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier3 = new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm1, "[null]");
            org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException; message: Unsupported JWS algorithm: 0123456789abcdef0123456789abcdef0123456789abcdef");
        } catch (java.lang.IllegalArgumentException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(jWSAlgorithm1);
    }

    @Test
    public void test014() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test014");
        com.nimbusds.jose.JWSAlgorithm.Family family0 = com.nimbusds.jose.JWSAlgorithm.Family.HMAC_SHA;
        java.lang.Object[] objArray1 = family0.toArray();
        java.util.SequencedSet<com.nimbusds.jose.JWSAlgorithm> jWSAlgorithmSet2 = family0.reversed();
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm3 = com.nimbusds.jose.JWSAlgorithm.ES512;
        com.nimbusds.jose.JWSHeader jWSHeader4 = new com.nimbusds.jose.JWSHeader(jWSAlgorithm3);
        family0.addLast(jWSAlgorithm3);
        java.lang.String str6 = jWSAlgorithm3.toString();
        org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier8 = new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm3, "0123456789abcdef0123456789abcdef0123456789abcdef");
        java.util.Set<com.nimbusds.jose.JWSAlgorithm> jWSAlgorithmSet9 = accessTokenJWSVerifier8.supportedJWSAlgorithms();
        com.nimbusds.jose.jca.JCAContext jCAContext10 = accessTokenJWSVerifier8.getJCAContext();
        org.junit.Assert.assertNotNull(family0);
        org.junit.Assert.assertNotNull(objArray1);
        org.junit.Assert.assertEquals(java.util.Arrays.deepToString(objArray1), "[HS256, HS384, HS512, ES512]");
        org.junit.Assert.assertEquals(java.util.Arrays.toString(objArray1), "[HS256, HS384, HS512, ES512]");
        org.junit.Assert.assertNotNull(jWSAlgorithmSet2);
        org.junit.Assert.assertNotNull(jWSAlgorithm3);
        org.junit.Assert.assertEquals("'" + str6 + "' != '" + "ES512" + "'", str6, "ES512");
        org.junit.Assert.assertNotNull(jWSAlgorithmSet9);
        org.junit.Assert.assertNotNull(jCAContext10);
    }

    @Test
    public void test015() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test015");
        com.nimbusds.jose.JWSAlgorithm.Family family0 = com.nimbusds.jose.JWSAlgorithm.Family.HMAC_SHA;
        java.lang.Object[] objArray1 = family0.toArray();
        java.util.SequencedSet<com.nimbusds.jose.JWSAlgorithm> jWSAlgorithmSet2 = family0.reversed();
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm3 = com.nimbusds.jose.JWSAlgorithm.ES512;
        com.nimbusds.jose.JWSHeader jWSHeader4 = new com.nimbusds.jose.JWSHeader(jWSAlgorithm3);
        family0.addLast(jWSAlgorithm3);
        java.lang.String str6 = jWSAlgorithm3.toString();
        org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier8 = new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm3, "0123456789abcdef0123456789abcdef0123456789abcdef");
        com.nimbusds.jose.jca.JCAContext jCAContext9 = accessTokenJWSVerifier8.getJCAContext();
        com.nimbusds.jose.jca.JCAContext jCAContext10 = accessTokenJWSVerifier8.getJCAContext();
        com.nimbusds.jose.jca.JCAContext jCAContext11 = accessTokenJWSVerifier8.getJCAContext();
        org.junit.Assert.assertNotNull(family0);
        org.junit.Assert.assertNotNull(objArray1);
        org.junit.Assert.assertEquals(java.util.Arrays.deepToString(objArray1), "[HS256, HS384, HS512, ES512]");
        org.junit.Assert.assertEquals(java.util.Arrays.toString(objArray1), "[HS256, HS384, HS512, ES512]");
        org.junit.Assert.assertNotNull(jWSAlgorithmSet2);
        org.junit.Assert.assertNotNull(jWSAlgorithm3);
        org.junit.Assert.assertEquals("'" + str6 + "' != '" + "ES512" + "'", str6, "ES512");
        org.junit.Assert.assertNotNull(jCAContext9);
        org.junit.Assert.assertNotNull(jCAContext10);
        org.junit.Assert.assertNotNull(jCAContext11);
    }

    @Test
    public void test016() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test016");
        com.nimbusds.jose.JWSAlgorithm.Family family0 = com.nimbusds.jose.JWSAlgorithm.Family.HMAC_SHA;
        java.lang.Object[] objArray1 = family0.toArray();
        java.util.SequencedSet<com.nimbusds.jose.JWSAlgorithm> jWSAlgorithmSet2 = family0.reversed();
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm3 = com.nimbusds.jose.JWSAlgorithm.ES512;
        com.nimbusds.jose.JWSHeader jWSHeader4 = new com.nimbusds.jose.JWSHeader(jWSAlgorithm3);
        family0.addLast(jWSAlgorithm3);
        java.lang.String str6 = jWSAlgorithm3.toString();
        org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier8 = new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm3, "0123456789abcdef0123456789abcdef0123456789abcdef");
        com.nimbusds.jose.jca.JCAContext jCAContext9 = accessTokenJWSVerifier8.getJCAContext();
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm10 = com.nimbusds.jose.JWSAlgorithm.Ed25519;
        com.nimbusds.jose.Requirement requirement11 = jWSAlgorithm10.getRequirement();
        com.nimbusds.jose.JWSHeader jWSHeader12 = new com.nimbusds.jose.JWSHeader(jWSAlgorithm10);
        java.net.URI uRI13 = jWSHeader12.getX509CertURL();
        com.nimbusds.jose.util.Base64URL base64URL15 = new com.nimbusds.jose.util.Base64URL("/wH//wA=");
        byte[] byteArray16 = base64URL15.decode();
        com.nimbusds.jose.util.Base64URL base64URL18 = com.nimbusds.jose.util.Base64URL.encode("HS384");
        // The following exception was thrown during execution in test generation
        try {
            boolean boolean19 = accessTokenJWSVerifier8.verify(jWSHeader12, byteArray16, base64URL18);
            org.junit.Assert.fail("Expected exception of type com.nimbusds.jose.JOSEException; message: Unsupported JWS algorithm Ed25519, must be HS256, HS384 or HS512");
        } catch (com.nimbusds.jose.JOSEException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(family0);
        org.junit.Assert.assertNotNull(objArray1);
        org.junit.Assert.assertEquals(java.util.Arrays.deepToString(objArray1), "[HS256, HS384, HS512, ES512]");
        org.junit.Assert.assertEquals(java.util.Arrays.toString(objArray1), "[HS256, HS384, HS512, ES512]");
        org.junit.Assert.assertNotNull(jWSAlgorithmSet2);
        org.junit.Assert.assertNotNull(jWSAlgorithm3);
        org.junit.Assert.assertEquals("'" + str6 + "' != '" + "ES512" + "'", str6, "ES512");
        org.junit.Assert.assertNotNull(jCAContext9);
        org.junit.Assert.assertNotNull(jWSAlgorithm10);
        org.junit.Assert.assertTrue("'" + requirement11 + "' != '" + com.nimbusds.jose.Requirement.OPTIONAL + "'", requirement11.equals(com.nimbusds.jose.Requirement.OPTIONAL));
        org.junit.Assert.assertNull(uRI13);
        org.junit.Assert.assertNotNull(byteArray16);
        org.junit.Assert.assertArrayEquals(byteArray16, new byte[] { (byte) -1, (byte) 1, (byte) -1, (byte) -1, (byte) 0 });
        org.junit.Assert.assertNotNull(base64URL18);
    }

    @Test
    public void test017() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test017");
        com.nimbusds.jose.JWSAlgorithm.Family family0 = com.nimbusds.jose.JWSAlgorithm.Family.RSA;
        boolean boolean1 = family0.isEmpty();
        com.nimbusds.jose.JWSAlgorithm.Family family2 = com.nimbusds.jose.JWSAlgorithm.Family.HMAC_SHA;
        java.lang.Object[] objArray3 = family2.toArray();
        java.util.SequencedSet<com.nimbusds.jose.JWSAlgorithm> jWSAlgorithmSet4 = family2.reversed();
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm5 = com.nimbusds.jose.JWSAlgorithm.ES512;
        com.nimbusds.jose.JWSHeader jWSHeader6 = new com.nimbusds.jose.JWSHeader(jWSAlgorithm5);
        family2.addLast(jWSAlgorithm5);
        java.lang.String str8 = jWSAlgorithm5.toString();
        org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier10 = new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm5, "0123456789abcdef0123456789abcdef0123456789abcdef");
        family0.addLast(jWSAlgorithm5);
        org.junit.Assert.assertNotNull(family0);
        org.junit.Assert.assertTrue("'" + boolean1 + "' != '" + false + "'", boolean1 == false);
        org.junit.Assert.assertNotNull(family2);
        org.junit.Assert.assertNotNull(objArray3);
        org.junit.Assert.assertEquals(java.util.Arrays.deepToString(objArray3), "[HS256, HS384, HS512, ES512]");
        org.junit.Assert.assertEquals(java.util.Arrays.toString(objArray3), "[HS256, HS384, HS512, ES512]");
        org.junit.Assert.assertNotNull(jWSAlgorithmSet4);
        org.junit.Assert.assertNotNull(jWSAlgorithm5);
        org.junit.Assert.assertEquals("'" + str8 + "' != '" + "ES512" + "'", str8, "ES512");
    }

    @Test
    public void test018() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test018");
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm0 = com.nimbusds.jose.JWSAlgorithm.RS256;
        java.lang.String str1 = jWSAlgorithm0.toString();
        // The following exception was thrown during execution in test generation
        try {
            org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier3 = new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm0, "Ed448");
            org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException; message: A key pair is required, in the 'private:public' format");
        } catch (java.lang.IllegalArgumentException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(jWSAlgorithm0);
        org.junit.Assert.assertEquals("'" + str1 + "' != '" + "RS256" + "'", str1, "RS256");
    }

    @Test
    public void test019() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test019");
        com.nimbusds.jose.JWSAlgorithm.Family family0 = com.nimbusds.jose.JWSAlgorithm.Family.ED;
        boolean boolean1 = family0.isEmpty();
        com.nimbusds.jose.JWSAlgorithm[] jWSAlgorithmArray2 = new com.nimbusds.jose.JWSAlgorithm[] {};
        com.nimbusds.jose.JWSAlgorithm.Family family3 = new com.nimbusds.jose.JWSAlgorithm.Family(jWSAlgorithmArray2);
        boolean boolean5 = family3.contains((java.lang.Object) "0123456789abcdef0123456789abcdef");
        boolean boolean7 = family3.equals((java.lang.Object) 0L);
        java.util.SequencedSet<com.nimbusds.jose.JWSAlgorithm> jWSAlgorithmSet8 = family3.reversed();
        com.nimbusds.jose.JWSAlgorithm.Family family9 = com.nimbusds.jose.JWSAlgorithm.Family.HMAC_SHA;
        java.lang.Object[] objArray10 = family9.toArray();
        boolean boolean11 = family3.equals((java.lang.Object) family9);
        boolean boolean12 = family0.containsAll((java.util.Collection<com.nimbusds.jose.JWSAlgorithm>) family3);
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm13 = com.nimbusds.jose.JWSAlgorithm.ES512;
        com.nimbusds.jose.JWSHeader jWSHeader14 = new com.nimbusds.jose.JWSHeader(jWSAlgorithm13);
        java.lang.String str15 = jWSAlgorithm13.toJSONString();
        family0.addLast(jWSAlgorithm13);
        // The following exception was thrown during execution in test generation
        try {
            org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier18 = new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm13, "RWQ0NDg=");
            org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException; message: A key pair is required, in the 'private:public' format");
        } catch (java.lang.IllegalArgumentException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(family0);
        org.junit.Assert.assertTrue("'" + boolean1 + "' != '" + false + "'", boolean1 == false);
        org.junit.Assert.assertNotNull(jWSAlgorithmArray2);
        org.junit.Assert.assertArrayEquals(jWSAlgorithmArray2, new com.nimbusds.jose.JWSAlgorithm[] {});
        org.junit.Assert.assertTrue("'" + boolean5 + "' != '" + false + "'", boolean5 == false);
        org.junit.Assert.assertTrue("'" + boolean7 + "' != '" + false + "'", boolean7 == false);
        org.junit.Assert.assertNotNull(jWSAlgorithmSet8);
        org.junit.Assert.assertNotNull(family9);
        org.junit.Assert.assertNotNull(objArray10);
        org.junit.Assert.assertEquals(java.util.Arrays.deepToString(objArray10), "[HS256, HS384, HS512, ES512]");
        org.junit.Assert.assertEquals(java.util.Arrays.toString(objArray10), "[HS256, HS384, HS512, ES512]");
        org.junit.Assert.assertTrue("'" + boolean11 + "' != '" + false + "'", boolean11 == false);
        org.junit.Assert.assertTrue("'" + boolean12 + "' != '" + true + "'", boolean12 == true);
        org.junit.Assert.assertNotNull(jWSAlgorithm13);
        org.junit.Assert.assertEquals("'" + str15 + "' != '" + "\"ES512\"" + "'", str15, "\"ES512\"");
    }

    @Test
    public void test020() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test020");
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm0 = com.nimbusds.jose.JWSAlgorithm.ES256K;
        // The following exception was thrown during execution in test generation
        try {
            org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier2 = new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm0, "\"77+9CgoBAA\\u003d\\u003d\"");
            org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException; message: Unsupported JWS algorithm: ES256K");
        } catch (java.lang.IllegalArgumentException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(jWSAlgorithm0);
    }

    @Test
    public void test021() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test021");
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm0 = null;
        // The following exception was thrown during execution in test generation
        try {
            org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier2 = new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm0, "\"ES384\"");
            org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException; message: Cannot invoke \"com.nimbusds.jose.JWSAlgorithm.getName()\" because \"jwsAlgorithm\" is null");
        } catch (java.lang.NullPointerException e) {
            // Expected exception.
        }
    }

    @Test
    public void test022() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test022");
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm3 = com.nimbusds.jose.JWSAlgorithm.HS256;
        com.nimbusds.jose.Requirement requirement4 = jWSAlgorithm3.getRequirement();
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm5 = new com.nimbusds.jose.JWSAlgorithm("HS384", requirement4);
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm6 = new com.nimbusds.jose.JWSAlgorithm("[null]", requirement4);
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm7 = new com.nimbusds.jose.JWSAlgorithm("\ufffd\n\n\001\000", requirement4);
        com.nimbusds.jose.JWSHeader jWSHeader8 = new com.nimbusds.jose.JWSHeader(jWSAlgorithm7);
        // The following exception was thrown during execution in test generation
        try {
            org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier10 = new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm7, "MDEyMzQ1Njc4OWFiY2RlZjAxMjM0NTY3ODlhYmNkZWY");
            org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException; message: Unsupported JWS algorithm: ?????");
        } catch (java.lang.IllegalArgumentException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(jWSAlgorithm3);
        org.junit.Assert.assertTrue("'" + requirement4 + "' != '" + com.nimbusds.jose.Requirement.REQUIRED + "'", requirement4.equals(com.nimbusds.jose.Requirement.REQUIRED));
    }

    @Test
    public void test023() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test023");
        com.nimbusds.jose.Algorithm algorithm1 = com.nimbusds.jose.Algorithm.NONE;
        java.lang.String str2 = algorithm1.getName();
        com.nimbusds.jose.Requirement requirement3 = algorithm1.getRequirement();
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm4 = new com.nimbusds.jose.JWSAlgorithm("header.payload", requirement3);
        // The following exception was thrown during execution in test generation
        try {
            org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier6 = new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm4, "hi!");
            org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException; message: Unsupported JWS algorithm: header.payload");
        } catch (java.lang.IllegalArgumentException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(algorithm1);
        org.junit.Assert.assertEquals("'" + str2 + "' != '" + "none" + "'", str2, "none");
        org.junit.Assert.assertTrue("'" + requirement3 + "' != '" + com.nimbusds.jose.Requirement.REQUIRED + "'", requirement3.equals(com.nimbusds.jose.Requirement.REQUIRED));
    }

    @Test
    public void test024() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test024");
        com.nimbusds.jose.JWSAlgorithm[] jWSAlgorithmArray0 = new com.nimbusds.jose.JWSAlgorithm[] {};
        com.nimbusds.jose.JWSAlgorithm.Family family1 = new com.nimbusds.jose.JWSAlgorithm.Family(jWSAlgorithmArray0);
        boolean boolean3 = family1.contains((java.lang.Object) "0123456789abcdef0123456789abcdef");
        boolean boolean5 = family1.equals((java.lang.Object) 0L);
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm7 = com.nimbusds.jose.JWSAlgorithm.parse("ES256");
        boolean boolean8 = family1.contains((java.lang.Object) jWSAlgorithm7);
        // The following exception was thrown during execution in test generation
        try {
            org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier10 = new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm7, "Ed25519");
            org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException; message: Unsupported JWS algorithm: ES256");
        } catch (java.lang.IllegalArgumentException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(jWSAlgorithmArray0);
        org.junit.Assert.assertArrayEquals(jWSAlgorithmArray0, new com.nimbusds.jose.JWSAlgorithm[] {});
        org.junit.Assert.assertTrue("'" + boolean3 + "' != '" + false + "'", boolean3 == false);
        org.junit.Assert.assertTrue("'" + boolean5 + "' != '" + false + "'", boolean5 == false);
        org.junit.Assert.assertNotNull(jWSAlgorithm7);
        org.junit.Assert.assertTrue("'" + boolean8 + "' != '" + false + "'", boolean8 == false);
    }

    @Test
    public void test025() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test025");
        com.nimbusds.jose.JWSAlgorithm[] jWSAlgorithmArray0 = new com.nimbusds.jose.JWSAlgorithm[] {};
        com.nimbusds.jose.JWSAlgorithm.Family family1 = new com.nimbusds.jose.JWSAlgorithm.Family(jWSAlgorithmArray0);
        boolean boolean3 = family1.contains((java.lang.Object) "0123456789abcdef0123456789abcdef");
        boolean boolean5 = family1.equals((java.lang.Object) 0L);
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm6 = com.nimbusds.jose.JWSAlgorithm.HS384;
        java.lang.String str7 = jWSAlgorithm6.getName();
        family1.addFirst(jWSAlgorithm6);
        java.lang.String str9 = jWSAlgorithm6.getName();
        // The following exception was thrown during execution in test generation
        try {
            org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier11 = new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm6, "eyJhbGciOiJFZDQ0OCJ9");
            org.junit.Assert.fail("Expected exception of type com.nimbusds.jose.KeyLengthException; message: The secret length must be at least 256 bits");
        } catch (com.nimbusds.jose.KeyLengthException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(jWSAlgorithmArray0);
        org.junit.Assert.assertArrayEquals(jWSAlgorithmArray0, new com.nimbusds.jose.JWSAlgorithm[] {});
        org.junit.Assert.assertTrue("'" + boolean3 + "' != '" + false + "'", boolean3 == false);
        org.junit.Assert.assertTrue("'" + boolean5 + "' != '" + false + "'", boolean5 == false);
        org.junit.Assert.assertNotNull(jWSAlgorithm6);
        org.junit.Assert.assertEquals("'" + str7 + "' != '" + "HS384" + "'", str7, "HS384");
        org.junit.Assert.assertEquals("'" + str9 + "' != '" + "HS384" + "'", str9, "HS384");
    }

    @Test
    public void test026() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test026");
        com.nimbusds.jose.JWSAlgorithm[] jWSAlgorithmArray0 = new com.nimbusds.jose.JWSAlgorithm[] {};
        com.nimbusds.jose.JWSAlgorithm.Family family1 = new com.nimbusds.jose.JWSAlgorithm.Family(jWSAlgorithmArray0);
        boolean boolean3 = family1.contains((java.lang.Object) "0123456789abcdef0123456789abcdef");
        boolean boolean5 = family1.equals((java.lang.Object) 0L);
        boolean boolean6 = family1.isEmpty();
        java.util.SequencedSet<com.nimbusds.jose.JWSAlgorithm> jWSAlgorithmSet7 = family1.reversed();
        java.lang.Object obj8 = family1.clone();
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm9 = com.nimbusds.jose.JWSAlgorithm.HS384;
        family1.addFirst(jWSAlgorithm9);
        java.util.stream.Stream<com.nimbusds.jose.JWSAlgorithm> jWSAlgorithmStream11 = family1.parallelStream();
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm13 = com.nimbusds.jose.JWSAlgorithm.parse("\"ZmVkY2JhOTg3NjU0MzIxMGZlZGNiYTk4NzY1NDMyMTA\\u003d\"");
        family1.addFirst(jWSAlgorithm13);
        // The following exception was thrown during execution in test generation
        try {
            org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier16 = new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm13, "\"RVMyNTY\"");
            org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException; message: A key pair is required, in the 'private:public' format");
        } catch (java.lang.IllegalArgumentException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(jWSAlgorithmArray0);
        org.junit.Assert.assertArrayEquals(jWSAlgorithmArray0, new com.nimbusds.jose.JWSAlgorithm[] {});
        org.junit.Assert.assertTrue("'" + boolean3 + "' != '" + false + "'", boolean3 == false);
        org.junit.Assert.assertTrue("'" + boolean5 + "' != '" + false + "'", boolean5 == false);
        org.junit.Assert.assertTrue("'" + boolean6 + "' != '" + true + "'", boolean6 == true);
        org.junit.Assert.assertNotNull(jWSAlgorithmSet7);
        org.junit.Assert.assertNotNull(obj8);
        org.junit.Assert.assertEquals(obj8.toString(), "[]");
        org.junit.Assert.assertEquals(java.lang.String.valueOf(obj8), "[]");
        org.junit.Assert.assertEquals(java.util.Objects.toString(obj8), "[]");
        org.junit.Assert.assertNotNull(jWSAlgorithm9);
        org.junit.Assert.assertNotNull(jWSAlgorithmStream11);
        org.junit.Assert.assertNotNull(jWSAlgorithm13);
    }

    @Test
    public void test027() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test027");
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm1 = com.nimbusds.jose.JWSAlgorithm.HS256;
        com.nimbusds.jose.JWSHeader jWSHeader2 = new com.nimbusds.jose.JWSHeader(jWSAlgorithm1);
        boolean boolean3 = jWSHeader2.isBase64URLEncodePayload();
        java.net.URI uRI4 = jWSHeader2.getX509CertURL();
        com.nimbusds.jose.JWSHeader jWSHeader5 = new com.nimbusds.jose.JWSHeader(jWSHeader2);
        java.lang.Object obj7 = jWSHeader5.getCustomParam("");
        com.nimbusds.jose.util.Base64URL base64URL8 = jWSHeader5.toBase64URL();
        com.nimbusds.jose.Algorithm algorithm9 = com.nimbusds.jose.Algorithm.NONE;
        java.lang.String str10 = algorithm9.getName();
        com.nimbusds.jose.Requirement requirement11 = algorithm9.getRequirement();
        boolean boolean12 = base64URL8.equals((java.lang.Object) requirement11);
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm13 = new com.nimbusds.jose.JWSAlgorithm("\"/wH//wA\\u003d\"", requirement11);
        // The following exception was thrown during execution in test generation
        try {
            org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier15 = new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm13, "[RS256, RS384, RS512, PS256, PS384, PS512]");
            org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException; message: Unsupported JWS algorithm: \"/wH//wA\\u003d\"");
        } catch (java.lang.IllegalArgumentException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(jWSAlgorithm1);
        org.junit.Assert.assertTrue("'" + boolean3 + "' != '" + true + "'", boolean3 == true);
        org.junit.Assert.assertNull(uRI4);
        org.junit.Assert.assertNull(obj7);
        org.junit.Assert.assertNotNull(base64URL8);
        org.junit.Assert.assertNotNull(algorithm9);
        org.junit.Assert.assertEquals("'" + str10 + "' != '" + "none" + "'", str10, "none");
        org.junit.Assert.assertTrue("'" + requirement11 + "' != '" + com.nimbusds.jose.Requirement.REQUIRED + "'", requirement11.equals(com.nimbusds.jose.Requirement.REQUIRED));
        org.junit.Assert.assertTrue("'" + boolean12 + "' != '" + false + "'", boolean12 == false);
    }

    @Test
    public void test028() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test028");
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm1 = new com.nimbusds.jose.JWSAlgorithm("PS256");
        com.nimbusds.jose.util.Base64URL base64URL3 = com.nimbusds.jose.util.Base64URL.encode("ES256");
        java.lang.String str4 = base64URL3.toJSONString();
        boolean boolean5 = jWSAlgorithm1.equals((java.lang.Object) str4);
        // The following exception was thrown during execution in test generation
        try {
            org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier7 = new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm1, "{\"alg\":\"Ed448\"}");
            org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException; message: Illegal base64 character 22");
        } catch (java.lang.IllegalArgumentException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(base64URL3);
        org.junit.Assert.assertEquals("'" + str4 + "' != '" + "\"RVMyNTY\"" + "'", str4, "\"RVMyNTY\"");
        org.junit.Assert.assertTrue("'" + boolean5 + "' != '" + false + "'", boolean5 == false);
    }

    @Test
    public void test029() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test029");
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm0 = com.nimbusds.jose.JWSAlgorithm.Ed25519;
        java.lang.String str1 = jWSAlgorithm0.getName();
        com.nimbusds.jose.JWSHeader.Builder builder2 = new com.nimbusds.jose.JWSHeader.Builder(jWSAlgorithm0);
        // The following exception was thrown during execution in test generation
        try {
            org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier4 = new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm0, "");
            org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException; message: Unsupported JWS algorithm: Ed25519");
        } catch (java.lang.IllegalArgumentException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(jWSAlgorithm0);
        org.junit.Assert.assertEquals("'" + str1 + "' != '" + "Ed25519" + "'", str1, "Ed25519");
    }

    @Test
    public void test030() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test030");
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm0 = com.nimbusds.jose.JWSAlgorithm.HS256;
        com.nimbusds.jose.JWSHeader jWSHeader1 = new com.nimbusds.jose.JWSHeader(jWSAlgorithm0);
        boolean boolean2 = jWSHeader1.isBase64URLEncodePayload();
        java.net.URI uRI3 = jWSHeader1.getX509CertURL();
        java.lang.String str4 = jWSHeader1.getKeyID();
        java.util.Map<java.lang.String, java.lang.Object> strMap5 = jWSHeader1.toJSONObject();
        com.nimbusds.jose.util.Base64URL base64URL7 = com.nimbusds.jose.util.Base64URL.encode("0123456789abcdef0123456789abcdef");
        com.nimbusds.jose.JWSHeader jWSHeader8 = com.nimbusds.jose.JWSHeader.parse(strMap5, base64URL7);
        com.nimbusds.jose.util.Base64URL base64URL9 = jWSHeader8.getX509CertThumbprint();
        java.lang.String str10 = jWSHeader8.getKeyID();
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm11 = jWSHeader8.getAlgorithm();
        // The following exception was thrown during execution in test generation
        try {
            org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier13 = new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm11, "");
            org.junit.Assert.fail("Expected exception of type com.nimbusds.jose.KeyLengthException; message: The secret length must be at least 256 bits");
        } catch (com.nimbusds.jose.KeyLengthException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(jWSAlgorithm0);
        org.junit.Assert.assertTrue("'" + boolean2 + "' != '" + true + "'", boolean2 == true);
        org.junit.Assert.assertNull(uRI3);
        org.junit.Assert.assertNull(str4);
        org.junit.Assert.assertNotNull(strMap5);
        org.junit.Assert.assertNotNull(base64URL7);
        org.junit.Assert.assertNotNull(jWSHeader8);
        org.junit.Assert.assertNull(base64URL9);
        org.junit.Assert.assertNull(str10);
        org.junit.Assert.assertNotNull(jWSAlgorithm11);
    }

    @Test
    public void test031() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test031");
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm0 = com.nimbusds.jose.JWSAlgorithm.ES384;
        com.nimbusds.jose.Requirement requirement1 = jWSAlgorithm0.getRequirement();
        // The following exception was thrown during execution in test generation
        try {
            org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier3 = new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm0, "\"ES384\"");
            org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException; message: Unsupported JWS algorithm: ES384");
        } catch (java.lang.IllegalArgumentException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(jWSAlgorithm0);
        org.junit.Assert.assertTrue("'" + requirement1 + "' != '" + com.nimbusds.jose.Requirement.OPTIONAL + "'", requirement1.equals(com.nimbusds.jose.Requirement.OPTIONAL));
    }

    @Test
    public void test032() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test032");
        com.nimbusds.jose.JWSAlgorithm[] jWSAlgorithmArray0 = new com.nimbusds.jose.JWSAlgorithm[] {};
        com.nimbusds.jose.JWSAlgorithm.Family family1 = new com.nimbusds.jose.JWSAlgorithm.Family(jWSAlgorithmArray0);
        com.nimbusds.jose.JWSAlgorithm.Family family2 = com.nimbusds.jose.JWSAlgorithm.Family.ED;
        boolean boolean3 = family1.containsAll((java.util.Collection<com.nimbusds.jose.JWSAlgorithm>) family2);
        java.lang.Object[] objArray4 = family2.toArray();
        int int5 = family2.size();
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm6 = com.nimbusds.jose.JWSAlgorithm.HS384;
        java.lang.String str7 = jWSAlgorithm6.getName();
        family2.addLast(jWSAlgorithm6);
        com.nimbusds.jose.JWSHeader.Builder builder9 = new com.nimbusds.jose.JWSHeader.Builder(jWSAlgorithm6);
        // The following exception was thrown during execution in test generation
        try {
            org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier11 = new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm6, "\"\\\"/wH//wA\\\\u003d\\\"\"");
            org.junit.Assert.fail("Expected exception of type com.nimbusds.jose.KeyLengthException; message: The secret length must be at least 256 bits");
        } catch (com.nimbusds.jose.KeyLengthException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(jWSAlgorithmArray0);
        org.junit.Assert.assertArrayEquals(jWSAlgorithmArray0, new com.nimbusds.jose.JWSAlgorithm[] {});
        org.junit.Assert.assertNotNull(family2);
        org.junit.Assert.assertTrue("'" + boolean3 + "' != '" + false + "'", boolean3 == false);
        org.junit.Assert.assertNotNull(objArray4);
// flaky "3) test032(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertEquals(java.util.Arrays.deepToString(objArray4), "[Ed25519, null, ES256, ES384, Ed448, [null], \"\", ES512, \"/wH//wA\\u003d\", HS384, HS256, EdDSA]");
// flaky "3) test032(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertEquals(java.util.Arrays.toString(objArray4), "[Ed25519, null, ES256, ES384, Ed448, [null], \"\", ES512, \"/wH//wA\\u003d\", HS384, HS256, EdDSA]");
// flaky "2) test032(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertTrue("'" + int5 + "' != '" + 12 + "'", int5 == 12);
        org.junit.Assert.assertNotNull(jWSAlgorithm6);
        org.junit.Assert.assertEquals("'" + str7 + "' != '" + "HS384" + "'", str7, "HS384");
    }

    @Test
    public void test033() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test033");
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm1 = new com.nimbusds.jose.JWSAlgorithm("\"eyJhbGciOiJFZDQ0OCJ9\"");
        // The following exception was thrown during execution in test generation
        try {
            org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier3 = new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm1, "[Ed25519, null, ES256, ES384, Ed448, \"/wH//wA\\u003d\", EdDSA, HS384, HS256]");
            org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException; message: Unsupported JWS algorithm: \"eyJhbGciOiJFZDQ0OCJ9\"");
        } catch (java.lang.IllegalArgumentException e) {
            // Expected exception.
        }
    }

    @Test
    public void test034() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test034");
        com.nimbusds.jose.JWSAlgorithm.Family family0 = com.nimbusds.jose.JWSAlgorithm.Family.ED;
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm1 = com.nimbusds.jose.JWSAlgorithm.EdDSA;
        family0.addLast(jWSAlgorithm1);
        java.util.SequencedSet<com.nimbusds.jose.JWSAlgorithm> jWSAlgorithmSet3 = family0.reversed();
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm4 = com.nimbusds.jose.JWSAlgorithm.HS256;
        com.nimbusds.jose.JWSHeader jWSHeader5 = new com.nimbusds.jose.JWSHeader(jWSAlgorithm4);
        com.nimbusds.jose.UnprotectedHeader unprotectedHeader6 = null;
        com.nimbusds.jose.Header header7 = jWSHeader5.join(unprotectedHeader6);
        java.lang.String str8 = jWSHeader5.getContentType();
        java.util.Map<java.lang.String, java.lang.Object> strMap9 = jWSHeader5.getCustomParams();
        java.lang.Object obj11 = jWSHeader5.getCustomParam("\"ZmVkY2JhOTg3NjU0MzIxMGZlZGNiYTk4NzY1NDMyMTA\\u003d\"");
        com.nimbusds.jose.util.Base64URL base64URL12 = jWSHeader5.getParsedBase64URL();
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm13 = jWSHeader5.getAlgorithm();
        family0.addLast(jWSAlgorithm13);
        com.nimbusds.jose.Requirement requirement15 = jWSAlgorithm13.getRequirement();
        com.nimbusds.jose.Requirement requirement16 = jWSAlgorithm13.getRequirement();
        // The following exception was thrown during execution in test generation
        try {
            org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier18 = new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm13, "\"/woKAQA\\u003d\"");
            org.junit.Assert.fail("Expected exception of type com.nimbusds.jose.KeyLengthException; message: The secret length must be at least 256 bits");
        } catch (com.nimbusds.jose.KeyLengthException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(family0);
        org.junit.Assert.assertNotNull(jWSAlgorithm1);
        org.junit.Assert.assertNotNull(jWSAlgorithmSet3);
        org.junit.Assert.assertNotNull(jWSAlgorithm4);
        org.junit.Assert.assertNotNull(header7);
        org.junit.Assert.assertNull(str8);
        org.junit.Assert.assertNotNull(strMap9);
        org.junit.Assert.assertNull(obj11);
        org.junit.Assert.assertNull(base64URL12);
        org.junit.Assert.assertNotNull(jWSAlgorithm13);
        org.junit.Assert.assertTrue("'" + requirement15 + "' != '" + com.nimbusds.jose.Requirement.REQUIRED + "'", requirement15.equals(com.nimbusds.jose.Requirement.REQUIRED));
        org.junit.Assert.assertTrue("'" + requirement16 + "' != '" + com.nimbusds.jose.Requirement.REQUIRED + "'", requirement16.equals(com.nimbusds.jose.Requirement.REQUIRED));
    }

    @Test
    public void test035() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test035");
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm0 = com.nimbusds.jose.JWSAlgorithm.Ed448;
        boolean boolean2 = jWSAlgorithm0.equals((java.lang.Object) "\ufffd\n\n\001\000");
        com.nimbusds.jose.JWSHeader.Builder builder3 = new com.nimbusds.jose.JWSHeader.Builder(jWSAlgorithm0);
        com.nimbusds.jose.JWSHeader.Builder builder5 = builder3.base64URLEncodePayload(true);
        com.nimbusds.jose.JWSHeader.Builder builder7 = builder5.keyID("0123456789abcdef0123456789abcdef");
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm8 = com.nimbusds.jose.JWSAlgorithm.HS256;
        com.nimbusds.jose.JWSHeader jWSHeader9 = new com.nimbusds.jose.JWSHeader(jWSAlgorithm8);
        com.nimbusds.jose.UnprotectedHeader unprotectedHeader10 = null;
        com.nimbusds.jose.Header header11 = jWSHeader9.join(unprotectedHeader10);
        java.lang.String str12 = jWSHeader9.getContentType();
        java.util.Map<java.lang.String, java.lang.Object> strMap13 = jWSHeader9.getCustomParams();
        com.nimbusds.jose.JWSHeader.Builder builder14 = builder7.customParams(strMap13);
        com.nimbusds.jose.JWSHeader jWSHeader15 = builder7.build();
        java.util.Map<java.lang.String, java.lang.Object> strMap16 = jWSHeader15.toJSONObject();
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm17 = jWSHeader15.getAlgorithm();
        // The following exception was thrown during execution in test generation
{ // flaky ('try' without 'catch', 'finally' or resource declarations):         try {
            org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier19 = null; // flaky "4) test035(AccessTokenVerifierRandoopRegressionS00)": new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm17, "\"\\\"HS256\\\"\"");
// flaky "4) test035(AccessTokenVerifierRandoopRegressionS00)":             org.junit.Assert.fail("Expected exception of type com.nimbusds.jose.KeyLengthException; message: The secret length must be at least 256 bits");
// flaky (is never thrown in body of corresponding try statement):         } catch (com.nimbusds.jose.KeyLengthException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(jWSAlgorithm0);
        org.junit.Assert.assertTrue("'" + boolean2 + "' != '" + false + "'", boolean2 == false);
        org.junit.Assert.assertNotNull(builder5);
        org.junit.Assert.assertNotNull(builder7);
        org.junit.Assert.assertNotNull(jWSAlgorithm8);
        org.junit.Assert.assertNotNull(header11);
        org.junit.Assert.assertNull(str12);
        org.junit.Assert.assertNotNull(strMap13);
        org.junit.Assert.assertNotNull(builder14);
        org.junit.Assert.assertNotNull(jWSHeader15);
        org.junit.Assert.assertNotNull(strMap16);
        org.junit.Assert.assertNotNull(jWSAlgorithm17);
    }

    @Test
    public void test036() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test036");
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm1 = new com.nimbusds.jose.JWSAlgorithm("[ES384, ES256, Ed25519, Ed448, HS384, HS256, EdDSA]");
        // The following exception was thrown during execution in test generation
        try {
            org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier3 = new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm1, "77-9Ae-_ve-_vQA");
            org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException; message: Unsupported JWS algorithm: [ES384, ES256, Ed25519, Ed448, HS384, HS256, EdDSA]");
        } catch (java.lang.IllegalArgumentException e) {
            // Expected exception.
        }
    }

    @Test
    public void test037() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test037");
        com.nimbusds.jose.JWSAlgorithm[] jWSAlgorithmArray0 = new com.nimbusds.jose.JWSAlgorithm[] {};
        com.nimbusds.jose.JWSAlgorithm.Family family1 = new com.nimbusds.jose.JWSAlgorithm.Family(jWSAlgorithmArray0);
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm2 = com.nimbusds.jose.JWSAlgorithm.PS384;
        com.nimbusds.jose.JWSHeader jWSHeader3 = new com.nimbusds.jose.JWSHeader(jWSAlgorithm2);
        family1.addFirst(jWSAlgorithm2);
        // The following exception was thrown during execution in test generation
        try {
            org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier6 = new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm2, "HS256");
            org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException; message: A key pair is required, in the 'private:public' format");
        } catch (java.lang.IllegalArgumentException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(jWSAlgorithmArray0);
        org.junit.Assert.assertArrayEquals(jWSAlgorithmArray0, new com.nimbusds.jose.JWSAlgorithm[] {});
        org.junit.Assert.assertNotNull(jWSAlgorithm2);
    }

    @Test
    public void test038() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test038");
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm0 = com.nimbusds.jose.JWSAlgorithm.HS256;
        com.nimbusds.jose.JWSHeader jWSHeader1 = new com.nimbusds.jose.JWSHeader(jWSAlgorithm0);
        boolean boolean2 = jWSHeader1.isBase64URLEncodePayload();
        com.nimbusds.jose.JWSHeader jWSHeader3 = new com.nimbusds.jose.JWSHeader(jWSHeader1);
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm4 = jWSHeader1.getAlgorithm();
        // The following exception was thrown during execution in test generation
        try {
            org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier6 = new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm4, "");
            org.junit.Assert.fail("Expected exception of type com.nimbusds.jose.KeyLengthException; message: The secret length must be at least 256 bits");
        } catch (com.nimbusds.jose.KeyLengthException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(jWSAlgorithm0);
        org.junit.Assert.assertTrue("'" + boolean2 + "' != '" + true + "'", boolean2 == true);
        org.junit.Assert.assertNotNull(jWSAlgorithm4);
    }

    @Test
    public void test039() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test039");
        com.nimbusds.jose.JWSAlgorithm.Family family0 = com.nimbusds.jose.JWSAlgorithm.Family.HMAC_SHA;
        java.util.SequencedSet<com.nimbusds.jose.JWSAlgorithm> jWSAlgorithmSet1 = family0.reversed();
        com.nimbusds.jose.JWSAlgorithm[] jWSAlgorithmArray2 = new com.nimbusds.jose.JWSAlgorithm[] {};
        com.nimbusds.jose.JWSAlgorithm.Family family3 = new com.nimbusds.jose.JWSAlgorithm.Family(jWSAlgorithmArray2);
        boolean boolean5 = family3.contains((java.lang.Object) "0123456789abcdef0123456789abcdef");
        boolean boolean7 = family3.equals((java.lang.Object) 0L);
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm8 = com.nimbusds.jose.JWSAlgorithm.HS384;
        java.lang.String str9 = jWSAlgorithm8.getName();
        family3.addFirst(jWSAlgorithm8);
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm11 = family3.getFirst();
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm12 = com.nimbusds.jose.JWSAlgorithm.Ed25519;
        family3.addFirst(jWSAlgorithm12);
        boolean boolean14 = family0.equals((java.lang.Object) family3);
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm15 = com.nimbusds.jose.JWSAlgorithm.PS384;
        com.nimbusds.jose.JWSHeader jWSHeader16 = new com.nimbusds.jose.JWSHeader(jWSAlgorithm15);
        family0.addFirst(jWSAlgorithm15);
        // The following exception was thrown during execution in test generation
        try {
            org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier19 = new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm15, "[null, ES256, Ed25519, ES384, Ed448, \"/wH//wA\\u003d\", HS256, EdDSA, HS384]");
            org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException; message: A key pair is required, in the 'private:public' format");
        } catch (java.lang.IllegalArgumentException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(family0);
        org.junit.Assert.assertNotNull(jWSAlgorithmSet1);
        org.junit.Assert.assertNotNull(jWSAlgorithmArray2);
        org.junit.Assert.assertArrayEquals(jWSAlgorithmArray2, new com.nimbusds.jose.JWSAlgorithm[] {});
        org.junit.Assert.assertTrue("'" + boolean5 + "' != '" + false + "'", boolean5 == false);
        org.junit.Assert.assertTrue("'" + boolean7 + "' != '" + false + "'", boolean7 == false);
        org.junit.Assert.assertNotNull(jWSAlgorithm8);
        org.junit.Assert.assertEquals("'" + str9 + "' != '" + "HS384" + "'", str9, "HS384");
        org.junit.Assert.assertNotNull(jWSAlgorithm11);
        org.junit.Assert.assertNotNull(jWSAlgorithm12);
        org.junit.Assert.assertTrue("'" + boolean14 + "' != '" + false + "'", boolean14 == false);
        org.junit.Assert.assertNotNull(jWSAlgorithm15);
    }

    @Test
    public void test040() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test040");
        com.nimbusds.jose.JWSAlgorithm[] jWSAlgorithmArray0 = new com.nimbusds.jose.JWSAlgorithm[] {};
        com.nimbusds.jose.JWSAlgorithm.Family family1 = new com.nimbusds.jose.JWSAlgorithm.Family(jWSAlgorithmArray0);
        boolean boolean3 = family1.contains((java.lang.Object) "0123456789abcdef0123456789abcdef");
        boolean boolean5 = family1.equals((java.lang.Object) 0L);
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm6 = com.nimbusds.jose.JWSAlgorithm.HS384;
        java.lang.String str7 = jWSAlgorithm6.getName();
        family1.addFirst(jWSAlgorithm6);
        java.lang.String str9 = jWSAlgorithm6.getName();
        com.nimbusds.jose.JWSHeader jWSHeader10 = new com.nimbusds.jose.JWSHeader(jWSAlgorithm6);
        boolean boolean12 = jWSAlgorithm6.equals((java.lang.Object) "[RS256, RS384, RS512, PS256, PS384, PS512, ES512]");
        // The following exception was thrown during execution in test generation
        try {
            org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier14 = new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm6, "Ed448");
            org.junit.Assert.fail("Expected exception of type com.nimbusds.jose.KeyLengthException; message: The secret length must be at least 256 bits");
        } catch (com.nimbusds.jose.KeyLengthException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(jWSAlgorithmArray0);
        org.junit.Assert.assertArrayEquals(jWSAlgorithmArray0, new com.nimbusds.jose.JWSAlgorithm[] {});
        org.junit.Assert.assertTrue("'" + boolean3 + "' != '" + false + "'", boolean3 == false);
        org.junit.Assert.assertTrue("'" + boolean5 + "' != '" + false + "'", boolean5 == false);
        org.junit.Assert.assertNotNull(jWSAlgorithm6);
        org.junit.Assert.assertEquals("'" + str7 + "' != '" + "HS384" + "'", str7, "HS384");
        org.junit.Assert.assertEquals("'" + str9 + "' != '" + "HS384" + "'", str9, "HS384");
        org.junit.Assert.assertTrue("'" + boolean12 + "' != '" + false + "'", boolean12 == false);
    }

    @Test
    public void test041() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test041");
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm3 = com.nimbusds.jose.JWSAlgorithm.HS256;
        com.nimbusds.jose.Requirement requirement4 = jWSAlgorithm3.getRequirement();
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm5 = new com.nimbusds.jose.JWSAlgorithm("\ufffd\n\n\001\000", requirement4);
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm6 = new com.nimbusds.jose.JWSAlgorithm("\"\"", requirement4);
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm7 = new com.nimbusds.jose.JWSAlgorithm("\"77+9CgoBAA\\u003d\\u003d\"", requirement4);
        // The following exception was thrown during execution in test generation
        try {
            org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier9 = new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm7, "fedcba9876543210fedcba9876543210");
            org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException; message: Unsupported JWS algorithm: \"77+9CgoBAA\\u003d\\u003d\"");
        } catch (java.lang.IllegalArgumentException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(jWSAlgorithm3);
        org.junit.Assert.assertTrue("'" + requirement4 + "' != '" + com.nimbusds.jose.Requirement.REQUIRED + "'", requirement4.equals(com.nimbusds.jose.Requirement.REQUIRED));
    }

    @Test
    public void test042() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test042");
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm3 = com.nimbusds.jose.JWSAlgorithm.HS256;
        com.nimbusds.jose.Requirement requirement4 = jWSAlgorithm3.getRequirement();
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm5 = new com.nimbusds.jose.JWSAlgorithm("\ufffd\n\n\001\000", requirement4);
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm6 = new com.nimbusds.jose.JWSAlgorithm("\"\"", requirement4);
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm7 = new com.nimbusds.jose.JWSAlgorithm("[null]", requirement4);
        // The following exception was thrown during execution in test generation
        try {
            org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier9 = new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm7, "\"\\\"Ed25519\\\"\"");
            org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException; message: Unsupported JWS algorithm: [null]");
        } catch (java.lang.IllegalArgumentException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(jWSAlgorithm3);
        org.junit.Assert.assertTrue("'" + requirement4 + "' != '" + com.nimbusds.jose.Requirement.REQUIRED + "'", requirement4.equals(com.nimbusds.jose.Requirement.REQUIRED));
    }

    @Test
    public void test043() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test043");
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm3 = com.nimbusds.jose.JWSAlgorithm.HS256;
        com.nimbusds.jose.Requirement requirement4 = jWSAlgorithm3.getRequirement();
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm5 = new com.nimbusds.jose.JWSAlgorithm("\ufffd\n\n\001\000", requirement4);
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm6 = new com.nimbusds.jose.JWSAlgorithm("\"\"", requirement4);
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm7 = new com.nimbusds.jose.JWSAlgorithm("[RS256, RS384, RS512, PS256, PS384, PS512, ES512, \"ZmVkY2JhOTg3NjU0MzIxMGZlZGNiYTk4NzY1NDMyMTA\\u003d\"]", requirement4);
        // The following exception was thrown during execution in test generation
        try {
            org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier9 = new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm7, "\"RS256\"");
            org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException; message: Unsupported JWS algorithm: [RS256, RS384, RS512, PS256, PS384, PS512, ES512, \"ZmVkY2JhOTg3NjU0MzIxMGZlZGNiYTk4NzY1NDMyMTA\\u003d\"]");
        } catch (java.lang.IllegalArgumentException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(jWSAlgorithm3);
        org.junit.Assert.assertTrue("'" + requirement4 + "' != '" + com.nimbusds.jose.Requirement.REQUIRED + "'", requirement4.equals(com.nimbusds.jose.Requirement.REQUIRED));
    }

    @Test
    public void test044() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test044");
        com.nimbusds.jose.JWSAlgorithm[] jWSAlgorithmArray0 = new com.nimbusds.jose.JWSAlgorithm[] {};
        com.nimbusds.jose.JWSAlgorithm.Family family1 = new com.nimbusds.jose.JWSAlgorithm.Family(jWSAlgorithmArray0);
        com.nimbusds.jose.JWSAlgorithm.Family family2 = com.nimbusds.jose.JWSAlgorithm.Family.ED;
        boolean boolean3 = family1.containsAll((java.util.Collection<com.nimbusds.jose.JWSAlgorithm>) family2);
        java.lang.Object[] objArray4 = family2.toArray();
        java.util.Spliterator<com.nimbusds.jose.JWSAlgorithm> jWSAlgorithmSpliterator5 = family2.spliterator();
        boolean boolean6 = family2.isEmpty();
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm7 = com.nimbusds.jose.JWSAlgorithm.HS256;
        com.nimbusds.jose.Requirement requirement8 = jWSAlgorithm7.getRequirement();
        family2.addLast(jWSAlgorithm7);
        java.util.stream.Stream<com.nimbusds.jose.JWSAlgorithm> jWSAlgorithmStream10 = family2.parallelStream();
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm11 = com.nimbusds.jose.JWSAlgorithm.HS256;
        com.nimbusds.jose.JWSHeader jWSHeader12 = new com.nimbusds.jose.JWSHeader(jWSAlgorithm11);
        com.nimbusds.jose.UnprotectedHeader unprotectedHeader13 = null;
        com.nimbusds.jose.Header header14 = jWSHeader12.join(unprotectedHeader13);
        java.util.Set<java.lang.String> strSet15 = jWSHeader12.getCriticalParams();
        boolean boolean16 = family2.contains((java.lang.Object) jWSHeader12);
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm17 = jWSHeader12.getAlgorithm();
        // The following exception was thrown during execution in test generation
        try {
            org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier19 = new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm17, "[RS256, RS384, RS512, PS256, PS384, PS512]");
// flaky "5) test044(AccessTokenVerifierRandoopRegressionS00)":             org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException; message: A key pair is required, in the 'private:public' format");
        } catch (java.lang.IllegalArgumentException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(jWSAlgorithmArray0);
        org.junit.Assert.assertArrayEquals(jWSAlgorithmArray0, new com.nimbusds.jose.JWSAlgorithm[] {});
        org.junit.Assert.assertNotNull(family2);
        org.junit.Assert.assertTrue("'" + boolean3 + "' != '" + false + "'", boolean3 == false);
        org.junit.Assert.assertNotNull(objArray4);
// flaky "5) test044(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertEquals(java.util.Arrays.deepToString(objArray4), "[ES256, Ed25519, PS384, [null, ES256, Ed25519, ES384, Ed448, \"/wH//wA\\u003d\", HS256, EdDSA, HS384], eyJhbGciOiJIUzI1NiJ9, Ed448, [null], null, [ES384, ES256, Ed25519, Ed448, HS384, HS256, EdDSA], \"\", ES512, HS384, ES384, PS256, \"/wH//wA\\u003d\", HS256, EdDSA]");
// flaky "3) test044(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertEquals(java.util.Arrays.toString(objArray4), "[ES256, Ed25519, PS384, [null, ES256, Ed25519, ES384, Ed448, \"/wH//wA\\u003d\", HS256, EdDSA, HS384], eyJhbGciOiJIUzI1NiJ9, Ed448, [null], null, [ES384, ES256, Ed25519, Ed448, HS384, HS256, EdDSA], \"\", ES512, HS384, ES384, PS256, \"/wH//wA\\u003d\", HS256, EdDSA]");
        org.junit.Assert.assertNotNull(jWSAlgorithmSpliterator5);
        org.junit.Assert.assertTrue("'" + boolean6 + "' != '" + false + "'", boolean6 == false);
        org.junit.Assert.assertNotNull(jWSAlgorithm7);
        org.junit.Assert.assertTrue("'" + requirement8 + "' != '" + com.nimbusds.jose.Requirement.REQUIRED + "'", requirement8.equals(com.nimbusds.jose.Requirement.REQUIRED));
        org.junit.Assert.assertNotNull(jWSAlgorithmStream10);
        org.junit.Assert.assertNotNull(jWSAlgorithm11);
        org.junit.Assert.assertNotNull(header14);
        org.junit.Assert.assertNull(strSet15);
        org.junit.Assert.assertTrue("'" + boolean16 + "' != '" + false + "'", boolean16 == false);
        org.junit.Assert.assertNotNull(jWSAlgorithm17);
    }

    @Test
    public void test045() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test045");
        com.nimbusds.jose.JWSAlgorithm.Family family0 = com.nimbusds.jose.JWSAlgorithm.Family.ED;
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm1 = com.nimbusds.jose.JWSAlgorithm.EdDSA;
        family0.addLast(jWSAlgorithm1);
        java.util.SequencedSet<com.nimbusds.jose.JWSAlgorithm> jWSAlgorithmSet3 = family0.reversed();
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm4 = com.nimbusds.jose.JWSAlgorithm.HS256;
        com.nimbusds.jose.JWSHeader jWSHeader5 = new com.nimbusds.jose.JWSHeader(jWSAlgorithm4);
        com.nimbusds.jose.UnprotectedHeader unprotectedHeader6 = null;
        com.nimbusds.jose.Header header7 = jWSHeader5.join(unprotectedHeader6);
        java.lang.String str8 = jWSHeader5.getContentType();
        java.util.Map<java.lang.String, java.lang.Object> strMap9 = jWSHeader5.getCustomParams();
        java.lang.Object obj11 = jWSHeader5.getCustomParam("\"ZmVkY2JhOTg3NjU0MzIxMGZlZGNiYTk4NzY1NDMyMTA\\u003d\"");
        com.nimbusds.jose.util.Base64URL base64URL12 = jWSHeader5.getParsedBase64URL();
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm13 = jWSHeader5.getAlgorithm();
        family0.addLast(jWSAlgorithm13);
        com.nimbusds.jose.Requirement requirement15 = jWSAlgorithm13.getRequirement();
        com.nimbusds.jose.Requirement requirement16 = jWSAlgorithm13.getRequirement();
        // The following exception was thrown during execution in test generation
        try {
            org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier18 = new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm13, "[HS256, ES256, ES384, Ed25519, null, Ed448, [null], HS384, \"\", ES512, EdDSA, \"/wH//wA\\u003d\"]");
// flaky "6) test045(AccessTokenVerifierRandoopRegressionS00)":             org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException; message: A key pair is required, in the 'private:public' format");
        } catch (java.lang.IllegalArgumentException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(family0);
        org.junit.Assert.assertNotNull(jWSAlgorithm1);
        org.junit.Assert.assertNotNull(jWSAlgorithmSet3);
        org.junit.Assert.assertNotNull(jWSAlgorithm4);
        org.junit.Assert.assertNotNull(header7);
        org.junit.Assert.assertNull(str8);
        org.junit.Assert.assertNotNull(strMap9);
        org.junit.Assert.assertNull(obj11);
        org.junit.Assert.assertNull(base64URL12);
        org.junit.Assert.assertNotNull(jWSAlgorithm13);
        org.junit.Assert.assertTrue("'" + requirement15 + "' != '" + com.nimbusds.jose.Requirement.REQUIRED + "'", requirement15.equals(com.nimbusds.jose.Requirement.REQUIRED));
        org.junit.Assert.assertTrue("'" + requirement16 + "' != '" + com.nimbusds.jose.Requirement.REQUIRED + "'", requirement16.equals(com.nimbusds.jose.Requirement.REQUIRED));
    }

    @Test
    public void test046() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test046");
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm1 = com.nimbusds.jose.JWSAlgorithm.parse("ES256");
        com.nimbusds.jose.JWSHeader jWSHeader2 = new com.nimbusds.jose.JWSHeader(jWSAlgorithm1);
        java.lang.String str3 = jWSAlgorithm1.toString();
        java.lang.String str4 = jWSAlgorithm1.getName();
        // The following exception was thrown during execution in test generation
        try {
            org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier6 = new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm1, "[ES256, ES256K, ES384, ES512]");
            org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException; message: Unsupported JWS algorithm: ES256");
        } catch (java.lang.IllegalArgumentException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(jWSAlgorithm1);
        org.junit.Assert.assertEquals("'" + str3 + "' != '" + "ES256" + "'", str3, "ES256");
        org.junit.Assert.assertEquals("'" + str4 + "' != '" + "ES256" + "'", str4, "ES256");
    }

    @Test
    public void test047() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test047");
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm0 = com.nimbusds.jose.JWSAlgorithm.HS256;
        java.lang.String str1 = jWSAlgorithm0.getName();
        // The following exception was thrown during execution in test generation
        try {
            org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier3 = null; // flaky "7) test047(AccessTokenVerifierRandoopRegressionS00)": new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm0, "RVMyNTY=");
// flaky "6) test047(AccessTokenVerifierRandoopRegressionS00)":             org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException; message: A key pair is required, in the 'private:public' format");
        } catch (java.lang.IllegalArgumentException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(jWSAlgorithm0);
        org.junit.Assert.assertEquals("'" + str1 + "' != '" + "HS256" + "'", str1, "HS256");
    }

    @Test
    public void test048() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test048");
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm3 = com.nimbusds.jose.JWSAlgorithm.HS256;
        com.nimbusds.jose.Requirement requirement4 = jWSAlgorithm3.getRequirement();
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm5 = new com.nimbusds.jose.JWSAlgorithm("HS384", requirement4);
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm6 = new com.nimbusds.jose.JWSAlgorithm("[null]", requirement4);
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm7 = new com.nimbusds.jose.JWSAlgorithm("\"HS256\"", requirement4);
        // The following exception was thrown during execution in test generation
        try {
            org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier9 = new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm7, "[ES256, null, ES384, Ed25519, Ed448, [null], \"\", ES512, HS384, \"/wH//wA\\u003d\", EdDSA, HS256]");
            org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException; message: Unsupported JWS algorithm: \"HS256\"");
        } catch (java.lang.IllegalArgumentException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(jWSAlgorithm3);
        org.junit.Assert.assertTrue("'" + requirement4 + "' != '" + com.nimbusds.jose.Requirement.REQUIRED + "'", requirement4.equals(com.nimbusds.jose.Requirement.REQUIRED));
    }

    @Test
    public void test049() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test049");
        com.nimbusds.jose.JWSAlgorithm[] jWSAlgorithmArray0 = new com.nimbusds.jose.JWSAlgorithm[] {};
        com.nimbusds.jose.JWSAlgorithm.Family family1 = new com.nimbusds.jose.JWSAlgorithm.Family(jWSAlgorithmArray0);
        com.nimbusds.jose.JWSAlgorithm.Family family2 = com.nimbusds.jose.JWSAlgorithm.Family.ED;
        boolean boolean3 = family1.containsAll((java.util.Collection<com.nimbusds.jose.JWSAlgorithm>) family2);
        java.lang.Object[] objArray4 = family2.toArray();
        java.util.Spliterator<com.nimbusds.jose.JWSAlgorithm> jWSAlgorithmSpliterator5 = family2.spliterator();
        java.util.SequencedSet<com.nimbusds.jose.JWSAlgorithm> jWSAlgorithmSet6 = family2.reversed();
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm8 = new com.nimbusds.jose.JWSAlgorithm("[null, ES256, Ed25519, ES384, Ed448, \"/wH//wA\\u003d\", HS256, EdDSA, HS384]");
        family2.addFirst(jWSAlgorithm8);
        // The following exception was thrown during execution in test generation
        try {
            org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier11 = new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm8, "\021\ufffd\ufffd");
            org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException; message: Unsupported JWS algorithm: [null, ES256, Ed25519, ES384, Ed448, \"/wH//wA\\u003d\", HS256, EdDSA, HS384]");
        } catch (java.lang.IllegalArgumentException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(jWSAlgorithmArray0);
        org.junit.Assert.assertArrayEquals(jWSAlgorithmArray0, new com.nimbusds.jose.JWSAlgorithm[] {});
        org.junit.Assert.assertNotNull(family2);
        org.junit.Assert.assertTrue("'" + boolean3 + "' != '" + false + "'", boolean3 == false);
        org.junit.Assert.assertNotNull(objArray4);
// flaky "8) test049(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertEquals(java.util.Arrays.deepToString(objArray4), "[Ed25519, ES256, PS256, PS384, [null, ES256, Ed25519, ES384, Ed448, \"/wH//wA\\u003d\", HS256, EdDSA, HS384], eyJhbGciOiJIUzI1NiJ9, Ed448, [null], \"\", ES512, HS384, null, [ES384, ES256, Ed25519, Ed448, HS384, HS256, EdDSA], EdDSA, ES384, \"/wH//wA\\u003d\", HS256]");
// flaky "7) test049(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertEquals(java.util.Arrays.toString(objArray4), "[Ed25519, ES256, PS256, PS384, [null, ES256, Ed25519, ES384, Ed448, \"/wH//wA\\u003d\", HS256, EdDSA, HS384], eyJhbGciOiJIUzI1NiJ9, Ed448, [null], \"\", ES512, HS384, null, [ES384, ES256, Ed25519, Ed448, HS384, HS256, EdDSA], EdDSA, ES384, \"/wH//wA\\u003d\", HS256]");
        org.junit.Assert.assertNotNull(jWSAlgorithmSpliterator5);
        org.junit.Assert.assertNotNull(jWSAlgorithmSet6);
    }

    @Test
    public void test050() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test050");
        com.nimbusds.jose.JWSAlgorithm.Family family0 = com.nimbusds.jose.JWSAlgorithm.Family.SIGNATURE;
        com.nimbusds.jose.Algorithm algorithm1 = com.nimbusds.jose.Algorithm.NONE;
        com.nimbusds.jose.Requirement requirement2 = algorithm1.getRequirement();
        boolean boolean3 = family0.contains((java.lang.Object) requirement2);
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm4 = family0.getLast();
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm5 = family0.getLast();
        // The following exception was thrown during execution in test generation
        try {
            org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier7 = new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm5, "{\"alg\":\"HS256\"}");
            org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException; message: Unsupported JWS algorithm: fedcba9876543210fedcba9876543210");
        } catch (java.lang.IllegalArgumentException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(family0);
        org.junit.Assert.assertNotNull(algorithm1);
        org.junit.Assert.assertTrue("'" + requirement2 + "' != '" + com.nimbusds.jose.Requirement.REQUIRED + "'", requirement2.equals(com.nimbusds.jose.Requirement.REQUIRED));
        org.junit.Assert.assertTrue("'" + boolean3 + "' != '" + false + "'", boolean3 == false);
        org.junit.Assert.assertNotNull(jWSAlgorithm4);
        org.junit.Assert.assertNotNull(jWSAlgorithm5);
    }

    @Test
    public void test051() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test051");
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm0 = com.nimbusds.jose.JWSAlgorithm.HS256;
        com.nimbusds.jose.JWSHeader jWSHeader1 = new com.nimbusds.jose.JWSHeader(jWSAlgorithm0);
        boolean boolean2 = jWSHeader1.isBase64URLEncodePayload();
        java.net.URI uRI3 = jWSHeader1.getX509CertURL();
        java.lang.String str4 = jWSHeader1.getKeyID();
        java.util.Map<java.lang.String, java.lang.Object> strMap5 = jWSHeader1.toJSONObject();
        com.nimbusds.jose.util.Base64URL base64URL7 = com.nimbusds.jose.util.Base64URL.encode("0123456789abcdef0123456789abcdef");
        com.nimbusds.jose.JWSHeader jWSHeader8 = com.nimbusds.jose.JWSHeader.parse(strMap5, base64URL7);
        com.nimbusds.jose.JWSHeader jWSHeader9 = com.nimbusds.jose.JWSHeader.parse(strMap5);
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm10 = jWSHeader9.getAlgorithm();
        // The following exception was thrown during execution in test generation
        try {
            org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier12 = new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm10, "[PS256, HS256, Ed25519, ES256, PS384, [null, ES256, Ed25519, ES384, Ed448, \"/wH//wA\\u003d\", HS256, EdDSA, HS384], eyJhbGciOiJIUzI1NiJ9, Ed448, [null], null, [ES384, ES256, Ed25519, Ed448, HS384, HS256, EdDSA], \"\", ES512, HS384, \"/wH//wA\\u003d\", EdDSA, ES384]");
// flaky "9) test051(AccessTokenVerifierRandoopRegressionS00)":             org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException; message: A key pair is required, in the 'private:public' format");
        } catch (java.lang.IllegalArgumentException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(jWSAlgorithm0);
        org.junit.Assert.assertTrue("'" + boolean2 + "' != '" + true + "'", boolean2 == true);
        org.junit.Assert.assertNull(uRI3);
        org.junit.Assert.assertNull(str4);
        org.junit.Assert.assertNotNull(strMap5);
        org.junit.Assert.assertNotNull(base64URL7);
        org.junit.Assert.assertNotNull(jWSHeader8);
        org.junit.Assert.assertNotNull(jWSHeader9);
        org.junit.Assert.assertNotNull(jWSAlgorithm10);
    }

    @Test
    public void test052() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test052");
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm0 = com.nimbusds.jose.JWSAlgorithm.HS256;
        com.nimbusds.jose.JWSAlgorithm[] jWSAlgorithmArray1 = new com.nimbusds.jose.JWSAlgorithm[] {};
        com.nimbusds.jose.JWSAlgorithm.Family family2 = new com.nimbusds.jose.JWSAlgorithm.Family(jWSAlgorithmArray1);
        com.nimbusds.jose.JWSAlgorithm.Family family3 = com.nimbusds.jose.JWSAlgorithm.Family.ED;
        boolean boolean4 = family2.containsAll((java.util.Collection<com.nimbusds.jose.JWSAlgorithm>) family3);
        boolean boolean5 = jWSAlgorithm0.equals((java.lang.Object) family2);
        com.nimbusds.jose.JWSHeader.Builder builder6 = new com.nimbusds.jose.JWSHeader.Builder(jWSAlgorithm0);
        // The following exception was thrown during execution in test generation
        try {
            org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier8 = null; // flaky "10) test052(AccessTokenVerifierRandoopRegressionS00)": new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm0, "\"ES384\"");
// flaky "8) test052(AccessTokenVerifierRandoopRegressionS00)":             org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException; message: A key pair is required, in the 'private:public' format");
        } catch (java.lang.IllegalArgumentException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(jWSAlgorithm0);
        org.junit.Assert.assertNotNull(jWSAlgorithmArray1);
        org.junit.Assert.assertArrayEquals(jWSAlgorithmArray1, new com.nimbusds.jose.JWSAlgorithm[] {});
        org.junit.Assert.assertNotNull(family3);
        org.junit.Assert.assertTrue("'" + boolean4 + "' != '" + false + "'", boolean4 == false);
        org.junit.Assert.assertTrue("'" + boolean5 + "' != '" + false + "'", boolean5 == false);
    }

    @Test
    public void test053() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test053");
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm0 = com.nimbusds.jose.JWSAlgorithm.HS256;
        com.nimbusds.jose.JWSHeader jWSHeader1 = new com.nimbusds.jose.JWSHeader(jWSAlgorithm0);
        boolean boolean2 = jWSHeader1.isBase64URLEncodePayload();
        java.net.URI uRI3 = jWSHeader1.getX509CertURL();
        java.lang.String str4 = jWSHeader1.getKeyID();
        java.lang.String str5 = jWSHeader1.getKeyID();
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm6 = jWSHeader1.getAlgorithm();
        // The following exception was thrown during execution in test generation
        try {
            org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier8 = null; // flaky "11) test053(AccessTokenVerifierRandoopRegressionS00)": new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm6, "ES384");
// flaky "9) test053(AccessTokenVerifierRandoopRegressionS00)":             org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException; message: A key pair is required, in the 'private:public' format");
        } catch (java.lang.IllegalArgumentException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(jWSAlgorithm0);
        org.junit.Assert.assertTrue("'" + boolean2 + "' != '" + true + "'", boolean2 == true);
        org.junit.Assert.assertNull(uRI3);
        org.junit.Assert.assertNull(str4);
        org.junit.Assert.assertNull(str5);
        org.junit.Assert.assertNotNull(jWSAlgorithm6);
    }

    @Test
    public void test054() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test054");
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm1 = new com.nimbusds.jose.JWSAlgorithm("PS256");
        com.nimbusds.jose.JWSHeader jWSHeader2 = new com.nimbusds.jose.JWSHeader(jWSAlgorithm1);
        java.lang.String str3 = jWSAlgorithm1.toString();
        com.nimbusds.jose.JWSHeader jWSHeader4 = new com.nimbusds.jose.JWSHeader(jWSAlgorithm1);
        com.nimbusds.jose.JWSHeader jWSHeader5 = new com.nimbusds.jose.JWSHeader(jWSAlgorithm1);
        // The following exception was thrown during execution in test generation
        try {
            org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier7 = new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm1, "[[null, ES256, Ed25519, ES384, Ed448, \"/wH//wA\\u003d\", HS256, EdDSA, HS384], Ed25519, ES256, PS256, PS384, eyJhbGciOiJIUzI1NiJ9, Ed448, [null], \"\", ES512, null, [ES384, ES256, Ed25519, Ed448, HS384, HS256, EdDSA], ES384, \"/wH//wA\\u003d\", HS384, EdDSA, HS256]");
            org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException; message: A key pair is required, in the 'private:public' format");
        } catch (java.lang.IllegalArgumentException e) {
            // Expected exception.
        }
        org.junit.Assert.assertEquals("'" + str3 + "' != '" + "PS256" + "'", str3, "PS256");
    }

    @Test
    public void test055() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test055");
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm1 = com.nimbusds.jose.JWSAlgorithm.parse("fedcba9876543210fedcba9876543210");
        java.lang.String str2 = jWSAlgorithm1.toJSONString();
        // The following exception was thrown during execution in test generation
        try {
            org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier4 = new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm1, "[[null, ES256, Ed25519, ES384, Ed448, \"/wH//wA\\u003d\", HS256, EdDSA, HS384], Ed25519, ES256, PS256, PS384, eyJhbGciOiJIUzI1NiJ9, Ed448, [null], \"\", ES512, null, [ES384, ES256, Ed25519, Ed448, HS384, HS256, EdDSA], ES384, \"/wH//wA\\u003d\", HS384, HS256, EdDSA]");
            org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException; message: Unsupported JWS algorithm: fedcba9876543210fedcba9876543210");
        } catch (java.lang.IllegalArgumentException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(jWSAlgorithm1);
        org.junit.Assert.assertEquals("'" + str2 + "' != '" + "\"fedcba9876543210fedcba9876543210\"" + "'", str2, "\"fedcba9876543210fedcba9876543210\"");
    }

    @Test
    public void test056() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test056");
        com.nimbusds.jose.JWSAlgorithm[] jWSAlgorithmArray0 = new com.nimbusds.jose.JWSAlgorithm[] {};
        com.nimbusds.jose.JWSAlgorithm.Family family1 = new com.nimbusds.jose.JWSAlgorithm.Family(jWSAlgorithmArray0);
        com.nimbusds.jose.JWSAlgorithm.Family family2 = com.nimbusds.jose.JWSAlgorithm.Family.ED;
        boolean boolean3 = family1.containsAll((java.util.Collection<com.nimbusds.jose.JWSAlgorithm>) family2);
        java.lang.Object[] objArray4 = family2.toArray();
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm5 = family2.getFirst();
        com.nimbusds.jose.Requirement requirement6 = jWSAlgorithm5.getRequirement();
        // The following exception was thrown during execution in test generation
        try {
            org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier8 = new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm5, "SFMzODQ");
            org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException; message: Unsupported JWS algorithm: Ed25519");
        } catch (java.lang.IllegalArgumentException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(jWSAlgorithmArray0);
        org.junit.Assert.assertArrayEquals(jWSAlgorithmArray0, new com.nimbusds.jose.JWSAlgorithm[] {});
        org.junit.Assert.assertNotNull(family2);
        org.junit.Assert.assertTrue("'" + boolean3 + "' != '" + false + "'", boolean3 == false);
        org.junit.Assert.assertNotNull(objArray4);
// flaky "12) test056(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertEquals(java.util.Arrays.deepToString(objArray4), "[Ed25519, ES384, ES256, null, [null, ES256, Ed25519, ES384, Ed448, \"/wH//wA\\u003d\", HS256, EdDSA, HS384], PS256, PS384, eyJhbGciOiJIUzI1NiJ9, Ed448, [null], \"\", [ES384, ES256, Ed25519, Ed448, HS384, HS256, EdDSA], ES512, HS384, HS256, \"/wH//wA\\u003d\", EdDSA]");
// flaky "10) test056(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertEquals(java.util.Arrays.toString(objArray4), "[Ed25519, ES384, ES256, null, [null, ES256, Ed25519, ES384, Ed448, \"/wH//wA\\u003d\", HS256, EdDSA, HS384], PS256, PS384, eyJhbGciOiJIUzI1NiJ9, Ed448, [null], \"\", [ES384, ES256, Ed25519, Ed448, HS384, HS256, EdDSA], ES512, HS384, HS256, \"/wH//wA\\u003d\", EdDSA]");
        org.junit.Assert.assertNotNull(jWSAlgorithm5);
// flaky "4) test056(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertTrue("'" + requirement6 + "' != '" + com.nimbusds.jose.Requirement.OPTIONAL + "'", requirement6.equals(com.nimbusds.jose.Requirement.OPTIONAL));
    }

    @Test
    public void test057() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test057");
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm1 = com.nimbusds.jose.JWSAlgorithm.parse("fedcba9876543210fedcba9876543210");
        java.lang.String str2 = jWSAlgorithm1.toString();
        com.nimbusds.jose.Requirement requirement3 = jWSAlgorithm1.getRequirement();
        com.nimbusds.jose.Requirement requirement4 = jWSAlgorithm1.getRequirement();
        java.lang.String str5 = jWSAlgorithm1.toJSONString();
        // The following exception was thrown during execution in test generation
        try {
            org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier7 = new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm1, "\ufffd\n\n\001\000");
            org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException; message: Unsupported JWS algorithm: fedcba9876543210fedcba9876543210");
        } catch (java.lang.IllegalArgumentException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(jWSAlgorithm1);
        org.junit.Assert.assertEquals("'" + str2 + "' != '" + "fedcba9876543210fedcba9876543210" + "'", str2, "fedcba9876543210fedcba9876543210");
        org.junit.Assert.assertNull(requirement3);
        org.junit.Assert.assertNull(requirement4);
        org.junit.Assert.assertEquals("'" + str5 + "' != '" + "\"fedcba9876543210fedcba9876543210\"" + "'", str5, "\"fedcba9876543210fedcba9876543210\"");
    }

    @Test
    public void test058() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test058");
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm1 = com.nimbusds.jose.JWSAlgorithm.ES384;
        com.nimbusds.jose.JWSHeader.Builder builder2 = new com.nimbusds.jose.JWSHeader.Builder(jWSAlgorithm1);
        com.nimbusds.jose.Requirement requirement3 = jWSAlgorithm1.getRequirement();
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm4 = new com.nimbusds.jose.JWSAlgorithm("", requirement3);
        java.lang.String str5 = jWSAlgorithm4.toString();
        // The following exception was thrown during execution in test generation
        try {
            org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier7 = new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm4, "kid0123456789abcdef0123456789abcdefalgEd448=");
            org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException; message: Unsupported JWS algorithm: ");
        } catch (java.lang.IllegalArgumentException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(jWSAlgorithm1);
        org.junit.Assert.assertTrue("'" + requirement3 + "' != '" + com.nimbusds.jose.Requirement.OPTIONAL + "'", requirement3.equals(com.nimbusds.jose.Requirement.OPTIONAL));
        org.junit.Assert.assertEquals("'" + str5 + "' != '" + "" + "'", str5, "");
    }

    @Test
    public void test059() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test059");
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm0 = com.nimbusds.jose.JWSAlgorithm.Ed448;
        boolean boolean2 = jWSAlgorithm0.equals((java.lang.Object) "\ufffd\n\n\001\000");
        com.nimbusds.jose.JWSHeader.Builder builder3 = new com.nimbusds.jose.JWSHeader.Builder(jWSAlgorithm0);
        com.nimbusds.jose.JWSHeader.Builder builder5 = builder3.base64URLEncodePayload(true);
        java.util.LinkedHashSet<java.lang.String> strSet7 = java.util.LinkedHashSet.newLinkedHashSet((int) '#');
        com.nimbusds.jose.JWSHeader.Builder builder8 = builder3.criticalParams((java.util.Set<java.lang.String>) strSet7);
        com.nimbusds.jose.JWSHeader jWSHeader9 = builder3.build();
        com.nimbusds.jose.JWSHeader.Builder builder10 = new com.nimbusds.jose.JWSHeader.Builder(jWSHeader9);
        com.nimbusds.jose.jwk.JWK jWK11 = jWSHeader9.getJWK();
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm12 = jWSHeader9.getAlgorithm();
        java.lang.String str13 = jWSAlgorithm12.toString();
        // The following exception was thrown during execution in test generation
{ // flaky ('try' without 'catch', 'finally' or resource declarations):         try {
            org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier15 = null; // flaky "13) test059(AccessTokenVerifierRandoopRegressionS00)": new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm12, "{\"alg\":\"ES256\"}");
// flaky "11) test059(AccessTokenVerifierRandoopRegressionS00)":             org.junit.Assert.fail("Expected exception of type com.nimbusds.jose.KeyLengthException; message: The secret length must be at least 256 bits");
// flaky (is never thrown in body of corresponding try statement):         } catch (com.nimbusds.jose.KeyLengthException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(jWSAlgorithm0);
        org.junit.Assert.assertTrue("'" + boolean2 + "' != '" + false + "'", boolean2 == false);
        org.junit.Assert.assertNotNull(builder5);
        org.junit.Assert.assertNotNull(strSet7);
        org.junit.Assert.assertNotNull(builder8);
        org.junit.Assert.assertNotNull(jWSHeader9);
        org.junit.Assert.assertNull(jWK11);
        org.junit.Assert.assertNotNull(jWSAlgorithm12);
        org.junit.Assert.assertEquals("'" + str13 + "' != '" + "Ed448" + "'", str13, "Ed448");
    }

    @Test
    public void test060() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test060");
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm1 = com.nimbusds.jose.JWSAlgorithm.parse("MDEyMzQ1Njc4OWFiY2RlZjAxMjM0NTY3ODlhYmNkZWY");
        // The following exception was thrown during execution in test generation
        try {
            org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier3 = new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm1, "[Ed25519, eyJhbGciOiJIUzI1NiJ9, ES384, ES256, [null, ES256, Ed25519, ES384, Ed448, \"/wH//wA\\u003d\", HS256, EdDSA, HS384], PS256, PS384, Ed448, [null], \"\", [ES384, ES256, Ed25519, Ed448, HS384, HS256, EdDSA], ES512, HS384, \"/wH//wA\\u003d\", null, EdDSA, HS256]");
            org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException; message: Unsupported JWS algorithm: MDEyMzQ1Njc4OWFiY2RlZjAxMjM0NTY3ODlhYmNkZWY");
        } catch (java.lang.IllegalArgumentException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(jWSAlgorithm1);
    }

    @Test
    public void test061() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test061");
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm1 = com.nimbusds.jose.JWSAlgorithm.parse("eyJhbGciOiJIUzI1NiJ9");
        // The following exception was thrown during execution in test generation
        try {
            org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier3 = new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm1, "\ufffd\n\n\001\000\ufffd\ufffd");
            org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException; message: Unsupported JWS algorithm: eyJhbGciOiJIUzI1NiJ9");
        } catch (java.lang.IllegalArgumentException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(jWSAlgorithm1);
    }

    @Test
    public void test062() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test062");
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm0 = com.nimbusds.jose.JWSAlgorithm.Ed25519;
        com.nimbusds.jose.Requirement requirement1 = jWSAlgorithm0.getRequirement();
        // The following exception was thrown during execution in test generation
        try {
            org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier3 = new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm0, "HS256");
            org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException; message: Unsupported JWS algorithm: Ed25519");
        } catch (java.lang.IllegalArgumentException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(jWSAlgorithm0);
        org.junit.Assert.assertTrue("'" + requirement1 + "' != '" + com.nimbusds.jose.Requirement.OPTIONAL + "'", requirement1.equals(com.nimbusds.jose.Requirement.OPTIONAL));
    }

    @Test
    public void test063() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test063");
        com.nimbusds.jose.JWSAlgorithm.Family family0 = com.nimbusds.jose.JWSAlgorithm.Family.EC;
        int int1 = family0.size();
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm2 = family0.getFirst();
        // The following exception was thrown during execution in test generation
        try {
            org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier4 = new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm2, "\"EdDSA\"");
            org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException; message: Unsupported JWS algorithm: ES256");
        } catch (java.lang.IllegalArgumentException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(family0);
// flaky "14) test063(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertTrue("'" + int1 + "' != '" + 6 + "'", int1 == 6);
        org.junit.Assert.assertNotNull(jWSAlgorithm2);
    }

    @Test
    public void test064() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test064");
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm1 = com.nimbusds.jose.JWSAlgorithm.parse("{\"alg\":\"HS256\"}");
        java.lang.String str2 = jWSAlgorithm1.getName();
        // The following exception was thrown during execution in test generation
        try {
            org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier4 = new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm1, "[PS256, HS256, Ed25519, ES256, PS384, [null, ES256, Ed25519, ES384, Ed448, \"/wH//wA\\u003d\", HS256, EdDSA, HS384], eyJhbGciOiJIUzI1NiJ9, Ed448, [null], null, [ES384, ES256, Ed25519, Ed448, HS384, HS256, EdDSA], \"\", ES512, HS384, \"/wH//wA\\u003d\", EdDSA, ES384]");
            org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException; message: Unsupported JWS algorithm: {\"alg\":\"HS256\"}");
        } catch (java.lang.IllegalArgumentException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(jWSAlgorithm1);
        org.junit.Assert.assertEquals("'" + str2 + "' != '" + "{\"alg\":\"HS256\"}" + "'", str2, "{\"alg\":\"HS256\"}");
    }

    @Test
    public void test065() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test065");
        com.nimbusds.jose.JWSAlgorithm[] jWSAlgorithmArray0 = new com.nimbusds.jose.JWSAlgorithm[] {};
        com.nimbusds.jose.JWSAlgorithm.Family family1 = new com.nimbusds.jose.JWSAlgorithm.Family(jWSAlgorithmArray0);
        com.nimbusds.jose.JWSAlgorithm.Family family2 = com.nimbusds.jose.JWSAlgorithm.Family.ED;
        boolean boolean3 = family1.containsAll((java.util.Collection<com.nimbusds.jose.JWSAlgorithm>) family2);
        java.lang.Object[] objArray4 = family2.toArray();
        java.lang.Object obj5 = family2.clone();
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm6 = com.nimbusds.jose.JWSAlgorithm.Ed25519;
        family2.addFirst(jWSAlgorithm6);
        java.util.stream.Stream<com.nimbusds.jose.JWSAlgorithm> jWSAlgorithmStream8 = family2.stream();
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm10 = com.nimbusds.jose.JWSAlgorithm.parse("Ed448");
        org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier12 = null; // flaky "15) test065(AccessTokenVerifierRandoopRegressionS00)": new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm10, "[ES384, ES256, Ed25519, Ed448, HS384, EdDSA, HS256]");
        java.util.Set<com.nimbusds.jose.JWSAlgorithm> jWSAlgorithmSet13 = null; // flaky "12) test065(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier12.supportedJWSAlgorithms();
        com.nimbusds.jose.jca.JCAAware[] jCAAwareArray15 = new com.nimbusds.jose.jca.JCAAware[1];
        @SuppressWarnings("unchecked")
        com.nimbusds.jose.jca.JCAAware<com.nimbusds.jose.jca.JCAContext>[] jCAContextJCAAwareArray16 = (com.nimbusds.jose.jca.JCAAware<com.nimbusds.jose.jca.JCAContext>[]) jCAAwareArray15;
        jCAContextJCAAwareArray16[0] = accessTokenJWSVerifier12;
        // The following exception was thrown during execution in test generation
        try {
            com.nimbusds.jose.jca.JCAAware<com.nimbusds.jose.jca.JCAContext>[] jCAContextJCAAwareArray19 = family2.toArray(jCAContextJCAAwareArray16);
            org.junit.Assert.fail("Expected exception of type java.lang.ArrayStoreException; message: com.nimbusds.jose.JWSAlgorithm");
        } catch (java.lang.ArrayStoreException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(jWSAlgorithmArray0);
        org.junit.Assert.assertArrayEquals(jWSAlgorithmArray0, new com.nimbusds.jose.JWSAlgorithm[] {});
        org.junit.Assert.assertNotNull(family2);
        org.junit.Assert.assertTrue("'" + boolean3 + "' != '" + false + "'", boolean3 == false);
        org.junit.Assert.assertNotNull(objArray4);
// flaky "5) test065(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertEquals(java.util.Arrays.deepToString(objArray4), "[PS256, HS256, Ed25519, null, ES256, [null, ES256, Ed25519, ES384, Ed448, \"/wH//wA\\u003d\", HS256, EdDSA, HS384], \"\", eyJhbGciOiJIUzI1NiJ9, PS384, Ed448, [null], [ES384, ES256, Ed25519, Ed448, HS384, HS256, EdDSA], HS384, ES512, \"/wH//wA\\u003d\", ES384, EdDSA]");
// flaky "2) test065(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertEquals(java.util.Arrays.toString(objArray4), "[PS256, HS256, Ed25519, null, ES256, [null, ES256, Ed25519, ES384, Ed448, \"/wH//wA\\u003d\", HS256, EdDSA, HS384], \"\", eyJhbGciOiJIUzI1NiJ9, PS384, Ed448, [null], [ES384, ES256, Ed25519, Ed448, HS384, HS256, EdDSA], HS384, ES512, \"/wH//wA\\u003d\", ES384, EdDSA]");
        org.junit.Assert.assertNotNull(obj5);
// flaky "2) test065(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertEquals(obj5.toString(), "[PS256, HS256, Ed25519, null, ES256, [null, ES256, Ed25519, ES384, Ed448, \"/wH//wA\\u003d\", HS256, EdDSA, HS384], \"\", eyJhbGciOiJIUzI1NiJ9, PS384, Ed448, [null], [ES384, ES256, Ed25519, Ed448, HS384, HS256, EdDSA], HS384, ES512, \"/wH//wA\\u003d\", ES384, EdDSA]");
// flaky "1) test065(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertEquals(java.lang.String.valueOf(obj5), "[PS256, HS256, Ed25519, null, ES256, [null, ES256, Ed25519, ES384, Ed448, \"/wH//wA\\u003d\", HS256, EdDSA, HS384], \"\", eyJhbGciOiJIUzI1NiJ9, PS384, Ed448, [null], [ES384, ES256, Ed25519, Ed448, HS384, HS256, EdDSA], HS384, ES512, \"/wH//wA\\u003d\", ES384, EdDSA]");
// flaky "1) test065(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertEquals(java.util.Objects.toString(obj5), "[PS256, HS256, Ed25519, null, ES256, [null, ES256, Ed25519, ES384, Ed448, \"/wH//wA\\u003d\", HS256, EdDSA, HS384], \"\", eyJhbGciOiJIUzI1NiJ9, PS384, Ed448, [null], [ES384, ES256, Ed25519, Ed448, HS384, HS256, EdDSA], HS384, ES512, \"/wH//wA\\u003d\", ES384, EdDSA]");
        org.junit.Assert.assertNotNull(jWSAlgorithm6);
        org.junit.Assert.assertNotNull(jWSAlgorithmStream8);
        org.junit.Assert.assertNotNull(jWSAlgorithm10);
// flaky "1) test065(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jWSAlgorithmSet13);
        org.junit.Assert.assertNotNull(jCAAwareArray15);
        org.junit.Assert.assertNotNull(jCAContextJCAAwareArray16);
    }

    @Test
    public void test066() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test066");
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm0 = com.nimbusds.jose.JWSAlgorithm.HS256;
        com.nimbusds.jose.JWSAlgorithm[] jWSAlgorithmArray1 = new com.nimbusds.jose.JWSAlgorithm[] {};
        com.nimbusds.jose.JWSAlgorithm.Family family2 = new com.nimbusds.jose.JWSAlgorithm.Family(jWSAlgorithmArray1);
        com.nimbusds.jose.JWSAlgorithm.Family family3 = com.nimbusds.jose.JWSAlgorithm.Family.ED;
        boolean boolean4 = family2.containsAll((java.util.Collection<com.nimbusds.jose.JWSAlgorithm>) family3);
        boolean boolean5 = jWSAlgorithm0.equals((java.lang.Object) family2);
        com.nimbusds.jose.JWSHeader.Builder builder6 = new com.nimbusds.jose.JWSHeader.Builder(jWSAlgorithm0);
        // The following exception was thrown during execution in test generation
        try {
            org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier8 = null; // flaky "16) test066(AccessTokenVerifierRandoopRegressionS00)": new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm0, "\"Ed25519\"");
// flaky "13) test066(AccessTokenVerifierRandoopRegressionS00)":             org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException; message: A key pair is required, in the 'private:public' format");
        } catch (java.lang.IllegalArgumentException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(jWSAlgorithm0);
        org.junit.Assert.assertNotNull(jWSAlgorithmArray1);
        org.junit.Assert.assertArrayEquals(jWSAlgorithmArray1, new com.nimbusds.jose.JWSAlgorithm[] {});
        org.junit.Assert.assertNotNull(family3);
        org.junit.Assert.assertTrue("'" + boolean4 + "' != '" + false + "'", boolean4 == false);
        org.junit.Assert.assertTrue("'" + boolean5 + "' != '" + false + "'", boolean5 == false);
    }

    @Test
    public void test067() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test067");
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm1 = new com.nimbusds.jose.JWSAlgorithm("PS256");
        com.nimbusds.jose.JWSHeader jWSHeader2 = new com.nimbusds.jose.JWSHeader(jWSAlgorithm1);
        java.lang.String str3 = jWSAlgorithm1.toString();
        // The following exception was thrown during execution in test generation
        try {
            org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier5 = new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm1, "Ii93SC8vd0FcdTAwM2Qi");
            org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException; message: A key pair is required, in the 'private:public' format");
        } catch (java.lang.IllegalArgumentException e) {
            // Expected exception.
        }
        org.junit.Assert.assertEquals("'" + str3 + "' != '" + "PS256" + "'", str3, "PS256");
    }

    @Test
    public void test068() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test068");
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm1 = new com.nimbusds.jose.JWSAlgorithm("PS256");
        // The following exception was thrown during execution in test generation
        try {
            org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier3 = new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm1, "header.payload");
            org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException; message: A key pair is required, in the 'private:public' format");
        } catch (java.lang.IllegalArgumentException e) {
            // Expected exception.
        }
    }

    @Test
    public void test069() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test069");
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm1 = new com.nimbusds.jose.JWSAlgorithm("none");
        // The following exception was thrown during execution in test generation
        try {
            org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier3 = new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm1, "\"\\\"\\\\\\\"\\\\\\\"\\\"\"");
            org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException; message: Unsupported JWS algorithm: none");
        } catch (java.lang.IllegalArgumentException e) {
            // Expected exception.
        }
    }

    @Test
    public void test070() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test070");
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm0 = null;
        // The following exception was thrown during execution in test generation
{ // flaky ('try' without 'catch', 'finally' or resource declarations):         try {
            org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier2 = null; // flaky "17) test070(AccessTokenVerifierRandoopRegressionS00)": new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm0, "\"\\\"eyJhbGciOiJIUzI1NiJ9\\\"\"");
// flaky "14) test070(AccessTokenVerifierRandoopRegressionS00)":             org.junit.Assert.fail("Expected exception of type com.nimbusds.jose.KeyLengthException; message: The secret length must be at least 256 bits");
// flaky (is never thrown in body of corresponding try statement):         } catch (com.nimbusds.jose.KeyLengthException e) {
            // Expected exception.
        }
    }

    @Test
    public void test071() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test071");
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm0 = com.nimbusds.jose.JWSAlgorithm.PS256;
        java.lang.String str1 = jWSAlgorithm0.toString();
        java.lang.String str2 = jWSAlgorithm0.toString();
        // The following exception was thrown during execution in test generation
        try {
            org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier4 = new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm0, "\"ZXlKaGJHY2lPaUpGWkRRME9DSjk\"");
            org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException; message: A key pair is required, in the 'private:public' format");
        } catch (java.lang.IllegalArgumentException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(jWSAlgorithm0);
        org.junit.Assert.assertEquals("'" + str1 + "' != '" + "PS256" + "'", str1, "PS256");
        org.junit.Assert.assertEquals("'" + str2 + "' != '" + "PS256" + "'", str2, "PS256");
    }

    @Test
    public void test072() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test072");
        com.nimbusds.jose.JWSAlgorithm[] jWSAlgorithmArray0 = new com.nimbusds.jose.JWSAlgorithm[] {};
        com.nimbusds.jose.JWSAlgorithm.Family family1 = new com.nimbusds.jose.JWSAlgorithm.Family(jWSAlgorithmArray0);
        boolean boolean3 = family1.contains((java.lang.Object) "0123456789abcdef0123456789abcdef");
        boolean boolean5 = family1.equals((java.lang.Object) 0L);
        boolean boolean6 = family1.isEmpty();
        java.util.SequencedSet<com.nimbusds.jose.JWSAlgorithm> jWSAlgorithmSet7 = family1.reversed();
        java.lang.Object obj8 = family1.clone();
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm9 = com.nimbusds.jose.JWSAlgorithm.HS384;
        family1.addFirst(jWSAlgorithm9);
        java.util.stream.Stream<com.nimbusds.jose.JWSAlgorithm> jWSAlgorithmStream11 = family1.parallelStream();
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm13 = com.nimbusds.jose.JWSAlgorithm.parse("\"ZmVkY2JhOTg3NjU0MzIxMGZlZGNiYTk4NzY1NDMyMTA\\u003d\"");
        family1.addFirst(jWSAlgorithm13);
        // The following exception was thrown during execution in test generation
        try {
            org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier16 = new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm13, "\035\ufffd\ufffd\ufffd\ufffd\022\ufffd\ufffd\004\ufffd\ufffd\035\ufffd\ufffdYGK\1778\021\ufffdGCH\t\ufffd\ufffd\037\ufffd\ufffd\002\ufffd\ufffd");
            org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException; message: A key pair is required, in the 'private:public' format");
        } catch (java.lang.IllegalArgumentException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(jWSAlgorithmArray0);
        org.junit.Assert.assertArrayEquals(jWSAlgorithmArray0, new com.nimbusds.jose.JWSAlgorithm[] {});
        org.junit.Assert.assertTrue("'" + boolean3 + "' != '" + false + "'", boolean3 == false);
        org.junit.Assert.assertTrue("'" + boolean5 + "' != '" + false + "'", boolean5 == false);
        org.junit.Assert.assertTrue("'" + boolean6 + "' != '" + true + "'", boolean6 == true);
        org.junit.Assert.assertNotNull(jWSAlgorithmSet7);
        org.junit.Assert.assertNotNull(obj8);
        org.junit.Assert.assertEquals(obj8.toString(), "[]");
        org.junit.Assert.assertEquals(java.lang.String.valueOf(obj8), "[]");
        org.junit.Assert.assertEquals(java.util.Objects.toString(obj8), "[]");
        org.junit.Assert.assertNotNull(jWSAlgorithm9);
        org.junit.Assert.assertNotNull(jWSAlgorithmStream11);
        org.junit.Assert.assertNotNull(jWSAlgorithm13);
    }

    @Test
    public void test073() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test073");
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm1 = new com.nimbusds.jose.JWSAlgorithm("\"\\\"ES256K\\\"\"");
        // The following exception was thrown during execution in test generation
        try {
            org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier3 = new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm1, "\"PS256\"");
            org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException; message: Unsupported JWS algorithm: \"\\\"ES256K\\\"\"");
        } catch (java.lang.IllegalArgumentException e) {
            // Expected exception.
        }
    }

    @Test
    public void test074() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test074");
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm0 = com.nimbusds.jose.JWSAlgorithm.HS256;
        com.nimbusds.jose.JWSHeader jWSHeader1 = new com.nimbusds.jose.JWSHeader(jWSAlgorithm0);
        boolean boolean2 = jWSHeader1.isBase64URLEncodePayload();
        java.net.URI uRI3 = jWSHeader1.getJWKURL();
        java.util.Set<java.lang.String> strSet4 = jWSHeader1.getIncludedParams();
        java.lang.String str5 = jWSHeader1.toString();
        java.lang.String str6 = jWSHeader1.getKeyID();
        java.util.Set<java.lang.String> strSet7 = jWSHeader1.getIncludedParams();
        java.util.Set<java.lang.String> strSet8 = jWSHeader1.getCriticalParams();
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm9 = jWSHeader1.getAlgorithm();
        // The following exception was thrown during execution in test generation
        try {
            org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier11 = null; // flaky "18) test074(AccessTokenVerifierRandoopRegressionS00)": new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm9, "\ufffd\001\ufffd\ufffd\000\ufffd\ufffd");
// flaky "15) test074(AccessTokenVerifierRandoopRegressionS00)":             org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException; message: A key pair is required, in the 'private:public' format");
        } catch (java.lang.IllegalArgumentException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(jWSAlgorithm0);
        org.junit.Assert.assertTrue("'" + boolean2 + "' != '" + true + "'", boolean2 == true);
        org.junit.Assert.assertNull(uRI3);
        org.junit.Assert.assertNotNull(strSet4);
        org.junit.Assert.assertEquals("'" + str5 + "' != '" + "{\"alg\":\"HS256\"}" + "'", str5, "{\"alg\":\"HS256\"}");
        org.junit.Assert.assertNull(str6);
        org.junit.Assert.assertNotNull(strSet7);
        org.junit.Assert.assertNull(strSet8);
        org.junit.Assert.assertNotNull(jWSAlgorithm9);
    }

    @Test
    public void test075() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test075");
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm2 = com.nimbusds.jose.JWSAlgorithm.HS512;
        com.nimbusds.jose.Requirement requirement3 = jWSAlgorithm2.getRequirement();
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm4 = new com.nimbusds.jose.JWSAlgorithm("\"RS256\"", requirement3);
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm5 = new com.nimbusds.jose.JWSAlgorithm("[[null, ES256, Ed25519, ES384, Ed448, \"/wH//wA\\u003d\", HS256, EdDSA, HS384], Ed25519, ES256, PS256, PS384, eyJhbGciOiJIUzI1NiJ9, Ed448, [null], \"\", ES512, null, [ES384, ES256, Ed25519, Ed448, HS384, HS256, EdDSA], ES384, \"/wH//wA\\u003d\", HS384, EdDSA, HS256]", requirement3);
        // The following exception was thrown during execution in test generation
        try {
            org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier7 = new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm5, "\"77+9CgoBAA\\u003d\\u003d\"");
            org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException; message: Unsupported JWS algorithm: [[null, ES256, Ed25519, ES384, Ed448, \"/wH//wA\\u003d\", HS256, EdDSA, HS384], Ed25519, ES256, PS256, PS384, eyJhbGciOiJIUzI1NiJ9, Ed448, [null], \"\", ES512, null, [ES384, ES256, Ed25519, Ed448, HS384, HS256, EdDSA], ES384, \"/wH//wA\\u003d\", HS384, EdDSA, HS256]");
        } catch (java.lang.IllegalArgumentException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(jWSAlgorithm2);
        org.junit.Assert.assertTrue("'" + requirement3 + "' != '" + com.nimbusds.jose.Requirement.OPTIONAL + "'", requirement3.equals(com.nimbusds.jose.Requirement.OPTIONAL));
    }

    @Test
    public void test076() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test076");
        com.nimbusds.jose.JWSAlgorithm.Family family0 = com.nimbusds.jose.JWSAlgorithm.Family.EC;
        int int1 = family0.size();
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm2 = family0.getFirst();
        com.nimbusds.jose.JWSHeader.Builder builder3 = new com.nimbusds.jose.JWSHeader.Builder(jWSAlgorithm2);
        // The following exception was thrown during execution in test generation
        try {
            org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier5 = new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm2, "[RS256, RS384, RS512, PS256, PS384, PS512]");
            org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException; message: Unsupported JWS algorithm: eyJhbGciOiJFZDQ0OCJ9");
        } catch (java.lang.IllegalArgumentException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(family0);
// flaky "19) test076(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertTrue("'" + int1 + "' != '" + 7 + "'", int1 == 7);
        org.junit.Assert.assertNotNull(jWSAlgorithm2);
    }

    @Test
    public void test077() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test077");
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm0 = null;
        // The following exception was thrown during execution in test generation
{ // flaky ('try' without 'catch', 'finally' or resource declarations):         try {
            org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier2 = null; // flaky "20) test077(AccessTokenVerifierRandoopRegressionS00)": new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm0, "\"[ES256, ES256K, ES384, ES512]\"");
// flaky "16) test077(AccessTokenVerifierRandoopRegressionS00)":             org.junit.Assert.fail("Expected exception of type com.nimbusds.jose.KeyLengthException; message: The secret length must be at least 256 bits");
// flaky (is never thrown in body of corresponding try statement):         } catch (com.nimbusds.jose.KeyLengthException e) {
            // Expected exception.
        }
    }

    @Test
    public void test078() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test078");
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm1 = new com.nimbusds.jose.JWSAlgorithm("none");
        // The following exception was thrown during execution in test generation
        try {
            org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier3 = new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm1, "[ES384, ES256, Ed25519, Ed448, HS384, EdDSA, HS256]");
            org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException; message: Unsupported JWS algorithm: none");
        } catch (java.lang.IllegalArgumentException e) {
            // Expected exception.
        }
    }

    @Test
    public void test079() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test079");
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm0 = com.nimbusds.jose.JWSAlgorithm.ES384;
        com.nimbusds.jose.JWSHeader jWSHeader1 = new com.nimbusds.jose.JWSHeader(jWSAlgorithm0);
        java.lang.String str2 = jWSAlgorithm0.toString();
        // The following exception was thrown during execution in test generation
        try {
            org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier4 = new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm0, "[ES256, Ed25519, PS384, [null, ES256, Ed25519, ES384, Ed448, \"/wH//wA\\u003d\", HS256, EdDSA, HS384], eyJhbGciOiJIUzI1NiJ9, Ed448, [null], null, [ES384, ES256, Ed25519, Ed448, HS384, HS256, EdDSA], \"\", ES512, HS384, PS256, \"/wH//wA\\u003d\", HS256, EdDSA, ES384]");
            org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException; message: Unsupported JWS algorithm: ES384");
        } catch (java.lang.IllegalArgumentException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(jWSAlgorithm0);
        org.junit.Assert.assertEquals("'" + str2 + "' != '" + "ES384" + "'", str2, "ES384");
    }

    @Test
    public void test080() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test080");
        com.nimbusds.jose.JWSAlgorithm.Family family0 = com.nimbusds.jose.JWSAlgorithm.Family.EC;
        int int1 = family0.size();
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm3 = new com.nimbusds.jose.JWSAlgorithm("eyJhbGciOiJFZDQ0OCJ9");
        family0.addFirst(jWSAlgorithm3);
        // The following exception was thrown during execution in test generation
        try {
            org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier6 = new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm3, "ES512");
            org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException; message: Unsupported JWS algorithm: eyJhbGciOiJFZDQ0OCJ9");
        } catch (java.lang.IllegalArgumentException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(family0);
// flaky "21) test080(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertTrue("'" + int1 + "' != '" + 7 + "'", int1 == 7);
    }

    @Test
    public void test081() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test081");
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm0 = com.nimbusds.jose.JWSAlgorithm.Ed25519;
        java.lang.String str1 = jWSAlgorithm0.toString();
        // The following exception was thrown during execution in test generation
        try {
            org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier3 = new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm0, "_wH__wA");
            org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException; message: Unsupported JWS algorithm: Ed25519");
        } catch (java.lang.IllegalArgumentException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(jWSAlgorithm0);
        org.junit.Assert.assertEquals("'" + str1 + "' != '" + "Ed25519" + "'", str1, "Ed25519");
    }

    @Test
    public void test082() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test082");
        com.nimbusds.jose.JWSAlgorithm[] jWSAlgorithmArray0 = new com.nimbusds.jose.JWSAlgorithm[] {};
        com.nimbusds.jose.JWSAlgorithm.Family family1 = new com.nimbusds.jose.JWSAlgorithm.Family(jWSAlgorithmArray0);
        com.nimbusds.jose.JWSAlgorithm.Family family2 = com.nimbusds.jose.JWSAlgorithm.Family.ED;
        boolean boolean3 = family1.containsAll((java.util.Collection<com.nimbusds.jose.JWSAlgorithm>) family2);
        java.lang.Object[] objArray4 = family2.toArray();
        java.lang.Object obj5 = family2.clone();
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm6 = com.nimbusds.jose.JWSAlgorithm.HS256;
        com.nimbusds.jose.JWSAlgorithm[] jWSAlgorithmArray7 = new com.nimbusds.jose.JWSAlgorithm[] {};
        com.nimbusds.jose.JWSAlgorithm.Family family8 = new com.nimbusds.jose.JWSAlgorithm.Family(jWSAlgorithmArray7);
        com.nimbusds.jose.JWSAlgorithm.Family family9 = com.nimbusds.jose.JWSAlgorithm.Family.ED;
        boolean boolean10 = family8.containsAll((java.util.Collection<com.nimbusds.jose.JWSAlgorithm>) family9);
        boolean boolean11 = jWSAlgorithm6.equals((java.lang.Object) family8);
        family2.addFirst(jWSAlgorithm6);
        boolean boolean14 = family2.equals((java.lang.Object) 3);
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm15 = family2.getFirst();
        // The following exception was thrown during execution in test generation
        try {
            org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier17 = null; // flaky "22) test082(AccessTokenVerifierRandoopRegressionS00)": new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm15, "\"HS256\"");
// flaky "17) test082(AccessTokenVerifierRandoopRegressionS00)":             org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException; message: A key pair is required, in the 'private:public' format");
        } catch (java.lang.IllegalArgumentException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(jWSAlgorithmArray0);
        org.junit.Assert.assertArrayEquals(jWSAlgorithmArray0, new com.nimbusds.jose.JWSAlgorithm[] {});
        org.junit.Assert.assertNotNull(family2);
        org.junit.Assert.assertTrue("'" + boolean3 + "' != '" + false + "'", boolean3 == false);
        org.junit.Assert.assertNotNull(objArray4);
// flaky "6) test082(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertEquals(java.util.Arrays.deepToString(objArray4), "[Ed25519, ES256, 77+9CgoBAA==, eyJhbGciOiJIUzI1NiJ9, [null, ES256, Ed25519, ES384, Ed448, \"/wH//wA\\u003d\", HS256, EdDSA, HS384], Ed448, \"\", PS384, [null], PS256, ES512, null, [ES384, ES256, Ed25519, Ed448, HS384, HS256, EdDSA], \"/wH//wA\\u003d\", HS384, EdDSA, ES384, HS256]");
// flaky "3) test082(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertEquals(java.util.Arrays.toString(objArray4), "[Ed25519, ES256, 77+9CgoBAA==, eyJhbGciOiJIUzI1NiJ9, [null, ES256, Ed25519, ES384, Ed448, \"/wH//wA\\u003d\", HS256, EdDSA, HS384], Ed448, \"\", PS384, [null], PS256, ES512, null, [ES384, ES256, Ed25519, Ed448, HS384, HS256, EdDSA], \"/wH//wA\\u003d\", HS384, EdDSA, ES384, HS256]");
        org.junit.Assert.assertNotNull(obj5);
// flaky "3) test082(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertEquals(obj5.toString(), "[Ed25519, ES256, 77+9CgoBAA==, eyJhbGciOiJIUzI1NiJ9, [null, ES256, Ed25519, ES384, Ed448, \"/wH//wA\\u003d\", HS256, EdDSA, HS384], Ed448, \"\", PS384, [null], PS256, ES512, null, [ES384, ES256, Ed25519, Ed448, HS384, HS256, EdDSA], \"/wH//wA\\u003d\", HS384, EdDSA, ES384, HS256]");
// flaky "2) test082(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertEquals(java.lang.String.valueOf(obj5), "[Ed25519, ES256, 77+9CgoBAA==, eyJhbGciOiJIUzI1NiJ9, [null, ES256, Ed25519, ES384, Ed448, \"/wH//wA\\u003d\", HS256, EdDSA, HS384], Ed448, \"\", PS384, [null], PS256, ES512, null, [ES384, ES256, Ed25519, Ed448, HS384, HS256, EdDSA], \"/wH//wA\\u003d\", HS384, EdDSA, ES384, HS256]");
// flaky "2) test082(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertEquals(java.util.Objects.toString(obj5), "[Ed25519, ES256, 77+9CgoBAA==, eyJhbGciOiJIUzI1NiJ9, [null, ES256, Ed25519, ES384, Ed448, \"/wH//wA\\u003d\", HS256, EdDSA, HS384], Ed448, \"\", PS384, [null], PS256, ES512, null, [ES384, ES256, Ed25519, Ed448, HS384, HS256, EdDSA], \"/wH//wA\\u003d\", HS384, EdDSA, ES384, HS256]");
        org.junit.Assert.assertNotNull(jWSAlgorithm6);
        org.junit.Assert.assertNotNull(jWSAlgorithmArray7);
        org.junit.Assert.assertArrayEquals(jWSAlgorithmArray7, new com.nimbusds.jose.JWSAlgorithm[] {});
        org.junit.Assert.assertNotNull(family9);
        org.junit.Assert.assertTrue("'" + boolean10 + "' != '" + false + "'", boolean10 == false);
        org.junit.Assert.assertTrue("'" + boolean11 + "' != '" + false + "'", boolean11 == false);
        org.junit.Assert.assertTrue("'" + boolean14 + "' != '" + false + "'", boolean14 == false);
        org.junit.Assert.assertNotNull(jWSAlgorithm15);
    }

    @Test
    public void test083() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test083");
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm1 = com.nimbusds.jose.JWSAlgorithm.parse("\"\"");
        com.nimbusds.jose.JWSHeader jWSHeader2 = new com.nimbusds.jose.JWSHeader(jWSAlgorithm1);
        // The following exception was thrown during execution in test generation
        try {
            org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier4 = new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm1, "\"IlptVmtZMkpoT1RnM05qVTBNekl4TUdabFpHTmlZVGs0TnpZMU5ETXlNVEFcdTAwM2Qi\"");
            org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException; message: Unsupported JWS algorithm: \"\"");
        } catch (java.lang.IllegalArgumentException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(jWSAlgorithm1);
    }

    @Test
    public void test084() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test084");
        com.nimbusds.jose.JWSAlgorithm.Family family0 = com.nimbusds.jose.JWSAlgorithm.Family.ED;
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm1 = com.nimbusds.jose.JWSAlgorithm.EdDSA;
        family0.addLast(jWSAlgorithm1);
        com.nimbusds.jose.JWSHeader.Builder builder3 = new com.nimbusds.jose.JWSHeader.Builder(jWSAlgorithm1);
        java.lang.String str4 = jWSAlgorithm1.toJSONString();
        // The following exception was thrown during execution in test generation
        try {
            org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier6 = new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm1, "\"\\\"\\\\\\\"ES256K\\\\\\\"\\\"\"");
            org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException; message: Unsupported JWS algorithm: EdDSA");
        } catch (java.lang.IllegalArgumentException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(family0);
        org.junit.Assert.assertNotNull(jWSAlgorithm1);
        org.junit.Assert.assertEquals("'" + str4 + "' != '" + "\"EdDSA\"" + "'", str4, "\"EdDSA\"");
    }

    @Test
    public void test085() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test085");
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm1 = new com.nimbusds.jose.JWSAlgorithm("\"ES256\"");
        // The following exception was thrown during execution in test generation
        try {
            org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier3 = new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm1, "ES256");
            org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException; message: Unsupported JWS algorithm: \"ES256\"");
        } catch (java.lang.IllegalArgumentException e) {
            // Expected exception.
        }
    }

    @Test
    public void test086() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test086");
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm0 = com.nimbusds.jose.JWSAlgorithm.RS256;
        java.lang.String str1 = jWSAlgorithm0.getName();
        java.lang.String str2 = jWSAlgorithm0.getName();
        // The following exception was thrown during execution in test generation
        try {
            org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier4 = new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm0, "\"\\\"ES256K\\\"\"");
            org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException; message: A key pair is required, in the 'private:public' format");
        } catch (java.lang.IllegalArgumentException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(jWSAlgorithm0);
        org.junit.Assert.assertEquals("'" + str1 + "' != '" + "RS256" + "'", str1, "RS256");
        org.junit.Assert.assertEquals("'" + str2 + "' != '" + "RS256" + "'", str2, "RS256");
    }

    @Test
    public void test087() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test087");
        com.nimbusds.jose.JWSAlgorithm[] jWSAlgorithmArray0 = new com.nimbusds.jose.JWSAlgorithm[] {};
        com.nimbusds.jose.JWSAlgorithm.Family family1 = new com.nimbusds.jose.JWSAlgorithm.Family(jWSAlgorithmArray0);
        com.nimbusds.jose.JWSAlgorithm.Family family2 = com.nimbusds.jose.JWSAlgorithm.Family.ED;
        boolean boolean3 = family1.containsAll((java.util.Collection<com.nimbusds.jose.JWSAlgorithm>) family2);
        java.lang.Object[] objArray4 = family2.toArray();
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm6 = new com.nimbusds.jose.JWSAlgorithm("PS256");
        com.nimbusds.jose.JWSHeader jWSHeader7 = new com.nimbusds.jose.JWSHeader(jWSAlgorithm6);
        java.lang.String str8 = jWSAlgorithm6.toString();
        family2.addLast(jWSAlgorithm6);
        // The following exception was thrown during execution in test generation
        try {
            org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier11 = new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm6, "");
            org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException; message: A key pair is required, in the 'private:public' format");
        } catch (java.lang.IllegalArgumentException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(jWSAlgorithmArray0);
        org.junit.Assert.assertArrayEquals(jWSAlgorithmArray0, new com.nimbusds.jose.JWSAlgorithm[] {});
        org.junit.Assert.assertNotNull(family2);
        org.junit.Assert.assertTrue("'" + boolean3 + "' != '" + false + "'", boolean3 == false);
        org.junit.Assert.assertNotNull(objArray4);
// flaky "23) test087(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertEquals(java.util.Arrays.deepToString(objArray4), "[Ed25519, ES256, eyJhbGciOiJIUzI1NiJ9, 77+9CgoBAA==, [null, ES256, Ed25519, ES384, Ed448, \"/wH//wA\\u003d\", HS256, EdDSA, HS384], Ed448, PS384, [null], PS256, [ES384, ES256, Ed25519, Ed448, HS384, HS256, EdDSA], ES384, ES512, HS384, \"\", EdDSA, HS256, \"/wH//wA\\u003d\", null]");
// flaky "18) test087(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertEquals(java.util.Arrays.toString(objArray4), "[Ed25519, ES256, eyJhbGciOiJIUzI1NiJ9, 77+9CgoBAA==, [null, ES256, Ed25519, ES384, Ed448, \"/wH//wA\\u003d\", HS256, EdDSA, HS384], Ed448, PS384, [null], PS256, [ES384, ES256, Ed25519, Ed448, HS384, HS256, EdDSA], ES384, ES512, HS384, \"\", EdDSA, HS256, \"/wH//wA\\u003d\", null]");
        org.junit.Assert.assertEquals("'" + str8 + "' != '" + "PS256" + "'", str8, "PS256");
    }

    @Test
    public void test088() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test088");
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm0 = com.nimbusds.jose.JWSAlgorithm.Ed448;
        boolean boolean2 = jWSAlgorithm0.equals((java.lang.Object) "\ufffd\n\n\001\000");
        com.nimbusds.jose.JWSHeader.Builder builder3 = new com.nimbusds.jose.JWSHeader.Builder(jWSAlgorithm0);
        com.nimbusds.jose.JWSHeader.Builder builder5 = builder3.base64URLEncodePayload(true);
        java.util.LinkedHashSet<java.lang.String> strSet7 = java.util.LinkedHashSet.newLinkedHashSet((int) '#');
        com.nimbusds.jose.JWSHeader.Builder builder8 = builder3.criticalParams((java.util.Set<java.lang.String>) strSet7);
        com.nimbusds.jose.JWSHeader jWSHeader9 = builder3.build();
        com.nimbusds.jose.JWSHeader.Builder builder10 = new com.nimbusds.jose.JWSHeader.Builder(jWSHeader9);
        com.nimbusds.jose.jwk.JWK jWK11 = jWSHeader9.getJWK();
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm12 = jWSHeader9.getAlgorithm();
        org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier14 = null; // flaky "24) test088(AccessTokenVerifierRandoopRegressionS00)": new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm12, "[Ed25519, ES384, ES256, null, [null, ES256, Ed25519, ES384, Ed448, \"/wH//wA\\u003d\", HS256, EdDSA, HS384], PS256, PS384, eyJhbGciOiJIUzI1NiJ9, Ed448, [null], \"\", [ES384, ES256, Ed25519, Ed448, HS384, HS256, EdDSA], ES512, HS384, HS256, \"/wH//wA\\u003d\", EdDSA]");
        org.junit.Assert.assertNotNull(jWSAlgorithm0);
        org.junit.Assert.assertTrue("'" + boolean2 + "' != '" + false + "'", boolean2 == false);
        org.junit.Assert.assertNotNull(builder5);
        org.junit.Assert.assertNotNull(strSet7);
        org.junit.Assert.assertNotNull(builder8);
        org.junit.Assert.assertNotNull(jWSHeader9);
        org.junit.Assert.assertNull(jWK11);
        org.junit.Assert.assertNotNull(jWSAlgorithm12);
    }

    @Test
    public void test089() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test089");
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm0 = null;
        // The following exception was thrown during execution in test generation
{ // flaky ('try' without 'catch', 'finally' or resource declarations):         try {
            org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier2 = null; // flaky "25) test089(AccessTokenVerifierRandoopRegressionS00)": new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm0, "\"\\\"Ed25519\\\"\"");
// flaky "19) test089(AccessTokenVerifierRandoopRegressionS00)":             org.junit.Assert.fail("Expected exception of type com.nimbusds.jose.KeyLengthException; message: The secret length must be at least 256 bits");
// flaky (is never thrown in body of corresponding try statement):         } catch (com.nimbusds.jose.KeyLengthException e) {
            // Expected exception.
        }
    }

    @Test
    public void test090() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test090");
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm0 = null;
        org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier2 = null; // flaky "26) test090(AccessTokenVerifierRandoopRegressionS00)": new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm0, "[Ed25519, ES384, ES256, null, [null, ES256, Ed25519, ES384, Ed448, \"/wH//wA\\u003d\", HS256, EdDSA, HS384], PS256, PS384, eyJhbGciOiJIUzI1NiJ9, Ed448, [null], \"\", [ES384, ES256, Ed25519, Ed448, HS384, HS256, EdDSA], ES512, HS384, HS256, \"/wH//wA\\u003d\", EdDSA]");
        com.nimbusds.jose.jca.JCAContext jCAContext3 = null; // flaky "20) test090(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier2.getJCAContext();
// flaky "7) test090(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jCAContext3);
    }

    @Test
    public void test091() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test091");
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm1 = com.nimbusds.jose.JWSAlgorithm.parse("Ed25519");
        java.lang.String str2 = jWSAlgorithm1.toJSONString();
        // The following exception was thrown during execution in test generation
        try {
            org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier4 = new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm1, "[ES256, PS256, Ed25519, PS384, [null, ES256, Ed25519, ES384, Ed448, \"/wH//wA\\u003d\", HS256, EdDSA, HS384], eyJhbGciOiJIUzI1NiJ9, Ed448, [null], \"\", ES512, HS384, null, [ES384, ES256, Ed25519, Ed448, HS384, HS256, EdDSA], EdDSA, ES384, \"/wH//wA\\u003d\", HS256]");
            org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException; message: Unsupported JWS algorithm: Ed25519");
        } catch (java.lang.IllegalArgumentException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(jWSAlgorithm1);
        org.junit.Assert.assertEquals("'" + str2 + "' != '" + "\"Ed25519\"" + "'", str2, "\"Ed25519\"");
    }

    @Test
    public void test092() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test092");
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm3 = com.nimbusds.jose.JWSAlgorithm.HS256;
        com.nimbusds.jose.Requirement requirement4 = jWSAlgorithm3.getRequirement();
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm5 = new com.nimbusds.jose.JWSAlgorithm("HS384", requirement4);
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm6 = new com.nimbusds.jose.JWSAlgorithm("[null]", requirement4);
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm7 = new com.nimbusds.jose.JWSAlgorithm("\ufffd\n\n\001\000", requirement4);
        com.nimbusds.jose.JWSHeader jWSHeader8 = new com.nimbusds.jose.JWSHeader(jWSAlgorithm7);
        java.lang.String str9 = jWSAlgorithm7.getName();
        // The following exception was thrown during execution in test generation
{ // flaky ('try' without 'catch', 'finally' or resource declarations):         try {
            org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier11 = null; // flaky "27) test092(AccessTokenVerifierRandoopRegressionS00)": new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm7, "\"RS384\"");
// flaky "21) test092(AccessTokenVerifierRandoopRegressionS00)":             org.junit.Assert.fail("Expected exception of type com.nimbusds.jose.KeyLengthException; message: The secret length must be at least 256 bits");
// flaky (is never thrown in body of corresponding try statement):         } catch (com.nimbusds.jose.KeyLengthException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(jWSAlgorithm3);
        org.junit.Assert.assertTrue("'" + requirement4 + "' != '" + com.nimbusds.jose.Requirement.REQUIRED + "'", requirement4.equals(com.nimbusds.jose.Requirement.REQUIRED));
        org.junit.Assert.assertEquals("'" + str9 + "' != '" + "\ufffd\n\n\001\000" + "'", str9, "\ufffd\n\n\001\000");
    }

    @Test
    public void test093() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test093");
        com.nimbusds.jose.JWSAlgorithm[] jWSAlgorithmArray0 = new com.nimbusds.jose.JWSAlgorithm[] {};
        com.nimbusds.jose.JWSAlgorithm.Family family1 = new com.nimbusds.jose.JWSAlgorithm.Family(jWSAlgorithmArray0);
        com.nimbusds.jose.JWSAlgorithm.Family family2 = com.nimbusds.jose.JWSAlgorithm.Family.ED;
        boolean boolean3 = family1.containsAll((java.util.Collection<com.nimbusds.jose.JWSAlgorithm>) family2);
        java.lang.Object[] objArray4 = family2.toArray();
        java.util.Spliterator<com.nimbusds.jose.JWSAlgorithm> jWSAlgorithmSpliterator5 = family2.spliterator();
        boolean boolean6 = family2.isEmpty();
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm7 = com.nimbusds.jose.JWSAlgorithm.HS256;
        com.nimbusds.jose.Requirement requirement8 = jWSAlgorithm7.getRequirement();
        family2.addLast(jWSAlgorithm7);
        java.util.stream.Stream<com.nimbusds.jose.JWSAlgorithm> jWSAlgorithmStream10 = family2.parallelStream();
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm11 = com.nimbusds.jose.JWSAlgorithm.HS256;
        com.nimbusds.jose.JWSHeader jWSHeader12 = new com.nimbusds.jose.JWSHeader(jWSAlgorithm11);
        com.nimbusds.jose.UnprotectedHeader unprotectedHeader13 = null;
        com.nimbusds.jose.Header header14 = jWSHeader12.join(unprotectedHeader13);
        java.util.Set<java.lang.String> strSet15 = jWSHeader12.getCriticalParams();
        boolean boolean16 = family2.contains((java.lang.Object) jWSHeader12);
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm17 = jWSHeader12.getAlgorithm();
        // The following exception was thrown during execution in test generation
        try {
            org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier19 = null; // flaky "28) test093(AccessTokenVerifierRandoopRegressionS00)": new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm17, "\"ES384\"");
// flaky "22) test093(AccessTokenVerifierRandoopRegressionS00)":             org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException; message: A key pair is required, in the 'private:public' format");
        } catch (java.lang.IllegalArgumentException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(jWSAlgorithmArray0);
        org.junit.Assert.assertArrayEquals(jWSAlgorithmArray0, new com.nimbusds.jose.JWSAlgorithm[] {});
        org.junit.Assert.assertNotNull(family2);
        org.junit.Assert.assertTrue("'" + boolean3 + "' != '" + false + "'", boolean3 == false);
        org.junit.Assert.assertNotNull(objArray4);
// flaky "8) test093(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertEquals(java.util.Arrays.deepToString(objArray4), "\ufffd\n\n\001\000, eyJhbGciOiJIUzI1NiJ9, Ed25519, 77+9CgoBAA==, [null, ES256, Ed25519, ES384, Ed448, \"/wH//wA\\u003d\", HS256, EdDSA, HS384], Ed448, PS384, [null], [ES384, ES256, Ed25519, Ed448, HS384, HS256, EdDSA], \"\", \"/wH//wA\\u003d\", ES512, ES384, null, HS384, EdDSA, \"fedcba9876543210fedcba9876543210\", HS256]");
// flaky "4) test093(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertEquals(java.util.Arrays.toString(objArray4), "\ufffd\n\n\001\000, eyJhbGciOiJIUzI1NiJ9, Ed25519, 77+9CgoBAA==, [null, ES256, Ed25519, ES384, Ed448, \"/wH//wA\\u003d\", HS256, EdDSA, HS384], Ed448, PS384, [null], [ES384, ES256, Ed25519, Ed448, HS384, HS256, EdDSA], \"\", \"/wH//wA\\u003d\", ES512, ES384, null, HS384, EdDSA, \"fedcba9876543210fedcba9876543210\", HS256]");
        org.junit.Assert.assertNotNull(jWSAlgorithmSpliterator5);
        org.junit.Assert.assertTrue("'" + boolean6 + "' != '" + false + "'", boolean6 == false);
        org.junit.Assert.assertNotNull(jWSAlgorithm7);
        org.junit.Assert.assertTrue("'" + requirement8 + "' != '" + com.nimbusds.jose.Requirement.REQUIRED + "'", requirement8.equals(com.nimbusds.jose.Requirement.REQUIRED));
        org.junit.Assert.assertNotNull(jWSAlgorithmStream10);
        org.junit.Assert.assertNotNull(jWSAlgorithm11);
        org.junit.Assert.assertNotNull(header14);
        org.junit.Assert.assertNull(strSet15);
        org.junit.Assert.assertTrue("'" + boolean16 + "' != '" + false + "'", boolean16 == false);
        org.junit.Assert.assertNotNull(jWSAlgorithm17);
    }

    @Test
    public void test094() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test094");
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm1 = com.nimbusds.jose.JWSAlgorithm.parse("[ES256, Ed25519, PS256, null, [null, ES256, Ed25519, ES384, Ed448, \"/wH//wA\\u003d\", HS256, EdDSA, HS384], \"\", eyJhbGciOiJIUzI1NiJ9, PS384, Ed448, [null], [ES384, ES256, Ed25519, Ed448, HS384, HS256, EdDSA], ES512, ES384, EdDSA, HS384, \"/wH//wA\\u003d\", HS256]");
        // The following exception was thrown during execution in test generation
        try {
            org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier3 = new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm1, "0123456789abcdef0123456789abcdef");
            org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException; message: Unsupported JWS algorithm: [ES256, Ed25519, PS256, null, [null, ES256, Ed25519, ES384, Ed448, \"/wH//wA\\u003d\", HS256, EdDSA, HS384], \"\", eyJhbGciOiJIUzI1NiJ9, PS384, Ed448, [null], [ES384, ES256, Ed25519, Ed448, HS384, HS256, EdDSA], ES512, ES384, EdDSA, HS384, \"/wH//wA\\u003d\", HS256]");
        } catch (java.lang.IllegalArgumentException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(jWSAlgorithm1);
    }

    @Test
    public void test095() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test095");
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm1 = com.nimbusds.jose.JWSAlgorithm.parse("Ed448");
        org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier3 = null; // flaky "29) test095(AccessTokenVerifierRandoopRegressionS00)": new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm1, "[ES384, ES256, Ed25519, Ed448, HS384, EdDSA, HS256]");
        com.nimbusds.jose.jca.JCAContext jCAContext4 = null; // flaky "23) test095(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.getJCAContext();
        java.util.Set<com.nimbusds.jose.JWSAlgorithm> jWSAlgorithmSet5 = null; // flaky "9) test095(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.supportedJWSAlgorithms();
        com.nimbusds.jose.jca.JCAContext jCAContext6 = null; // flaky "5) test095(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.getJCAContext();
        java.util.Set<com.nimbusds.jose.JWSAlgorithm> jWSAlgorithmSet7 = null; // flaky "4) test095(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.supportedJWSAlgorithms();
        com.nimbusds.jose.jca.JCAContext jCAContext8 = null; // flaky "3) test095(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.getJCAContext();
        java.util.Set<com.nimbusds.jose.JWSAlgorithm> jWSAlgorithmSet9 = null; // flaky "3) test095(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.supportedJWSAlgorithms();
        java.util.Set<com.nimbusds.jose.JWSAlgorithm> jWSAlgorithmSet10 = null; // flaky "2) test095(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.supportedJWSAlgorithms();
        com.nimbusds.jose.jca.JCAContext jCAContext11 = null; // flaky "1) test095(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.getJCAContext();
        org.junit.Assert.assertNotNull(jWSAlgorithm1);
// flaky "1) test095(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jCAContext4);
// flaky "1) test095(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jWSAlgorithmSet5);
// flaky "1) test095(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jCAContext6);
// flaky "1) test095(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jWSAlgorithmSet7);
// flaky "1) test095(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jCAContext8);
// flaky "1) test095(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jWSAlgorithmSet9);
// flaky "1) test095(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jWSAlgorithmSet10);
// flaky "1) test095(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jCAContext11);
    }

    @Test
    public void test096() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test096");
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm1 = com.nimbusds.jose.JWSAlgorithm.parse("Ed448");
        org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier3 = null; // flaky "30) test096(AccessTokenVerifierRandoopRegressionS00)": new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm1, "[ES384, ES256, Ed25519, Ed448, HS384, EdDSA, HS256]");
        java.util.Set<com.nimbusds.jose.JWSAlgorithm> jWSAlgorithmSet4 = null; // flaky "24) test096(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.supportedJWSAlgorithms();
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm5 = com.nimbusds.jose.JWSAlgorithm.HS256;
        com.nimbusds.jose.JWSHeader jWSHeader6 = new com.nimbusds.jose.JWSHeader(jWSAlgorithm5);
        boolean boolean7 = jWSHeader6.isBase64URLEncodePayload();
        java.net.URI uRI8 = jWSHeader6.getJWKURL();
        java.lang.String str9 = jWSHeader6.toString();
        com.nimbusds.jose.JWSHeader jWSHeader10 = new com.nimbusds.jose.JWSHeader(jWSHeader6);
        boolean boolean11 = jWSHeader6.isBase64URLEncodePayload();
        com.nimbusds.jose.util.Base64URL base64URL13 = new com.nimbusds.jose.util.Base64URL("/wH//wA=");
        java.math.BigInteger bigInteger14 = base64URL13.decodeToBigInteger();
        com.nimbusds.jose.util.Base64URL base64URL15 = com.nimbusds.jose.util.Base64URL.encode(bigInteger14);
        byte[] byteArray16 = base64URL15.decode();
        com.nimbusds.jose.util.Base64URL base64URL18 = com.nimbusds.jose.util.Base64URL.encode("\"MDEyMzQ1Njc4OWFiY2RlZjAxMjM0NTY3ODlhYmNkZWY\"");
        boolean boolean19 = false; // flaky "10) test096(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.verify(jWSHeader6, byteArray16, base64URL18);
        org.junit.Assert.assertNotNull(jWSAlgorithm1);
// flaky "6) test096(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jWSAlgorithmSet4);
        org.junit.Assert.assertNotNull(jWSAlgorithm5);
        org.junit.Assert.assertTrue("'" + boolean7 + "' != '" + true + "'", boolean7 == true);
        org.junit.Assert.assertNull(uRI8);
        org.junit.Assert.assertEquals("'" + str9 + "' != '" + "{\"alg\":\"HS256\"}" + "'", str9, "{\"alg\":\"HS256\"}");
        org.junit.Assert.assertTrue("'" + boolean11 + "' != '" + true + "'", boolean11 == true);
        org.junit.Assert.assertNotNull(bigInteger14);
        org.junit.Assert.assertNotNull(base64URL15);
        org.junit.Assert.assertNotNull(byteArray16);
        org.junit.Assert.assertArrayEquals(byteArray16, new byte[] { (byte) -1, (byte) 1, (byte) -1, (byte) -1, (byte) 0 });
        org.junit.Assert.assertNotNull(base64URL18);
        org.junit.Assert.assertTrue("'" + boolean19 + "' != '" + false + "'", boolean19 == false);
    }

    @Test
    public void test097() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test097");
        com.nimbusds.jose.JWSAlgorithm[] jWSAlgorithmArray0 = new com.nimbusds.jose.JWSAlgorithm[] {};
        com.nimbusds.jose.JWSAlgorithm.Family family1 = new com.nimbusds.jose.JWSAlgorithm.Family(jWSAlgorithmArray0);
        com.nimbusds.jose.JWSAlgorithm.Family family2 = com.nimbusds.jose.JWSAlgorithm.Family.ED;
        boolean boolean3 = family1.containsAll((java.util.Collection<com.nimbusds.jose.JWSAlgorithm>) family2);
        java.lang.Object[] objArray4 = family2.toArray();
        java.util.Spliterator<com.nimbusds.jose.JWSAlgorithm> jWSAlgorithmSpliterator5 = family2.spliterator();
        java.lang.Object[] objArray6 = family2.toArray();
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm7 = family2.getLast();
        // The following exception was thrown during execution in test generation
        try {
            org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier9 = null; // flaky "31) test097(AccessTokenVerifierRandoopRegressionS00)": new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm7, "\"/woKAQA\\u003d\"");
// flaky "25) test097(AccessTokenVerifierRandoopRegressionS00)":             org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException; message: Unsupported JWS algorithm: \"/wH//wA\\u003d\"");
        } catch (java.lang.IllegalArgumentException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(jWSAlgorithmArray0);
        org.junit.Assert.assertArrayEquals(jWSAlgorithmArray0, new com.nimbusds.jose.JWSAlgorithm[] {});
        org.junit.Assert.assertNotNull(family2);
        org.junit.Assert.assertTrue("'" + boolean3 + "' != '" + false + "'", boolean3 == false);
        org.junit.Assert.assertNotNull(objArray4);
// flaky "11) test097(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertEquals(java.util.Arrays.deepToString(objArray4), "\ufffd\n\n\001\000, eyJhbGciOiJIUzI1NiJ9, 77+9CgoBAA==, [null, ES256, Ed25519, ES384, Ed448, \"/wH//wA\\u003d\", HS256, EdDSA, HS384], Ed448, PS384, [null], [ES384, ES256, Ed25519, Ed448, HS384, HS256, EdDSA], \"\", ES512, ES384, null, HS384, EdDSA, \"fedcba9876543210fedcba9876543210\", HS256, \"/wH//wA\\u003d\"]");
// flaky "7) test097(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertEquals(java.util.Arrays.toString(objArray4), "\ufffd\n\n\001\000, eyJhbGciOiJIUzI1NiJ9, 77+9CgoBAA==, [null, ES256, Ed25519, ES384, Ed448, \"/wH//wA\\u003d\", HS256, EdDSA, HS384], Ed448, PS384, [null], [ES384, ES256, Ed25519, Ed448, HS384, HS256, EdDSA], \"\", ES512, ES384, null, HS384, EdDSA, \"fedcba9876543210fedcba9876543210\", HS256, \"/wH//wA\\u003d\"]");
        org.junit.Assert.assertNotNull(jWSAlgorithmSpliterator5);
        org.junit.Assert.assertNotNull(objArray6);
// flaky "5) test097(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertEquals(java.util.Arrays.deepToString(objArray6), "\ufffd\n\n\001\000, eyJhbGciOiJIUzI1NiJ9, 77+9CgoBAA==, [null, ES256, Ed25519, ES384, Ed448, \"/wH//wA\\u003d\", HS256, EdDSA, HS384], Ed448, PS384, [null], [ES384, ES256, Ed25519, Ed448, HS384, HS256, EdDSA], \"\", ES512, ES384, null, HS384, EdDSA, \"fedcba9876543210fedcba9876543210\", HS256, \"/wH//wA\\u003d\"]");
// flaky "4) test097(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertEquals(java.util.Arrays.toString(objArray6), "\ufffd\n\n\001\000, eyJhbGciOiJIUzI1NiJ9, 77+9CgoBAA==, [null, ES256, Ed25519, ES384, Ed448, \"/wH//wA\\u003d\", HS256, EdDSA, HS384], Ed448, PS384, [null], [ES384, ES256, Ed25519, Ed448, HS384, HS256, EdDSA], \"\", ES512, ES384, null, HS384, EdDSA, \"fedcba9876543210fedcba9876543210\", HS256, \"/wH//wA\\u003d\"]");
        org.junit.Assert.assertNotNull(jWSAlgorithm7);
    }

    @Test
    public void test098() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test098");
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm0 = com.nimbusds.jose.JWSAlgorithm.PS256;
        com.nimbusds.jose.Requirement requirement1 = jWSAlgorithm0.getRequirement();
        // The following exception was thrown during execution in test generation
        try {
            org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier3 = new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm0, "\ufffd\001\ufffd\ufffd\000\ufffd\ufffd");
            org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException; message: A key pair is required, in the 'private:public' format");
        } catch (java.lang.IllegalArgumentException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(jWSAlgorithm0);
        org.junit.Assert.assertTrue("'" + requirement1 + "' != '" + com.nimbusds.jose.Requirement.OPTIONAL + "'", requirement1.equals(com.nimbusds.jose.Requirement.OPTIONAL));
    }

    @Test
    public void test099() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test099");
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm0 = com.nimbusds.jose.JWSAlgorithm.HS256;
        com.nimbusds.jose.JWSHeader jWSHeader1 = new com.nimbusds.jose.JWSHeader(jWSAlgorithm0);
        com.nimbusds.jose.UnprotectedHeader unprotectedHeader2 = null;
        com.nimbusds.jose.Header header3 = jWSHeader1.join(unprotectedHeader2);
        java.lang.String str4 = jWSHeader1.getContentType();
        java.util.Map<java.lang.String, java.lang.Object> strMap5 = jWSHeader1.getCustomParams();
        java.lang.Object obj7 = jWSHeader1.getCustomParam("\"ZmVkY2JhOTg3NjU0MzIxMGZlZGNiYTk4NzY1NDMyMTA\\u003d\"");
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm8 = jWSHeader1.getAlgorithm();
        // The following exception was thrown during execution in test generation
        try {
            org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier10 = null; // flaky "32) test099(AccessTokenVerifierRandoopRegressionS00)": new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm8, "\u0727\ufffd\ufffd\ufffd\001\035\ufffd");
// flaky "26) test099(AccessTokenVerifierRandoopRegressionS00)":             org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException; message: A key pair is required, in the 'private:public' format");
        } catch (java.lang.IllegalArgumentException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(jWSAlgorithm0);
        org.junit.Assert.assertNotNull(header3);
        org.junit.Assert.assertNull(str4);
        org.junit.Assert.assertNotNull(strMap5);
        org.junit.Assert.assertNull(obj7);
        org.junit.Assert.assertNotNull(jWSAlgorithm8);
    }

    @Test
    public void test100() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test100");
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm1 = com.nimbusds.jose.JWSAlgorithm.parse("Ed448");
        org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier3 = null; // flaky "33) test100(AccessTokenVerifierRandoopRegressionS00)": new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm1, "[ES384, ES256, Ed25519, Ed448, HS384, EdDSA, HS256]");
        java.util.Set<com.nimbusds.jose.JWSAlgorithm> jWSAlgorithmSet4 = null; // flaky "27) test100(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.supportedJWSAlgorithms();
        java.util.Set<com.nimbusds.jose.JWSAlgorithm> jWSAlgorithmSet5 = null; // flaky "12) test100(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.supportedJWSAlgorithms();
        java.util.Set<com.nimbusds.jose.JWSAlgorithm> jWSAlgorithmSet6 = null; // flaky "8) test100(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.supportedJWSAlgorithms();
        org.junit.Assert.assertNotNull(jWSAlgorithm1);
// flaky "6) test100(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jWSAlgorithmSet4);
// flaky "5) test100(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jWSAlgorithmSet5);
// flaky "4) test100(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jWSAlgorithmSet6);
    }

    @Test
    public void test101() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test101");
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm1 = com.nimbusds.jose.JWSAlgorithm.parse("ES256");
        com.nimbusds.jose.JWSHeader jWSHeader2 = new com.nimbusds.jose.JWSHeader(jWSAlgorithm1);
        com.nimbusds.jose.Requirement requirement3 = jWSAlgorithm1.getRequirement();
        // The following exception was thrown during execution in test generation
        try {
            org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier5 = new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm1, "\"/woKAQA\\u003d\"");
            org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException; message: Unsupported JWS algorithm: ES256");
        } catch (java.lang.IllegalArgumentException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(jWSAlgorithm1);
        org.junit.Assert.assertTrue("'" + requirement3 + "' != '" + com.nimbusds.jose.Requirement.RECOMMENDED + "'", requirement3.equals(com.nimbusds.jose.Requirement.RECOMMENDED));
    }

    @Test
    public void test102() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test102");
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm0 = com.nimbusds.jose.JWSAlgorithm.Ed448;
        boolean boolean2 = jWSAlgorithm0.equals((java.lang.Object) "\ufffd\n\n\001\000");
        com.nimbusds.jose.JWSHeader.Builder builder3 = new com.nimbusds.jose.JWSHeader.Builder(jWSAlgorithm0);
        com.nimbusds.jose.JWSHeader.Builder builder5 = builder3.base64URLEncodePayload(true);
        java.util.LinkedHashSet<java.lang.String> strSet7 = java.util.LinkedHashSet.newLinkedHashSet((int) '#');
        com.nimbusds.jose.JWSHeader.Builder builder8 = builder3.criticalParams((java.util.Set<java.lang.String>) strSet7);
        com.nimbusds.jose.JWSHeader jWSHeader9 = builder3.build();
        com.nimbusds.jose.JWSHeader.Builder builder10 = new com.nimbusds.jose.JWSHeader.Builder(jWSHeader9);
        com.nimbusds.jose.jwk.JWK jWK11 = jWSHeader9.getJWK();
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm12 = jWSHeader9.getAlgorithm();
        java.lang.String str13 = jWSAlgorithm12.toString();
        com.nimbusds.jose.Requirement requirement14 = jWSAlgorithm12.getRequirement();
        // The following exception was thrown during execution in test generation
{ // flaky ('try' without 'catch', 'finally' or resource declarations):         try {
            org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier16 = null; // flaky "34) test102(AccessTokenVerifierRandoopRegressionS00)": new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm12, "\"PS256\"");
// flaky "28) test102(AccessTokenVerifierRandoopRegressionS00)":             org.junit.Assert.fail("Expected exception of type com.nimbusds.jose.KeyLengthException; message: The secret length must be at least 256 bits");
// flaky (is never thrown in body of corresponding try statement):         } catch (com.nimbusds.jose.KeyLengthException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(jWSAlgorithm0);
        org.junit.Assert.assertTrue("'" + boolean2 + "' != '" + false + "'", boolean2 == false);
        org.junit.Assert.assertNotNull(builder5);
        org.junit.Assert.assertNotNull(strSet7);
        org.junit.Assert.assertNotNull(builder8);
        org.junit.Assert.assertNotNull(jWSHeader9);
        org.junit.Assert.assertNull(jWK11);
        org.junit.Assert.assertNotNull(jWSAlgorithm12);
        org.junit.Assert.assertEquals("'" + str13 + "' != '" + "Ed448" + "'", str13, "Ed448");
        org.junit.Assert.assertTrue("'" + requirement14 + "' != '" + com.nimbusds.jose.Requirement.OPTIONAL + "'", requirement14.equals(com.nimbusds.jose.Requirement.OPTIONAL));
    }

    @Test
    public void test103() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test103");
        com.nimbusds.jose.JWSAlgorithm.Family family0 = com.nimbusds.jose.JWSAlgorithm.Family.EC;
        int int1 = family0.size();
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm2 = family0.getFirst();
        // The following exception was thrown during execution in test generation
        try {
            org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier4 = new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm2, "[HS256, ES256, ES384, Ed25519, Ed448, [null], HS384, \"\", ES512, EdDSA, null, \"/wH//wA\\u003d\"]");
            org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException; message: Unsupported JWS algorithm: \"/wH//wA\\u003d\"");
        } catch (java.lang.IllegalArgumentException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(family0);
// flaky "35) test103(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertTrue("'" + int1 + "' != '" + 9 + "'", int1 == 9);
        org.junit.Assert.assertNotNull(jWSAlgorithm2);
    }

    @Test
    public void test104() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test104");
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm0 = null;
        // The following exception was thrown during execution in test generation
{ // flaky ('try' without 'catch', 'finally' or resource declarations):         try {
            org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier2 = null; // flaky "36) test104(AccessTokenVerifierRandoopRegressionS00)": new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm0, "\ufffd\001\ufffd\ufffd\000\ufffd\ufffd");
// flaky "29) test104(AccessTokenVerifierRandoopRegressionS00)":             org.junit.Assert.fail("Expected exception of type com.nimbusds.jose.KeyLengthException; message: The secret length must be at least 256 bits");
// flaky (is never thrown in body of corresponding try statement):         } catch (com.nimbusds.jose.KeyLengthException e) {
            // Expected exception.
        }
    }

    @Test
    public void test105() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test105");
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm0 = com.nimbusds.jose.JWSAlgorithm.HS384;
        java.lang.String str1 = jWSAlgorithm0.getName();
        // The following exception was thrown during execution in test generation
        try {
            org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier3 = new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm0, "\"RWQ0NDg\\u003d\"");
            org.junit.Assert.fail("Expected exception of type com.nimbusds.jose.KeyLengthException; message: The secret length must be at least 256 bits");
        } catch (com.nimbusds.jose.KeyLengthException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(jWSAlgorithm0);
        org.junit.Assert.assertEquals("'" + str1 + "' != '" + "HS384" + "'", str1, "HS384");
    }

    @Test
    public void test106() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test106");
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm1 = new com.nimbusds.jose.JWSAlgorithm("\"Ed25519\"");
        java.lang.String str2 = jWSAlgorithm1.toJSONString();
        com.nimbusds.jose.JWSHeader.Builder builder3 = new com.nimbusds.jose.JWSHeader.Builder(jWSAlgorithm1);
        // The following exception was thrown during execution in test generation
        try {
            org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier5 = new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm1, "\"\\\"\\\\\\\"ES256K\\\\\\\"\\\"\"");
            org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException; message: Unsupported JWS algorithm: \"Ed25519\"");
        } catch (java.lang.IllegalArgumentException e) {
            // Expected exception.
        }
        org.junit.Assert.assertEquals("'" + str2 + "' != '" + "\"\\\"Ed25519\\\"\"" + "'", str2, "\"\\\"Ed25519\\\"\"");
    }

    @Test
    public void test107() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test107");
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm0 = com.nimbusds.jose.JWSAlgorithm.HS256;
        com.nimbusds.jose.JWSHeader jWSHeader1 = new com.nimbusds.jose.JWSHeader(jWSAlgorithm0);
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm2 = jWSHeader1.getAlgorithm();
        com.nimbusds.jose.Requirement requirement3 = jWSAlgorithm2.getRequirement();
        // The following exception was thrown during execution in test generation
        try {
            org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier5 = null; // flaky "37) test107(AccessTokenVerifierRandoopRegressionS00)": new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm2, "\"ES512\"");
// flaky "30) test107(AccessTokenVerifierRandoopRegressionS00)":             org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException; message: A key pair is required, in the 'private:public' format");
        } catch (java.lang.IllegalArgumentException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(jWSAlgorithm0);
        org.junit.Assert.assertNotNull(jWSAlgorithm2);
        org.junit.Assert.assertTrue("'" + requirement3 + "' != '" + com.nimbusds.jose.Requirement.REQUIRED + "'", requirement3.equals(com.nimbusds.jose.Requirement.REQUIRED));
    }

    @Test
    public void test108() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test108");
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm0 = com.nimbusds.jose.JWSAlgorithm.ES256;
        com.nimbusds.jose.JWSHeader jWSHeader1 = new com.nimbusds.jose.JWSHeader(jWSAlgorithm0);
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm3 = com.nimbusds.jose.JWSAlgorithm.parse("fedcba9876543210fedcba9876543210");
        java.lang.String str4 = jWSAlgorithm3.toString();
        com.nimbusds.jose.Requirement requirement5 = jWSAlgorithm3.getRequirement();
        java.lang.String str6 = jWSAlgorithm3.toJSONString();
        boolean boolean7 = jWSAlgorithm0.equals((java.lang.Object) jWSAlgorithm3);
        // The following exception was thrown during execution in test generation
        try {
            org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier9 = new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm0, "\"RVMyNTY\"");
            org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException; message: Unsupported JWS algorithm: ES256");
        } catch (java.lang.IllegalArgumentException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(jWSAlgorithm0);
        org.junit.Assert.assertNotNull(jWSAlgorithm3);
        org.junit.Assert.assertEquals("'" + str4 + "' != '" + "fedcba9876543210fedcba9876543210" + "'", str4, "fedcba9876543210fedcba9876543210");
        org.junit.Assert.assertNull(requirement5);
        org.junit.Assert.assertEquals("'" + str6 + "' != '" + "\"fedcba9876543210fedcba9876543210\"" + "'", str6, "\"fedcba9876543210fedcba9876543210\"");
        org.junit.Assert.assertTrue("'" + boolean7 + "' != '" + false + "'", boolean7 == false);
    }

    @Test
    public void test109() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test109");
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm2 = com.nimbusds.jose.JWSAlgorithm.HS256;
        com.nimbusds.jose.Requirement requirement3 = jWSAlgorithm2.getRequirement();
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm4 = new com.nimbusds.jose.JWSAlgorithm("\ufffd\n\n\001\000", requirement3);
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm5 = new com.nimbusds.jose.JWSAlgorithm("\"\"", requirement3);
        // The following exception was thrown during execution in test generation
        try {
            org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier7 = new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm5, "\"HS256\"");
            org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException; message: Unsupported JWS algorithm: \"\"");
        } catch (java.lang.IllegalArgumentException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(jWSAlgorithm2);
        org.junit.Assert.assertTrue("'" + requirement3 + "' != '" + com.nimbusds.jose.Requirement.REQUIRED + "'", requirement3.equals(com.nimbusds.jose.Requirement.REQUIRED));
    }

    @Test
    public void test110() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test110");
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm0 = com.nimbusds.jose.JWSAlgorithm.Ed448;
        boolean boolean2 = jWSAlgorithm0.equals((java.lang.Object) "\ufffd\n\n\001\000");
        com.nimbusds.jose.JWSHeader.Builder builder3 = new com.nimbusds.jose.JWSHeader.Builder(jWSAlgorithm0);
        com.nimbusds.jose.JWSHeader.Builder builder5 = builder3.base64URLEncodePayload(true);
        com.nimbusds.jose.JWSHeader.Builder builder7 = builder5.keyID("0123456789abcdef0123456789abcdef");
        com.nimbusds.jose.JWSHeader jWSHeader8 = builder7.build();
        com.nimbusds.jose.JWSHeader.Builder builder9 = new com.nimbusds.jose.JWSHeader.Builder(jWSHeader8);
        java.util.Map<java.lang.String, java.lang.Object> strMap10 = jWSHeader8.toJSONObject();
        com.nimbusds.jose.JWSHeader jWSHeader11 = com.nimbusds.jose.JWSHeader.parse(strMap10);
        com.nimbusds.jose.util.Base64URL base64URL13 = com.nimbusds.jose.util.Base64URL.from("0123456789abcdef0123456789abcdef");
        com.nimbusds.jose.JWSHeader jWSHeader14 = com.nimbusds.jose.JWSHeader.parse(strMap10, base64URL13);
        java.util.List list15 = jWSHeader14.getX509CertChain();
        com.nimbusds.jose.JWSHeader jWSHeader16 = new com.nimbusds.jose.JWSHeader(jWSHeader14);
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm17 = jWSHeader14.getAlgorithm();
        org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier19 = null; // flaky "38) test110(AccessTokenVerifierRandoopRegressionS00)": new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm17, "[ES256, Ed25519, \"\"\ufffd\n\n\001\000, eyJhbGciOiJIUzI1NiJ9, 77+9CgoBAA==, [null, ES256, Ed25519, ES384, Ed448, \"/wH//wA\\u003d\", HS256, EdDSA, HS384], Ed448, PS384, [null], [ES384, ES256, Ed25519, Ed448, HS384, HS256, EdDSA], HS384, ES512, \"fedcba9876543210fedcba9876543210\", ES384, null, PS256, \"/wH//wA\\u003d\", EdDSA, HS256]");
        org.junit.Assert.assertNotNull(jWSAlgorithm0);
        org.junit.Assert.assertTrue("'" + boolean2 + "' != '" + false + "'", boolean2 == false);
        org.junit.Assert.assertNotNull(builder5);
        org.junit.Assert.assertNotNull(builder7);
        org.junit.Assert.assertNotNull(jWSHeader8);
        org.junit.Assert.assertNotNull(strMap10);
        org.junit.Assert.assertNotNull(jWSHeader11);
        org.junit.Assert.assertNotNull(base64URL13);
        org.junit.Assert.assertNotNull(jWSHeader14);
        org.junit.Assert.assertNull(list15);
        org.junit.Assert.assertNotNull(jWSAlgorithm17);
    }

    @Test
    public void test111() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test111");
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm1 = com.nimbusds.jose.JWSAlgorithm.parse("eyJhbGciOiJIUzI1NiJ9");
        // The following exception was thrown during execution in test generation
        try {
            org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier3 = new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm1, "\ufffd");
            org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException; message: Unsupported JWS algorithm: eyJhbGciOiJIUzI1NiJ9");
        } catch (java.lang.IllegalArgumentException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(jWSAlgorithm1);
    }

    @Test
    public void test112() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test112");
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm1 = com.nimbusds.jose.JWSAlgorithm.parse("\"\"");
        com.nimbusds.jose.JWSHeader jWSHeader2 = new com.nimbusds.jose.JWSHeader(jWSAlgorithm1);
        // The following exception was thrown during execution in test generation
        try {
            org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier4 = new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm1, "eyJhbGciOiJIUzI1NiJ9");
            org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException; message: Unsupported JWS algorithm: \"\"");
        } catch (java.lang.IllegalArgumentException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(jWSAlgorithm1);
    }

    @Test
    public void test113() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test113");
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm0 = com.nimbusds.jose.JWSAlgorithm.PS384;
        com.nimbusds.jose.JWSAlgorithm[] jWSAlgorithmArray1 = new com.nimbusds.jose.JWSAlgorithm[] { jWSAlgorithm0 };
        com.nimbusds.jose.JWSAlgorithm.Family family2 = new com.nimbusds.jose.JWSAlgorithm.Family(jWSAlgorithmArray1);
        com.nimbusds.jose.JWSAlgorithm.Family family3 = new com.nimbusds.jose.JWSAlgorithm.Family(jWSAlgorithmArray1);
        com.nimbusds.jose.JWSAlgorithm.Family family4 = new com.nimbusds.jose.JWSAlgorithm.Family(jWSAlgorithmArray1);
        com.nimbusds.jose.JWSAlgorithm.Family family5 = new com.nimbusds.jose.JWSAlgorithm.Family(jWSAlgorithmArray1);
        com.nimbusds.jose.JWSAlgorithm.Family family6 = new com.nimbusds.jose.JWSAlgorithm.Family(jWSAlgorithmArray1);
        com.nimbusds.jose.JWSAlgorithm.Family family7 = new com.nimbusds.jose.JWSAlgorithm.Family(jWSAlgorithmArray1);
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm8 = family7.getFirst();
        // The following exception was thrown during execution in test generation
        try {
            org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier10 = new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm8, "[RS256, RS384, RS512, PS256, PS384, PS512]");
            org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException; message: A key pair is required, in the 'private:public' format");
        } catch (java.lang.IllegalArgumentException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(jWSAlgorithm0);
        org.junit.Assert.assertNotNull(jWSAlgorithmArray1);
        org.junit.Assert.assertNotNull(jWSAlgorithm8);
    }

    @Test
    public void test114() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test114");
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm1 = com.nimbusds.jose.JWSAlgorithm.PS384;
        com.nimbusds.jose.JWSAlgorithm[] jWSAlgorithmArray2 = new com.nimbusds.jose.JWSAlgorithm[] { jWSAlgorithm1 };
        com.nimbusds.jose.JWSAlgorithm.Family family3 = new com.nimbusds.jose.JWSAlgorithm.Family(jWSAlgorithmArray2);
        java.util.Spliterator<com.nimbusds.jose.JWSAlgorithm> jWSAlgorithmSpliterator4 = family3.spliterator();
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm6 = new com.nimbusds.jose.JWSAlgorithm("");
        java.lang.String str7 = jWSAlgorithm6.toString();
        family3.addFirst(jWSAlgorithm6);
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm9 = family3.getLast();
        com.nimbusds.jose.Requirement requirement10 = jWSAlgorithm9.getRequirement();
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm11 = new com.nimbusds.jose.JWSAlgorithm("\"\\\"_woKAQA\\\"\"", requirement10);
        java.util.LinkedHashSet<java.util.Set<com.nimbusds.jose.JWSAlgorithm>> jWSAlgorithmSetSet13 = java.util.LinkedHashSet.newLinkedHashSet((int) '4');
        boolean boolean14 = jWSAlgorithm11.equals((java.lang.Object) jWSAlgorithmSetSet13);
        // The following exception was thrown during execution in test generation
        try {
            org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier16 = new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm11, "[ES256, Ed25519, PS256, [null, ES256, Ed25519, ES384, Ed448, \"/wH//wA\\u003d\", HS256, EdDSA, HS384], \"\", eyJhbGciOiJIUzI1NiJ9, PS384, Ed448, [null], [ES384, ES256, Ed25519, Ed448, HS384, HS256, EdDSA], \"/wH//wA\\u003d\", HS384, ES512, HS256, ES384, EdDSA, null]");
            org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException; message: Unsupported JWS algorithm: \"\\\"_woKAQA\\\"\"");
        } catch (java.lang.IllegalArgumentException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(jWSAlgorithm1);
        org.junit.Assert.assertNotNull(jWSAlgorithmArray2);
        org.junit.Assert.assertNotNull(jWSAlgorithmSpliterator4);
        org.junit.Assert.assertEquals("'" + str7 + "' != '" + "" + "'", str7, "");
        org.junit.Assert.assertNotNull(jWSAlgorithm9);
        org.junit.Assert.assertTrue("'" + requirement10 + "' != '" + com.nimbusds.jose.Requirement.OPTIONAL + "'", requirement10.equals(com.nimbusds.jose.Requirement.OPTIONAL));
        org.junit.Assert.assertNotNull(jWSAlgorithmSetSet13);
        org.junit.Assert.assertTrue("'" + boolean14 + "' != '" + false + "'", boolean14 == false);
    }

    @Test
    public void test115() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test115");
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm0 = com.nimbusds.jose.JWSAlgorithm.HS256;
        com.nimbusds.jose.JWSHeader jWSHeader1 = new com.nimbusds.jose.JWSHeader(jWSAlgorithm0);
        boolean boolean2 = jWSHeader1.isBase64URLEncodePayload();
        java.net.URI uRI3 = jWSHeader1.getX509CertURL();
        com.nimbusds.jose.JWSHeader jWSHeader4 = new com.nimbusds.jose.JWSHeader(jWSHeader1);
        java.lang.Object obj6 = jWSHeader4.getCustomParam("/wH//wA=");
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm7 = jWSHeader4.getAlgorithm();
        // The following exception was thrown during execution in test generation
        try {
            org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier9 = null; // flaky "39) test115(AccessTokenVerifierRandoopRegressionS00)": new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm7, "{\"alg\":\"\\\"\\\\\\\"\\\\\\\"\\\"\"}");
// flaky "31) test115(AccessTokenVerifierRandoopRegressionS00)":             org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException; message: Illegal base64 character 22");
        } catch (java.lang.IllegalArgumentException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(jWSAlgorithm0);
        org.junit.Assert.assertTrue("'" + boolean2 + "' != '" + true + "'", boolean2 == true);
        org.junit.Assert.assertNull(uRI3);
        org.junit.Assert.assertNull(obj6);
        org.junit.Assert.assertNotNull(jWSAlgorithm7);
    }

    @Test
    public void test116() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test116");
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm0 = com.nimbusds.jose.JWSAlgorithm.PS384;
        com.nimbusds.jose.JWSAlgorithm[] jWSAlgorithmArray1 = new com.nimbusds.jose.JWSAlgorithm[] { jWSAlgorithm0 };
        com.nimbusds.jose.JWSAlgorithm.Family family2 = new com.nimbusds.jose.JWSAlgorithm.Family(jWSAlgorithmArray1);
        java.util.Spliterator<com.nimbusds.jose.JWSAlgorithm> jWSAlgorithmSpliterator3 = family2.spliterator();
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm5 = new com.nimbusds.jose.JWSAlgorithm("");
        java.lang.String str6 = jWSAlgorithm5.toString();
        family2.addFirst(jWSAlgorithm5);
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm8 = family2.getLast();
        com.nimbusds.jose.Requirement requirement9 = jWSAlgorithm8.getRequirement();
        java.lang.String str10 = jWSAlgorithm8.toJSONString();
        // The following exception was thrown during execution in test generation
        try {
            org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier12 = new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm8, "{\"alg\":\"ES256\"}");
            org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException; message: Illegal base64 character 22");
        } catch (java.lang.IllegalArgumentException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(jWSAlgorithm0);
        org.junit.Assert.assertNotNull(jWSAlgorithmArray1);
        org.junit.Assert.assertNotNull(jWSAlgorithmSpliterator3);
        org.junit.Assert.assertEquals("'" + str6 + "' != '" + "" + "'", str6, "");
        org.junit.Assert.assertNotNull(jWSAlgorithm8);
        org.junit.Assert.assertTrue("'" + requirement9 + "' != '" + com.nimbusds.jose.Requirement.OPTIONAL + "'", requirement9.equals(com.nimbusds.jose.Requirement.OPTIONAL));
        org.junit.Assert.assertEquals("'" + str10 + "' != '" + "\"PS384\"" + "'", str10, "\"PS384\"");
    }

    @Test
    public void test117() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test117");
        com.nimbusds.jose.JWSAlgorithm[] jWSAlgorithmArray0 = new com.nimbusds.jose.JWSAlgorithm[] {};
        com.nimbusds.jose.JWSAlgorithm.Family family1 = new com.nimbusds.jose.JWSAlgorithm.Family(jWSAlgorithmArray0);
        com.nimbusds.jose.JWSAlgorithm.Family family2 = com.nimbusds.jose.JWSAlgorithm.Family.ED;
        boolean boolean3 = family1.containsAll((java.util.Collection<com.nimbusds.jose.JWSAlgorithm>) family2);
        java.lang.Object[] objArray4 = family2.toArray();
        java.util.Spliterator<com.nimbusds.jose.JWSAlgorithm> jWSAlgorithmSpliterator5 = family2.spliterator();
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm6 = family2.getLast();
        // The following exception was thrown during execution in test generation
        try {
            org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier8 = new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm6, "kid0123456789abcdef0123456789abcdefalgEd448=");
// flaky "40) test117(AccessTokenVerifierRandoopRegressionS00)":             org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException; message: A key pair is required, in the 'private:public' format");
        } catch (java.lang.IllegalArgumentException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(jWSAlgorithmArray0);
        org.junit.Assert.assertArrayEquals(jWSAlgorithmArray0, new com.nimbusds.jose.JWSAlgorithm[] {});
        org.junit.Assert.assertNotNull(family2);
        org.junit.Assert.assertTrue("'" + boolean3 + "' != '" + false + "'", boolean3 == false);
        org.junit.Assert.assertNotNull(objArray4);
// flaky "32) test117(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertEquals(java.util.Arrays.deepToString(objArray4), "[Ed25519, null, \"0123456789abcdef0123456789abcdef\", [null, ES256, Ed25519, ES384, Ed448, \"/wH//wA\\u003d\"\ufffd\001\ufffd\ufffd\000\ufffd\ufffd, ES256, \"\"\ufffd\n\n\001\000, eyJhbGciOiJIUzI1NiJ9, 77+9CgoBAA==, Ed448, PS384, [null], [ES384, ES256, Ed25519, Ed448, HS384, HS256, EdDSA], PS256, \"/wH//wA\\u003d\", ES384, ES512, HS384, EdDSA, \"fedcba9876543210fedcba9876543210\", HS256]");
// flaky "13) test117(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertEquals(java.util.Arrays.toString(objArray4), "[Ed25519, null, \"0123456789abcdef0123456789abcdef\", [null, ES256, Ed25519, ES384, Ed448, \"/wH//wA\\u003d\"\ufffd\001\ufffd\ufffd\000\ufffd\ufffd, ES256, \"\"\ufffd\n\n\001\000, eyJhbGciOiJIUzI1NiJ9, 77+9CgoBAA==, Ed448, PS384, [null], [ES384, ES256, Ed25519, Ed448, HS384, HS256, EdDSA], PS256, \"/wH//wA\\u003d\", ES384, ES512, HS384, EdDSA, \"fedcba9876543210fedcba9876543210\", HS256]");
        org.junit.Assert.assertNotNull(jWSAlgorithmSpliterator5);
        org.junit.Assert.assertNotNull(jWSAlgorithm6);
    }

    @Test
    public void test118() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test118");
        com.nimbusds.jose.JWSAlgorithm.Family family0 = com.nimbusds.jose.JWSAlgorithm.Family.SIGNATURE;
        boolean boolean1 = family0.isEmpty();
        java.util.Spliterator<com.nimbusds.jose.JWSAlgorithm> jWSAlgorithmSpliterator2 = family0.spliterator();
        java.lang.String str3 = family0.toString();
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm4 = family0.getFirst();
        // The following exception was thrown during execution in test generation
        try {
            org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier6 = new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm4, "{\"alg\":\"HS256\"}");
            org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException; message: Illegal base64 character 22");
        } catch (java.lang.IllegalArgumentException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(family0);
        org.junit.Assert.assertTrue("'" + boolean1 + "' != '" + false + "'", boolean1 == false);
        org.junit.Assert.assertNotNull(jWSAlgorithmSpliterator2);
// flaky "41) test118(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertEquals("'" + str3 + "' != '" + "[RS256, RS384, RS512, PS256, PS384, PS512, ES256K, ES384, ES512, EdDSA, Ed25519, ES256, fedcba9876543210fedcba9876543210, Ed448, HS256, \"\\\"\\\"\"]" + "'", str3, "[RS256, RS384, RS512, PS256, PS384, PS512, ES256K, ES384, ES512, EdDSA, Ed25519, ES256, fedcba9876543210fedcba9876543210, Ed448, HS256, \"\\\"\\\"\"]");
        org.junit.Assert.assertNotNull(jWSAlgorithm4);
    }

    @Test
    public void test119() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test119");
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm1 = com.nimbusds.jose.JWSAlgorithm.parse("Ed448");
        org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier3 = null; // flaky "42) test119(AccessTokenVerifierRandoopRegressionS00)": new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm1, "[ES384, ES256, Ed25519, Ed448, HS384, EdDSA, HS256]");
        com.nimbusds.jose.jca.JCAContext jCAContext4 = null; // flaky "33) test119(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.getJCAContext();
        com.nimbusds.jose.jca.JCAContext jCAContext5 = null; // flaky "14) test119(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.getJCAContext();
        com.nimbusds.jose.jca.JCAContext jCAContext6 = null; // flaky "9) test119(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.getJCAContext();
        com.nimbusds.jose.jca.JCAContext jCAContext7 = null; // flaky "7) test119(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.getJCAContext();
        java.util.Set<com.nimbusds.jose.JWSAlgorithm> jWSAlgorithmSet8 = null; // flaky "6) test119(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.supportedJWSAlgorithms();
        com.nimbusds.jose.jca.JCAContext jCAContext9 = null; // flaky "5) test119(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.getJCAContext();
        com.nimbusds.jose.jca.JCAContext jCAContext10 = null; // flaky "3) test119(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.getJCAContext();
        org.junit.Assert.assertNotNull(jWSAlgorithm1);
// flaky "2) test119(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jCAContext4);
// flaky "2) test119(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jCAContext5);
// flaky "2) test119(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jCAContext6);
// flaky "2) test119(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jCAContext7);
// flaky "2) test119(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jWSAlgorithmSet8);
// flaky "2) test119(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jCAContext9);
// flaky "2) test119(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jCAContext10);
    }

    @Test
    public void test120() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test120");
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm1 = com.nimbusds.jose.JWSAlgorithm.parse("Ed448");
        org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier3 = null; // flaky "43) test120(AccessTokenVerifierRandoopRegressionS00)": new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm1, "[ES384, ES256, Ed25519, Ed448, HS384, EdDSA, HS256]");
        com.nimbusds.jose.jca.JCAContext jCAContext4 = null; // flaky "34) test120(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.getJCAContext();
        com.nimbusds.jose.jca.JCAContext jCAContext5 = null; // flaky "15) test120(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.getJCAContext();
        com.nimbusds.jose.jca.JCAContext jCAContext6 = null; // flaky "10) test120(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.getJCAContext();
        com.nimbusds.jose.jca.JCAContext jCAContext7 = null; // flaky "8) test120(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.getJCAContext();
        java.util.Set<com.nimbusds.jose.JWSAlgorithm> jWSAlgorithmSet8 = null; // flaky "7) test120(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.supportedJWSAlgorithms();
        java.util.Set<com.nimbusds.jose.JWSAlgorithm> jWSAlgorithmSet9 = null; // flaky "6) test120(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.supportedJWSAlgorithms();
        com.nimbusds.jose.jca.JCAContext jCAContext10 = null; // flaky "4) test120(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.getJCAContext();
        com.nimbusds.jose.jca.JCAContext jCAContext11 = null; // flaky "3) test120(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.getJCAContext();
        org.junit.Assert.assertNotNull(jWSAlgorithm1);
// flaky "3) test120(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jCAContext4);
// flaky "3) test120(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jCAContext5);
// flaky "3) test120(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jCAContext6);
// flaky "3) test120(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jCAContext7);
// flaky "3) test120(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jWSAlgorithmSet8);
// flaky "3) test120(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jWSAlgorithmSet9);
// flaky "2) test120(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jCAContext10);
// flaky "2) test120(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jCAContext11);
    }

    @Test
    public void test121() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test121");
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm1 = com.nimbusds.jose.JWSAlgorithm.parse("Ed448");
        org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier3 = null; // flaky "44) test121(AccessTokenVerifierRandoopRegressionS00)": new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm1, "[ES384, ES256, Ed25519, Ed448, HS384, EdDSA, HS256]");
        com.nimbusds.jose.jca.JCAContext jCAContext4 = null; // flaky "35) test121(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.getJCAContext();
        java.util.Set<com.nimbusds.jose.JWSAlgorithm> jWSAlgorithmSet5 = null; // flaky "16) test121(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.supportedJWSAlgorithms();
        java.util.stream.Stream<com.nimbusds.jose.JWSAlgorithm> jWSAlgorithmStream6 = null; // flaky "11) test121(AccessTokenVerifierRandoopRegressionS00)": jWSAlgorithmSet5.parallelStream();
        org.junit.Assert.assertNotNull(jWSAlgorithm1);
// flaky "9) test121(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jCAContext4);
// flaky "8) test121(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jWSAlgorithmSet5);
// flaky "7) test121(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jWSAlgorithmStream6);
    }

    @Test
    public void test122() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test122");
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm1 = com.nimbusds.jose.JWSAlgorithm.parse("Ed448");
        org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier3 = null; // flaky "45) test122(AccessTokenVerifierRandoopRegressionS00)": new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm1, "[ES384, ES256, Ed25519, Ed448, HS384, EdDSA, HS256]");
        com.nimbusds.jose.jca.JCAContext jCAContext4 = null; // flaky "36) test122(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.getJCAContext();
        com.nimbusds.jose.jca.JCAContext jCAContext5 = null; // flaky "17) test122(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.getJCAContext();
        com.nimbusds.jose.jca.JCAContext jCAContext6 = null; // flaky "12) test122(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.getJCAContext();
        com.nimbusds.jose.jca.JCAContext jCAContext7 = null; // flaky "10) test122(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.getJCAContext();
        java.util.Set<com.nimbusds.jose.JWSAlgorithm> jWSAlgorithmSet8 = null; // flaky "9) test122(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.supportedJWSAlgorithms();
        com.nimbusds.jose.jca.JCAContext jCAContext9 = null; // flaky "8) test122(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.getJCAContext();
        java.util.Set<com.nimbusds.jose.JWSAlgorithm> jWSAlgorithmSet10 = null; // flaky "5) test122(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.supportedJWSAlgorithms();
        com.nimbusds.jose.jca.JCAContext jCAContext11 = null; // flaky "4) test122(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.getJCAContext();
        org.junit.Assert.assertNotNull(jWSAlgorithm1);
// flaky "4) test122(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jCAContext4);
// flaky "4) test122(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jCAContext5);
// flaky "4) test122(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jCAContext6);
// flaky "4) test122(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jCAContext7);
// flaky "4) test122(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jWSAlgorithmSet8);
// flaky "4) test122(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jCAContext9);
// flaky "3) test122(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jWSAlgorithmSet10);
// flaky "3) test122(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jCAContext11);
    }

    @Test
    public void test123() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test123");
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm0 = null;
        org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier2 = null; // flaky "46) test123(AccessTokenVerifierRandoopRegressionS00)": new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm0, "\ufffd\n\n\001\000, eyJhbGciOiJIUzI1NiJ9, Ed25519, 77+9CgoBAA==, [null, ES256, Ed25519, ES384, Ed448, \"/wH//wA\\u003d\", HS256, EdDSA, HS384], Ed448, PS384, [null], [ES384, ES256, Ed25519, Ed448, HS384, HS256, EdDSA], HS384, \"\", null, PS256, HS256, \"/wH//wA\\u003d\", EdDSA, ES384, ES512]");
    }

    @Test
    public void test124() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test124");
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm0 = com.nimbusds.jose.JWSAlgorithm.PS384;
        com.nimbusds.jose.JWSHeader jWSHeader1 = new com.nimbusds.jose.JWSHeader(jWSAlgorithm0);
        java.lang.String str2 = jWSAlgorithm0.toString();
        // The following exception was thrown during execution in test generation
        try {
            org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier4 = new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm0, "{\"b64\":false,\"kid\":\"none\",\"alg\":\"ES384\"}");
            org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException; message: Illegal base64 character 2c");
        } catch (java.lang.IllegalArgumentException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(jWSAlgorithm0);
        org.junit.Assert.assertEquals("'" + str2 + "' != '" + "PS384" + "'", str2, "PS384");
    }

    @Test
    public void test125() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test125");
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm1 = com.nimbusds.jose.JWSAlgorithm.parse("Ed448");
        org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier3 = null; // flaky "47) test125(AccessTokenVerifierRandoopRegressionS00)": new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm1, "[ES384, ES256, Ed25519, Ed448, HS384, EdDSA, HS256]");
        com.nimbusds.jose.jca.JCAContext jCAContext4 = null; // flaky "37) test125(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.getJCAContext();
        java.util.Set<com.nimbusds.jose.JWSAlgorithm> jWSAlgorithmSet5 = null; // flaky "18) test125(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.supportedJWSAlgorithms();
        com.nimbusds.jose.jca.JCAContext jCAContext6 = null; // flaky "13) test125(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.getJCAContext();
        java.util.Set<com.nimbusds.jose.JWSAlgorithm> jWSAlgorithmSet7 = null; // flaky "11) test125(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.supportedJWSAlgorithms();
        com.nimbusds.jose.jca.JCAContext jCAContext8 = null; // flaky "10) test125(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.getJCAContext();
        com.nimbusds.jose.jca.JCAContext jCAContext9 = null; // flaky "9) test125(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.getJCAContext();
        java.util.Set<com.nimbusds.jose.JWSAlgorithm> jWSAlgorithmSet10 = null; // flaky "6) test125(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.supportedJWSAlgorithms();
        java.util.Set<com.nimbusds.jose.JWSAlgorithm> jWSAlgorithmSet11 = null; // flaky "5) test125(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.supportedJWSAlgorithms();
        org.junit.Assert.assertNotNull(jWSAlgorithm1);
// flaky "5) test125(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jCAContext4);
// flaky "5) test125(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jWSAlgorithmSet5);
// flaky "5) test125(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jCAContext6);
// flaky "5) test125(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jWSAlgorithmSet7);
// flaky "5) test125(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jCAContext8);
// flaky "5) test125(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jCAContext9);
// flaky "4) test125(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jWSAlgorithmSet10);
// flaky "4) test125(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jWSAlgorithmSet11);
    }

    @Test
    public void test126() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test126");
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm1 = com.nimbusds.jose.JWSAlgorithm.parse("Ed448");
        org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier3 = null; // flaky "48) test126(AccessTokenVerifierRandoopRegressionS00)": new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm1, "[ES384, ES256, Ed25519, Ed448, HS384, EdDSA, HS256]");
        com.nimbusds.jose.jca.JCAContext jCAContext4 = null; // flaky "38) test126(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.getJCAContext();
        java.util.Set<com.nimbusds.jose.JWSAlgorithm> jWSAlgorithmSet5 = null; // flaky "19) test126(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.supportedJWSAlgorithms();
        com.nimbusds.jose.jca.JCAContext jCAContext6 = null; // flaky "14) test126(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.getJCAContext();
        com.nimbusds.jose.jca.JCAContext jCAContext7 = null; // flaky "12) test126(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.getJCAContext();
        com.nimbusds.jose.jca.JCAContext jCAContext8 = null; // flaky "11) test126(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.getJCAContext();
        java.util.Set<com.nimbusds.jose.JWSAlgorithm> jWSAlgorithmSet9 = null; // flaky "10) test126(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.supportedJWSAlgorithms();
        org.junit.Assert.assertNotNull(jWSAlgorithm1);
// flaky "7) test126(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jCAContext4);
// flaky "6) test126(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jWSAlgorithmSet5);
// flaky "6) test126(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jCAContext6);
// flaky "6) test126(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jCAContext7);
// flaky "6) test126(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jCAContext8);
// flaky "6) test126(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jWSAlgorithmSet9);
    }

    @Test
    public void test127() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test127");
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm1 = com.nimbusds.jose.JWSAlgorithm.parse("Ed448");
        org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier3 = null; // flaky "49) test127(AccessTokenVerifierRandoopRegressionS00)": new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm1, "[ES384, ES256, Ed25519, Ed448, HS384, EdDSA, HS256]");
        com.nimbusds.jose.jca.JCAContext jCAContext4 = null; // flaky "39) test127(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.getJCAContext();
        java.util.Set<com.nimbusds.jose.JWSAlgorithm> jWSAlgorithmSet5 = null; // flaky "20) test127(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.supportedJWSAlgorithms();
        com.nimbusds.jose.jca.JCAContext jCAContext6 = null; // flaky "15) test127(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.getJCAContext();
        java.util.Set<com.nimbusds.jose.JWSAlgorithm> jWSAlgorithmSet7 = null; // flaky "13) test127(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.supportedJWSAlgorithms();
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm8 = com.nimbusds.jose.JWSAlgorithm.PS384;
        com.nimbusds.jose.JWSHeader jWSHeader9 = new com.nimbusds.jose.JWSHeader(jWSAlgorithm8);
        java.lang.String str10 = jWSAlgorithm8.toString();
        com.nimbusds.jose.JWSHeader jWSHeader11 = new com.nimbusds.jose.JWSHeader(jWSAlgorithm8);
        byte[] byteArray12 = null;
        com.nimbusds.jose.util.Base64URL base64URL14 = com.nimbusds.jose.util.Base64URL.from("");
        // The following exception was thrown during execution in test generation
{ // flaky ('try' without 'catch', 'finally' or resource declarations):         try {
            boolean boolean15 = false; // flaky "12) test127(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.verify(jWSHeader11, byteArray12, base64URL14);
// flaky "11) test127(AccessTokenVerifierRandoopRegressionS00)":             org.junit.Assert.fail("Expected exception of type com.nimbusds.jose.JOSEException; message: Unsupported JWS algorithm PS384, must be HS256, HS384 or HS512");
// flaky (is never thrown in body of corresponding try statement):         } catch (com.nimbusds.jose.JOSEException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(jWSAlgorithm1);
// flaky "8) test127(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jCAContext4);
// flaky "7) test127(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jWSAlgorithmSet5);
// flaky "7) test127(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jCAContext6);
// flaky "7) test127(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jWSAlgorithmSet7);
        org.junit.Assert.assertNotNull(jWSAlgorithm8);
        org.junit.Assert.assertEquals("'" + str10 + "' != '" + "PS384" + "'", str10, "PS384");
        org.junit.Assert.assertNotNull(base64URL14);
    }

    @Test
    public void test128() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test128");
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm1 = com.nimbusds.jose.JWSAlgorithm.parse("[[null, ES256, Ed25519, ES384, Ed448, \"/wH//wA\\u003d\", HS256, EdDSA, HS384], Ed25519, ES256, PS256, PS384, eyJhbGciOiJIUzI1NiJ9, Ed448, [null], \"\", ES512, HS384, null, [ES384, ES256, Ed25519, Ed448, HS384, HS256, EdDSA], ES384, \"/wH//wA\\u003d\", HS256, EdDSA]");
        // The following exception was thrown during execution in test generation
        try {
            org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier3 = new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm1, "\"HS256\"");
            org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException; message: Unsupported JWS algorithm: [[null, ES256, Ed25519, ES384, Ed448, \"/wH//wA\\u003d\", HS256, EdDSA, HS384], Ed25519, ES256, PS256, PS384, eyJhbGciOiJIUzI1NiJ9, Ed448, [null], \"\", ES512, HS384, null, [ES384, ES256, Ed25519, Ed448, HS384, HS256, EdDSA], ES384, \"/wH//wA\\u003d\", HS256, EdDSA]");
        } catch (java.lang.IllegalArgumentException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(jWSAlgorithm1);
    }

    @Test
    public void test129() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test129");
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm1 = com.nimbusds.jose.JWSAlgorithm.parse("Ed448");
        org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier3 = null; // flaky "50) test129(AccessTokenVerifierRandoopRegressionS00)": new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm1, "[ES384, ES256, Ed25519, Ed448, HS384, EdDSA, HS256]");
        com.nimbusds.jose.jca.JCAContext jCAContext4 = null; // flaky "40) test129(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.getJCAContext();
        java.util.Set<com.nimbusds.jose.JWSAlgorithm> jWSAlgorithmSet5 = null; // flaky "21) test129(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.supportedJWSAlgorithms();
        com.nimbusds.jose.jca.JCAContext jCAContext6 = null; // flaky "16) test129(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.getJCAContext();
        java.util.Set<com.nimbusds.jose.JWSAlgorithm> jWSAlgorithmSet7 = null; // flaky "14) test129(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.supportedJWSAlgorithms();
        java.util.Set<com.nimbusds.jose.JWSAlgorithm> jWSAlgorithmSet8 = null; // flaky "13) test129(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.supportedJWSAlgorithms();
        org.junit.Assert.assertNotNull(jWSAlgorithm1);
// flaky "12) test129(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jCAContext4);
// flaky "9) test129(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jWSAlgorithmSet5);
// flaky "8) test129(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jCAContext6);
// flaky "8) test129(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jWSAlgorithmSet7);
// flaky "8) test129(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jWSAlgorithmSet8);
    }

    @Test
    public void test130() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test130");
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm1 = com.nimbusds.jose.JWSAlgorithm.parse("Ed448");
        org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier3 = null; // flaky "51) test130(AccessTokenVerifierRandoopRegressionS00)": new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm1, "[ES384, ES256, Ed25519, Ed448, HS384, EdDSA, HS256]");
        com.nimbusds.jose.jca.JCAContext jCAContext4 = null; // flaky "41) test130(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.getJCAContext();
        java.util.Set<com.nimbusds.jose.JWSAlgorithm> jWSAlgorithmSet5 = null; // flaky "22) test130(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.supportedJWSAlgorithms();
        com.nimbusds.jose.jca.JCAContext jCAContext6 = null; // flaky "17) test130(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.getJCAContext();
        java.util.Set<com.nimbusds.jose.JWSAlgorithm> jWSAlgorithmSet7 = null; // flaky "15) test130(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.supportedJWSAlgorithms();
        com.nimbusds.jose.jca.JCAContext jCAContext8 = null; // flaky "14) test130(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.getJCAContext();
        java.util.Set<com.nimbusds.jose.JWSAlgorithm> jWSAlgorithmSet9 = null; // flaky "13) test130(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.supportedJWSAlgorithms();
        java.util.Set<com.nimbusds.jose.JWSAlgorithm> jWSAlgorithmSet10 = null; // flaky "10) test130(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.supportedJWSAlgorithms();
        java.util.Set<com.nimbusds.jose.JWSAlgorithm> jWSAlgorithmSet11 = null; // flaky "9) test130(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.supportedJWSAlgorithms();
        org.junit.Assert.assertNotNull(jWSAlgorithm1);
// flaky "9) test130(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jCAContext4);
// flaky "9) test130(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jWSAlgorithmSet5);
// flaky "7) test130(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jCAContext6);
// flaky "7) test130(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jWSAlgorithmSet7);
// flaky "6) test130(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jCAContext8);
// flaky "6) test130(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jWSAlgorithmSet9);
// flaky "5) test130(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jWSAlgorithmSet10);
// flaky "5) test130(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jWSAlgorithmSet11);
    }

    @Test
    public void test131() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test131");
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm1 = new com.nimbusds.jose.JWSAlgorithm("[Ed448, PS384]");
        // The following exception was thrown during execution in test generation
        try {
            org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier3 = new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm1, "EdDSA");
            org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException; message: Unsupported JWS algorithm: [Ed448, PS384]");
        } catch (java.lang.IllegalArgumentException e) {
            // Expected exception.
        }
    }

    @Test
    public void test132() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test132");
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm1 = com.nimbusds.jose.JWSAlgorithm.parse("77+9CgoBAA==");
        // The following exception was thrown during execution in test generation
        try {
            org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier3 = new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm1, "\"Ed448\"");
            org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException; message: Unsupported JWS algorithm: 77+9CgoBAA==");
        } catch (java.lang.IllegalArgumentException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(jWSAlgorithm1);
    }

    @Test
    public void test133() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test133");
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm1 = com.nimbusds.jose.JWSAlgorithm.parse("Ed448");
        org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier3 = null; // flaky "52) test133(AccessTokenVerifierRandoopRegressionS00)": new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm1, "[ES384, ES256, Ed25519, Ed448, HS384, EdDSA, HS256]");
        com.nimbusds.jose.jca.JCAContext jCAContext4 = null; // flaky "42) test133(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.getJCAContext();
        java.util.Set<com.nimbusds.jose.JWSAlgorithm> jWSAlgorithmSet5 = null; // flaky "23) test133(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.supportedJWSAlgorithms();
        com.nimbusds.jose.jca.JCAContext jCAContext6 = null; // flaky "18) test133(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.getJCAContext();
        java.util.Set<com.nimbusds.jose.JWSAlgorithm> jWSAlgorithmSet7 = null; // flaky "16) test133(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.supportedJWSAlgorithms();
        com.nimbusds.jose.jca.JCAContext jCAContext8 = null; // flaky "15) test133(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.getJCAContext();
        com.nimbusds.jose.jca.JCAContext jCAContext9 = null; // flaky "14) test133(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.getJCAContext();
        java.util.Set<com.nimbusds.jose.JWSAlgorithm> jWSAlgorithmSet10 = null; // flaky "11) test133(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.supportedJWSAlgorithms();
        com.nimbusds.jose.jca.JCAContext jCAContext11 = null; // flaky "10) test133(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.getJCAContext();
        com.nimbusds.jose.jca.JCAContext jCAContext12 = null; // flaky "10) test133(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.getJCAContext();
        org.junit.Assert.assertNotNull(jWSAlgorithm1);
// flaky "10) test133(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jCAContext4);
// flaky "8) test133(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jWSAlgorithmSet5);
// flaky "8) test133(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jCAContext6);
// flaky "7) test133(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jWSAlgorithmSet7);
// flaky "7) test133(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jCAContext8);
// flaky "6) test133(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jCAContext9);
// flaky "6) test133(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jWSAlgorithmSet10);
// flaky "1) test133(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jCAContext11);
// flaky "1) test133(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jCAContext12);
    }

    @Test
    public void test134() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test134");
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm1 = new com.nimbusds.jose.JWSAlgorithm("\"ImZlZGNiYTk4NzY1NDMyMTBmZWRjYmE5ODc2NTQzMjEwIg\\u003d\\u003d\"");
        // The following exception was thrown during execution in test generation
        try {
            org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier3 = new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm1, "\ufffd\ufffd\024\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffda\022\ufffd]");
            org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException; message: Unsupported JWS algorithm: \"ImZlZGNiYTk4NzY1NDMyMTBmZWRjYmE5ODc2NTQzMjEwIg\\u003d\\u003d\"");
        } catch (java.lang.IllegalArgumentException e) {
            // Expected exception.
        }
    }

    @Test
    public void test135() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test135");
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm1 = new com.nimbusds.jose.JWSAlgorithm("");
        // The following exception was thrown during execution in test generation
        try {
            org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier3 = new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm1, "\ufffd\001\ufffd\ufffd\000");
            org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException; message: Unsupported JWS algorithm: ");
        } catch (java.lang.IllegalArgumentException e) {
            // Expected exception.
        }
    }

    @Test
    public void test136() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test136");
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm1 = com.nimbusds.jose.JWSAlgorithm.parse("Ed448");
        org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier3 = null; // flaky "53) test136(AccessTokenVerifierRandoopRegressionS00)": new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm1, "[ES384, ES256, Ed25519, Ed448, HS384, EdDSA, HS256]");
        com.nimbusds.jose.jca.JCAContext jCAContext4 = null; // flaky "43) test136(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.getJCAContext();
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm5 = com.nimbusds.jose.JWSAlgorithm.HS256;
        com.nimbusds.jose.JWSHeader jWSHeader6 = new com.nimbusds.jose.JWSHeader(jWSAlgorithm5);
        boolean boolean7 = jWSHeader6.isBase64URLEncodePayload();
        java.net.URI uRI8 = jWSHeader6.getJWKURL();
        java.util.Set<java.lang.String> strSet9 = jWSHeader6.getIncludedParams();
        com.nimbusds.jose.JOSEObjectType jOSEObjectType10 = jWSHeader6.getType();
        java.util.Map<java.lang.String, java.lang.Object> strMap11 = jWSHeader6.toJSONObject();
        com.nimbusds.jose.util.Base64URL base64URL13 = new com.nimbusds.jose.util.Base64URL("\"/wH//wA\\u003d\"");
        java.lang.String str14 = base64URL13.toString();
        byte[] byteArray15 = base64URL13.decode();
        com.nimbusds.jose.util.Base64URL base64URL17 = com.nimbusds.jose.util.Base64URL.from("[ES256, Ed25519, \"\"\ufffd\n\n\001\000, eyJhbGciOiJIUzI1NiJ9, 77+9CgoBAA==, [null, ES256, Ed25519, ES384, Ed448, \"/wH//wA\\u003d\", HS256, EdDSA, HS384], Ed448, PS384, [null], [ES384, ES256, Ed25519, Ed448, HS384, HS256, EdDSA], HS384, ES512, \"fedcba9876543210fedcba9876543210\", ES384, null, PS256, \"/wH//wA\\u003d\", EdDSA, HS256]");
        boolean boolean18 = false; // flaky "24) test136(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.verify(jWSHeader6, byteArray15, base64URL17);
        com.nimbusds.jose.util.Base64URL base64URL19 = com.nimbusds.jose.util.Base64URL.encode(byteArray15);
        org.junit.Assert.assertNotNull(jWSAlgorithm1);
// flaky "19) test136(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jCAContext4);
        org.junit.Assert.assertNotNull(jWSAlgorithm5);
        org.junit.Assert.assertTrue("'" + boolean7 + "' != '" + true + "'", boolean7 == true);
        org.junit.Assert.assertNull(uRI8);
        org.junit.Assert.assertNotNull(strSet9);
        org.junit.Assert.assertNull(jOSEObjectType10);
        org.junit.Assert.assertNotNull(strMap11);
        org.junit.Assert.assertEquals("'" + str14 + "' != '" + "\"/wH//wA\\u003d\"" + "'", str14, "\"/wH//wA\\u003d\"");
        org.junit.Assert.assertNotNull(byteArray15);
        org.junit.Assert.assertArrayEquals(byteArray15, new byte[] { (byte) -1, (byte) 1, (byte) -1, (byte) -1, (byte) 0, (byte) 46, (byte) -45, (byte) 77, (byte) -35 });
        org.junit.Assert.assertNotNull(base64URL17);
        org.junit.Assert.assertTrue("'" + boolean18 + "' != '" + false + "'", boolean18 == false);
        org.junit.Assert.assertNotNull(base64URL19);
    }

    @Test
    public void test137() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test137");
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm0 = com.nimbusds.jose.JWSAlgorithm.PS384;
        com.nimbusds.jose.JWSHeader jWSHeader1 = new com.nimbusds.jose.JWSHeader(jWSAlgorithm0);
        com.nimbusds.jose.util.Base64URL base64URL2 = jWSHeader1.getParsedBase64URL();
        java.lang.String str3 = jWSHeader1.toString();
        java.util.Set<java.lang.String> strSet4 = jWSHeader1.getCriticalParams();
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm5 = jWSHeader1.getAlgorithm();
        java.lang.String str6 = jWSAlgorithm5.getName();
        // The following exception was thrown during execution in test generation
        try {
            org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier8 = new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm5, "[[null, ES256, Ed25519, ES384, Ed448, \"/wH//wA\\u003d\", HS256, EdDSA, HS384], \"\\\"ES256K\\\"\", null, Ed25519, ES256, \"0123456789abcdef0123456789abcdef\"\ufffd\001\ufffd\ufffd\000\ufffd\ufffd, \"\"\ufffd\n\n\001\000, eyJhbGciOiJIUzI1NiJ9, 77+9CgoBAA==, Ed448, PS384, [null], [ES384, ES256, Ed25519, Ed448, HS384, HS256, EdDSA], PS256, ES512, \"fedcba9876543210fedcba9876543210\", \"/wH//wA\\u003d\", HS256, ES384, HS384, EdDSA]");
            org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException; message: A key pair is required, in the 'private:public' format");
        } catch (java.lang.IllegalArgumentException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(jWSAlgorithm0);
        org.junit.Assert.assertNull(base64URL2);
        org.junit.Assert.assertEquals("'" + str3 + "' != '" + "{\"alg\":\"PS384\"}" + "'", str3, "{\"alg\":\"PS384\"}");
        org.junit.Assert.assertNull(strSet4);
        org.junit.Assert.assertNotNull(jWSAlgorithm5);
        org.junit.Assert.assertEquals("'" + str6 + "' != '" + "PS384" + "'", str6, "PS384");
    }

    @Test
    public void test138() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test138");
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm1 = com.nimbusds.jose.JWSAlgorithm.parse("Ed448");
        org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier3 = null; // flaky "54) test138(AccessTokenVerifierRandoopRegressionS00)": new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm1, "[ES384, ES256, Ed25519, Ed448, HS384, EdDSA, HS256]");
        com.nimbusds.jose.jca.JCAContext jCAContext4 = null; // flaky "44) test138(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.getJCAContext();
        java.util.Set<com.nimbusds.jose.JWSAlgorithm> jWSAlgorithmSet5 = null; // flaky "25) test138(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.supportedJWSAlgorithms();
        com.nimbusds.jose.jca.JCAContext jCAContext6 = null; // flaky "20) test138(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.getJCAContext();
        com.nimbusds.jose.jca.JCAContext jCAContext7 = null; // flaky "17) test138(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.getJCAContext();
        com.nimbusds.jose.jca.JCAContext jCAContext8 = null; // flaky "16) test138(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.getJCAContext();
        com.nimbusds.jose.jca.JCAContext jCAContext9 = null; // flaky "15) test138(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.getJCAContext();
        org.junit.Assert.assertNotNull(jWSAlgorithm1);
// flaky "12) test138(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jCAContext4);
// flaky "11) test138(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jWSAlgorithmSet5);
// flaky "11) test138(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jCAContext6);
// flaky "11) test138(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jCAContext7);
// flaky "9) test138(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jCAContext8);
// flaky "9) test138(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jCAContext9);
    }

    @Test
    public void test139() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test139");
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm1 = com.nimbusds.jose.JWSAlgorithm.parse("Ed448");
        org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier3 = null; // flaky "55) test139(AccessTokenVerifierRandoopRegressionS00)": new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm1, "[ES384, ES256, Ed25519, Ed448, HS384, EdDSA, HS256]");
        com.nimbusds.jose.jca.JCAContext jCAContext4 = null; // flaky "45) test139(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.getJCAContext();
        com.nimbusds.jose.jca.JCAContext jCAContext5 = null; // flaky "26) test139(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.getJCAContext();
        com.nimbusds.jose.jca.JCAContext jCAContext6 = null; // flaky "21) test139(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.getJCAContext();
        com.nimbusds.jose.jca.JCAContext jCAContext7 = null; // flaky "18) test139(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.getJCAContext();
        java.util.Set<com.nimbusds.jose.JWSAlgorithm> jWSAlgorithmSet8 = null; // flaky "17) test139(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.supportedJWSAlgorithms();
        com.nimbusds.jose.jca.JCAContext jCAContext9 = null; // flaky "16) test139(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.getJCAContext();
        java.util.Set<com.nimbusds.jose.JWSAlgorithm> jWSAlgorithmSet10 = null; // flaky "13) test139(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.supportedJWSAlgorithms();
        java.util.Set<com.nimbusds.jose.JWSAlgorithm> jWSAlgorithmSet11 = null; // flaky "12) test139(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.supportedJWSAlgorithms();
        org.junit.Assert.assertNotNull(jWSAlgorithm1);
// flaky "12) test139(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jCAContext4);
// flaky "12) test139(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jCAContext5);
// flaky "10) test139(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jCAContext6);
// flaky "10) test139(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jCAContext7);
// flaky "8) test139(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jWSAlgorithmSet8);
// flaky "8) test139(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jCAContext9);
// flaky "7) test139(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jWSAlgorithmSet10);
// flaky "7) test139(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jWSAlgorithmSet11);
    }

    @Test
    public void test140() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test140");
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm0 = com.nimbusds.jose.JWSAlgorithm.HS256;
        com.nimbusds.jose.JWSHeader jWSHeader1 = new com.nimbusds.jose.JWSHeader(jWSAlgorithm0);
        boolean boolean2 = jWSHeader1.isBase64URLEncodePayload();
        java.net.URI uRI3 = jWSHeader1.getX509CertURL();
        java.lang.String str4 = jWSHeader1.getKeyID();
        java.util.Map<java.lang.String, java.lang.Object> strMap5 = jWSHeader1.toJSONObject();
        com.nimbusds.jose.util.Base64URL base64URL7 = com.nimbusds.jose.util.Base64URL.encode("0123456789abcdef0123456789abcdef");
        com.nimbusds.jose.JWSHeader jWSHeader8 = com.nimbusds.jose.JWSHeader.parse(strMap5, base64URL7);
        com.nimbusds.jose.util.Base64URL base64URL9 = jWSHeader8.getX509CertThumbprint();
        java.lang.Object obj11 = jWSHeader8.getCustomParam("0123456789abcdef0123456789abcdef");
        com.nimbusds.jose.JWSHeader jWSHeader12 = new com.nimbusds.jose.JWSHeader(jWSHeader8);
        java.util.Map<java.lang.String, java.lang.Object> strMap13 = jWSHeader8.toJSONObject();
        com.nimbusds.jose.Algorithm algorithm14 = com.nimbusds.jose.Header.parseAlgorithm(strMap13);
        com.nimbusds.jose.JWSHeader jWSHeader15 = com.nimbusds.jose.JWSHeader.parse(strMap13);
        java.util.List list16 = jWSHeader15.getX509CertChain();
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm17 = jWSHeader15.getAlgorithm();
        // The following exception was thrown during execution in test generation
        try {
            org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier19 = null; // flaky "56) test140(AccessTokenVerifierRandoopRegressionS00)": new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm17, "\ufffd\ufffd\ufffd\ufffdM4");
// flaky "46) test140(AccessTokenVerifierRandoopRegressionS00)":             org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException; message: A key pair is required, in the 'private:public' format");
        } catch (java.lang.IllegalArgumentException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(jWSAlgorithm0);
        org.junit.Assert.assertTrue("'" + boolean2 + "' != '" + true + "'", boolean2 == true);
        org.junit.Assert.assertNull(uRI3);
        org.junit.Assert.assertNull(str4);
        org.junit.Assert.assertNotNull(strMap5);
        org.junit.Assert.assertNotNull(base64URL7);
        org.junit.Assert.assertNotNull(jWSHeader8);
        org.junit.Assert.assertNull(base64URL9);
        org.junit.Assert.assertNull(obj11);
        org.junit.Assert.assertNotNull(strMap13);
        org.junit.Assert.assertNotNull(algorithm14);
        org.junit.Assert.assertNotNull(jWSHeader15);
        org.junit.Assert.assertNull(list16);
        org.junit.Assert.assertNotNull(jWSAlgorithm17);
    }

    @Test
    public void test141() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test141");
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm1 = com.nimbusds.jose.JWSAlgorithm.parse("Ed448");
        org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier3 = null; // flaky "57) test141(AccessTokenVerifierRandoopRegressionS00)": new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm1, "[ES384, ES256, Ed25519, Ed448, HS384, EdDSA, HS256]");
        com.nimbusds.jose.jca.JCAContext jCAContext4 = null; // flaky "47) test141(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.getJCAContext();
        com.nimbusds.jose.jca.JCAContext jCAContext5 = null; // flaky "27) test141(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.getJCAContext();
        com.nimbusds.jose.jca.JCAContext jCAContext6 = null; // flaky "22) test141(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.getJCAContext();
        com.nimbusds.jose.jca.JCAContext jCAContext7 = null; // flaky "19) test141(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.getJCAContext();
        java.util.Set<com.nimbusds.jose.JWSAlgorithm> jWSAlgorithmSet8 = null; // flaky "18) test141(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.supportedJWSAlgorithms();
        java.util.Set<com.nimbusds.jose.JWSAlgorithm> jWSAlgorithmSet9 = null; // flaky "17) test141(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.supportedJWSAlgorithms();
        com.nimbusds.jose.jca.JCAContext jCAContext10 = null; // flaky "14) test141(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.getJCAContext();
        java.util.Set<com.nimbusds.jose.JWSAlgorithm> jWSAlgorithmSet11 = null; // flaky "13) test141(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.supportedJWSAlgorithms();
        org.junit.Assert.assertNotNull(jWSAlgorithm1);
// flaky "13) test141(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jCAContext4);
// flaky "13) test141(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jCAContext5);
// flaky "11) test141(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jCAContext6);
// flaky "11) test141(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jCAContext7);
// flaky "9) test141(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jWSAlgorithmSet8);
// flaky "9) test141(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jWSAlgorithmSet9);
// flaky "8) test141(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jCAContext10);
// flaky "8) test141(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jWSAlgorithmSet11);
    }

    @Test
    public void test142() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test142");
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm1 = com.nimbusds.jose.JWSAlgorithm.parse("[Ed25519, ES256, eyJhbGciOiJIUzI1NiJ9, Ed448, ES384, null, [null], \"\", ES512, HS384, EdDSA, HS256, \"/wH//wA\\u003d\"]");
        // The following exception was thrown during execution in test generation
        try {
            org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier3 = new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm1, "\ufffd\ufffd\024\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd");
            org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException; message: Unsupported JWS algorithm: [Ed25519, ES256, eyJhbGciOiJIUzI1NiJ9, Ed448, ES384, null, [null], \"\", ES512, HS384, EdDSA, HS256, \"/wH//wA\\u003d\"]");
        } catch (java.lang.IllegalArgumentException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(jWSAlgorithm1);
    }

    @Test
    public void test143() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test143");
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm1 = com.nimbusds.jose.JWSAlgorithm.parse("Ed448");
        org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier3 = null; // flaky "58) test143(AccessTokenVerifierRandoopRegressionS00)": new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm1, "[ES384, ES256, Ed25519, Ed448, HS384, EdDSA, HS256]");
        com.nimbusds.jose.jca.JCAContext jCAContext4 = null; // flaky "48) test143(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.getJCAContext();
        com.nimbusds.jose.jca.JCAContext jCAContext5 = null; // flaky "28) test143(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.getJCAContext();
        java.util.Set<com.nimbusds.jose.JWSAlgorithm> jWSAlgorithmSet6 = null; // flaky "23) test143(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.supportedJWSAlgorithms();
        java.util.Set<com.nimbusds.jose.JWSAlgorithm> jWSAlgorithmSet7 = null; // flaky "20) test143(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.supportedJWSAlgorithms();
        com.nimbusds.jose.jca.JCAContext jCAContext8 = null; // flaky "19) test143(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.getJCAContext();
        java.util.Set<com.nimbusds.jose.JWSAlgorithm> jWSAlgorithmSet9 = null; // flaky "18) test143(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.supportedJWSAlgorithms();
        com.nimbusds.jose.jca.JCAContext jCAContext10 = null; // flaky "15) test143(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.getJCAContext();
        com.nimbusds.jose.jca.JCAContext jCAContext11 = null; // flaky "14) test143(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.getJCAContext();
        com.nimbusds.jose.jca.JCAContext jCAContext12 = null; // flaky "14) test143(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.getJCAContext();
        java.util.Set<com.nimbusds.jose.JWSAlgorithm> jWSAlgorithmSet13 = null; // flaky "14) test143(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.supportedJWSAlgorithms();
        java.util.Set<com.nimbusds.jose.JWSAlgorithm> jWSAlgorithmSet14 = null; // flaky "12) test143(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.supportedJWSAlgorithms();
        org.junit.Assert.assertNotNull(jWSAlgorithm1);
// flaky "12) test143(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jCAContext4);
// flaky "10) test143(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jCAContext5);
// flaky "10) test143(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jWSAlgorithmSet6);
// flaky "9) test143(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jWSAlgorithmSet7);
// flaky "9) test143(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jCAContext8);
// flaky "2) test143(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jWSAlgorithmSet9);
// flaky "2) test143(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jCAContext10);
// flaky "1) test143(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jCAContext11);
// flaky "1) test143(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jCAContext12);
// flaky "1) test143(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jWSAlgorithmSet13);
// flaky "1) test143(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jWSAlgorithmSet14);
    }

    @Test
    public void test144() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test144");
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm1 = com.nimbusds.jose.JWSAlgorithm.parse("Ed448");
        org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier3 = null; // flaky "59) test144(AccessTokenVerifierRandoopRegressionS00)": new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm1, "[ES384, ES256, Ed25519, Ed448, HS384, EdDSA, HS256]");
        com.nimbusds.jose.jca.JCAContext jCAContext4 = null; // flaky "49) test144(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.getJCAContext();
        java.util.Set<com.nimbusds.jose.JWSAlgorithm> jWSAlgorithmSet5 = null; // flaky "29) test144(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.supportedJWSAlgorithms();
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm7 = com.nimbusds.jose.JWSAlgorithm.parse("\"HS256\"");
        com.nimbusds.jose.JWSHeader jWSHeader8 = new com.nimbusds.jose.JWSHeader(jWSAlgorithm7);
        com.nimbusds.jose.util.Base64URL base64URL10 = com.nimbusds.jose.util.Base64URL.encode("eyJhbGciOiJIUzI1NiJ9");
        byte[] byteArray11 = base64URL10.decode();
        com.nimbusds.jose.util.Base64URL base64URL12 = com.nimbusds.jose.util.Base64URL.encode(byteArray11);
        com.nimbusds.jose.util.Base64 base64_13 = com.nimbusds.jose.util.Base64.encode(byteArray11);
        com.nimbusds.jose.util.Base64URL base64URL14 = null;
        // The following exception was thrown during execution in test generation
{ // flaky ('try' without 'catch', 'finally' or resource declarations):         try {
            boolean boolean15 = false; // flaky "24) test144(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.verify(jWSHeader8, byteArray11, base64URL14);
// flaky "21) test144(AccessTokenVerifierRandoopRegressionS00)":             org.junit.Assert.fail("Expected exception of type com.nimbusds.jose.JOSEException; message: Unsupported JWS algorithm \"HS256\", must be HS256, HS384 or HS512");
// flaky (is never thrown in body of corresponding try statement):         } catch (com.nimbusds.jose.JOSEException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(jWSAlgorithm1);
// flaky "20) test144(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jCAContext4);
// flaky "19) test144(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jWSAlgorithmSet5);
        org.junit.Assert.assertNotNull(jWSAlgorithm7);
        org.junit.Assert.assertNotNull(base64URL10);
        org.junit.Assert.assertNotNull(byteArray11);
        org.junit.Assert.assertArrayEquals(byteArray11, new byte[] { (byte) 101, (byte) 121, (byte) 74, (byte) 104, (byte) 98, (byte) 71, (byte) 99, (byte) 105, (byte) 79, (byte) 105, (byte) 74, (byte) 73, (byte) 85, (byte) 122, (byte) 73, (byte) 49, (byte) 78, (byte) 105, (byte) 74, (byte) 57 });
        org.junit.Assert.assertNotNull(base64URL12);
        org.junit.Assert.assertNotNull(base64_13);
    }

    @Test
    public void test145() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test145");
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm1 = com.nimbusds.jose.JWSAlgorithm.parse("Ed448");
        org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier3 = null; // flaky "60) test145(AccessTokenVerifierRandoopRegressionS00)": new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm1, "[ES384, ES256, Ed25519, Ed448, HS384, EdDSA, HS256]");
        com.nimbusds.jose.jca.JCAContext jCAContext4 = null; // flaky "50) test145(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.getJCAContext();
        java.util.Set<com.nimbusds.jose.JWSAlgorithm> jWSAlgorithmSet5 = null; // flaky "30) test145(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.supportedJWSAlgorithms();
        com.nimbusds.jose.jca.JCAContext jCAContext6 = null; // flaky "25) test145(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.getJCAContext();
        java.util.Set<com.nimbusds.jose.JWSAlgorithm> jWSAlgorithmSet7 = null; // flaky "22) test145(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.supportedJWSAlgorithms();
        com.nimbusds.jose.jca.JCAContext jCAContext8 = null; // flaky "21) test145(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.getJCAContext();
        com.nimbusds.jose.jca.JCAContext jCAContext9 = null; // flaky "20) test145(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.getJCAContext();
        com.nimbusds.jose.jca.JCAContext jCAContext10 = null; // flaky "16) test145(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.getJCAContext();
        java.util.Set<com.nimbusds.jose.JWSAlgorithm> jWSAlgorithmSet11 = null; // flaky "15) test145(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.supportedJWSAlgorithms();
        org.junit.Assert.assertNotNull(jWSAlgorithm1);
// flaky "15) test145(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jCAContext4);
// flaky "15) test145(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jWSAlgorithmSet5);
// flaky "13) test145(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jCAContext6);
// flaky "13) test145(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jWSAlgorithmSet7);
// flaky "11) test145(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jCAContext8);
// flaky "11) test145(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jCAContext9);
// flaky "10) test145(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jCAContext10);
// flaky "10) test145(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jWSAlgorithmSet11);
    }

    @Test
    public void test146() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test146");
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm1 = com.nimbusds.jose.JWSAlgorithm.parse("Ed448");
        org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier3 = null; // flaky "61) test146(AccessTokenVerifierRandoopRegressionS00)": new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm1, "[ES384, ES256, Ed25519, Ed448, HS384, EdDSA, HS256]");
        com.nimbusds.jose.JWSHeader jWSHeader4 = new com.nimbusds.jose.JWSHeader(jWSAlgorithm1);
        org.junit.Assert.assertNotNull(jWSAlgorithm1);
    }

    @Test
    public void test147() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test147");
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm1 = com.nimbusds.jose.JWSAlgorithm.parse("Ed448");
        org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier3 = null; // flaky "62) test147(AccessTokenVerifierRandoopRegressionS00)": new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm1, "[ES384, ES256, Ed25519, Ed448, HS384, EdDSA, HS256]");
        com.nimbusds.jose.jca.JCAContext jCAContext4 = null; // flaky "51) test147(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.getJCAContext();
        com.nimbusds.jose.jca.JCAContext jCAContext5 = null; // flaky "31) test147(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.getJCAContext();
        java.util.Set<com.nimbusds.jose.JWSAlgorithm> jWSAlgorithmSet6 = null; // flaky "26) test147(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.supportedJWSAlgorithms();
        java.util.Set<com.nimbusds.jose.JWSAlgorithm> jWSAlgorithmSet7 = null; // flaky "23) test147(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.supportedJWSAlgorithms();
        com.nimbusds.jose.jca.JCAContext jCAContext8 = null; // flaky "22) test147(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.getJCAContext();
        java.util.Set<com.nimbusds.jose.JWSAlgorithm> jWSAlgorithmSet9 = null; // flaky "21) test147(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.supportedJWSAlgorithms();
        java.util.Set<com.nimbusds.jose.JWSAlgorithm> jWSAlgorithmSet10 = null; // flaky "17) test147(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.supportedJWSAlgorithms();
        java.util.Set<com.nimbusds.jose.JWSAlgorithm> jWSAlgorithmSet11 = null; // flaky "16) test147(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.supportedJWSAlgorithms();
        java.util.Set<com.nimbusds.jose.JWSAlgorithm> jWSAlgorithmSet12 = null; // flaky "16) test147(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.supportedJWSAlgorithms();
        com.nimbusds.jose.jca.JCAContext jCAContext13 = null; // flaky "16) test147(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.getJCAContext();
        org.junit.Assert.assertNotNull(jWSAlgorithm1);
// flaky "14) test147(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jCAContext4);
// flaky "14) test147(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jCAContext5);
// flaky "12) test147(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jWSAlgorithmSet6);
// flaky "12) test147(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jWSAlgorithmSet7);
// flaky "11) test147(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jCAContext8);
// flaky "11) test147(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jWSAlgorithmSet9);
// flaky "3) test147(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jWSAlgorithmSet10);
// flaky "3) test147(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jWSAlgorithmSet11);
// flaky "2) test147(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jWSAlgorithmSet12);
// flaky "2) test147(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jCAContext13);
    }

    @Test
    public void test148() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test148");
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm1 = com.nimbusds.jose.JWSAlgorithm.parse("Ed448");
        org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier3 = null; // flaky "63) test148(AccessTokenVerifierRandoopRegressionS00)": new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm1, "[ES384, ES256, Ed25519, Ed448, HS384, EdDSA, HS256]");
        com.nimbusds.jose.jca.JCAContext jCAContext4 = null; // flaky "52) test148(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.getJCAContext();
        com.nimbusds.jose.jca.JCAContext jCAContext5 = null; // flaky "32) test148(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.getJCAContext();
        java.util.Set<com.nimbusds.jose.JWSAlgorithm> jWSAlgorithmSet6 = null; // flaky "27) test148(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.supportedJWSAlgorithms();
        java.util.Set<com.nimbusds.jose.JWSAlgorithm> jWSAlgorithmSet7 = null; // flaky "24) test148(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.supportedJWSAlgorithms();
        com.nimbusds.jose.jca.JCAContext jCAContext8 = null; // flaky "23) test148(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.getJCAContext();
        java.util.Set<com.nimbusds.jose.JWSAlgorithm> jWSAlgorithmSet9 = null; // flaky "22) test148(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.supportedJWSAlgorithms();
        com.nimbusds.jose.jca.JCAContext jCAContext10 = null; // flaky "18) test148(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.getJCAContext();
        java.util.Set<com.nimbusds.jose.JWSAlgorithm> jWSAlgorithmSet11 = null; // flaky "17) test148(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.supportedJWSAlgorithms();
        org.junit.Assert.assertNotNull(jWSAlgorithm1);
// flaky "17) test148(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jCAContext4);
// flaky "17) test148(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jCAContext5);
// flaky "15) test148(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jWSAlgorithmSet6);
// flaky "15) test148(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jWSAlgorithmSet7);
// flaky "13) test148(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jCAContext8);
// flaky "13) test148(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jWSAlgorithmSet9);
// flaky "12) test148(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jCAContext10);
// flaky "12) test148(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jWSAlgorithmSet11);
    }

    @Test
    public void test149() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test149");
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm1 = com.nimbusds.jose.JWSAlgorithm.parse("Ed448");
        org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier3 = null; // flaky "64) test149(AccessTokenVerifierRandoopRegressionS00)": new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm1, "[ES384, ES256, Ed25519, Ed448, HS384, EdDSA, HS256]");
        com.nimbusds.jose.jca.JCAContext jCAContext4 = null; // flaky "53) test149(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.getJCAContext();
        java.util.Set<com.nimbusds.jose.JWSAlgorithm> jWSAlgorithmSet5 = null; // flaky "33) test149(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.supportedJWSAlgorithms();
        com.nimbusds.jose.jca.JCAContext jCAContext6 = null; // flaky "28) test149(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.getJCAContext();
        java.util.Set<com.nimbusds.jose.JWSAlgorithm> jWSAlgorithmSet7 = null; // flaky "25) test149(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.supportedJWSAlgorithms();
        com.nimbusds.jose.jca.JCAContext jCAContext8 = null; // flaky "24) test149(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.getJCAContext();
        java.util.Set<com.nimbusds.jose.JWSAlgorithm> jWSAlgorithmSet9 = null; // flaky "23) test149(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.supportedJWSAlgorithms();
        com.nimbusds.jose.jca.JCAContext jCAContext10 = null; // flaky "19) test149(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.getJCAContext();
        java.util.Set<com.nimbusds.jose.JWSAlgorithm> jWSAlgorithmSet11 = null; // flaky "18) test149(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.supportedJWSAlgorithms();
        com.nimbusds.jose.jca.JCAContext jCAContext12 = null; // flaky "18) test149(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.getJCAContext();
        com.nimbusds.jose.jca.JCAContext jCAContext13 = null; // flaky "18) test149(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.getJCAContext();
        org.junit.Assert.assertNotNull(jWSAlgorithm1);
// flaky "16) test149(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jCAContext4);
// flaky "16) test149(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jWSAlgorithmSet5);
// flaky "14) test149(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jCAContext6);
// flaky "14) test149(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jWSAlgorithmSet7);
// flaky "13) test149(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jCAContext8);
// flaky "13) test149(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jWSAlgorithmSet9);
// flaky "4) test149(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jCAContext10);
// flaky "4) test149(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jWSAlgorithmSet11);
// flaky "3) test149(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jCAContext12);
// flaky "3) test149(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jCAContext13);
    }

    @Test
    public void test150() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test150");
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm1 = com.nimbusds.jose.JWSAlgorithm.parse("Ed448");
        org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier3 = null; // flaky "65) test150(AccessTokenVerifierRandoopRegressionS00)": new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm1, "[ES384, ES256, Ed25519, Ed448, HS384, EdDSA, HS256]");
        com.nimbusds.jose.jca.JCAContext jCAContext4 = null; // flaky "54) test150(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.getJCAContext();
        java.util.Set<com.nimbusds.jose.JWSAlgorithm> jWSAlgorithmSet5 = null; // flaky "34) test150(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.supportedJWSAlgorithms();
        com.nimbusds.jose.jca.JCAContext jCAContext6 = null; // flaky "29) test150(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.getJCAContext();
        com.nimbusds.jose.jca.JCAContext jCAContext7 = null; // flaky "26) test150(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.getJCAContext();
        java.util.Set<com.nimbusds.jose.JWSAlgorithm> jWSAlgorithmSet8 = null; // flaky "25) test150(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.supportedJWSAlgorithms();
        org.junit.Assert.assertNotNull(jWSAlgorithm1);
// flaky "24) test150(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jCAContext4);
// flaky "20) test150(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jWSAlgorithmSet5);
// flaky "19) test150(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jCAContext6);
// flaky "19) test150(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jCAContext7);
// flaky "19) test150(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jWSAlgorithmSet8);
    }

    @Test
    public void test151() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test151");
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm0 = com.nimbusds.jose.JWSAlgorithm.Ed448;
        boolean boolean2 = jWSAlgorithm0.equals((java.lang.Object) "\ufffd\n\n\001\000");
        com.nimbusds.jose.JWSHeader.Builder builder3 = new com.nimbusds.jose.JWSHeader.Builder(jWSAlgorithm0);
        com.nimbusds.jose.JWSHeader.Builder builder5 = builder3.base64URLEncodePayload(true);
        com.nimbusds.jose.JWSHeader.Builder builder7 = builder5.keyID("0123456789abcdef0123456789abcdef");
        com.nimbusds.jose.JWSHeader jWSHeader8 = builder7.build();
        com.nimbusds.jose.JWSHeader.Builder builder9 = new com.nimbusds.jose.JWSHeader.Builder(jWSHeader8);
        java.util.Map<java.lang.String, java.lang.Object> strMap10 = jWSHeader8.toJSONObject();
        com.nimbusds.jose.JWSHeader jWSHeader11 = com.nimbusds.jose.JWSHeader.parse(strMap10);
        com.nimbusds.jose.util.Base64URL base64URL13 = new com.nimbusds.jose.util.Base64URL("\"/wH//wA\\u003d\"");
        com.nimbusds.jose.JWSHeader jWSHeader14 = com.nimbusds.jose.JWSHeader.parse(strMap10, base64URL13);
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm15 = jWSHeader14.getAlgorithm();
        com.nimbusds.jose.JWSHeader jWSHeader16 = new com.nimbusds.jose.JWSHeader(jWSAlgorithm15);
        org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier18 = null; // flaky "66) test151(AccessTokenVerifierRandoopRegressionS00)": new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm15, "[ES384, \"\", none, ES256, [null, ES256, Ed25519, ES384, Ed448, \"/wH//wA\\u003d\", HS256, EdDSA, HS384], \"\\\"ES256K\\\"\", null, Ed25519, \"0123456789abcdef0123456789abcdef\"\ufffd\001\ufffd\ufffd\000\ufffd\ufffd\ufffd\n\n\001\000, eyJhbGciOiJIUzI1NiJ9, 77+9CgoBAA==, Ed448, PS384, [null], [ES384, ES256, Ed25519, Ed448, HS384, HS256, EdDSA], PS256, ES512, \"fedcba9876543210fedcba9876543210\", HS256, HS384, \"/wH//wA\\u003d\", RS256, EdDSA]");
        org.junit.Assert.assertNotNull(jWSAlgorithm0);
        org.junit.Assert.assertTrue("'" + boolean2 + "' != '" + false + "'", boolean2 == false);
        org.junit.Assert.assertNotNull(builder5);
        org.junit.Assert.assertNotNull(builder7);
        org.junit.Assert.assertNotNull(jWSHeader8);
        org.junit.Assert.assertNotNull(strMap10);
        org.junit.Assert.assertNotNull(jWSHeader11);
        org.junit.Assert.assertNotNull(jWSHeader14);
        org.junit.Assert.assertNotNull(jWSAlgorithm15);
    }

    @Test
    public void test152() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test152");
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm1 = com.nimbusds.jose.JWSAlgorithm.PS256;
        com.nimbusds.jose.Requirement requirement2 = jWSAlgorithm1.getRequirement();
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm3 = new com.nimbusds.jose.JWSAlgorithm("", requirement2);
        com.nimbusds.jose.JWSHeader.Builder builder4 = new com.nimbusds.jose.JWSHeader.Builder(jWSAlgorithm3);
        // The following exception was thrown during execution in test generation
        try {
            org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier6 = new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm3, "ES512");
            org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException; message: Unsupported JWS algorithm: ");
        } catch (java.lang.IllegalArgumentException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(jWSAlgorithm1);
        org.junit.Assert.assertTrue("'" + requirement2 + "' != '" + com.nimbusds.jose.Requirement.OPTIONAL + "'", requirement2.equals(com.nimbusds.jose.Requirement.OPTIONAL));
    }

    @Test
    public void test153() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS00.test153");
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm1 = com.nimbusds.jose.JWSAlgorithm.parse("Ed448");
        org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier3 = null; // flaky "67) test153(AccessTokenVerifierRandoopRegressionS00)": new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm1, "[ES384, ES256, Ed25519, Ed448, HS384, EdDSA, HS256]");
        com.nimbusds.jose.jca.JCAContext jCAContext4 = null; // flaky "55) test153(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.getJCAContext();
        com.nimbusds.jose.jca.JCAContext jCAContext5 = null; // flaky "35) test153(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.getJCAContext();
        com.nimbusds.jose.jca.JCAContext jCAContext6 = null; // flaky "30) test153(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.getJCAContext();
        java.util.Set<com.nimbusds.jose.JWSAlgorithm> jWSAlgorithmSet7 = null; // flaky "27) test153(AccessTokenVerifierRandoopRegressionS00)": accessTokenJWSVerifier3.supportedJWSAlgorithms();
        java.util.stream.Stream<com.nimbusds.jose.JWSAlgorithm> jWSAlgorithmStream8 = null; // flaky "26) test153(AccessTokenVerifierRandoopRegressionS00)": jWSAlgorithmSet7.parallelStream();
        org.junit.Assert.assertNotNull(jWSAlgorithm1);
// flaky "25) test153(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jCAContext4);
// flaky "21) test153(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jCAContext5);
// flaky "20) test153(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jCAContext6);
// flaky "20) test153(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jWSAlgorithmSet7);
// flaky "20) test153(AccessTokenVerifierRandoopRegressionS00)":         org.junit.Assert.assertNotNull(jWSAlgorithmStream8);
    }
}
