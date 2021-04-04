package com.kdimitrov.rabbitmq.common.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize
@JsonPOJOBuilder
public class User implements Serializable {

    @JsonProperty("id")
    private long id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("phone")
    private String phone;

    @JsonProperty("email")
    private String email;

    public User() {
    }

    public User(long id, String name, String phone, String email) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
    }

    public String getEmail() {
        return this.email;
    }
}
