package com.kdimitrov.edentist.common.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity(name = "doctor")
@Table(name = "doctors")
public class Doctor extends com.kdimitrov.edentist.common.models.Entity {

    @Column
    private String email;

    @Column
    private String password;

    @Column
    private String phone;

    @Column
    private String name;

    public Doctor() {
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
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

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setName(String name) {
        this.name = name;
    }


}
