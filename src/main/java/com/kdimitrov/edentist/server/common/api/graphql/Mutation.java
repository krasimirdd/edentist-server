package com.kdimitrov.edentist.server.common.api.graphql;

import com.kdimitrov.edentist.server.common.services.abstractions.ApiService;
import graphql.kickstart.tools.GraphQLMutationResolver;
import org.springframework.stereotype.Service;

@Service
public class Mutation implements GraphQLMutationResolver {
    private final ApiService service;

    public Mutation(final ApiService service) {
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

}
