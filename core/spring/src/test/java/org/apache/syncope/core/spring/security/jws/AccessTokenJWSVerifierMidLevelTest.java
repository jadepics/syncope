package org.apache.syncope.core.spring.security.jws;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.KeyLengthException;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.util.Base64URL;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test manuali di livello intermedio per AccessTokenJWSVerifier.
 *
 * Non viene utilizzato Mockito perché firme, chiavi e verificatori
 * possono essere creati direttamente con Nimbus e con la JCA.
 */
class AccessTokenJWSVerifierMidLevelTest {

    /**
     * Contenuto comune firmato nei test.
     */
    private static final byte[] SIGNING_INPUT =
            "header.payload".getBytes(StandardCharsets.US_ASCII);

    /**
     * Prima coppia RSA, utilizzata nei casi nominali.
     */
    private static KeyPair rsaKeyPair;

    /**
     * Seconda coppia RSA, utilizzata per verificare
     * il comportamento con una chiave pubblica differente.
     */
    private static KeyPair differentRSAKeyPair;

    /**
     * Genera una sola volta le due coppie RSA.
     *
     * In questo modo non si ripete la stessa configurazione
     * all'interno di ogni test.
     */
    @BeforeAll
    static void generateRSAKeys() throws Exception {
        KeyPairGenerator generator =
                KeyPairGenerator.getInstance("RSA");

        generator.initialize(2048);

        rsaKeyPair = generator.generateKeyPair();
        differentRSAKeyPair = generator.generateKeyPair();
    }

    /**
     * Verifica costruzione e firma valida con HS384.
     */
    @Test
    void constructAndVerifyWithHS384() throws Exception {
        String key = asciiKey(48);

        assertTrue(verifyHMAC(
                JWSAlgorithm.HS384,
                key,
                SIGNING_INPUT));
    }

    /**
     * Verifica costruzione e firma valida con HS512.
     */
    @Test
    void constructAndVerifyWithHS512() throws Exception {
        String key = asciiKey(64);

        assertTrue(verifyHMAC(
                JWSAlgorithm.HS512,
                key,
                SIGNING_INPUT));
    }

    /**
     * Boundary per HS256:
     *
     * - 31 byte: chiave troppo corta;
     * - 32 byte: lunghezza minima valida;
     * - 33 byte: lunghezza superiore al minimo.
     */
    @Test
    void hs256KeyBoundary() throws Exception {
        assertThrows(
                KeyLengthException.class,
                () -> new AccessTokenJWSVerifier(
                        JWSAlgorithm.HS256,
                        asciiKey(31)));

        assertTrue(verifyHMAC(
                JWSAlgorithm.HS256,
                asciiKey(32),
                SIGNING_INPUT));

        assertTrue(verifyHMAC(
                JWSAlgorithm.HS256,
                asciiKey(33),
                SIGNING_INPUT));
    }

    /**
     * Boundary per HS384.
     *
     * La costruzione con 47 byte riesce perché MACVerifier
     * richiede genericamente almeno 32 byte. La verifica HS384
     * viene però rifiutata perché il suo header richiede
     * almeno 48 byte.
     */
    @Test
    void hs384KeyBoundary() throws Exception {
        AccessTokenJWSVerifier verifierWith47Bytes =
                new AccessTokenJWSVerifier(
                        JWSAlgorithm.HS384,
                        asciiKey(47));

        Base64URL dummySignature =
                Base64URL.encode(new byte[48]);

        assertThrows(
                KeyLengthException.class,
                () -> verifierWith47Bytes.verify(
                        header(JWSAlgorithm.HS384),
                        SIGNING_INPUT,
                        dummySignature));

        assertTrue(verifyHMAC(
                JWSAlgorithm.HS384,
                asciiKey(48),
                SIGNING_INPUT));

        assertTrue(verifyHMAC(
                JWSAlgorithm.HS384,
                asciiKey(49),
                SIGNING_INPUT));
    }

