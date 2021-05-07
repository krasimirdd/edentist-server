package com.kdimitrov.edentist.common.api;

import com.kdimitrov.edentist.common.models.dto.DoctorDto;
import com.kdimitrov.edentist.common.models.dto.ServiceDto;
import com.kdimitrov.edentist.common.services.DefServiceImpl;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.kdimitrov.edentist.common.utils.Routes.API_DOCTORS;
import static com.kdimitrov.edentist.common.utils.Routes.API_SERVICES;
import static com.kdimitrov.edentist.common.utils.Routes.API_USER;
import static com.kdimitrov.edentist.common.utils.Routes.API_USER_WITH_EMAIL;
import static com.kdimitrov.edentist.common.utils.Routes.USEREMAIL_PARAM;

@RestController
@CrossOrigin("*")
public class DefController {

    final DefServiceImpl defService;

    public DefController(DefServiceImpl defService) {
        this.defService = defService;
    }

    @GetMapping(API_SERVICES)
    @ResponseBody
    public List<ServiceDto> getServices() {
        return defService.findAllServices();
    }

    @GetMapping(API_DOCTORS)
    @ResponseBody
    public List<DoctorDto> getDoctors() {
        return defService.findAllDoctors();
    }

    @GetMapping(API_USER_WITH_EMAIL)
    @ResponseBody
    public String getUserMetadata(@PathVariable(value = USEREMAIL_PARAM) String userEmail) {
        return defService.findById(userEmail);
    }

    @PostMapping(API_USER)
    public String postUserMetadata(@RequestBody String userEmail) {
        return defService.save(userEmail);
    }

}
