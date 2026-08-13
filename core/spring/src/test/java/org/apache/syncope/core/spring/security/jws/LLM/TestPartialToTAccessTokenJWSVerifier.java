package org.apache.syncope.core.spring.security.jws.LLM;

import static org.junit.jupiter.api.Assertions.*;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.jca.JCAContext;
import com.nimbusds.jose.util.Base64URL;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Set;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier;

public class TestPartialToTAccessTokenJWSVerifier {

    private static String validRsaKeyString;
    private static final String VALID_HMAC_KEY = "12345678901234567890123456789012"; // 32 chars required for MACVerifier

    @BeforeAll
    public static void setupRsaKey() throws NoSuchAlgorithmException {
        // Generate a valid RSA public key to test the RSASSAVerifier initialization properly
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        KeyPair pair = keyGen.generateKeyPair();
        String base64PublicKey = Base64.getEncoder().encodeToString(pair.getPublic().getEncoded());

        // Format required by the class: "private:public"
        validRsaKeyString = "dummyPrivateKeyData:" + base64PublicKey;
    }

    @Test
    @DisplayName("Should successfully instantiate with HMAC algorithm and test delegations")
    public void testHmacInstantiationAndDelegation() throws Exception {

        AccessTokenJWSVerifier verifier = new AccessTokenJWSVerifier(JWSAlgorithm.HS256, VALID_HMAC_KEY);

        // Test supportedJWSAlgorithms delegation
        Set<JWSAlgorithm> algorithms = verifier.supportedJWSAlgorithms();
        assertNotNull(algorithms);
        assertTrue(algorithms.contains(JWSAlgorithm.HS256));

        // Test getJCAContext delegation
        JCAContext jcaContext = verifier.getJCAContext();
        assertNotNull(jcaContext);

        // Test verify delegation (using a real header to avoid JOSEException for null algorithm)
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);
        byte[] signingInput = "dummyInput".getBytes();
        Base64URL dummySignature = new Base64URL("dummySig");

        boolean isValid = verifier.verify(header, signingInput, dummySignature);
        assertFalse(isValid, "Verification should fail for dummy data but not throw unexpected exceptions");
    }

    @Test
    @DisplayName("Should successfully instantiate with RSA algorithm")
    public void testRsaInstantiation() throws Exception {
        AccessTokenJWSVerifier verifier = new AccessTokenJWSVerifier(JWSAlgorithm.RS256, validRsaKeyString);

        Set<JWSAlgorithm> algorithms = verifier.supportedJWSAlgorithms();
        assertNotNull(algorithms);
        assertTrue(algorithms.contains(JWSAlgorithm.RS256));
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when RSA key lacks colon")
    public void testRsaMissingColonEdgeCase() {
        String invalidRsaKey = "justSomeKeyDataWithoutColon";

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new AccessTokenJWSVerifier(JWSAlgorithm.RS256, invalidRsaKey)
        );

        assertTrue(exception.getMessage().contains("A key pair is required, in the 'private:public' format"));
    }

    @Test
    @DisplayName("Should throw InvalidKeySpecException or IllegalArgumentException on malformed Base64 RSA key")
    public void testRsaInvalidKeySpecEdgeCase() {
        String malformedRsaKey = "privateKey:NotAValidBase64EncodedX509Key!!";

        assertThrows(
                Exception.class, // May throw IllegalArgumentException (Base64 decode) or InvalidKeySpecException
                () -> new AccessTokenJWSVerifier(JWSAlgorithm.RS256, malformedRsaKey)
        );
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException for unsupported JWS algorithm")
    public void testUnsupportedAlgorithmEdgeCase() {
        // ES256 is an Elliptic Curve algorithm, not supported by the RSA or HMAC logic in the class
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new AccessTokenJWSVerifier(JWSAlgorithm.ES256, "someKey")
        );

        assertTrue(exception.getMessage().contains("Unsupported JWS algorithm"));
    }
}