    /**
     * Boundary per HS512.
     *
     * La chiave di 63 byte permette di costruire il delegate
     * HMAC, ma non è sufficiente per verificare HS512.
     */
    @Test
    void hs512KeyBoundary() throws Exception {
        AccessTokenJWSVerifier verifierWith63Bytes =
                new AccessTokenJWSVerifier(
                        JWSAlgorithm.HS512,
                        asciiKey(63));

        Base64URL dummySignature =
                Base64URL.encode(new byte[64]);

        assertThrows(
                KeyLengthException.class,
                () -> verifierWith63Bytes.verify(
                        header(JWSAlgorithm.HS512),
                        SIGNING_INPUT,
                        dummySignature));

        assertTrue(verifyHMAC(
                JWSAlgorithm.HS512,
                asciiKey(64),
                SIGNING_INPUT));

        assertTrue(verifyHMAC(
                JWSAlgorithm.HS512,
                asciiKey(65),
                SIGNING_INPUT));
    }

    /**
     * Verifica un header HMAC differente dall'algoritmo
     * passato al costruttore.
     *
     * La chiave è lunga 64 byte e permette quindi a MACVerifier
     * di gestire HS256, HS384 e HS512.
     *
     * L'implementazione corrente non conserva HS256 come unico
     * algoritmo ammesso: il parametro serve soltanto a scegliere
     * la famiglia HMAC. La firma HS384 viene quindi accettata.
     */
    @Test
    void differentHMACHeaderIsAcceptedWhenKeySupportsIt()
            throws Exception {

        String key = asciiKey(64);

        AccessTokenJWSVerifier verifier =
                new AccessTokenJWSVerifier(
                        JWSAlgorithm.HS256,
                        key);

        JWSHeader hs384Header =
                header(JWSAlgorithm.HS384);

        Base64URL signature =
                new MACSigner(key).sign(
                        hs384Header,
                        SIGNING_INPUT);

        assertTrue(verifier.verify(
                hs384Header,
                SIGNING_INPUT,
                signature));
    }

    /**
     * Un verifier HMAC non può verificare
     * un header appartenente alla famiglia RSA.
     */
    @Test
    void rsaHeaderOnHMACVerifier() throws Exception {
        AccessTokenJWSVerifier verifier =
                new AccessTokenJWSVerifier(
                        JWSAlgorithm.HS256,
                        asciiKey(32));

        JWSHeader rsaHeader =
                header(JWSAlgorithm.RS256);

        Base64URL rsaSignature =
                signRSA(
                        JWSAlgorithm.RS256,
                        rsaKeyPair,
                        SIGNING_INPUT);

        assertThrows(
                JOSEException.class,
                () -> verifier.verify(
                        rsaHeader,
                        SIGNING_INPUT,
                        rsaSignature));
    }

    /**
     * Una firma vuota deve essere respinta.
     */
    @Test
    void emptySignature() throws Exception {
        AccessTokenJWSVerifier verifier =
                new AccessTokenJWSVerifier(
                        JWSAlgorithm.HS256,
                        asciiKey(32));

        Base64URL emptySignature =
                Base64URL.encode(new byte[0]);

        assertFalse(verifier.verify(
                header(JWSAlgorithm.HS256),
                SIGNING_INPUT,
                emptySignature));
    }

    /**
     * Una firma HMAC con una lunghezza non corretta
     * deve essere respinta.
     */
    @Test
    void signatureWithIncorrectLength() throws Exception {
        AccessTokenJWSVerifier verifier =
                new AccessTokenJWSVerifier(
                        JWSAlgorithm.HS256,
                        asciiKey(32));

        Base64URL shortSignature =
                Base64URL.encode(new byte[10]);

        assertFalse(verifier.verify(
                header(JWSAlgorithm.HS256),
                SIGNING_INPUT,
                shortSignature));
    }

    /**
     * Verifica la costruzione con RS256
     * e una coppia RSA valida.
     */
    @Test
    void constructWithRS256AndValidKeyPair() {
        assertDoesNotThrow(
                () -> assertRSAConstruction(
                        JWSAlgorithm.RS256));
    }

    /**
     * Verifica la costruzione con RS384
     * e una coppia RSA valida.
     */
    @Test
    void constructWithRS384AndValidKeyPair() {
        assertDoesNotThrow(
                () -> assertRSAConstruction(
                        JWSAlgorithm.RS384));
    }

