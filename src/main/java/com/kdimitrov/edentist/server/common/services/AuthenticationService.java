package com.kdimitrov.edentist.server.common.services;

import org.springframework.http.ResponseEntity;

import javax.naming.AuthenticationException;
import java.util.concurrent.Callable;

public interface AuthenticationService {

    <T> ResponseEntity<T> withToken(Callable<T> callable, String token);

    <T> ResponseEntity<T> withSecret(Callable<T> callable, String secret, boolean genJwt);
}