package com.kdimitrov.edentist.server.common.api.graphql;

import com.kdimitrov.edentist.server.common.services.ApiService;
import graphql.kickstart.tools.GraphQLQueryResolver;
import org.springframework.stereotype.Service;

@Service
public class Query implements GraphQLQueryResolver {
    private final ApiService service;

    public Query(final ApiService service) {
        this.service = service;
    }

    public boolean findAllUsers() {
        return true;
    }
}
