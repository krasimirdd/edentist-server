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

    @Column
    private String description;

    @Column
    private String specialization;

    @Column
    private String img;

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

    public String getDescription() {
        return description;
    }

    public String getSpecialization() {
        return specialization;
    }

    public String getImg() {
        return img;
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

    public void setDescription(String description) {
        this.description = description;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public void setImg(String img) {
        this.img = img;
    }

    @Override
    public String toString() {
        return "Doctor{" +
                "email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", specialization='" + specialization + '\'' +
                ", img='" + img + '\'' +
                '}';
    }
}
