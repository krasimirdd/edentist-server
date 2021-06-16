package com.kdimitrov.edentist.server.common.services.abstractions;

public interface MQSender<T> {

    void send(T entity);
}
