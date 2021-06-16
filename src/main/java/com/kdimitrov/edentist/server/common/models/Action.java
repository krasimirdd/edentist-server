package com.kdimitrov.edentist.server.common.models;

public enum Action {
    ADD_NEW,
    EDIT;

    @Override
    public String toString() {
        return this.name().replace("[", "").replace("]", "");
    }
}
