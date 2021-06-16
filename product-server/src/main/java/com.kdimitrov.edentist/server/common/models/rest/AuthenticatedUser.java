package com.kdimitrov.edentist.server.common.models.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AuthenticatedUser {
    @JsonProperty(value = "role")
    public String role;

    @JsonProperty(value = "nickname")
    public String nickname;

    @JsonProperty(value = "name")
    public String name;

    @JsonProperty(value = "picture")
    public String picture;

    @JsonProperty(value = "updated_at")
    public String updated_at;

    @JsonProperty(value = "email")
    public String email;

    @JsonProperty(value = "email_verified")
    public boolean email_verified;

    @JsonProperty(value = "sub")
    public String sub;

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isEmail_verified() {
        return email_verified;
    }

    public void setEmail_verified(boolean email_verified) {
        this.email_verified = email_verified;
    }

    public String getSub() {
        return sub;
    }

    public void setSub(String sub) {
        this.sub = sub;
    }
}
