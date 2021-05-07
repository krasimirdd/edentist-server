package com.kdimitrov.edentist.common.services;

import com.kdimitrov.edentist.common.models.Doctor;
import com.kdimitrov.edentist.common.models.dto.DoctorDto;
import com.kdimitrov.edentist.common.models.dto.ServiceDto;

import java.util.List;

public interface DefService {
    List<ServiceDto> findAllServices();

    List<DoctorDto> findAllDoctors();

    Doctor createUser(final String email, String name, final String password, final String phone);
}
