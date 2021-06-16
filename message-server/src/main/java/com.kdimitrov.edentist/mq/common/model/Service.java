package com.kdimitrov.edentist.mq.common.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

@JsonDeserialize
@JsonPOJOBuilder
public class Service {

    @JsonProperty("id")
    private long id;

    @JsonProperty("type")
    private String type;

    public Service() {
    }

    public Service(long id, String type) {
        this.id = id;
        this.type = type;
    }
}
