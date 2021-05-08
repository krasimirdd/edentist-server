package com.kdimitrov.edentist.server.common.services;

import javax.naming.AuthenticationException;

public interface AuthenticationService {

    void validateToken(String token) throws AuthenticationException;
}