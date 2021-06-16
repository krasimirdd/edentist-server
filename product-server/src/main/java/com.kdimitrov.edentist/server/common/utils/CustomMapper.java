package com.kdimitrov.edentist.server.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.kdimitrov.edentist.server.common.models.Appointment;
import com.kdimitrov.edentist.server.common.models.Doctor;
import com.kdimitrov.edentist.server.common.models.Entity;
import com.kdimitrov.edentist.server.common.models.Patient;
import com.kdimitrov.edentist.server.common.models.Service;
import com.kdimitrov.edentist.server.common.models.rest.AppointmentRequest;

import java.time.LocalDateTime;
import java.time.ZoneId;

import static java.time.Instant.ofEpochMilli;

public class CustomMapper {
    public static String toAppointmentResponse(Appointment entity) {
        try {
            ObjectMapper mapper = new ObjectMapper();

            ObjectNode response = mapper.createObjectNode();
            response.put("id", entity.getId());
            response.put("visitCode", entity.getVisitCode());
            response.put("date", entity.getDate().toString());
            response.put("prescription", entity.getPrescription());
            response.put("medicalHistory", entity.getMedicalHistory());
            response.put("status", entity.getStatus());

            ObjectNode doctor = getDoctorNode(entity.getDoctor(), mapper);
            response.set("doctor", doctor);
            ObjectNode patient = getPatientNode(entity.getPatient(), mapper);
            response.set("patient", patient);
            ObjectNode service = getServiceNode(entity.getService(), mapper);
            response.set("service", service);

            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(response);

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static String toAppointmentProposition(AppointmentRequest nextAvailable, Doctor doctorEntity, Service serviceEntity) {
        try {
            ObjectMapper mapper = new ObjectMapper();

            ObjectNode response = mapper.createObjectNode();
            response.put("date", LocalDateTime.ofInstant(ofEpochMilli(nextAvailable.getTimestamp()), ZoneId.systemDefault())
                    .toString());
            ObjectNode doctorNode = getDoctorNode(doctorEntity, mapper);
            response.set("doctor", doctorNode);
            ObjectNode serviceNode = getServiceNode(serviceEntity, mapper);
            response.set("service", serviceNode);

            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(response);

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static String toAdminDtoString(String superadminEmail) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode response = getSuperadminNode(superadminEmail, mapper);
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
            return getDoctorNode((Doctor) t, mapper).put("role", "doctor");
        } else if (t instanceof Patient) {
            return getPatientNode((Patient) t, mapper).put("role", "patient");
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

    private static ObjectNode getSuperadminNode(String email, ObjectMapper mapper) {
        ObjectNode response = mapper.createObjectNode();
        response.put("email", email);
        response.put("role", "admin");

        return response;
    }

    private static ObjectNode getDoctorNode(Doctor doctorEntity, ObjectMapper mapper) {
        ObjectNode objectNode = mapper.createObjectNode();
        objectNode.put("id", doctorEntity.getId());
        objectNode.put("name", doctorEntity.getName());
        objectNode.put("phone", doctorEntity.getPhone());
        objectNode.put("email", doctorEntity.getEmail());
        objectNode.put("description", doctorEntity.getDescription());
        objectNode.put("specialization", doctorEntity.getSpecialization());
        objectNode.put("img", doctorEntity.getImg());

        return objectNode;
    }

    private static ObjectNode getPatientNode(Patient patientEntity, ObjectMapper mapper) {
        ObjectNode objectNode = mapper.createObjectNode();
        objectNode.put("id", patientEntity.getId());
        objectNode.put("email", patientEntity.getEmail());
        objectNode.put("name", patientEntity.getName());
        objectNode.put("phone", patientEntity.getPhone());
        objectNode.put("blood", patientEntity.getBloodType());

        return objectNode;
    }

    private static ObjectNode getServiceNode(Service serviceEntity, ObjectMapper mapper) {
        ObjectNode objectNode = mapper.createObjectNode();
        objectNode.put("id", serviceEntity.getId());
        objectNode.put("type", serviceEntity.getType());

        return objectNode;
    }
}
