package com.kdimitrov.edentist.server.common.services.abstractions;

import com.kdimitrov.edentist.server.common.models.ArchivedAppointment;
import com.kdimitrov.edentist.server.common.models.Doctor;
import com.kdimitrov.edentist.server.common.models.Service;

import java.util.List;

public interface GraphService {
    Doctor createUser(String email,
                      String name,
                      String phone,
                      String specialization,
                      String description,
                      String img);

    Service createService(String type);

    List<ArchivedAppointment> getArchivedAppointments();

    List<ArchivedAppointment> getArchivedAppointments(String patientEmail);
}
