package com.kdimitrov.edentist.common.services;

public interface MQSender<T> {

    void send(T entity);
}
