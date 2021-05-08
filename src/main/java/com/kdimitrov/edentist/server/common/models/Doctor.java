package com.kdimitrov.edentist.server.common.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity(name = "doctor")
@Table(name = "doctors")
public class Doctor extends com.kdimitrov.edentist.server.common.models.Entity {

    @Column
    private String email;

    @Column
    private String phone;

    @Column
    private String name;

    public Doctor() {
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getName() {
        return name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setName(String name) {
        this.name = name;
    }

}
