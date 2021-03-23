package com.kdimitrov.edentist.common.models.dto;

import com.kdimitrov.edentist.common.models.Doctor;

public class DoctorDto extends UserDTO {
    public DoctorDto() {
    }

    public DoctorDto(Doctor doctor) {
        super(doctor);
    }
}
