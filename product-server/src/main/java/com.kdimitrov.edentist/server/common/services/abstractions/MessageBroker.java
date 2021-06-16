package com.kdimitrov.edentist.server.common.services.abstractions;

public interface MessageBroker {

    <T> void translate(T entity);
}
