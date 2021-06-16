package com.kdimitrov.edentist.server.common.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity(name = "service_type")
@Table(name = "service_type")
public class Service extends com.kdimitrov.edentist.server.common.models.Entity {

    @Column
    private String type;

    public Service() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
