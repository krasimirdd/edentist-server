package com.kdimitrov.edentist.common.models.dto;

import com.kdimitrov.edentist.common.models.Service;

public class ServiceDto extends DTOEntity {
    private long id;
    private String type;

    public ServiceDto() {
    }

    public ServiceDto(Service service) {
        this.type = service.getType();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
