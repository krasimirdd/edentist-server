package com.kdimitrov.edentist.server.common.repository;

import com.kdimitrov.edentist.server.common.models.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PresentAppointmentsRepository extends JpaRepository<Appointment, Long>, AppointmentsRepository<Appointment> {
}
