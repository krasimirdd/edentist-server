package com.kdimitrov.edentist.server.common.models.dto;

import com.kdimitrov.edentist.server.common.models.Patient;

public class PatientDto extends UserDTO {
    private String bloodType;

    public PatientDto() {
    }

    public PatientDto(Patient patient) {
        super(patient);
    }
}
