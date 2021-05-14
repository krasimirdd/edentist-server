package com.kdimitrov.edentist.server.common.api.graphql;

import com.kdimitrov.edentist.server.common.services.implementations.GraphServiceImpl;
import graphql.kickstart.tools.GraphQLMutationResolver;
import org.springframework.stereotype.Service;

@Service
public class Mutation implements GraphQLMutationResolver {
    private final GraphServiceImpl service;

    public Mutation(GraphServiceImpl service) {
        this.service = service;
    }

    public boolean createUser(final String email,
                              final String name,
                              final String phone,
                              final String specialization,
                              final String description,
                              final String img) {
        return service.createUser(email, name, phone, specialization, description, img) != null;
    }

    public boolean createService(final String type) {
        return service.createService(type) != null;
    }
}
