package com.kdimitrov.edentist.server.common.services.implementations;


import com.kdimitrov.edentist.server.common.repository.ServicesRepository;
import com.kdimitrov.edentist.server.common.services.abstractions.AbstractEntityService;
import org.springframework.stereotype.Service;

@Service
public class ServicesService extends AbstractEntityService<com.kdimitrov.edentist.server.common.models.Service, ServicesRepository> {

    public ServicesService(ServicesRepository repository) {
        super(repository);
    }

}