package com.kdimitrov.edentist.server.common.services;

import com.kdimitrov.edentist.server.common.exceptions.NotFound;
import com.kdimitrov.edentist.server.common.models.Doctor;
import com.kdimitrov.edentist.server.common.models.dto.DoctorDto;
import com.kdimitrov.edentist.server.common.models.dto.ServiceDto;

import java.util.List;

public interface DefService {
    List<ServiceDto> findAllServices();

    List<DoctorDto> findAllDoctors();

    String findById(String userEmail) throws NotFound;

    String save(String email);

    Doctor createUser(String email, String name, String password, String phone);
}
