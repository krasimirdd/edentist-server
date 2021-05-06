package com.kdimitrov.edentist.common.models;

import com.kdimitrov.edentist.common.models.rest.AppointmentRequest;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity(name = "patient")
@Table(name = "patients")
public class Patient extends com.kdimitrov.edentist.common.models.Entity {

    @Column
    private String email;

    @Column
    private String phone;

    @Column
    private String name;

    @Column
    private String bloodType;

    public Patient() {
    }

    public Patient(AppointmentRequest request) {
        this.email = request.getEmail();
        this.name = request.getName();
        this.phone = request.getPhone();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBloodType() {
        return bloodType;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }

}

