package com.kdimitrov.rabbitmq.common.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize
@JsonPOJOBuilder
@JsonSerialize
public class VisitRequest implements Serializable {

    @JsonProperty(value = "id")
    private long id;

    @JsonProperty(value = "visitCode")
    private String visitCode;

    @JsonProperty(value = "date")
    private String date;

    @JsonProperty(value = "fee")
    private int fee;

    @JsonProperty(value = "status")
    private String status;

    @JsonProperty(value = "medicalHistory")
    private String medicalHistory;

    @JsonProperty(value = "prescription")
    private String prescription;

    @JsonProperty(value = "doctor")
    private User doctor;

    @JsonProperty(value = "patient")
    private User patient;

    @JsonProperty(value = "service")
    private Service service;

    public long getId() {
        return id;
    }

    public String getVisitCode() {
        return visitCode;
    }

    public String getDate() {
        return date;
    }

    public int getFee() {
        return fee;
    }

    public String getStatus() {
        return status;
    }

    public String getMedicalHistory() {
        return medicalHistory;
    }

    public String getPrescription() {
        return prescription;
    }

    public User getDoctor() {
        return doctor;
    }

    public User getPatient() {
        return patient;
    }

    public Service getService() {
        return service;
    }

    public VisitRequest(long id, String visitCode, String date, int fee, String status, String medicalHistory, String prescription, User doctor, User patient, Service service) {
        this.id = id;
        this.visitCode = visitCode;
        this.date = date;
        this.fee = fee;
        this.status = status;
        this.medicalHistory = medicalHistory;
        this.prescription = prescription;
        this.doctor = doctor;
        this.patient = patient;
        this.service = service;
    }

    public VisitRequest() {
    }

}