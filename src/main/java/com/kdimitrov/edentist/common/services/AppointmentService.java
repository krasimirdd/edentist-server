package com.kdimitrov.edentist.common.services;

import com.kdimitrov.edentist.common.models.Appointment;
import com.kdimitrov.edentist.common.models.dto.AppointmentDto;
import com.kdimitrov.edentist.common.models.rest.AppointmentRequest;
import javassist.NotFoundException;
import org.springframework.http.ResponseEntity;

import javax.management.OperationsException;
import java.util.List;

public interface AppointmentService {

    List<AppointmentDto> find(String filter, String userEmail);

    ResponseEntity<String> save(AppointmentRequest appointment) throws NotFoundException;

    String update(Appointment request, long id) throws NotFoundException;

    void delete(long id) throws NotFoundException, OperationsException;

}
