package com.kdimitrov.edentist.server.common.services.implementations;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.kdimitrov.edentist.server.config.ApplicationConfig;
import com.kdimitrov.edentist.server.common.exceptions.NotFound;
import com.kdimitrov.edentist.server.common.exceptions.OperationUnsuccessful;
import com.kdimitrov.edentist.server.common.services.abstractions.AuthenticationService;
import com.kdimitrov.edentist.server.common.utils.CryptoUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;
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

    public <T> String generateJwt(T t) {
        JSONObject jsonObject = new JSONObject((String) t);
        String roleClaim = jsonObject.getString("role");

        return JWT.create()
                .withIssuer(ISSUER)
                .withClaim("role", roleClaim)
                .sign(algorithm);
    }

    protected void validateToken(String token, Optional<String> claim) throws AuthenticationException {
        try {
            if (StringUtils.isEmpty(token)) {
                throw new AuthenticationException("Authentication header is missing!");
            }

            DecodedJWT decodedJWT = verifier.verify(token);
            claim.ifPresent(s -> {
                Claim roleClaim = decodedJWT.getClaim("role");
                if (roleClaim.isNull() || !s.equals(roleClaim.asString()))
                    throw new JWTVerificationException("Wrong claims found!");
            });
        } catch (JWTVerificationException e) {
            throw new AuthenticationException(e.getLocalizedMessage());
        }
    }

    private void validateSecret(String secret) throws AuthenticationException {

        if (StringUtils.isEmpty(secret)) {
            throw new AuthenticationException("Authentication header is missing!");
        }

        try {
            if (!CryptoUtils.getSecretHash().equalsIgnoreCase(secret)) {
                throw new AuthenticationException("Authentication header error!");
            }
        } catch (NoSuchAlgorithmException e) {
            throw new AuthenticationException("Authentication header error!");
        }
    }

    @Override
    public <T> ResponseEntity<T> withToken(Callable<T> callable, String token, Optional<String> claim) {
        try {
            validateToken(token, claim);
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
    public <T> ResponseEntity<T> withSecret(Callable<T> callable, String secret, boolean shouldGenJwt) {
        try {
            validateSecret(secret);

            T callableResult = callable.call();
            return shouldGenJwt
                   ? ResponseEntity.ok()
                           .header(AUTHORIZATION, generateJwt(callableResult))
                           .body(callableResult)
                   : ResponseEntity.ok()
                           .body(callableResult);
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
