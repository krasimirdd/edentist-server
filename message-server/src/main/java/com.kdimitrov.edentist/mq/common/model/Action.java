package com.kdimitrov.edentist.mq.common.model;

public enum Action {
    ADD_NEW,
    EDIT;

    @Override
    public String toString() {
        return this.name().replace("[", "").replace("]", "");
    }
}
