package com.kdimitrov.edentist.common.repository;

import com.kdimitrov.edentist.common.models.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorsRepository extends JpaRepository<Doctor, Long> {

    List<Doctor> findAll();


    Optional<Doctor> findByEmail(String email);

}
