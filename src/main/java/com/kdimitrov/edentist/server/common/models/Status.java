package com.kdimitrov.edentist.server.common.models;

import javax.persistence.Column;

@javax.persistence.Entity(name = "status")
public class Status extends Entity {

    @Column
    String name;

    public Status() {
    }

    public Status(Type type) {
        this.name = type.name();
    }

    public String getName() {
        return name;
    }

    public enum Type {
        PENDING,
        APPROVED,
        DENIED,
    }
}
