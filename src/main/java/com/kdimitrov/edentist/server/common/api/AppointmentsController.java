package com.kdimitrov.edentist.server.common.api;

import com.kdimitrov.edentist.server.common.exceptions.NotFound;
import com.kdimitrov.edentist.server.common.exceptions.OperationUnsuccessful;
import com.kdimitrov.edentist.server.common.models.Appointment;
import com.kdimitrov.edentist.server.common.models.dto.AppointmentDto;
import com.kdimitrov.edentist.server.common.models.rest.AppointmentRequest;
import com.kdimitrov.edentist.server.common.services.AppointmentServiceImpl;
import com.kdimitrov.edentist.server.common.services.AuthenticationServiceImpl;
import com.kdimitrov.edentist.server.common.services.MessageBroker;
import javassist.NotFoundException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.naming.AuthenticationException;
import java.util.List;

import static com.kdimitrov.edentist.server.common.utils.Routes.APPOINTMENTS;
import static com.kdimitrov.edentist.server.common.utils.Routes.ARCHIVED;
import static com.kdimitrov.edentist.server.common.utils.Routes.AUTHORIZATION;
import static com.kdimitrov.edentist.server.common.utils.Routes.BY_ID;
import static com.kdimitrov.edentist.server.common.utils.Routes.FILTER_HEADER;
import static com.kdimitrov.edentist.server.common.utils.Routes.ID_PARAM;
import static com.kdimitrov.edentist.server.common.utils.Routes.USER_PARAM;

@RestController
@RequestMapping(value = APPOINTMENTS)
@CrossOrigin("*")
public class AppointmentsController {

    final AuthenticationServiceImpl authenticationService;
    final AppointmentServiceImpl appointmentService;
    final MessageBroker broker;

    public AppointmentsController(AuthenticationServiceImpl authenticationService,
                                  AppointmentServiceImpl appointmentService,
                                  MessageBroker broker) {
        this.authenticationService = authenticationService;
        this.appointmentService = appointmentService;
        this.broker = broker;
    }

    @GetMapping
    @ResponseBody
    public ResponseEntity<List<AppointmentDto>> getAppointments(
            @RequestHeader(value = AUTHORIZATION, required = false) String auth,
            @RequestHeader(value = FILTER_HEADER, required = false) String filter,
            @RequestParam(name = USER_PARAM) String userEmail) {

        return authenticationService
                .withToken(() -> appointmentService.filterAppointments(filter, userEmail), auth);
    }


    @GetMapping(ARCHIVED)
    @ResponseBody
    public ResponseEntity<List<AppointmentDto>> getArchivedAppointments(
            @RequestHeader(value = AUTHORIZATION, required = false) String auth,
            @RequestHeader(value = FILTER_HEADER, required = false) String filter,
            @RequestParam(name = USER_PARAM, required = false) String userEmail) {

        return authenticationService
                .withToken(() -> appointmentService.filterArchivedAppointments(filter, userEmail), auth);
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<String> addAppointments(
            @RequestBody AppointmentRequest request) {

        try {
            ResponseEntity<String> entity = appointmentService.save(request);
            broker.translate(new JSONObject(entity.getBody()));
            return entity;

        } catch (NotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(BY_ID)
    @ResponseBody
    public ResponseEntity<String> updateAppointment(
            @RequestBody Appointment request,
            @RequestHeader(value = AUTHORIZATION, required = false) String auth,
            @PathVariable(value = ID_PARAM) long id) {

        return authenticationService
                .withToken(() -> appointmentService.update(request, id), auth);
    }

    @DeleteMapping(BY_ID)
    public ResponseEntity<String> deleteAppointment(
            @RequestHeader(value = AUTHORIZATION, required = false) String auth,
            @PathVariable(value = ID_PARAM) long id) {

        return authenticationService
                .withToken(() -> appointmentService.delete(id), auth);
    }
}