    /**
     * Verifica la costruzione con RS512
     * e una coppia RSA valida.
     */
    @Test
    void constructWithRS512AndValidKeyPair() {
        assertDoesNotThrow(
                () -> assertRSAConstruction(
                        JWSAlgorithm.RS512));
    }

    /**
     * Verifica nominale RSA:
     * firma e chiave pubblica appartengono
     * alla stessa coppia.
     */
    @Test
    void verifyValidRSASignature() throws Exception {
        AccessTokenJWSVerifier verifier =
                rsaVerifier(
                        JWSAlgorithm.RS256,
                        rsaKeyPair);

        Base64URL signature =
                signRSA(
                        JWSAlgorithm.RS256,
                        rsaKeyPair,
                        SIGNING_INPUT);

        assertTrue(verifier.verify(
                header(JWSAlgorithm.RS256),
                SIGNING_INPUT,
                signature));
    }

    /**
     * La firma viene prodotta con la prima coppia,
     * mentre il verifier utilizza una chiave pubblica differente.
     */
    @Test
    void rejectRSAWithDifferentPublicKey() throws Exception {
        AccessTokenJWSVerifier verifier =
                rsaVerifier(
                        JWSAlgorithm.RS256,
                        differentRSAKeyPair);

        Base64URL signature =
                signRSA(
                        JWSAlgorithm.RS256,
                        rsaKeyPair,
                        SIGNING_INPUT);

        assertFalse(verifier.verify(
                header(JWSAlgorithm.RS256),
                SIGNING_INPUT,
                signature));
    }

    /**
     * Verifica che una modifica del contenuto
     * renda non valida la firma RSA.
     */
    @Test
    void rejectRSAWithAlteredContent() throws Exception {
        AccessTokenJWSVerifier verifier =
                rsaVerifier(
                        JWSAlgorithm.RS256,
                        rsaKeyPair);

        Base64URL signature =
                signRSA(
                        JWSAlgorithm.RS256,
                        rsaKeyPair,
                        SIGNING_INPUT);

        byte[] alteredInput =
                SIGNING_INPUT.clone();

        alteredInput[0] ^= 1;

        assertFalse(verifier.verify(
                header(JWSAlgorithm.RS256),
                alteredInput,
                signature));
    }

    /**
     * Verifica che una modifica della firma RSA
     * venga rilevata.
     */
    @Test
    void rejectAlteredRSASignature() throws Exception {
        AccessTokenJWSVerifier verifier =
                rsaVerifier(
                        JWSAlgorithm.RS256,
                        rsaKeyPair);

        Base64URL validSignature =
                signRSA(
                        JWSAlgorithm.RS256,
                        rsaKeyPair,
                        SIGNING_INPUT);

        byte[] alteredBytes =
                validSignature.decode();

        alteredBytes[0] ^= 1;

        Base64URL alteredSignature =
                Base64URL.encode(alteredBytes);

        assertFalse(verifier.verify(
                header(JWSAlgorithm.RS256),
                SIGNING_INPUT,
                alteredSignature));
    }

    /**
     * Una chiave RSA senza il separatore ":" non rispetta
     * il formato private:public richiesto da Syncope.
     */
    @Test
    void rsaKeyWithoutSeparator() {
        String keyWithoutSeparator =
                publicKeyBase64(rsaKeyPair);

        assertThrows(
                IllegalArgumentException.class,
                () -> new AccessTokenJWSVerifier(
                        JWSAlgorithm.RS256,
                        keyWithoutSeparator));
    }

    /**
     * La parte pubblica è vuota.
     *
     * Il Base64 vuoto viene decodificato, ma non può
     * essere trasformato in una chiave pubblica RSA.
     */
    @Test
    void rsaKeyWithEmptyPublicPart() {
        String key =
                privateKeyBase64(rsaKeyPair) + ":";

        assertThrows(
                InvalidKeySpecException.class,
                () -> new AccessTokenJWSVerifier(
                        JWSAlgorithm.RS256,
                        key));
    }

