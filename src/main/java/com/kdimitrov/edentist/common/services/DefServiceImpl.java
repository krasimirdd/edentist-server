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
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DefServiceImpl implements DefService {

    final ServicesRepository servicesRepository;
    final DoctorsRepository doctorsRepository;
    final PatientRepository patientRepository;

    public DefServiceImpl(ServicesRepository servicesRepository,
                          DoctorsRepository doctorsRepository, PatientRepository patientRepository) {
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

    public List<DoctorDto> findAllDoctors() {
        return doctorsRepository.findAll()
                .stream()
                .map((e) -> ObjectMapperUtils.map(e, DoctorDto.class))
                .collect(Collectors.toList());
    }

    public String findById(String userEmail) {

        Optional<Doctor> docOpt = doctorsRepository.findByEmail(userEmail);
        if (docOpt.isPresent()) {
            return CustomMapper.toUserDto(docOpt.get());
        }

        Optional<Patient> patientOpt = patientRepository.findByEmail(userEmail);
        if (patientOpt.isPresent()) {
            return CustomMapper.toUserDto(patientOpt.get());
        }

        throw new NotFound("No such user");
    }
}
