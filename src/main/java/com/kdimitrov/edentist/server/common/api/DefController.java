package com.kdimitrov.edentist.server.common.api;

import com.kdimitrov.edentist.server.common.exceptions.NotFound;
import com.kdimitrov.edentist.server.common.models.dto.DoctorDto;
import com.kdimitrov.edentist.server.common.models.dto.ServiceDto;
import com.kdimitrov.edentist.server.common.services.AuthenticationServiceImpl;
import com.kdimitrov.edentist.server.common.services.DefServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.naming.AuthenticationException;
import java.util.List;

import static com.kdimitrov.edentist.server.common.utils.Routes.API_DOCTORS;
import static com.kdimitrov.edentist.server.common.utils.Routes.API_SERVICES;
import static com.kdimitrov.edentist.server.common.utils.Routes.API_USER;
import static com.kdimitrov.edentist.server.common.utils.Routes.API_USER_WITH_EMAIL;
import static com.kdimitrov.edentist.server.common.utils.Routes.AUTHORIZATION;
import static com.kdimitrov.edentist.server.common.utils.Routes.USEREMAIL_PARAM;

@RestController
@CrossOrigin("*")
public class DefController {

    final AuthenticationServiceImpl authenticationService;
    final DefServiceImpl defService;

    public DefController(AuthenticationServiceImpl authenticationService,
                         DefServiceImpl defService) {
        this.authenticationService = authenticationService;
        this.defService = defService;
    }

    @GetMapping(API_SERVICES)
    @ResponseBody
    public ResponseEntity<List<ServiceDto>> getServices(@RequestHeader(value = AUTHORIZATION, required = false) String auth) {
        try {
            this.authenticationService.validateSecret(auth);
            return new ResponseEntity<>(defService.findAllServices(), HttpStatus.OK);

        } catch (AuthenticationException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping(API_DOCTORS)
    @ResponseBody
    public ResponseEntity<List<DoctorDto>> getDoctors(@RequestHeader(value = AUTHORIZATION, required = false) String auth) {
        try {
            this.authenticationService.validateSecret(auth);
            return new ResponseEntity<>(defService.findAllDoctors(), HttpStatus.OK);

        } catch (AuthenticationException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping(API_USER_WITH_EMAIL)
    @ResponseBody
    public ResponseEntity<String> getUserMetadata(
            @PathVariable(value = USEREMAIL_PARAM) String userEmail,
            @RequestHeader(value = AUTHORIZATION) String auth) {

        try {
            this.authenticationService.validateSecret(auth);
            String entity = this.defService.findById(userEmail);
            return ResponseEntity.ok()
                    .header(AUTHORIZATION, this.authenticationService.generateJwt())
                    .body(entity);

        } catch (NotFound e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (AuthenticationException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

    }

    @PostMapping(API_USER)
    public ResponseEntity<String> postUserMetadata(
            @RequestBody String userEmail,
            @RequestHeader(value = AUTHORIZATION, required = false) String auth) {

        try {
            this.authenticationService.validateSecret(auth);
            return new ResponseEntity<>(defService.save(userEmail), HttpStatus.OK);

        } catch (AuthenticationException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

}
