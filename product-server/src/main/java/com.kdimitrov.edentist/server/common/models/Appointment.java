package com.kdimitrov.edentist.server.common.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Random;

@Entity(name = "appointment")
@Table(name = "appointment")
public class Appointment extends com.kdimitrov.edentist.server.common.models.Entity {

    @Column(name = "visit_code")
    private String visitCode;

    @Column(name = "date")
    private LocalDateTime date;

    @Column(name = "prescription")
    private String prescription;

    @Column(name = "medical_history")
    private String medicalHistory;

    @JoinColumn(name = "status", referencedColumnName = "name")
    private String status;

    @ManyToOne
    @JoinColumn(name = "service_type", referencedColumnName = "type")
    private Service service;

    @ManyToOne
    @JoinColumn(name = "patient_id", referencedColumnName = "id")
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    public Appointment() {
    }

    public String getVisitCode() {
        return visitCode;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public String getPrescription() {
        return prescription;
    }

    public String getMedicalHistory() {
        return medicalHistory;
    }

    public String getStatus() {
        return status;
    }

    public Service getService() {
        return service;
    }

    public Patient getPatient() {
        return patient;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setVisitCode(String visitCode) {
        this.visitCode = visitCode;
    }

    public void setDate(LocalDateTime dateTime) {
        this.date = dateTime;
    }

    public void setPrescription(String prescription) {
        this.prescription = prescription;
    }

    public void setMedicalHistory(String medicalHistory) {
        this.medicalHistory = medicalHistory;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    @PrePersist
    public void prePersist() {
        if (visitCode == null) {
            Random rnd = new Random();
            int number = rnd.nextInt(999999);
            // this will convert any number sequence into 6 character.
            visitCode = String.format("%06d", number);
        }
        if (status == null) {
            status = Status.Type.PENDING.name();
        }
    }
}
