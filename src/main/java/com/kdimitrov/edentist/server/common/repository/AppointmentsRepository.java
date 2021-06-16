package com.kdimitrov.edentist.server.common.repository;

import com.kdimitrov.edentist.server.common.models.Entity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AppointmentsRepository<T extends Entity> {

    List<T> findAllByStatus(String status);

    List<T> findAllByDoctorId(long id);

    List<T> findAllByPatientId(long id);

    Optional<T> findByDateEqualsAndDoctorId(LocalDateTime dateTime, long doctorId);

}
