package com.kdimitrov.edentist.server.common.services;

import com.kdimitrov.edentist.server.common.exceptions.NotFound;
import com.kdimitrov.edentist.server.common.models.Appointment;
import com.kdimitrov.edentist.server.common.models.dto.AppointmentDto;
import com.kdimitrov.edentist.server.common.models.rest.AppointmentRequest;
import org.springframework.http.ResponseEntity;

import javax.management.OperationsException;
import java.util.List;

public interface AppointmentService {

    List<AppointmentDto> filterAppointments(String filter, String userEmail);

    List<AppointmentDto> filterArchivedAppointments(String filter, String userEmail);

    AppointmentDto findSingleAppointment(String userEmail, String code) throws NotFound;

    ResponseEntity<String> saveAppointment(AppointmentRequest appointment) throws NotFound;

    String updateAppointment(Appointment request, long id) throws NotFound;

    String deleteAppointment(long id) throws OperationsException, NotFound;

}
