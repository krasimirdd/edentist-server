package com.kdimitrov.edentist.server.common.services.implementations;

import com.kdimitrov.edentist.app.config.ApplicationConfig;
import com.kdimitrov.edentist.server.common.exceptions.NotFound;
import com.kdimitrov.edentist.server.common.models.Doctor;
import com.kdimitrov.edentist.server.common.models.Patient;
import com.kdimitrov.edentist.server.common.models.dto.DoctorDto;
import com.kdimitrov.edentist.server.common.models.dto.ServiceDto;
import com.kdimitrov.edentist.server.common.repository.DoctorsRepository;
import com.kdimitrov.edentist.server.common.repository.PatientRepository;
import com.kdimitrov.edentist.server.common.repository.ServicesRepository;
import com.kdimitrov.edentist.server.common.services.abstractions.ApiService;
import com.kdimitrov.edentist.server.common.utils.CustomMapper;
import com.kdimitrov.edentist.server.common.utils.GraphApiExecutor;
import com.kdimitrov.edentist.server.common.utils.ObjectMapperUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ApiServiceImpl implements ApiService {
    private final Logger logger = LogManager.getLogger(ApiServiceImpl.class);

    private final ApplicationConfig config;

    private final ServicesRepository servicesRepository;
    private final DoctorsRepository doctorsRepository;
    private final PatientRepository patientRepository;

    public ApiServiceImpl(ApplicationConfig config,
                          ServicesRepository servicesRepository,
                          DoctorsRepository doctorsRepository,
                          PatientRepository patientRepository) {
        this.config = config;
        this.servicesRepository = servicesRepository;
        this.doctorsRepository = doctorsRepository;
        this.patientRepository = patientRepository;
    }

    @Override
    public List<ServiceDto> findAllServices() {
        return servicesRepository.findAll()
                .stream()
                .map((e) -> ObjectMapperUtils.map(e, ServiceDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<DoctorDto> findAllDoctors() {
        return doctorsRepository.findAll()
                .stream()
                .map((e) -> ObjectMapperUtils.map(e, DoctorDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public String findUser(String userEmail) throws NotFound {
        if (config.getSuperadminEmail().equals(userEmail)) {
            return CustomMapper.toAdminDtoString(config.getSuperadminEmail());
        }

        Optional<Doctor> docOpt = doctorsRepository.findByEmail(userEmail);
        if (docOpt.isPresent()) {
            return CustomMapper.toUserDtoString(docOpt.get());
        }

        Optional<Patient> patientOpt = patientRepository.findByEmail(userEmail);
        if (patientOpt.isPresent()) {
            return CustomMapper.toUserDtoString(patientOpt.get());
        }

        throw new NotFound("No such user");
    }

    @Override
    public String save(String email) {

        try {
            return findUser(email);
        } catch (NotFound notFound) {
            logger.info("Couldn't not find user with email '{}'. Creating one!", email);

            Patient patient = new Patient();
            patient.setEmail(email);
            return CustomMapper.toUserDtoString(patientRepository.saveAndFlush(patient));
        }
    }
}
