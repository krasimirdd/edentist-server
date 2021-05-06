package com.kdimitrov.edentist.common.models.dto;

import com.kdimitrov.edentist.common.models.Patient;

public class PatientDto extends UserDTO {
    private String bloodType;

    public PatientDto() {
    }

    public PatientDto(Patient patient) {
        super(patient);
    }
}
