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

import static com.kdimitrov.edentist.common.utils.Routes.APPOINTMENTS;
import static com.kdimitrov.edentist.common.utils.Routes.APPOINTMENTS_WITH_ID;
import static com.kdimitrov.edentist.common.utils.Routes.CODE_HEADER;
import static com.kdimitrov.edentist.common.utils.Routes.FILTER_HEADER;
import static com.kdimitrov.edentist.common.utils.Routes.ID_PARAM;
import static com.kdimitrov.edentist.common.utils.Routes.SINGLE_APPOINTMENT;
import static com.kdimitrov.edentist.common.utils.Routes.USER_PARAM;

@RestController
@CrossOrigin("*")
public class AppointmentsController {

    final AppointmentServiceImpl appointmentService;
    final MessageBroker broker;

    public AppointmentsController(AppointmentServiceImpl appointmentService, MessageBroker broker) {
        this.appointmentService = appointmentService;
        this.broker = broker;
    }

    @GetMapping(APPOINTMENTS)
    @ResponseBody
    public List<AppointmentDto> getAppointment(
            @RequestHeader(value = FILTER_HEADER, required = false) String filter,
            @RequestParam(name = USER_PARAM, required = false) String userEmail
    ) {
        return appointmentService.filterAppointments(filter, userEmail);
    }

    @GetMapping(SINGLE_APPOINTMENT)
    @ResponseBody
    public AppointmentDto getSingleAppointment(
            @RequestParam(name = USER_PARAM) String userEmail,
            @RequestHeader(name = CODE_HEADER) String code
    ) throws NotFoundException {
        return appointmentService.findSingleAppointment(userEmail, code);
    }

    @PostMapping(APPOINTMENTS)
    @ResponseBody
    public ResponseEntity addAppointments(
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

    @PostMapping(APPOINTMENTS_WITH_ID)
    @ResponseBody
    public String updateAppointment(
            @RequestBody Appointment request,
            @PathVariable(value = ID_PARAM) long id
    ) {
        try {
            return appointmentService.update(request, id);
        } catch (NotFoundException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    @DeleteMapping(APPOINTMENTS_WITH_ID)
    public ResponseEntity deleteAppointment(
            @PathVariable(value = ID_PARAM) long id
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
