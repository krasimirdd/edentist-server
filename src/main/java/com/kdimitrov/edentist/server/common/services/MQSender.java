package com.kdimitrov.edentist.server.common.services;

public interface MQSender<T> {

    void send(T entity);
}
