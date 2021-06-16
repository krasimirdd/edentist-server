package com.kdimitrov.edentist.server.common.services.abstractions;

import com.kdimitrov.edentist.server.common.exceptions.NotFound;
import com.kdimitrov.edentist.server.common.models.Entity;
import com.kdimitrov.edentist.server.common.models.dto.AppointmentDto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public abstract class AppointmentService<E extends Entity, R extends JpaRepository<E, Long>> extends AbstractEntityService<E, R> {

    public AppointmentService(R repository) {
        super(repository);
    }

    protected abstract List<AppointmentDto> filterAppointments(String filter, String userEmail) throws NotFound;

}
