package com.kdimitrov.edentist.server.common.repository;

import com.kdimitrov.edentist.server.common.models.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DoctorsRepository extends JpaRepository<Doctor, Long> {

    Optional<Doctor> findByEmail(String email);

}
