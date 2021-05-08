package com.kdimitrov.edentist.server.common.repository;

import com.kdimitrov.edentist.server.common.models.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentsRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findAllByStatus(String status);

    List<Appointment> findAllByDoctorId(long id);

    List<Appointment> findAllByPatientId(long id);

    Optional<Appointment> findByDateEqualsAndDoctorId(LocalDateTime dateTime, long doctorId);

//
//    @Query(value = "SELECT v from VisitEntity v where v.date =:date and v.remedial.email=:remedial")
//
//    Optional<Appointment> findAlreadyRequest(LocalDateTime date, String remedial);
}
