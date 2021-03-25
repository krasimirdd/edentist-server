package com.kdimitrov.edentist.common.api;

import com.kdimitrov.edentist.common.models.dto.DoctorDto;
import com.kdimitrov.edentist.common.models.dto.ServiceDto;
import com.kdimitrov.edentist.common.services.DefServiceImpl;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin("*")
public class DefController {

    final DefServiceImpl defService;

    public DefController(DefServiceImpl defService) {
        this.defService = defService;
    }

    @GetMapping("/api/services")
    @ResponseBody
    public List<ServiceDto> getServices() {
        return defService.findAllServices();
    }

    @GetMapping("/api/doctors")
    @ResponseBody
    public List<DoctorDto> getDoctors() {
        return defService.findAllDoctors();
    }

    @GetMapping("/api/user/{userEmail}")
    @ResponseBody
    public String getUserMetadata(@PathVariable(value = "userEmail") String userEmail) {
        return defService.findById(userEmail);
    }

}
