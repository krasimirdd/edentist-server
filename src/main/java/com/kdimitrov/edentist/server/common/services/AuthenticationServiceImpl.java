package com.kdimitrov.edentist.server.common.services;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.kdimitrov.edentist.app.config.ApplicationConfig;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import java.util.Arrays;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private static final String ISSUER = "edentist-server";

    private final byte[] secret;
    private final ApplicationConfig config;
    private final Algorithm algorithm;
    private final JWTVerifier verifier;

    public AuthenticationServiceImpl(ApplicationConfig config) {
        this.config = config;
        this.secret = config.getSecret();
        this.algorithm = Algorithm.HMAC256(secret);
        this.verifier = JWT.require(algorithm)
                .withIssuer(ISSUER)
                .build();
    }

    public String generateJwt() {
        return JWT.create()
                .withIssuer(ISSUER)
                .sign(algorithm);
    }

    @Override
    public void validateToken(String token) throws AuthenticationException {
        try {
            if (StringUtils.isEmpty(token)) {
                throw new AuthenticationException("Authentication header is missing!");
            }
            verifier.verify(token);
        } catch (JWTVerificationException e) {
            throw new AuthenticationException(e.getLocalizedMessage());
        }
    }

    public void validateSecret(String secret) throws AuthenticationException {
        if (StringUtils.isEmpty(secret) || !Arrays.equals(this.secret, secret.getBytes())) {
            throw new AuthenticationException("Authentication header error!");
        }
    }
}
