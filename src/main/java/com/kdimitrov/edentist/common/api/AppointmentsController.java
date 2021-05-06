package com.kdimitrov.edentist.common.api;

import com.kdimitrov.edentist.common.exceptions.NotFound;
import com.kdimitrov.edentist.common.exceptions.OperationUnsuccessful;
import com.kdimitrov.edentist.common.models.Appointment;
import com.kdimitrov.edentist.common.models.dto.AppointmentDto;
import com.kdimitrov.edentist.common.models.rest.AppointmentRequest;
import com.kdimitrov.edentist.common.services.AppointmentServiceImpl;
import com.kdimitrov.edentist.common.services.MessageBroker;
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

import java.util.List;

@RestController
@CrossOrigin("*")
public class AppointmentsController {

    final AppointmentServiceImpl appointmentService;
    final MessageBroker broker;

    public AppointmentsController(AppointmentServiceImpl appointmentService, MessageBroker broker) {
        this.appointmentService = appointmentService;
        this.broker = broker;
    }

    @GetMapping("/appointments")
    @ResponseBody
    public List<AppointmentDto> get(
            @RequestHeader(value = "Filter", required = false) String filter,
            @RequestHeader(value = "IsAdmin", required = false) boolean isAdmin,
            @RequestParam(name = "user", required = false) String userEmail
    ) {
        return appointmentService.find(filter, userEmail, isAdmin);
    }

    @GetMapping("/appointment")
    @ResponseBody
    public AppointmentDto get(
            @RequestParam(name = "user") String userEmail,
            @RequestHeader(name = "Code") String code
    ) throws NotFoundException {
        return appointmentService.find(userEmail, code);
    }

    @PostMapping("/appointments")
    @ResponseBody
    public ResponseEntity addAppointment(
            @RequestBody AppointmentRequest request
    ) {
        try {
            ResponseEntity<String> entity = appointmentService.save(request);
            broker.translate(new JSONObject(entity.getBody()));
            return entity;
        } catch (NotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/appointments/{id}")
    @ResponseBody
    public String updateAppointment(
            @RequestBody Appointment request,
            @PathVariable(value = "id") long id
    ) {
        try {
            return appointmentService.update(request, id);
        } catch (NotFoundException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    @DeleteMapping("/appointments/{id}")
    public ResponseEntity deleteAppointment(
            @PathVariable(value = "id") long id
    ) {
        try {
            appointmentService.delete(id);
        } catch (NotFound | OperationUnsuccessful e) {
            e.printStackTrace();
            throw e;
        }

        return new ResponseEntity(HttpStatus.OK);
    }
}
