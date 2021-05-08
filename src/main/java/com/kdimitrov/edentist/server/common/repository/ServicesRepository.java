package com.kdimitrov.edentist.server.common.repository;

import com.kdimitrov.edentist.server.common.models.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServicesRepository extends JpaRepository<Service, Long> {
}
