package com.kdimitrov.edentist.common.services;

import com.kdimitrov.edentist.common.models.Doctor;
import com.kdimitrov.edentist.common.models.dto.DoctorDto;
import com.kdimitrov.edentist.common.models.dto.ServiceDto;

import java.util.List;

public interface DefService {
    List<ServiceDto> findAllServices();

    List<DoctorDto> findAllDoctors();

    String findById(String userEmail);

    String save(String email);

    Doctor createUser(String email, String name, String password, String phone);
}