    /**
     * La parte pubblica contiene caratteri
     * non validi per Base64.
     */
    @Test
    void rsaPublicPartNotBase64() {
        String key =
                privateKeyBase64(rsaKeyPair)
                        + ":not-base64!!";

        assertThrows(
                IllegalArgumentException.class,
                () -> new AccessTokenJWSVerifier(
                        JWSAlgorithm.RS256,
                        key));
    }

    /**
     * La parte pubblica è Base64 formalmente valida,
     * ma i byte non rappresentano una chiave X.509 RSA.
     */
    @Test
    void rsaPublicPartBase64ButNotRSA() {
        String invalidPublicPart =
                Base64.getEncoder().encodeToString(
                        "not-an-rsa-key".getBytes(
                                StandardCharsets.UTF_8));

        String key =
                privateKeyBase64(rsaKeyPair)
                        + ":"
                        + invalidPublicPart;

        assertThrows(
                InvalidKeySpecException.class,
                () -> new AccessTokenJWSVerifier(
                        JWSAlgorithm.RS256,
                        key));
    }

    /**
     * ES256 appartiene a una famiglia non gestita
     * da AccessTokenJWSVerifier.
     */
    @Test
    void unsupportedAlgorithm() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new AccessTokenJWSVerifier(
                        JWSAlgorithm.ES256,
                        asciiKey(32)));
    }

    /**
     * L'algoritmo "none" non appartiene
     * né alla famiglia HMAC né alla famiglia RSA.
     */
    @Test
    void noneAlgorithm() {
        JWSAlgorithm none =
                JWSAlgorithm.parse("none");

        assertThrows(
                IllegalArgumentException.class,
                () -> new AccessTokenJWSVerifier(
                        none,
                        asciiKey(32)));
    }

    /**
     * Un algoritmo null non permette di determinare
     * quale delegate costruire.
     *
     * L'implementazione corrente genera
     * NullPointerException.
     */
    @Test
    void nullConstructorAlgorithm() {
        assertThrows(
                NullPointerException.class,
                () -> new AccessTokenJWSVerifier(
                        null,
                        asciiKey(32)));
    }

    /**
     * Una chiave null non può essere convertita
     * nei byte richiesti da MACVerifier.
     */
    @Test
    void nullConstructorKey() {
        assertThrows(
                NullPointerException.class,
                () -> new AccessTokenJWSVerifier(
                        JWSAlgorithm.HS256,
                        null));
    }

    /**
     * Una stringa vuota equivale a una chiave HMAC
     * di zero byte e viene quindi rifiutata.
     */
    @Test
    void emptyConstructorKey() {
        assertThrows(
                KeyLengthException.class,
                () -> new AccessTokenJWSVerifier(
                        JWSAlgorithm.HS256,
                        ""));
    }

    /**
     * L'header è un parametro necessario
     * per conoscere l'algoritmo da verificare.
     */
    @Test
    void nullVerifyHeader() throws Exception {
        String key = asciiKey(32);

        AccessTokenJWSVerifier verifier =
                new AccessTokenJWSVerifier(
                        JWSAlgorithm.HS256,
                        key);

        Base64URL signature =
                signHMAC(
                        JWSAlgorithm.HS256,
                        key,
                        SIGNING_INPUT);

        assertThrows(
                NullPointerException.class,
                () -> verifier.verify(
                        null,
                        SIGNING_INPUT,
                        signature));
    }

    /**
     * Un signingInput null non genera un'eccezione.
     *
     * Il verificatore Nimbus lo tratta come assenza di contenuto.
     * Poiché la firma utilizzata è stata prodotta sul normale
     * SIGNING_INPUT, la corrispondenza crittografica deve fallire.
     */
    @Test
    void nullSigningInput() throws Exception {
        String key = asciiKey(32);

        AccessTokenJWSVerifier verifier =
                new AccessTokenJWSVerifier(
                        JWSAlgorithm.HS256,
                        key);

        /*
         * La firma viene generata sul contenuto non nullo
         * utilizzato normalmente dagli altri test.
         */
        Base64URL signature =
                signHMAC(
                        JWSAlgorithm.HS256,
                        key,
                        SIGNING_INPUT);

        /*
         * L'implementazione non solleva eccezioni,
         * ma la firma non può corrispondere all'input null.
         */
        assertFalse(verifier.verify(
                header(JWSAlgorithm.HS256),
                null,
                signature));
    }

    /**
     * Una firma null non può essere decodificata
     * e confrontata con la firma calcolata.
     */
    @Test
    void nullSignature() throws Exception {
        AccessTokenJWSVerifier verifier =
                new AccessTokenJWSVerifier(
                        JWSAlgorithm.HS256,
                        asciiKey(32));

        assertThrows(
                NullPointerException.class,
                () -> verifier.verify(
                        header(JWSAlgorithm.HS256),
                        SIGNING_INPUT,
                        null));
    }

    /**
     * Costruisce un header semplice
     * con l'algoritmo indicato.
     */
    private JWSHeader header(
            final JWSAlgorithm algorithm) {

        return new JWSHeader.Builder(algorithm).build();
    }

    /**
     * Crea una chiave ASCII della lunghezza richiesta.
     *
     * Ogni carattere ASCII occupa un byte,
     * quindi caratteri e byte coincidono nei boundary.
     */
    private static String asciiKey(
            final int numberOfBytes) {

        return "k".repeat(numberOfBytes);
    }

    /**
     * Firma un contenuto tramite HMAC Nimbus.
     */
    private Base64URL signHMAC(
            final JWSAlgorithm algorithm,
            final String key,
            final byte[] input)
            throws JOSEException {

        JWSHeader header = header(algorithm);

        return new MACSigner(key).sign(
                header,
                input);
    }

    /**
     * Costruisce signer e verifier HMAC
     * usando algoritmo, chiave e contenuto coerenti.
     */
    private boolean verifyHMAC(
            final JWSAlgorithm algorithm,
            final String key,
            final byte[] input)
            throws Exception {

        JWSHeader header = header(algorithm);

        Base64URL signature =
                new MACSigner(key).sign(
                        header,
                        input);

        AccessTokenJWSVerifier verifier =
                new AccessTokenJWSVerifier(
                        algorithm,
                        key);

        return verifier.verify(
                header,
                input,
                signature);
    }

    /**
     * Firma un contenuto tramite la chiave privata RSA.
     */
    private Base64URL signRSA(
            final JWSAlgorithm algorithm,
            final KeyPair keyPair,
            final byte[] input)
            throws JOSEException {

        RSASSASigner signer =
                new RSASSASigner(
                        (RSAPrivateKey) keyPair.getPrivate());

        return signer.sign(
                header(algorithm),
                input);
    }

    /**
     * Crea un AccessTokenJWSVerifier RSA.
     */
    private AccessTokenJWSVerifier rsaVerifier(
            final JWSAlgorithm algorithm,
            final KeyPair keyPair)
            throws Exception {

        return new AccessTokenJWSVerifier(
                algorithm,
                rsaKeyPairString(keyPair));
    }

    /**
     * Controlla che il costruttore RSA produca
     * un oggetto valido e dichiari l'algoritmo richiesto.
     */
    private void assertRSAConstruction(
            final JWSAlgorithm algorithm)
            throws Exception {

        AccessTokenJWSVerifier verifier =
                rsaVerifier(
                        algorithm,
                        rsaKeyPair);

        assertNotNull(verifier);

        assertTrue(
                verifier.supportedJWSAlgorithms().
                        contains(algorithm));
    }

    /**
     * Costruisce il formato richiesto da Syncope:
     *
     * chiave privata PKCS#8 : chiave pubblica X.509
     */
    private static String rsaKeyPairString(
            final KeyPair keyPair) {

        return privateKeyBase64(keyPair)
                + ":"
                + publicKeyBase64(keyPair);
    }

    /**
     * Codifica la chiave privata PKCS#8 in Base64.
     */
    private static String privateKeyBase64(
            final KeyPair keyPair) {

        return Base64.getEncoder().encodeToString(
                keyPair.getPrivate().getEncoded());
    }

    /**
     * Codifica la chiave pubblica X.509 in Base64.
     */
    private static String publicKeyBase64(
            final KeyPair keyPair) {

        return Base64.getEncoder().encodeToString(
                keyPair.getPublic().getEncoded());
    }
}