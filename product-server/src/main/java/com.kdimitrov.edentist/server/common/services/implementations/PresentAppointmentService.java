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
import com.kdimitrov.edentist.server.common.repository.PresentAppointmentsRepository;
import com.kdimitrov.edentist.server.common.services.abstractions.AppointmentService;
import com.kdimitrov.edentist.server.common.utils.AppointmentsHelper;
import com.kdimitrov.edentist.server.common.utils.CustomMapper;
import com.kdimitrov.edentist.server.common.utils.ObjectMapperUtils;
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
public class PresentAppointmentService extends AppointmentService<Appointment, PresentAppointmentsRepository> {

    private final PresentAppointmentsRepository appointmentsRepository;
    private final DoctorsService doctorsService;
    private final ServicesService servicesService;
    private final PatientService patientService;
    private final AppointmentsHelper helper;

    public PresentAppointmentService(PresentAppointmentsRepository appointmentsRepository,
                                     DoctorsService doctorsService,
                                     ServicesService servicesService,
                                     PatientService patientsService) {
        super(appointmentsRepository);
        this.appointmentsRepository = appointmentsRepository;
        this.doctorsService = doctorsService;
        this.servicesService = servicesService;
        this.patientService = patientsService;
        helper = new AppointmentsHelper(doctorsService,
                                        patientsService,
                                        servicesService);
    }

    @Override
    public List<AppointmentDto> filterAppointments(String filter, String userEmail) throws NotFound {
        return helper.getFiltered(filter, userEmail, appointmentsRepository);
    }

    public AppointmentDto findSingleAppointment(String userEmail, String code) throws NotFound {
        return helper.getFiltered("", userEmail, appointmentsRepository)
                .stream()
                .filter(appointment -> appointment.getVisitCode().equals(code))
                .findFirst()
                .orElseThrow(() -> new NotFound("No record for provided code"));
    }

    public ResponseEntity<String> saveAppointment(AppointmentRequest request) throws NotFound {

        Doctor doctor = doctorsService.findById(request.getDoctorId());
        Service service = servicesService.findById(request.getServiceId());

        if (!isAvailable(request)) {
            return new ResponseEntity<>(
                    CustomMapper.toAppointmentProposition(this.getNextAvailable(request), doctor, service),
                    HttpStatus.ACCEPTED);
        }

        Patient patient;
        try {
            patient = patientService.findByEmail(request.getEmail());
        } catch (NotFound e) {
            patient = patientService.save(new Patient(request));
        }
        if (updatePatientInfoIfNeeded(request, patient)) {
            patientService.saveAndFlush(patient);
        }

        DoctorDto doctorDto = ObjectMapperUtils.map(doctor, new DoctorDto());
        ServiceDto serviceDto = ObjectMapperUtils.map(service, new ServiceDto());
        PatientDto patientDto = ObjectMapperUtils.map(patient, new PatientDto());
        AppointmentDto dto = new AppointmentDto(request, doctorDto, serviceDto, patientDto);

        Appointment entity = save(ObjectMapperUtils.map(dto, Appointment.class));
        return new ResponseEntity<>(CustomMapper.toAppointmentResponse(entity), HttpStatus.OK);
    }

    public String updateAppointment(Appointment request, long id) throws NotFound {

        helper.validateRequest(request, id);

        Optional<Appointment> persistedOpt = findByIdOpt(id);
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
                persisted.setService(servicesService.findById(request.getService().getId()));
            }

            persisted.setStatus(request.getStatus());
            patientService.saveAndFlush(request.getPatient());

            Appointment entity = saveAndFlush(persisted);
            return CustomMapper.toAppointmentResponse(entity);
        }
        return "not_found";
    }

    public String deleteAppointment(long id) throws NotFound {
        Appointment byId = findById(id);
        deleteById(id);
        if (existsById(id)) {
            throw new OperationUnsuccessful();
        }

        return CustomMapper.toAppointmentResponse(byId);
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
            date = getNextAvailableRecursive(date.plusMinutes(30), remedial);
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
