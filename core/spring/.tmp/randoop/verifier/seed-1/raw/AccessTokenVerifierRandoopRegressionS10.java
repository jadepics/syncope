import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AccessTokenVerifierRandoopRegressionS10 {

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
    public void test1() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS10.test1");
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm0 = null;
        // The following exception was thrown during execution in test generation
        try {
            org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier2 = new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm0, "hi!");
            org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException; message: Cannot invoke \"com.nimbusds.jose.JWSAlgorithm.getName()\" because \"jwsAlgorithm\" is null");
        } catch (java.lang.NullPointerException e) {
            // Expected exception.
        }
    }

    @Test
    public void test2() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS10.test2");
        com.nimbusds.jose.JWSAlgorithm jWSAlgorithm0 = null;
        // The following exception was thrown during execution in test generation
        try {
            org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier accessTokenJWSVerifier2 = new org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier(jWSAlgorithm0, "");
            org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException; message: Cannot invoke \"com.nimbusds.jose.JWSAlgorithm.getName()\" because \"jwsAlgorithm\" is null");
        } catch (java.lang.NullPointerException e) {
            // Expected exception.
        }
    }

    @Test
    public void test3() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "AccessTokenVerifierRandoopRegressionS10.test3");
        java.lang.Object obj0 = new java.lang.Object();
        java.lang.Class<?> wildcardClass1 = obj0.getClass();
        org.junit.Assert.assertNotNull(wildcardClass1);
    }
}

