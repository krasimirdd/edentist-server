package com.kdimitrov.edentist.server.common.services.implementations;


import com.kdimitrov.edentist.server.common.exceptions.NotFound;
import com.kdimitrov.edentist.server.common.models.ArchivedAppointment;
import com.kdimitrov.edentist.server.common.models.dto.AppointmentDto;
import com.kdimitrov.edentist.server.common.repository.ArchivedAppointmentsRepository;
import com.kdimitrov.edentist.server.common.services.abstractions.AppointmentService;
import com.kdimitrov.edentist.server.common.utils.AppointmentsHelper;

import java.util.List;

@org.springframework.stereotype.Service
public class ArchivedAppointmentService extends AppointmentService<ArchivedAppointment, ArchivedAppointmentsRepository> {

    private final AppointmentsHelper helper;

    public ArchivedAppointmentService(ArchivedAppointmentsRepository appointmentsRepository,
                                      DoctorsService doctorsService,
                                      ServicesService servicesService,
                                      PatientService patientsService) {
        super(appointmentsRepository);
        helper = new AppointmentsHelper(doctorsService,
                                        patientsService,
                                        servicesService);
    }

    @Override
    public List<AppointmentDto> filterAppointments(String filter, String userEmail) throws NotFound {
        return helper.getFiltered(filter, userEmail, super.repository);
    }
}
