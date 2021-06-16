package com.kdimitrov.edentist.server.common.api;

import com.kdimitrov.edentist.server.common.models.dto.DoctorDto;
import com.kdimitrov.edentist.server.common.models.dto.ServiceDto;
import com.kdimitrov.edentist.server.common.services.implementations.AuthenticationServiceImpl;
import com.kdimitrov.edentist.server.common.services.implementations.ApiServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.kdimitrov.edentist.server.common.utils.Routes.API;
import static com.kdimitrov.edentist.server.common.utils.Routes.AUTHORIZATION;
import static com.kdimitrov.edentist.server.common.utils.Routes.DOCTORS;
import static com.kdimitrov.edentist.server.common.utils.Routes.SERVICES;
import static com.kdimitrov.edentist.server.common.utils.Routes.USER;
import static com.kdimitrov.edentist.server.common.utils.Routes.USEREMAIL_PARAM;
import static com.kdimitrov.edentist.server.common.utils.Routes.USER_BY_EMAIL;

@RestController
@RequestMapping(value = API)
@CrossOrigin("*")
public class ApiController {

    final AuthenticationServiceImpl authenticationService;
    final ApiServiceImpl apiService;

    public ApiController(AuthenticationServiceImpl authenticationService,
                         ApiServiceImpl apiService) {
        this.authenticationService = authenticationService;
        this.apiService = apiService;
    }

    @GetMapping(USER_BY_EMAIL)
    @ResponseBody
    public ResponseEntity<String> getUserMetadata(
            @PathVariable(value = USEREMAIL_PARAM) String userEmail,
            @RequestHeader(value = AUTHORIZATION) String auth) {

        return authenticationService
                .withSecret(() -> apiService.findUser(userEmail), auth, true);
    }

    @GetMapping(SERVICES)
    @ResponseBody
    public ResponseEntity<List<ServiceDto>> getServices(@RequestHeader(value = AUTHORIZATION, required = false) String auth) {

        return authenticationService
                .withSecret(apiService::findAllServices, auth, false);
    }

    @GetMapping(DOCTORS)
    @ResponseBody
    public ResponseEntity<List<DoctorDto>> getDoctors(@RequestHeader(value = AUTHORIZATION, required = false) String auth) {

        return authenticationService
                .withSecret(apiService::findAllDoctors, auth, false);
    }

    @PostMapping(USER)
    public ResponseEntity<String> postUserMetadata(
            @RequestBody String userEmail,
            @RequestHeader(value = AUTHORIZATION, required = false) String auth) {

        return authenticationService
                .withSecret(() -> apiService.save(userEmail), auth, false);
    }

}
