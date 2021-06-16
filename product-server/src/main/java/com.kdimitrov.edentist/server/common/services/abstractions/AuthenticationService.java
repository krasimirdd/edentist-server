package com.kdimitrov.edentist.server.common.services.abstractions;

import org.springframework.http.ResponseEntity;

import javax.naming.AuthenticationException;
import java.util.Optional;
import java.util.concurrent.Callable;

public interface AuthenticationService {

    <T> ResponseEntity<T> withToken(Callable<T> callable, String token, Optional<String> claim);

    <T> ResponseEntity<T> withSecret(Callable<T> callable, String secret, boolean genJwt);

}