package com.kdimitrov.edentist.server.common.api.graphql;

import com.kdimitrov.edentist.server.common.services.DefService;
import graphql.kickstart.tools.GraphQLQueryResolver;
import org.springframework.stereotype.Service;

@Service
public class Query implements GraphQLQueryResolver {
    private final DefService service;

    public Query(final DefService service) {
        this.service = service;
    }

    public boolean findAllUsers() {
        return true;
    }
}