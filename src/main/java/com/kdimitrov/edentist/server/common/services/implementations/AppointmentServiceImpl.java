package com.kdimitrov.edentist.server.common.services.implementations;

import com.kdimitrov.edentist.server.common.exceptions.NotFound;
import com.kdimitrov.edentist.server.common.exceptions.OperationUnsuccessful;
import com.kdimitrov.edentist.server.common.models.Appointment;
import com.kdimitrov.edentist.server.common.models.Doctor;
import com.kdimitrov.edentist.server.common.models.Patient;
import com.kdimitrov.edentist.server.common.models.Service;
import com.kdimitrov.edentist.server.common.models.dto.AppointmentDto;
import com.kdimitrov.edentist.server.common.models.dto.DoctorDto;
import com.kdimitrov.edentist.server.common.models.dto.PatientDto;
import com.kdimitrov.edentist.server.common.models.dto.ServiceDto;
import com.kdimitrov.edentist.server.common.models.rest.AppointmentRequest;
import com.kdimitrov.edentist.server.common.repository.ArchiveAppointmentsRepository;
import com.kdimitrov.edentist.server.common.repository.DoctorsRepository;
import com.kdimitrov.edentist.server.common.repository.PatientRepository;
import com.kdimitrov.edentist.server.common.repository.PresentAppointmentsRepository;
import com.kdimitrov.edentist.server.common.repository.ServicesRepository;
import com.kdimitrov.edentist.server.common.services.AppointmentService;
import com.kdimitrov.edentist.server.common.utils.AppointmentsHelper;
import com.kdimitrov.edentist.server.common.utils.CustomMapper;
import com.kdimitrov.edentist.server.common.utils.ObjectMapperUtils;
import javassist.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.kdimitrov.edentist.server.common.utils.AppointmentsHelper.updatePatientInfoIfNeeded;
import static com.kdimitrov.edentist.server.common.utils.WorkableLocalDateTimeUtil.workable;
import static java.time.Instant.ofEpochMilli;
import static java.time.LocalDateTime.ofInstant;
import static java.time.ZoneId.systemDefault;

@org.springframework.stereotype.Service
public class AppointmentServiceImpl implements AppointmentService {

    private final PresentAppointmentsRepository appointmentsRepository;
    private final DoctorsRepository doctorsRepository;
    private final PatientRepository patientRepository;
    private final ServicesRepository serviceRepository;
    private final ArchiveAppointmentsRepository archivedAppointmentsRepository;
    private final AppointmentsHelper helper;

    public AppointmentServiceImpl(PresentAppointmentsRepository appointmentsRepository,
                                  DoctorsRepository doctorsRepository,
                                  PatientRepository patientRepository,
                                  ServicesRepository serviceRepository,
                                  ArchiveAppointmentsRepository archivedAppointmentsRepository) {
        this.appointmentsRepository = appointmentsRepository;
        this.doctorsRepository = doctorsRepository;
        this.patientRepository = patientRepository;
        this.serviceRepository = serviceRepository;
        this.archivedAppointmentsRepository = archivedAppointmentsRepository;
        helper = new AppointmentsHelper(doctorsRepository,
                                        patientRepository,
                                        serviceRepository);
    }

    @Override
    public List<AppointmentDto> filterAppointments(String filter, String userEmail) {
        return helper.getFiltered(filter, userEmail, appointmentsRepository);
    }

    @Override
    public List<AppointmentDto> filterArchivedAppointments(String filter, String userEmail) {
        return helper.getFiltered(filter, userEmail, archivedAppointmentsRepository);
    }

    @Override
    public AppointmentDto findSingleAppointment(String userEmail, String code) throws NotFound {
        return helper.getFiltered("", userEmail, archivedAppointmentsRepository)
                .stream()
                .filter(appointment -> appointment.getVisitCode().equals(code))
                .findFirst()
                .orElseThrow(() -> new NotFound("No record for provided code"));
    }

    @Override
    public ResponseEntity<String> saveAppointment(AppointmentRequest request) throws NotFound {

        Doctor doctor = doctorsRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new NotFound("No record for " + request.getDoctorId()));
        Service service = serviceRepository.findById(request.getServiceId())
                .orElseThrow(() -> new NotFound("No record for " + request.getServiceId()));

        if (!isAvailable(request)) {
            return new ResponseEntity<>(
                    CustomMapper.toAppointmentProposition(this.getNextAvailable(request), doctor, service),
                    HttpStatus.ACCEPTED);
        }

        Optional<Patient> patientOpt = patientRepository.findByEmail(request.getEmail());
        Patient patient = patientOpt.orElseGet(() -> patientRepository.save(new Patient(request)));
        if (updatePatientInfoIfNeeded(request, patient)) {
            patientRepository.saveAndFlush(patient);
        }

        DoctorDto doctorDto = ObjectMapperUtils.map(doctor, new DoctorDto());
        ServiceDto serviceDto = ObjectMapperUtils.map(service, new ServiceDto());
        PatientDto patientDto = ObjectMapperUtils.map(patient, new PatientDto());
        AppointmentDto dto = new AppointmentDto(request, doctorDto, serviceDto, patientDto);

        Appointment entity = appointmentsRepository.save(
                ObjectMapperUtils.map(dto, Appointment.class)
        );

        return new ResponseEntity<>(CustomMapper.toAppointmentResponse(entity), HttpStatus.OK);
    }

    @Override
    public String updateAppointment(Appointment request, long id) throws NotFound {

        helper.validateRequest(request, id);

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

            patientRepository.saveAndFlush(request.getPatient());
            Appointment entity = appointmentsRepository.saveAndFlush(persisted);
            return CustomMapper.toAppointmentResponse(entity);
        }
        return "not_found";
    }

    @Override
    public String deleteAppointment(long id) throws NotFound {
        Optional<Appointment> byId = appointmentsRepository.findById(id);

        if (byId.isPresent()) {
            appointmentsRepository.deleteById(id);
            if (appointmentsRepository.existsById(id)) {
                throw new OperationUnsuccessful();
            }

            return CustomMapper.toAppointmentResponse(byId.get());
        } else {
            throw new NotFound("No record for " + id);
        }
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
}
