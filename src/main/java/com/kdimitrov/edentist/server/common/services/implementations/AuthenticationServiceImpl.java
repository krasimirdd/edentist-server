package com.kdimitrov.edentist.server.common.services.implementations;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.kdimitrov.edentist.app.config.ApplicationConfig;
import com.kdimitrov.edentist.server.common.exceptions.NotFound;
import com.kdimitrov.edentist.server.common.exceptions.OperationUnsuccessful;
import com.kdimitrov.edentist.server.common.services.abstractions.AuthenticationService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import java.util.Arrays;
import java.util.concurrent.Callable;

import static com.kdimitrov.edentist.server.common.utils.Routes.AUTHORIZATION;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private static final String ISSUER = "edentist-server";

    private final byte[] secret;
    private final Algorithm algorithm;
    private final JWTVerifier verifier;

    public AuthenticationServiceImpl(ApplicationConfig config) {
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

    protected void validateToken(String token) throws AuthenticationException {
        try {
            if (StringUtils.isEmpty(token)) {
                throw new AuthenticationException("Authentication header is missing!");
            }
            verifier.verify(token);
        } catch (JWTVerificationException e) {
            throw new AuthenticationException(e.getLocalizedMessage());
        }
    }

    private void validateSecret(String secret) throws AuthenticationException {
        if (StringUtils.isEmpty(secret) || !Arrays.equals(this.secret, secret.getBytes())) {
            throw new AuthenticationException("Authentication header error!");
        }
    }

    @Override
    public <T> ResponseEntity<T> withToken(Callable<T> callable, String token) {
        try {
            validateToken(token);
            return new ResponseEntity<>(callable.call(), HttpStatus.OK);
        } catch (AuthenticationException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (NotFound | OperationUnsuccessful e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public <T> ResponseEntity<T> withSecret(Callable<T> callable, String secret, boolean genJwt) {
        try {
            validateSecret(secret);
            return genJwt
                   ? ResponseEntity.ok()
                           .header(AUTHORIZATION, generateJwt())
                           .body(callable.call())
                   : ResponseEntity.ok()
                           .body(callable.call());
        } catch (AuthenticationException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (NotFound | OperationUnsuccessful e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
