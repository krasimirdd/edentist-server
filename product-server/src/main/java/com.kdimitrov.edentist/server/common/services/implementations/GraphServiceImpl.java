package com.kdimitrov.edentist.server.common.services.implementations;

import com.kdimitrov.edentist.server.common.models.ArchivedAppointment;
import com.kdimitrov.edentist.server.common.models.Doctor;
import com.kdimitrov.edentist.server.common.models.Patient;
import com.kdimitrov.edentist.server.common.repository.ArchivedAppointmentsRepository;
import com.kdimitrov.edentist.server.common.repository.DoctorsRepository;
import com.kdimitrov.edentist.server.common.repository.PatientRepository;
import com.kdimitrov.edentist.server.common.repository.ServicesRepository;
import com.kdimitrov.edentist.server.common.services.abstractions.GraphService;
import com.kdimitrov.edentist.server.common.utils.GraphApiExecutor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class GraphServiceImpl implements GraphService {
    private final GraphApiExecutor graphExecutor;

    private final DoctorsRepository doctorsRepository;
    private final ServicesRepository servicesRepository;
    private final ArchivedAppointmentsRepository archivedAppointmentsRepository;
    private final PatientRepository patientRepository;

    public GraphServiceImpl(GraphApiExecutor graphExecutor,
                            DoctorsRepository doctorsRepository,
                            ServicesRepository servicesRepository,
                            ArchivedAppointmentsRepository archivedAppointmentsRepository,
                            PatientRepository patientRepository) {
        this.graphExecutor = graphExecutor;
        this.doctorsRepository = doctorsRepository;
        this.servicesRepository = servicesRepository;
        this.archivedAppointmentsRepository = archivedAppointmentsRepository;
        this.patientRepository = patientRepository;
    }

    @Override
    public Doctor createUser(String email,
                             String name,
                             String phone,
                             String specialization,
                             String description,
                             String img) {
        Doctor doctor = new Doctor();

        doctor.setEmail(email);
        doctor.setName(name);
        doctor.setPhone(phone);
        doctor.setSpecialization(specialization);
        doctor.setDescription(description);
        doctor.setImg(img);

        return graphExecutor.execute(() -> doctorsRepository.saveAndFlush(doctor),
                                     GraphApiExecutor.Method.CREATE);
    }

    @Override
    public com.kdimitrov.edentist.server.common.models.Service createService(String type) {
        com.kdimitrov.edentist.server.common.models.Service service =
                new com.kdimitrov.edentist.server.common.models.Service();

        service.setType(type);

        return graphExecutor.execute(() -> servicesRepository.saveAndFlush(service),
                                     GraphApiExecutor.Method.CREATE);
    }

    @Override
    public List<ArchivedAppointment> getArchivedAppointments() {
        return graphExecutor.execute(archivedAppointmentsRepository::findAll,
                                     GraphApiExecutor.Method.GET);
    }

    @Override
    public List<ArchivedAppointment> getArchivedAppointments(String patientEmail) {
        Optional<Patient> entityOpt =
                graphExecutor.execute(() -> patientRepository.findByEmail(patientEmail),
                                      GraphApiExecutor.Method.GET);

        List<ArchivedAppointment> result = new ArrayList<>();
        entityOpt.ifPresent(e -> result.addAll(
                graphExecutor.execute(() -> archivedAppointmentsRepository.findAllByPatientId(e.getId()),
                                      GraphApiExecutor.Method.FILTER)));
        return result;
    }
}
