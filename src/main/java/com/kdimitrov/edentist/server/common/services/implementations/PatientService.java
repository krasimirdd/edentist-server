package com.kdimitrov.edentist.server.common.services.implementations;

import com.kdimitrov.edentist.server.common.exceptions.NotFound;
import com.kdimitrov.edentist.server.common.models.Patient;
import com.kdimitrov.edentist.server.common.repository.PatientRepository;
import com.kdimitrov.edentist.server.common.services.abstractions.AbstractEntityService;
import com.kdimitrov.edentist.server.common.services.abstractions.UserService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PatientService extends AbstractEntityService<Patient, PatientRepository> implements UserService<Patient> {

    public PatientService(PatientRepository repository) {
        super(repository);
    }

    @Override
    public Patient findByEmail(String email) throws NotFound {
        return findByEmailOpt(email).orElseThrow(() -> new NotFound("No record for " + email));
    }

    @Override
    public Optional<Patient> findByEmailOpt(String email) {
        return repository.findByEmail(email);
    }
}
