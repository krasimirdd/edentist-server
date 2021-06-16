package com.kdimitrov.edentist.server.common.services.implementations;

import com.kdimitrov.edentist.server.common.exceptions.NotFound;
import com.kdimitrov.edentist.server.common.models.Doctor;
import com.kdimitrov.edentist.server.common.repository.DoctorsRepository;
import com.kdimitrov.edentist.server.common.services.abstractions.AbstractEntityService;
import com.kdimitrov.edentist.server.common.services.abstractions.UserService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DoctorsService extends AbstractEntityService<Doctor, DoctorsRepository> implements UserService<Doctor> {

    public DoctorsService(DoctorsRepository repository) {
        super(repository);
    }

    @Override
    public Doctor findByEmail(String email) throws NotFound {
        return findByEmailOpt(email).orElseThrow(() -> new NotFound("No record for " + email));
    }

    @Override
    public Optional<Doctor> findByEmailOpt(String email) {
        return repository.findByEmail(email);
    }
}
