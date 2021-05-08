package com.kdimitrov.edentist.server.common.models.rest;

import com.kdimitrov.edentist.server.common.models.dto.DoctorDto;
import com.kdimitrov.edentist.server.common.models.dto.PatientDto;
import com.kdimitrov.edentist.server.common.models.dto.ServiceDto;

import java.io.Serializable;
import java.time.LocalDateTime;

public class AppointmentResponse implements Serializable {

    private String visitCode;
    private LocalDateTime dateTime;
    private String prescription;
    private String medicalHistory;
    private String status;
    private ServiceDto serviceId;
    private PatientDto patientId;
    private DoctorDto doctorId;

    public AppointmentResponse(String visitCode,
                               LocalDateTime dateTime,
                               String prescription,
                               String medicalHistory,
                               String status,
                               ServiceDto serviceId,
                               PatientDto patientId,
                               DoctorDto doctorId) {
        this.visitCode = visitCode;
        this.dateTime = dateTime;
        this.prescription = prescription;
        this.medicalHistory = medicalHistory;
        this.status = status;
        this.serviceId = serviceId;
        this.patientId = patientId;
        this.doctorId = doctorId;
    }

    public AppointmentResponse() {
    }

    public String getVisitCode() {
        return visitCode;
    }

    public void setVisitCode(String visitCode) {
        this.visitCode = visitCode;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
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

    public ServiceDto getServiceId() {
        return serviceId;
    }

    public void setServiceId(ServiceDto serviceId) {
        this.serviceId = serviceId;
    }

    public PatientDto getPatientId() {
        return patientId;
    }

    public void setPatientId(PatientDto patientId) {
        this.patientId = patientId;
    }

    public DoctorDto getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(DoctorDto doctorId) {
        this.doctorId = doctorId;
    }
}
