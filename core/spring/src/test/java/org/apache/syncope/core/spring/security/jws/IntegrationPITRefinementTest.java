package org.apache.syncope.core.spring.security.jws;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.util.Base64URL;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.util.Base64;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Raffinamento black-box del test di integrazione tra AccessTokenJWSSigner
 * e AccessTokenJWSVerifier.
 *
 * I casi derivano esclusivamente da categorie pubbliche già considerate:
 * famiglia HMAC/RSA, relazione tra le chiavi e caratteristiche del
 * signing input. Vengono selezionati pochi casi rappresentativi senza
 * esplorare sistematicamente tutte le configurazioni supportate.
 */
class AccessTokenJWSSignerVerifierIntegrationBlackBoxRefinementTest {

    private static final String HS256_KEY =
            "0123456789abcdef0123456789abcdef";

    private static final String HS256_OTHER_KEY =
            "fedcba9876543210fedcba9876543210";

    private static final String HS384_KEY =
            "0123456789abcdef0123456789abcdef0123456789abcdef";

    private static final String HS512_KEY =
            "0123456789abcdef0123456789abcdef"
            + "0123456789abcdef0123456789abcdef";

    private static String rsaKey;
    private static String otherRsaKey;

    @BeforeAll
    static void prepareRsaMaterial() throws Exception {
        rsaKey = generateRsaKeyPair();
        otherRsaKey = generateRsaKeyPair();
    }




    @Test
    void hmacRoundTripWithDifferentVerifierKeyIsRejected() throws Exception {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);
        byte[] signingInput = bytes("header.payload");

        AccessTokenJWSSigner signer =
                new AccessTokenJWSSigner(JWSAlgorithm.HS256, HS256_KEY);

        AccessTokenJWSVerifier verifier =
                new AccessTokenJWSVerifier(JWSAlgorithm.HS256, HS256_OTHER_KEY);

        Base64URL signature = signer.sign(header, signingInput);

        assertNotNull(signature);
        assertFalse(verifier.verify(header, signingInput, signature));
    }



    @Test
    void hmacEmptySigningInputRoundTripIsAccepted() throws Exception {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);
        byte[] signingInput = new byte[0];

        AccessTokenJWSSigner signer =
                new AccessTokenJWSSigner(JWSAlgorithm.HS256, HS256_KEY);

        AccessTokenJWSVerifier verifier =
                new AccessTokenJWSVerifier(JWSAlgorithm.HS256, HS256_KEY);

        Base64URL signature = signer.sign(header, signingInput);

        assertNotNull(signature);
        assertTrue(verifier.verify(header, signingInput, signature));
    }

    @Test
    void rs256RoundTripIsAccepted() throws Exception {
        assertSuccessfulRoundTrip(
                JWSAlgorithm.RS256,
                rsaKey,
                "header.rs256.payload");
    }




    @Test
    void rsaRoundTripWithDifferentVerifierKeyIsRejected() throws Exception {
        JWSHeader header = new JWSHeader(JWSAlgorithm.RS256);
        byte[] signingInput = bytes("header.rsa.payload");

        AccessTokenJWSSigner signer =
                new AccessTokenJWSSigner(JWSAlgorithm.RS256, rsaKey);

        AccessTokenJWSVerifier verifier =
                new AccessTokenJWSVerifier(JWSAlgorithm.RS256, otherRsaKey);

        Base64URL signature = signer.sign(header, signingInput);

        assertNotNull(signature);
        assertFalse(verifier.verify(header, signingInput, signature));
    }



    private static void assertSuccessfulRoundTrip(
            final JWSAlgorithm algorithm,
            final String key,
            final String input) throws Exception {

        JWSHeader header = new JWSHeader(algorithm);
        byte[] signingInput = bytes(input);

        AccessTokenJWSSigner signer =
                new AccessTokenJWSSigner(algorithm, key);

        AccessTokenJWSVerifier verifier =
                new AccessTokenJWSVerifier(algorithm, key);

        Base64URL signature = signer.sign(header, signingInput);

        assertNotNull(signature);
        assertTrue(verifier.verify(header, signingInput, signature));
    }

    private static String generateRsaKeyPair() throws Exception {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);

        KeyPair pair = generator.generateKeyPair();

        String privateKey = Base64.getEncoder().encodeToString(
                pair.getPrivate().getEncoded());

        String publicKey = Base64.getEncoder().encodeToString(
                pair.getPublic().getEncoded());

        return privateKey + ":" + publicKey;
    }

    private static byte[] bytes(final String value) {
        return value.getBytes(StandardCharsets.UTF_8);
    }
}
