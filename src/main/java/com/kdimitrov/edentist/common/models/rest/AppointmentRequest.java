package com.kdimitrov.edentist.common.models.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AppointmentRequest {

    private Long timestamp;
    private String email;
    private String phone;
    private String name;
    private Long doctorId;
    private Long serviceId;

    @JsonProperty(value = "timestamp")
    public Long getTimestamp() {
        return timestamp;
    }

    @JsonProperty(value = "p_email")
    public String getEmail() {
        return email;
    }

    @JsonProperty(value = "p_phone")
    public String getPhone() {
        return phone;
    }

    @JsonProperty(value = "p_name")
    public String getName() {
        return name;
    }

    @JsonProperty(value = "doctorId")
    public Long getDoctorId() {
        return doctorId;
    }

    @JsonProperty(value = "serviceId")
    public Long getServiceId() {
        return serviceId;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
