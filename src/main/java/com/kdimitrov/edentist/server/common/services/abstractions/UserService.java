package com.kdimitrov.edentist.server.common.services.abstractions;

import com.kdimitrov.edentist.server.common.exceptions.NotFound;
import com.kdimitrov.edentist.server.common.models.Entity;

import java.util.Optional;

public interface UserService<T extends Entity> {

    T findByEmail(String email) throws NotFound;

    Optional<T> findByEmailOpt(String email);
}
