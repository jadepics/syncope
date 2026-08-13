package org.apache.syncope.core.spring.security.jws.LLM;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.util.Base64URL;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.util.Base64;
import java.util.Set;

import org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TestLLMFewShotAccessTokenJWSVerifier {

    /*
     * Chiave HMAC valida composta da 32 caratteri ASCII,
     * necessaria per testare la famiglia HMAC_SHA.
     */
    private static final String VALID_HMAC_KEY =
            "0123456789abcdef0123456789abcdef";

    /*
     * Stringa chiave RSA generata a runtime per garantire
     * un formato X.509 valido codificato in Base64.
     */
    private static String validRsaKeyString;

    /*
     * Esempio di input di firma (header + payload).
     */
    private static final byte[] SIGNING_INPUT =
            "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0In0".
                    getBytes(StandardCharsets.US_ASCII);

    private AccessTokenJWSVerifier hmacVerifier;

    @BeforeAll
    static void generateRSAKeys() throws Exception {
        // Genera una vera coppia di chiavi RSA per testare la decodifica X.509
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(2048);
        KeyPair kp = kpg.generateKeyPair();

        String publicKeyBase64 = Base64.getEncoder().encodeToString(kp.getPublic().getEncoded());

        // Formatta la chiave come richiesto dal costruttore: "private:public"
        validRsaKeyString = "dummyPrivateKey:" + publicKeyBase64;
    }

    @BeforeEach
    void setUp() throws Exception {
        // Inizializza un verificatore HMAC di base per i test di utilità
        hmacVerifier = new AccessTokenJWSVerifier(
                JWSAlgorithm.HS256,
                VALID_HMAC_KEY);
    }

    /**
     * Verifica la corretta inizializzazione quando viene
     * fornito un algoritmo RSA e una chiave nel formato corretto.
     */
    @Test
    void constructWithRSAAndValidKeyFormat() {
        AccessTokenJWSVerifier result = assertDoesNotThrow(
                () -> new AccessTokenJWSVerifier(
                        JWSAlgorithm.RS256,
                        validRsaKeyString));

        assertNotNull(result);
    }

    /**
     * Verifica che il costruttore lanci un'eccezione se la famiglia
     * di algoritmi è RSA ma manca il separatore ':' nella chiave.
     */
    @Test
    void constructWithRSAAndMissingColonThrowsException() {
        String invalidRsaKey = "invalidKeyWithoutColon";

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new AccessTokenJWSVerifier(
                        JWSAlgorithm.RS256,
                        invalidRsaKey));

        assertTrue(exception.getMessage().contains("A key pair is required"));
    }

    /**
     * Verifica la corretta inizializzazione per la famiglia
     * HMAC_SHA quando la chiave fornita rispetta i requisiti.
     */
    @Test
    void constructWithHMACAndValidKey() {
        assertDoesNotThrow(
                () -> new AccessTokenJWSVerifier(
                        JWSAlgorithm.HS512,
                        VALID_HMAC_KEY + VALID_HMAC_KEY)); // HS512 richiede chiavi più lunghe
    }

    /**
     * Verifica che venga lanciata un'eccezione per algoritmi non supportati
     * (es. famiglia EdDSA) che non rientrano né in RSA né in HMAC.
     */
    @Test
    void constructWithUnsupportedAlgorithmThrowsException() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new AccessTokenJWSVerifier(
                        JWSAlgorithm.EdDSA,
                        VALID_HMAC_KEY));

        assertTrue(exception.getMessage().contains("Unsupported JWS algorithm"));
    }

    /**
     * Verifica che la delega del metodo supportedJWSAlgorithms
     * funzioni correttamente e restituisca gli algoritmi attesi.
     */
    @Test
    void supportedAlgorithmsContainsInitializedAlgorithm() {
        Set<JWSAlgorithm> algorithms = hmacVerifier.supportedJWSAlgorithms();

        assertNotNull(algorithms);
        assertTrue(algorithms.contains(JWSAlgorithm.HS256));
        assertFalse(algorithms.isEmpty());
    }

    /**
     * Verifica che il metodo getJCAContext deleghi correttamente
     * e non restituisca un valore nullo.
     */
    @Test
    void getJcaContextReturnsNonNullDelegate() {
        assertNotNull(hmacVerifier.getJCAContext());
    }

    /**
     * Verifica che il metodo di utilità verify() deleghi il calcolo
     * e valuti correttamente una firma valida.
     */
    @Test
    void verifyReturnsTrueForValidSignature() throws Exception {
        JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.HS256).build();
        MACSigner signer = new MACSigner(VALID_HMAC_KEY);
        Base64URL validSignature = signer.sign(header, SIGNING_INPUT);

        assertTrue(
                hmacVerifier.verify(
                        header,
                        SIGNING_INPUT,
                        validSignature));
    }

    /**
     * Verifica che il metodo verify() intercetti correttamente
     * una firma non valida (es. generata con una chiave diversa).
     */
    @Test
    void verifyReturnsFalseForInvalidSignature() throws Exception {
        JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.HS256).build();
        String differentKey = "abcdef0123456789abcdef0123456789";
        MACSigner invalidSigner = new MACSigner(differentKey);
        Base64URL invalidSignature = invalidSigner.sign(header, SIGNING_INPUT);

        assertFalse(
                hmacVerifier.verify(
                        header,
                        SIGNING_INPUT,
                        invalidSignature));
    }
}
