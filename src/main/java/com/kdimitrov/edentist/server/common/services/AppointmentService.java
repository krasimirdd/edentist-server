package com.kdimitrov.edentist.server.common.services;

import com.kdimitrov.edentist.server.common.models.Appointment;
import com.kdimitrov.edentist.server.common.models.dto.AppointmentDto;
import com.kdimitrov.edentist.server.common.models.rest.AppointmentRequest;
import javassist.NotFoundException;
import jdk.nashorn.internal.objects.annotations.Function;
import org.springframework.http.ResponseEntity;

import javax.management.OperationsException;
import java.util.List;

public interface AppointmentService {

    List<AppointmentDto> filterAppointments(String filter, String userEmail);

    AppointmentDto findSingleAppointment(String userEmail, String code) throws NotFoundException;

    ResponseEntity<String> save(AppointmentRequest appointment) throws NotFoundException;

    String update(Appointment request, long id) throws NotFoundException;

    void delete(long id) throws NotFoundException, OperationsException;

}
