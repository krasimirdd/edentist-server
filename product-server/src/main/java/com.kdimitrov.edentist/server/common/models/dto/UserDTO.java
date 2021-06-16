package com.kdimitrov.edentist.server.common.models.dto;

import com.kdimitrov.edentist.server.common.models.Doctor;
import com.kdimitrov.edentist.server.common.models.Patient;

public class UserDTO extends DTOEntity {
    protected long id;
    protected String email;
    protected String phone;
    protected String name;
    protected String role;
    protected String description;
    protected String specialization;
    protected String img;

    public UserDTO() {
    }

    public UserDTO(Patient entity) {
        this.id = entity.getId();
        this.email = entity.getEmail();
        this.phone = entity.getPhone();
        this.name = entity.getName();
        this.role = "patient";
    }

    public UserDTO(Doctor entity) {
        this.id = entity.getId();
        this.email = entity.getEmail();
        this.phone = entity.getPhone();
        this.name = entity.getName();
        this.description = entity.getName();
        this.specialization = entity.getName();
        this.specialization = entity.getName();
        this.img = entity.getImg();
        this.role = "doctor";
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}