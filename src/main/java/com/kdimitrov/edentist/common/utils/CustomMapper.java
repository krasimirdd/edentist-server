package com.kdimitrov.edentist.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.kdimitrov.edentist.common.models.Appointment;
import com.kdimitrov.edentist.common.models.Doctor;
import com.kdimitrov.edentist.common.models.Entity;
import com.kdimitrov.edentist.common.models.Patient;
import com.kdimitrov.edentist.common.models.Service;
import com.kdimitrov.edentist.common.models.rest.AppointmentRequest;
import com.kdimitrov.edentist.config.ApplicationConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;

import static java.time.Instant.ofEpochMilli;

public class CustomMapper {
    public static String toAppointmentResponse(Appointment entity) {
        try {
            // create `ObjectMapper` instance
            ObjectMapper mapper = new ObjectMapper();

            // create a JSON object
            ObjectNode response = mapper.createObjectNode();
            response.put("id", entity.getId());
            response.put("visitCode", entity.getVisitCode());
            response.put("date", entity.getDate().toString());
            response.put("prescription", entity.getPrescription());
            response.put("medicalHistory", entity.getMedicalHistory());
            response.put("status", entity.getStatus());

            // create a child JSON object
            ObjectNode doctor = mapper.createObjectNode();
            Doctor doctorEntity = entity.getDoctor();
            doctor.put("id", doctorEntity.getId());
            doctor.put("name", doctorEntity.getName());
            doctor.put("phone", doctorEntity.getPhone());
            doctor.put("email", doctorEntity.getEmail());
            response.set("doctor", doctor);

            // create a child JSON object
            ObjectNode patient = mapper.createObjectNode();
            Patient patientEntity = entity.getPatient();
            patient.put("id", patientEntity.getId());
            patient.put("name", patientEntity.getName());
            patient.put("phone", patientEntity.getPhone());
            patient.put("email", patientEntity.getEmail());
            response.set("patient", patient);

            ObjectNode service = mapper.createObjectNode();
            Service serviceEntity = entity.getService();
            service.put("id", serviceEntity.getId());
            service.put("type", serviceEntity.getType());
            response.set("service", service);

            // convert `ObjectNode` to pretty-print JSON

            // print json
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(response);

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static String toAppointmentProposition(AppointmentRequest nextAvailable, Doctor doctorEntity, Service serviceEntity) {
        try {
            // create `ObjectMapper` instance
            ObjectMapper mapper = new ObjectMapper();

            // create a JSON object
            ObjectNode response = mapper.createObjectNode();
            response.put("date", LocalDateTime.ofInstant(ofEpochMilli(nextAvailable.getTimestamp()), ZoneId.systemDefault())
                    .toString());

            // create a child JSON object
            ObjectNode doctorNode = mapper.createObjectNode();
            doctorNode.put("id", doctorEntity.getId());
            doctorNode.put("name", doctorEntity.getName());
            doctorNode.put("phone", doctorEntity.getPhone());
            doctorNode.put("email", doctorEntity.getEmail());
            response.set("doctor", doctorNode);

            ObjectNode serviceNode = mapper.createObjectNode();
            serviceNode.put("id", serviceEntity.getId());
            serviceNode.put("type", serviceEntity.getType());
            response.set("service", serviceNode);

            // print json
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(response);

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static String toAdminDtoString(String superadminEmail) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode response = toSuperadmin(superadminEmail, mapper);
            // print json
            return toUserDtoString(response);

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static <T extends Entity> String toUserDtoString(T t) {
        try {
            ObjectNode response = getJsonNodes(t);
            // print json
            return toUserDtoString(response);

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static <T extends Entity> ObjectNode toUserDtoObject(T t) {
        try {
            ObjectNode response = getJsonNodes(t);
            // print json
            return response;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private static <T extends Entity> ObjectNode getJsonNodes(T t) {
        ObjectMapper mapper = new ObjectMapper();

        if (t instanceof Doctor) {
            return toDoctor((Doctor) t, mapper);
        } else if (t instanceof Patient) {
            return toPatient((Patient) t, mapper);
        }

        return null;
    }

    private static String toUserDtoString(ObjectNode node) {
        ObjectMapper mapper = new ObjectMapper();

        // print json
        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(node);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static ObjectNode toPatient(Patient entity, ObjectMapper mapper) {
        ObjectNode response = mapper.createObjectNode();
        response.put("id", entity.getId());
        response.put("email", entity.getEmail());
        response.put("name", entity.getName());
        response.put("phone", entity.getPhone());
        response.put("blood", entity.getBloodType());
        response.put("role", "patient");

        return response;
    }

    private static ObjectNode toDoctor(Doctor entity, ObjectMapper mapper) {
        ObjectNode response = mapper.createObjectNode();
        response.put("id", entity.getId());
        response.put("email", entity.getEmail());
        response.put("name", entity.getName());
        response.put("phone", entity.getPhone());
        response.put("role", "doctor");

        return response;
    }

    private static ObjectNode toSuperadmin(String email, ObjectMapper mapper) {
        ObjectNode response = mapper.createObjectNode();
        response.put("email", email);
        response.put("role", "admin");

        return response;
    }
}
