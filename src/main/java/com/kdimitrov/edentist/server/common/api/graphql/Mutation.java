package com.kdimitrov.edentist.server.common.api.graphql;

import com.kdimitrov.edentist.server.common.services.DefService;
import graphql.kickstart.tools.GraphQLMutationResolver;
import org.springframework.stereotype.Service;

@Service
public class Mutation implements GraphQLMutationResolver {
    private final DefService service;

    public Mutation(final DefService service) {
        this.service = service;
    }

    public boolean createUser(final String email, final String name, final String password, final String phone) {
        return service.createUser(email, name, password, phone) != null;
    }

}
