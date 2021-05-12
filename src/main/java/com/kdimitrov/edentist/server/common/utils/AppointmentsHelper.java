package com.kdimitrov.edentist.server.common.utils;

import com.kdimitrov.edentist.server.common.exceptions.NotFound;
import com.kdimitrov.edentist.server.common.models.Appointment;
import com.kdimitrov.edentist.server.common.models.Doctor;
import com.kdimitrov.edentist.server.common.models.Entity;
import com.kdimitrov.edentist.server.common.models.Patient;
import com.kdimitrov.edentist.server.common.models.dto.AppointmentDto;
import com.kdimitrov.edentist.server.common.models.rest.AppointmentRequest;
import com.kdimitrov.edentist.server.common.repository.AppointmentsRepository;
import com.kdimitrov.edentist.server.common.services.implementations.DoctorsService;
import com.kdimitrov.edentist.server.common.services.implementations.PatientService;
import com.kdimitrov.edentist.server.common.services.implementations.ServicesService;
import org.apache.logging.log4j.util.Strings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

public class AppointmentsHelper {

    private final DoctorsService doctorsService;
    private final PatientService patientsService;
    private final ServicesService servicesService;

    public AppointmentsHelper(DoctorsService doctorsService,
                              PatientService patientsService,
                              ServicesService servicesService) {

        this.servicesService = servicesService;
        this.patientsService= patientsService;
        this.doctorsService = doctorsService;
    }

    public static boolean updatePatientInfoIfNeeded(AppointmentRequest request, Patient patient) {
        boolean shouldUpdate = false;

        if (patient.getPhone() == null || !patient.getPhone().equals(request.getPhone())) {
            patient.setPhone(request.getPhone());
            shouldUpdate = true;
        }
        if (patient.getName() == null || !patient.getName().equals(request.getName())) {
            patient.setName(request.getName());
            shouldUpdate = true;
        }

        return shouldUpdate;
    }

    public <T extends AppointmentsRepository<? extends Entity>> List<AppointmentDto> getFiltered(String filter,
                                                                                                 String userEmail,
                                                                                                 T repository) throws NotFound {

        boolean shouldFilterByUser = Strings.isNotBlank(userEmail);
        List<AppointmentDto> filteredByUser = new ArrayList<>();
        if (shouldFilterByUser) {
            filteredByUser.addAll(findByUser(userEmail, repository));
        }

        if (Strings.isEmpty(filter)) {
            return filteredByUser;
        }

        // status:appointment,pending
        String[] tokens = filter.split(":");
        if ("status" .equals(tokens[0])) {
            return filterByStatus(shouldFilterByUser, filteredByUser, tokens[1], repository);
        }
        return Collections.emptyList();

    }

    @SuppressWarnings("rawtypes")
    private <T extends AppointmentsRepository<? extends Entity>> List<AppointmentDto> findByUser(String email,
                                                                                                 T repository) throws NotFound {

        Optional<Doctor> doctorOpt = doctorsService.findByEmailOpt(email);
        boolean isDoctor = doctorOpt.isPresent();

        Optional<Patient> patientOpt = patientsService.findByEmailOpt(email);
        boolean isPatient = patientOpt.isPresent();

        List appointments = new ArrayList();
        if (isDoctor) {
            appointments.addAll(repository.findAllByDoctorId(doctorOpt.get().getId()));
        } else if (isPatient) {
            appointments.addAll(repository.findAllByPatientId(patientOpt.get().getId()));
        } else {
            throw new NotFound("No user found");
        }

        return ObjectMapperUtils.mapAll(appointments, AppointmentDto.class);
    }

    private <T extends AppointmentsRepository<? extends Entity>> List<AppointmentDto> filterByStatus(boolean filterByUser,
                                                                                                     List<AppointmentDto> byUser,
                                                                                                     String token,
                                                                                                     T repository) {
        List<AppointmentDto> result = new ArrayList<>();
        BiPredicate<AppointmentDto, String> filterByStatusPredicate =
                (appointment, status) -> appointment.getStatus().equalsIgnoreCase(status);

        // appointment,pending
        String[] statusArray = token.split(",");

        for (String status : statusArray) {
            if (filterByUser) {
                result.addAll(
                        byUser.stream()
                                .filter(a -> filterByStatusPredicate.test(a, status))
                                .collect(Collectors.toList())
                );
            } else {
                result.addAll(ObjectMapperUtils.mapAll(repository.findAllByStatus(status), AppointmentDto.class));
            }
        }
        return result;
    }

    public void validateRequest(Appointment request, long id) throws NotFound {
        if (request.getId() != id) {
            throw new NotFound("No record for " + id);
        }

        doctorsService.findById(request.getDoctor().getId());
        servicesService.findById(request.getService().getId());
        patientsService.findByEmail(request.getPatient().getEmail());
    }

}
