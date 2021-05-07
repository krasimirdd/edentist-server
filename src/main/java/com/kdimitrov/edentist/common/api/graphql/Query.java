package com.kdimitrov.edentist.common.api.graphql;

import com.kdimitrov.edentist.common.models.dto.DoctorDto;
import com.kdimitrov.edentist.common.services.DefService;
import graphql.kickstart.tools.GraphQLQueryResolver;
import org.springframework.stereotype.Service;

import java.util.List;

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
