package com.kdimitrov.rabbitmq.common.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize
@JsonSerialize
@JsonPOJOBuilder
public class Body implements Serializable {
    @JsonProperty(value = "id")
    private long id;

    @JsonProperty(value = "visitCode")
    private String visitCode;

    @JsonProperty(value = "date")
//    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
//    @JsonSerialize(using = StringSerializer.class)
//    @JsonDeserialize(using = StringDeserializer.class)
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

    public Body() {
        super();
    }

    public Body(long id, String visitCode, String date, String status, String medicalHistory, String prescription, User doctor, User patient, Service service) {
        this.id = id;
        this.visitCode = visitCode;
        this.date = date;
        this.status = status;
        this.medicalHistory = medicalHistory;
        this.prescription = prescription;
        this.doctor = doctor;
        this.patient = patient;
        this.service = service;
    }


//    public static class Builder {
//
//        private long id;
//        private String visitCode;
//        private String date;
//        private int fee;
//        private String status;
//        private String medicalHistory;
//        private String prescription;
//        private User doctor;
//        private User patient;
//        private Service service;
//
//        public Builder() {
//            super();
//        }
//
//        public void setId(long id) {
//            this.id = id;
//        }
//
//        public void setVisitCode(String visitCode) {
//            this.visitCode = visitCode;
//        }
//
//        public void setDate(String date) {
//            this.date = date;
//        }
//
//        public void setFee(int fee) {
//            this.fee = fee;
//        }
//
//        public void setStatus(String status) {
//            this.status = status;
//        }
//
//        public void setMedicalHistory(String medicalHistory) {
//            this.medicalHistory = medicalHistory;
//        }
//
//        public void setPrescription(String prescription) {
//            this.prescription = prescription;
//        }
//
//        public void setDoctor(User doctor) {
//            this.doctor = doctor;
//        }
//
//        public void setPatient(User patient) {
//            this.patient = patient;
//        }
//
//        public void setService(Service service) {
//            this.service = service;
//        }
//
//        public Body build() {
//            return new Body(id, visitCode, date, fee, status, medicalHistory, prescription, doctor, patient, service);
//        }
//    }
}
