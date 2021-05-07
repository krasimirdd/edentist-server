package com.kdimitrov.edentist.common.services;

import com.kdimitrov.edentist.common.exceptions.NotFound;
import com.kdimitrov.edentist.common.models.Doctor;
import com.kdimitrov.edentist.common.models.Patient;
import com.kdimitrov.edentist.common.models.dto.DoctorDto;
import com.kdimitrov.edentist.common.models.dto.ServiceDto;
import com.kdimitrov.edentist.common.repository.DoctorsRepository;
import com.kdimitrov.edentist.common.repository.PatientRepository;
import com.kdimitrov.edentist.common.repository.ServicesRepository;
import com.kdimitrov.edentist.common.utils.CustomMapper;
import com.kdimitrov.edentist.common.utils.ObjectMapperUtils;
import com.kdimitrov.edentist.config.ApplicationConfig;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DefServiceImpl implements DefService {
    final ApplicationConfig config;

    final ServicesRepository servicesRepository;
    final DoctorsRepository doctorsRepository;
    final PatientRepository patientRepository;

    public DefServiceImpl(ApplicationConfig config, ServicesRepository servicesRepository,
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
    public Doctor createUser(String email, String name, String password, String phone) {
        Doctor doctor = new Doctor();

        doctor.setEmail(email);
        doctor.setName(name);
        doctor.setPassword(password);
        doctor.setPhone(phone);
        return doctorsRepository.saveAndFlush(doctor);
    }

    public String findById(String userEmail) {
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

    public String save(String email) {
        Optional<Patient> patientOpt = patientRepository.findByEmail(email);
        if (patientOpt.isPresent()) {
            return findById(email);
        }

        Patient patient = new Patient();
        patient.setEmail(email);
        return CustomMapper.toUserDtoString(patientRepository.saveAndFlush(patient));
    }
}
