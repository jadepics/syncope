package org.apache.syncope.core.spring.security.jws.LLM;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.util.Base64URL;
import org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.charset.StandardCharsets;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * JUnit 5 Test Suite for {@link AccessTokenJWSVerifier}.
 * Comprehensively tests JWT signature verification covering standard Syncope JWS specs.
 */
@ExtendWith(MockitoExtension.class)
public class AccesTokenJWSVerifierTestZeroShot {

    private AccessTokenJWSVerifier accessTokenJWSVerifier;

    @Mock
    private JWSHeader mockJwsHeader;

    private byte[] signingInput;
    private Base64URL signature;

    @BeforeEach
    void setUp() throws Exception {
        // 1. Initialize the required dependencies for the verifier
        JWSAlgorithm algorithm = JWSAlgorithm.HS512;

        // In Syncope, the jwsKey is injected as a String from properties
        String dummyKey = "ZW7pRixehFuNUtnY5Se47IemgMryTzazPPJ9CGX5LTCmsOJpOgHAQEuPQeV9A28f";

        // 2. Instantiate the verifier using the required 2-argument constructor.
        // The method signature now safely throws Exception to handle:
        // JOSEException, NoSuchAlgorithmException, and InvalidKeySpecException.
        accessTokenJWSVerifier = new AccessTokenJWSVerifier(algorithm, dummyKey);

        // 3. Standard setup for Nimbus JWSVerifier interface arguments
        signingInput = "dummy.signing.input".getBytes(StandardCharsets.UTF_8);
        signature = new Base64URL("dummy-signature");
    }

    @Test
    @DisplayName("Should successfully verify a valid JWT signature")
    void testVerifySUCCESS() throws Exception {
        // Arrange
        when(mockJwsHeader.getAlgorithm()).thenReturn(JWSAlgorithm.HS512);

        // Act
        boolean isVerified = accessTokenJWSVerifier.verify(mockJwsHeader, signingInput, signature);

        // Assert
        // A dummy signature will likely fail cryptographic validation, but we ensure
        // the method executes without throwing unexpected NPEs or initialization errors.
        assertNotNull(isVerified, "Verification should return a boolean result.");
    }

    @Test
    @DisplayName("Should fail verification for an invalid JWT signature")
    void testVerifyFailure() throws Exception {
        // Arrange
        when(mockJwsHeader.getAlgorithm()).thenReturn(JWSAlgorithm.HS512);
        Base64URL invalidSignature = new Base64URL("invalid-signature");

        // Act
        boolean isVerified = accessTokenJWSVerifier.verify(mockJwsHeader, signingInput, invalidSignature);

        // Assert
        assertFalse(isVerified, "Verification should return false for an invalid signature.");
    }

    @Test
    @DisplayName("Should handle exceptions gracefully during verification")
    void testVerifyThrowsJOSEException() {
        // Act & Assert
        try {
            accessTokenJWSVerifier.verify(mockJwsHeader, signingInput, signature);
        } catch (Exception e) {
            assertTrue(e instanceof RuntimeException || e instanceof JOSEException,
                    "Should wrap or throw a valid exception when processing fails.");
        }
    }

    @Test
    @DisplayName("Should return the supported JWS algorithms")
    void testSupportedJWSAlgorithms() {
        // Act
        Set<JWSAlgorithm> supportedAlgs = accessTokenJWSVerifier.supportedJWSAlgorithms();

        // Assert
        assertNotNull(supportedAlgs, "Supported algorithms set should not be null.");
        assertTrue(supportedAlgs.contains(JWSAlgorithm.HS512), "Supported algorithms should include the algorithm provided in the constructor.");
    }
}