package com.kdimitrov.edentist.server.common.services;

public interface MessageBroker {

    <T> void translate(T entity);
}
