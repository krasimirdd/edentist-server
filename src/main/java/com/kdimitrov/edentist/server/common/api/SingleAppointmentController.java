package com.kdimitrov.edentist.server.common.api;

import com.kdimitrov.edentist.server.common.models.dto.AppointmentDto;
import com.kdimitrov.edentist.server.common.services.implementations.AppointmentServiceImpl;
import javassist.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import static com.kdimitrov.edentist.server.common.utils.Routes.CODE_HEADER;
import static com.kdimitrov.edentist.server.common.utils.Routes.SINGLE_APPOINTMENT;
import static com.kdimitrov.edentist.server.common.utils.Routes.USER_PARAM;

@RestController
@RequestMapping(value = SINGLE_APPOINTMENT)
@CrossOrigin("*")
public class SingleAppointmentController {

    final AppointmentServiceImpl appointmentService;

    public SingleAppointmentController(AppointmentServiceImpl appointmentService) {
        this.appointmentService = appointmentService;
    }

    @GetMapping
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
}
