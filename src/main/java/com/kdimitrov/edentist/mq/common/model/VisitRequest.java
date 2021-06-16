package com.kdimitrov.edentist.mq.common.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.kdimitrov.edentist.server.common.models.Action;

import java.io.Serializable;

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

    @JsonProperty(value = "action")
    private String action;

    public long getId() {
        return id;
    }

    public String getVisitCode() {
        return visitCode;
    }

    public String getDate() {
        return date;
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

    public Action getAction() {
        return Action.valueOf(action);
    }

    public void setAction(String  action) {
        this.action = action;
    }

    public VisitRequest(long id,
                        String visitCode,
                        String date,
                        String status,
                        String medicalHistory,
                        String prescription,
                        User doctor,
                        User patient,
                        Service service,
                        String action) {
        this.id = id;
        this.visitCode = visitCode;
        this.date = date;
        this.status = status;
        this.medicalHistory = medicalHistory;
        this.prescription = prescription;
        this.doctor = doctor;
        this.patient = patient;
        this.service = service;
        this.action = action;
    }

    public VisitRequest() {
    }

    @Override
    public String toString() {
        return "VisitRequest{" +
                "id=" + id +
                ", visitCode='" + visitCode + '\'' +
                ", date='" + date + '\'' +
                ", status='" + status + '\'' +
                ", medicalHistory='" + medicalHistory + '\'' +
                ", prescription='" + prescription + '\'' +
                ", doctor=" + doctor +
                ", patient=" + patient +
                ", service=" + service +
                ", action=" + action +
                '}';
    }
}