/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0.
 */
package org.apache.syncope.core.spring.security.jws;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.util.Base64URL;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Semplice test di integrazione tra il signer
 * e il verifier di Apache Syncope.
 *
 * Non deve essere contato tra i test di unità
 * della sola classe AccessTokenJWSVerifier.
 */
class AccessTokenJWSSignerVerifierIntegrationTest {

    /**
     * Verifica che una firma generata dal signer Syncope
     * sia accettata dal verifier Syncope configurato
     * con lo stesso algoritmo e la stessa chiave.
     */
    @Test
    void hmacRoundTrip() throws Exception {
        String key =
                "12345678901234567890123456789012";

        byte[] signingInput =
                "header.payload".getBytes(
                        StandardCharsets.US_ASCII);

        JWSHeader header =
                new JWSHeader.Builder(
                        JWSAlgorithm.HS256).
                        build();

        AccessTokenJWSSigner signer =
                new AccessTokenJWSSigner(
                        JWSAlgorithm.HS256,
                        key);

        AccessTokenJWSVerifier verifier =
                new AccessTokenJWSVerifier(
                        JWSAlgorithm.HS256,
                        key);

        Base64URL signature =
                signer.sign(
                        header,
                        signingInput);

        assertTrue(verifier.verify(
                header,
                signingInput,
                signature));
    }
}