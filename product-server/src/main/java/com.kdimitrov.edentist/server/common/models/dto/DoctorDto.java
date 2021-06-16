package com.kdimitrov.edentist.server.common.models.dto;

import com.kdimitrov.edentist.server.common.models.Doctor;

public class DoctorDto extends UserDTO {
    public DoctorDto() {
    }

    public DoctorDto(Doctor doctor) {
        super(doctor);
    }
}
