package com.kdimitrov.edentist.server.common.services.abstractions;

import com.kdimitrov.edentist.server.common.exceptions.NotFound;
import com.kdimitrov.edentist.server.common.models.Entity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public abstract class AbstractEntityService<E extends Entity, R extends JpaRepository<E, Long>> {

    protected final R repository;

    public AbstractEntityService(R repository) {
        this.repository = repository;
    }

    public E findById(Long id) throws NotFound {
        return findByIdOpt(id).orElseThrow(() -> new NotFound("No record for " + id));
    }

    public Optional<E> findByIdOpt(Long id) {
        return repository.findById(id);
    }

    public E save(E entity) {
        return repository.save(entity);
    }

    public E saveAndFlush(E entity) {
        return repository.saveAndFlush(entity);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return repository.existsById(id);
    }
}
