package com.kdimitrov.edentist.common.models.dto;

import com.kdimitrov.edentist.common.models.Status;

public class StatusDto extends DTOEntity {
    private String name;

    public StatusDto() {
    }

    public StatusDto(Status.Type status) {
        this.name = status.name();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
