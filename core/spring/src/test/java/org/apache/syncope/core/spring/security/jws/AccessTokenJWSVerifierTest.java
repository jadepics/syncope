package org.apache.syncope.core.spring.security.jws;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.util.Base64URL;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AccessTokenJWSVerifierTest {

    /*
     * Chiave HMAC valida composta da 32 caratteri ASCII.
     *
     * Ogni carattere ASCII occupa un byte, quindi la chiave
     * fornisce i 32 byte richiesti come minimo per HS256.
     */
    private static final String VALID_KEY =
            "0123456789abcdef0123456789abcdef";

    /*
     * Seconda chiave valida, della stessa lunghezza,
     * ma con contenuto differente.
     *
     * Viene utilizzata per verificare che una firma prodotta
     * con una chiave diversa non venga accettata.
     */
    private static final String DIFFERENT_VALID_KEY =
            "abcdef0123456789abcdef0123456789";

    /*
     * Chiave intenzionalmente inferiore ai 32 byte minimi
     * richiesti dal verificatore HMAC.
     */
    private static final String TOO_SHORT_KEY =
            "short-key";

    /*
     * Esempio realistico di signing input JWS.
     *
     * Lo signing input corrisponde normalmente alla concatenazione:
     *
     * Base64URL(header) + "." + Base64URL(payload)
     */
    private static final byte[] SIGNING_INPUT =
            "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJtcm9zc2kifQ".
                    getBytes(StandardCharsets.US_ASCII);

    /*
     * Header JWS che dichiara l'utilizzo dell'algoritmo HS256.
     */
    private JWSHeader header;

    /*
     * Oggetto reale sottoposto a test.
     */
    private AccessTokenJWSVerifier verifier;

    /*
     * Signer Nimbus indipendente utilizzato per produrre
     * le firme da sottoporre al verificatore Syncope.
     *
     * Non viene utilizzato AccessTokenJWSSigner, così il test
     * non dipende da un'altra classe Syncope che potrebbe
     * contenere lo stesso eventuale errore.
     */
    private MACSigner signer;

    @BeforeEach
    void setUp() throws Exception {
        header = new JWSHeader.Builder(
                JWSAlgorithm.HS256).
                build();

        /*
         * Il verificatore utilizza la chiave valida principale.
         */
        verifier = new AccessTokenJWSVerifier(
                JWSAlgorithm.HS256,
                VALID_KEY);

        /*
         * Il signer usa la stessa chiave per generare
         * le firme valide dei casi nominali.
         */
        signer = new MACSigner(VALID_KEY);
    }

    /**
     * Verifica che il costruttore accetti HS256 insieme
     * a una chiave HMAC della lunghezza minima valida.
     */
    @Test
    void constructWithHS256AndValidKey() {
        AccessTokenJWSVerifier result = assertDoesNotThrow(
                () -> new AccessTokenJWSVerifier(
                        JWSAlgorithm.HS256,
                        VALID_KEY));

        assertNotNull(result);
    }

    /**
     * Verifica che una chiave HMAC troppo corta
     * venga rifiutata durante la costruzione.
     *
     * Nimbus richiede almeno 256 bit, cioè 32 byte,
     * per la famiglia HMAC utilizzata da HS256.
     */
    @Test
    void rejectTooShortHMACKey() {
        assertThrows(
                JOSEException.class,
                () -> new AccessTokenJWSVerifier(
                        JWSAlgorithm.HS256,
                        TOO_SHORT_KEY));
    }

    /**
     * Verifica che il verificatore dichiari esplicitamente
     * il supporto per l'algoritmo HS256 configurato.
     */
    @Test
    void supportedAlgorithmsContainsHS256() {
        assertTrue(
                verifier.supportedJWSAlgorithms().
                        contains(JWSAlgorithm.HS256));
    }

    /**
     * Verifica che l'insieme degli algoritmi supportati
     * contenga almeno un elemento.
     */
    @Test
    void supportedAlgorithmsIsNotEmpty() {
        assertFalse(
                verifier.supportedJWSAlgorithms().isEmpty());
    }

    /**
     * Verifica che il contesto crittografico Java
     * utilizzato dal verificatore sia disponibile.
     */
    @Test
    void jcaContextIsNotNull() {
        assertNotNull(verifier.getJCAContext());
    }

    /**
     * Verifica il caso nominale:
     *
     * - stesso algoritmo;
     * - stessa chiave;
     * - stesso signing input;
     * - firma non alterata.
     *
     * La verifica deve restituire true.
     */
    @Test
    void verifyValidHMACSignature() throws Exception {
        Base64URL signature =
                signer.sign(header, SIGNING_INPUT);

        assertTrue(
                verifier.verify(
                        header,
                        SIGNING_INPUT,
                        signature));
    }

    /**
     * Verifica che una firma prodotta con una chiave
     * differente non venga accettata.
     *
     * La seconda chiave ha comunque una lunghezza valida:
     * l'unica condizione modificata è il suo contenuto.
     */
    @Test
    void rejectSignatureCreatedWithDifferentKey()
            throws Exception {

        MACSigner differentSigner =
                new MACSigner(DIFFERENT_VALID_KEY);

        Base64URL signature =
                differentSigner.sign(
                        header,
                        SIGNING_INPUT);

        assertFalse(
                verifier.verify(
                        header,
                        SIGNING_INPUT,
                        signature));
    }

    /**
     * Verifica che una firma valida non possa essere
     * riutilizzata dopo aver modificato il signing input.
     *
     * La firma viene calcolata sul contenuto originale,
     * mentre al verificatore viene passato un input con
     * un singolo byte modificato.
     */
    @Test
    void rejectAlteredSigningInput() throws Exception {
        Base64URL signature =
                signer.sign(header, SIGNING_INPUT);

        byte[] alteredInput = SIGNING_INPUT.clone();

        /*
         * Modifica un solo byte mantenendo invariata
         * la lunghezza complessiva del messaggio.
         */
        alteredInput[alteredInput.length - 1] ^= 1;

        assertFalse(
                verifier.verify(
                        header,
                        alteredInput,
                        signature));
    }

    /**
     * Verifica che la modifica di un solo bit della firma
     * renda la firma non valida.
     */
    @Test
    void rejectAlteredSignature() throws Exception {
        Base64URL validSignature =
                signer.sign(header, SIGNING_INPUT);

        /*
         * La firma Base64URL viene decodificata nei byte
         * crittografici originali.
         */
        byte[] alteredSignatureBytes =
                validSignature.decode();

        /*
         * Viene modificato un singolo bit della firma.
         */
        alteredSignatureBytes[0] ^= 1;

        Base64URL alteredSignature =
                Base64URL.encode(alteredSignatureBytes);

        assertFalse(
                verifier.verify(
                        header,
                        SIGNING_INPUT,
                        alteredSignature));
    }

    /**
     * Verifica che una firma valida, ma generata per
     * un messaggio completamente differente, non possa
     * essere utilizzata per il messaggio atteso.
     */
    @Test
    void rejectSignatureBelongingToAnotherMessage()
            throws Exception {

        byte[] otherSigningInput =
                "completely.different.message".
                        getBytes(StandardCharsets.US_ASCII);

        Base64URL otherMessageSignature =
                signer.sign(
                        header,
                        otherSigningInput);

        assertFalse(
                verifier.verify(
                        header,
                        SIGNING_INPUT,
                        otherMessageSignature));
    }

    /**
     * Verifica che anche un signing input vuoto possa
     * essere verificato quando è stato firmato correttamente.
     *
     * Il messaggio vuoto non significa firma assente:
     * viene comunque calcolato un codice HMAC valido
     * sulla sequenza di zero byte.
     */
    @Test
    void verifyCorrectlySignedEmptyInput()
            throws Exception {

        byte[] emptySigningInput = new byte[0];

        Base64URL signature =
                signer.sign(
                        header,
                        emptySigningInput);

        assertTrue(
                verifier.verify(
                        header,
                        emptySigningInput,
                        signature));
    }
}
