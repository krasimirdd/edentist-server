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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.naming.AuthenticationException;
import java.util.List;

import static com.kdimitrov.edentist.server.common.utils.Routes.APPOINTMENTS;
import static com.kdimitrov.edentist.server.common.utils.Routes.APPOINTMENTS_WITH_ID;
import static com.kdimitrov.edentist.server.common.utils.Routes.AUTHORIZATION;
import static com.kdimitrov.edentist.server.common.utils.Routes.CODE_HEADER;
import static com.kdimitrov.edentist.server.common.utils.Routes.FILTER_HEADER;
import static com.kdimitrov.edentist.server.common.utils.Routes.ID_PARAM;
import static com.kdimitrov.edentist.server.common.utils.Routes.SINGLE_APPOINTMENT;
import static com.kdimitrov.edentist.server.common.utils.Routes.USER_PARAM;

@RestController
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

    @GetMapping(APPOINTMENTS)
    @ResponseBody
    public ResponseEntity<List<AppointmentDto>> getAppointments(
            @RequestHeader(value = AUTHORIZATION, required = false) String auth,
            @RequestHeader(value = FILTER_HEADER, required = false) String filter,
            @RequestParam(name = USER_PARAM, required = false) String userEmail) {

        try {
            this.authenticationService.validateToken(auth);
            return new ResponseEntity<>(appointmentService.filterAppointments(filter, userEmail), HttpStatus.OK);

        } catch (AuthenticationException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping(SINGLE_APPOINTMENT)
    @ResponseBody
    public ResponseEntity<AppointmentDto> getSingleAppointment(
            @RequestParam(name = USER_PARAM) String userEmail,
            @RequestHeader(name = CODE_HEADER) String code) {

        try {
            return new ResponseEntity<>(appointmentService.findSingleAppointment(userEmail, code), HttpStatus.OK);

        } catch (NotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(APPOINTMENTS)
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

    @PostMapping(APPOINTMENTS_WITH_ID)
    @ResponseBody
    public ResponseEntity<String> updateAppointment(
            @RequestBody Appointment request,
            @RequestHeader(value = AUTHORIZATION, required = false) String auth,
            @PathVariable(value = ID_PARAM) long id) {

        try {
            this.authenticationService.validateToken(auth);
            return ResponseEntity.ok(appointmentService.update(request, id));

        } catch (NotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (AuthenticationException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @DeleteMapping(APPOINTMENTS_WITH_ID)
    public ResponseEntity<String> deleteAppointment(
            @RequestHeader(value = AUTHORIZATION, required = false) String auth,
            @PathVariable(value = ID_PARAM) long id) {

        try {
            this.authenticationService.validateToken(auth);
            appointmentService.delete(id);
            return new ResponseEntity<>(HttpStatus.OK);

        } catch (NotFound | OperationUnsuccessful e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (AuthenticationException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
}
