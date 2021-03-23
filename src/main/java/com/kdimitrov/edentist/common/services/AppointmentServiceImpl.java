package com.kdimitrov.edentist.common.services;

import com.kdimitrov.edentist.common.exceptions.NotFound;
import com.kdimitrov.edentist.common.exceptions.OperationUnsuccessful;
import com.kdimitrov.edentist.common.models.Appointment;
import com.kdimitrov.edentist.common.models.Doctor;
import com.kdimitrov.edentist.common.models.Patient;
import com.kdimitrov.edentist.common.models.Service;
import org.springframework.http.HttpStatus;
import com.kdimitrov.edentist.common.models.dto.AppointmentDto;
import com.kdimitrov.edentist.common.models.dto.DoctorDto;
import com.kdimitrov.edentist.common.models.dto.PatientDto;
import com.kdimitrov.edentist.common.models.dto.ServiceDto;
import com.kdimitrov.edentist.common.models.rest.AppointmentRequest;
import com.kdimitrov.edentist.common.repository.AppointmentsRepository;
import com.kdimitrov.edentist.common.repository.DoctorsRepository;
import com.kdimitrov.edentist.common.repository.PatientRepository;
import com.kdimitrov.edentist.common.repository.ServicesRepository;
import com.kdimitrov.edentist.common.utils.CustomMapper;
import com.kdimitrov.edentist.common.utils.ObjectMapperUtils;
import javassist.NotFoundException;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.ResponseEntity;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

import static com.kdimitrov.edentist.common.utils.WorkableLocalDateTimeUtil.workable;
import static java.time.Instant.ofEpochMilli;
import static java.time.LocalDateTime.ofInstant;
import static java.time.ZoneId.systemDefault;

@org.springframework.stereotype.Service
public class AppointmentServiceImpl implements AppointmentService {

    final AppointmentsRepository appointmentsRepository;
    final DoctorsRepository doctorsRepository;
    final PatientRepository patientRepository;
    final ServicesRepository serviceRepository;

    public AppointmentServiceImpl(AppointmentsRepository appointmentsRepository,
                                  DoctorsRepository doctorsRepository,
                                  PatientRepository patientRepository,
                                  ServicesRepository serviceRepository) {
        this.appointmentsRepository = appointmentsRepository;
        this.doctorsRepository = doctorsRepository;
        this.patientRepository = patientRepository;
        this.serviceRepository = serviceRepository;
    }

    @Override
    public List<AppointmentDto> find(String filter, String userEmail) {
        return Strings.isEmpty(filter) && Strings.isEmpty(userEmail)
               ? getAll()
               : getFiltered(filter, userEmail);
    }

    @Override
    public ResponseEntity<String> save(AppointmentRequest request) throws NotFoundException {

        Doctor doctor = doctorsRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new NotFoundException("No record for " + request.getDoctorId()));
        Service service = serviceRepository.findById(request.getServiceId())
                .orElseThrow(() -> new NotFoundException("No record for " + request.getServiceId()));

        if (!isAvailable(request)) {
            return new ResponseEntity<>(
                    CustomMapper.toAppointmentProposition(this.getNextAvailable(request), doctor, service),
                    HttpStatus.ACCEPTED);
        }

        Optional<Patient> patientOpt = patientRepository.findByEmail(request.getEmail());
        Patient patient = patientOpt.orElseGet(() -> patientRepository.save(new Patient(request)));

        DoctorDto doctorDto = ObjectMapperUtils.map(doctor, new DoctorDto());
        ServiceDto serviceDto = ObjectMapperUtils.map(service, new ServiceDto());
        PatientDto patientDto = ObjectMapperUtils.map(patient, new PatientDto());
        AppointmentDto dto = new AppointmentDto(request, doctorDto, serviceDto, patientDto);

        Appointment entity = appointmentsRepository.save(
                ObjectMapperUtils.map(dto, Appointment.class)
        );

