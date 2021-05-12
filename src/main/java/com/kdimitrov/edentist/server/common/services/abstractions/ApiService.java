package com.kdimitrov.edentist.server.common.services.abstractions;

import com.kdimitrov.edentist.server.common.exceptions.NotFound;
import com.kdimitrov.edentist.server.common.models.Doctor;
import com.kdimitrov.edentist.server.common.models.dto.DoctorDto;
import com.kdimitrov.edentist.server.common.models.dto.ServiceDto;

import java.util.List;

public interface ApiService {
    List<ServiceDto> findAllServices();

    List<DoctorDto> findAllDoctors();

    String findUser(String userEmail) throws NotFound;

    String save(String email);

    Doctor createUser(String email,
                      String name,
                      String phone,
                      String specialization,
                      String description,
                      String img);
}
