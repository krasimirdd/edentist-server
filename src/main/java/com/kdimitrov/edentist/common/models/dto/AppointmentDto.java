package com.kdimitrov.edentist.common.models.dto;

import com.kdimitrov.edentist.common.models.Doctor;
import com.kdimitrov.edentist.common.models.Patient;
import com.kdimitrov.edentist.common.models.Service;
import com.kdimitrov.edentist.common.models.rest.AppointmentRequest;
import com.kdimitrov.edentist.common.utils.ObjectMapperUtils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.TimeZone;

public class AppointmentDto extends DTOEntity {
    private long id;
    private String visitCode;
    private LocalDateTime date;
    private String prescription;
    private String medicalHistory;
    private String status;
    private Service service;
    private Patient patient;
    private Doctor doctor;

    public AppointmentDto() {
    }

    public AppointmentDto(AppointmentRequest request, DoctorDto doctor, ServiceDto service, PatientDto patient) {
        this.date = LocalDateTime.ofInstant(Instant.ofEpochMilli(request.getTimestamp()),
                                            TimeZone.getDefault().toZoneId());
        this.prescription = "";
        this.medicalHistory = "";
        this.service = ObjectMapperUtils.map(service, Service.class);
        this.patient = ObjectMapperUtils.map(patient, Patient.class);
        this.doctor = ObjectMapperUtils.map(doctor, Doctor.class);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getVisitCode() {
        return visitCode;
    }

    public void setVisitCode(String visitCode) {
        this.visitCode = visitCode;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getPrescription() {
        return prescription;
    }

    public void setPrescription(String prescription) {
        this.prescription = prescription;
    }

    public String getMedicalHistory() {
        return medicalHistory;
    }

    public void setMedicalHistory(String medicalHistory) {
        this.medicalHistory = medicalHistory;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }
}
