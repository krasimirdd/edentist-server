package com.kdimitrov.edentist.common.services;

import com.kdimitrov.edentist.config.MQConfig;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Service;

@Service
public class MQSenderImpl<T> implements MQSender<T> {

    private final AmqpTemplate rabbitTemplate;
    private final MQConfig mqConfig;

    public MQSenderImpl(AmqpTemplate rabbitTemplate, MQConfig mqConfig) {
        this.rabbitTemplate = rabbitTemplate;
        this.mqConfig = mqConfig;
    }

    @Override
    public void send(T entity) {
        rabbitTemplate.convertAndSend(mqConfig.getExchange(), mqConfig.getRoutingkey(), entity);
        System.out.println("Send msg = " + entity);
    }
}