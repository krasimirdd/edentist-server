package com.kdimitrov.edentist.server.common.api.graphql;

import com.kdimitrov.edentist.server.common.models.ArchivedAppointment;
import com.kdimitrov.edentist.server.common.services.implementations.GraphServiceImpl;
import graphql.kickstart.tools.GraphQLQueryResolver;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Query implements GraphQLQueryResolver {
    private final GraphServiceImpl service;

    public Query(GraphServiceImpl service) {
        this.service = service;
    }

    public List<ArchivedAppointment> findAllArchivedAppointments() {
        return service.getArchivedAppointments();
    }

    public List<ArchivedAppointment> findArchivedAppointments(String patientEmail) {
        return service.getArchivedAppointments(patientEmail);
    }
}
