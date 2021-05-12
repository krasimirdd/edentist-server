package com.kdimitrov.edentist.server.common.repository;

import com.kdimitrov.edentist.server.common.models.ArchivedAppointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArchiveAppointmentsRepository extends JpaRepository<ArchivedAppointment, Long>, AppointmentsRepository<ArchivedAppointment> {
}