        return new ResponseEntity<>(CustomMapper.toAppointmentResponse(entity), HttpStatus.OK);
    }

    private AppointmentRequest getNextAvailable(AppointmentRequest request) {
        LocalDateTime nextAvailableDateTime = getNextAvailableRecursive(
                workable(ofInstant(ofEpochMilli(request.getTimestamp()), systemDefault()).plusMinutes(30)),
                request.getDoctorId()
        );

        request.setTimestamp(Timestamp.valueOf(nextAvailableDateTime).getTime());
        return request;
    }

    private LocalDateTime getNextAvailableRecursive(LocalDateTime date, long remedial) {
        boolean isBooked = appointmentsRepository
                .findByDateEqualsAndDoctorId(date, remedial)
                .isPresent();
        if (isBooked) {
            getNextAvailableRecursive(date.plusMinutes(30), remedial);
        }

        return date;
    }

    private boolean isAvailable(AppointmentRequest request) {
        return !this.appointmentsRepository
                .findByDateEqualsAndDoctorId(
                        ofInstant(ofEpochMilli(request.getTimestamp()), systemDefault()),
                        request.getDoctorId())
                .isPresent();
    }

    @Override
    public String update(Appointment request, long id) throws NotFoundException {

        validateRequest(request, id);

        Optional<Appointment> persistedOpt = appointmentsRepository.findById(id);
        if (persistedOpt.isPresent()) {
            Appointment persisted = persistedOpt.get();
            if (!persisted.getDate().equals(request.getDate())) {
                persisted.setDate(request.getDate());
            }
            persisted.setDoctor(request.getDoctor());
            persisted.setPatient(request.getPatient());
            persisted.setPrescription(request.getPrescription());
            persisted.setMedicalHistory(request.getMedicalHistory());
            if (persisted.getService().getId() != request.getService().getId()) {
                persisted.setService(serviceRepository.findById(request.getService().getId())
                                             .orElseThrow(() -> new NotFound("Invalid service provided")));
            }

            persisted.setStatus(request.getStatus());
            persisted.setFee(request.getFee());

            Appointment entity = appointmentsRepository.saveAndFlush(persisted);
            return CustomMapper.toAppointmentResponse(entity);
        }
        return "not_found";
    }

    @Override
    public void delete(long id) {

        if (appointmentsRepository.findById(id).isPresent()) {
            appointmentsRepository.deleteById(id);
            if (appointmentsRepository.existsById(id)) {
                throw new OperationUnsuccessful();
            }
        } else {
            throw new NotFound("No record for " + id);
        }
    }

    private List<AppointmentDto> getFiltered(String filter, String userEmail) {

        boolean shouldFilterByUser = Strings.isNotBlank(userEmail);
        List<AppointmentDto> filteredByUser = new ArrayList<>();
        if (shouldFilterByUser) {
            filteredByUser.addAll(this.findByUser(userEmail));
        }

        if (Strings.isEmpty(filter)) {
            return filteredByUser;
        }

        // status:appointment,pending
        String[] tokens = filter.split(":");
        switch (tokens[0]) {
            case "status":
                return filterByStatus(shouldFilterByUser, filteredByUser, tokens[1]);

            default:
                return Collections.emptyList();
        }

    }

    private List<AppointmentDto> getAll() {
        return mapToDtoList(appointmentsRepository.findAll());
    }

    private List<AppointmentDto> findByUser(String email) {
        Optional<Doctor> doctorOpt = doctorsRepository.findByEmail(email);
        boolean isDoctor = doctorOpt.isPresent();

        Optional<Patient> patientOpt = patientRepository.findByEmail(email);
        boolean isPatient = patientOpt.isPresent();

        List<Appointment> appointments = new ArrayList<>();
        if (isDoctor) {
            appointments.addAll(appointmentsRepository.findAllByDoctorId(doctorOpt.get().getId()));
        } else if (isPatient) {
            appointments.addAll(appointmentsRepository.findAllByPatientId(patientOpt.get().getId()));
        } else {
            throw new NotFound("No user found");
        }

        return mapToDtoList(appointments);
    }

    private List<AppointmentDto> filterByStatus(boolean filterByUser, List<AppointmentDto> byUser, String token) {

        List<AppointmentDto> result = new ArrayList<>();
        BiPredicate<AppointmentDto, String> filterByStatusPredicate = (appointment, status) ->
                appointment.getStatus().equalsIgnoreCase(status);

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
                result.addAll(
                        mapToDtoList(appointmentsRepository.findAllByStatus(status))
                );
            }
        }
        return result;
    }

    private void validateRequest(Appointment request, long id) throws NotFoundException {
        if (request.getId() != id) {
            throw new NotFoundException("No record for " + id);
        }

        doctorsRepository.findById(request.getDoctor().getId())
                .orElseThrow(() -> new NotFoundException("No record for " + request.getDoctor().getId()));
        serviceRepository.findById(request.getService().getId())
                .orElseThrow(() -> new NotFoundException("No record for " + request.getService().getId()));
        patientRepository.findByEmail(request.getPatient().getEmail())
                .orElseThrow(() -> new NotFoundException("No record for " + request.getPatient().getEmail()));
    }

    private List<AppointmentDto> mapToDtoList(List<Appointment> allByStatus) {
        return allByStatus
                .stream()
                .map((e) -> ObjectMapperUtils.map(e, AppointmentDto.class))
                .collect(Collectors.toList());
    }
}
