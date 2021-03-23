package com.kdimitrov.edentist.common.services;

import com.kdimitrov.edentist.common.models.dto.ServiceDto;

import java.util.List;

public interface DefService {
    List<ServiceDto> findAllServices();
}
