package com.kdimitrov.edentist.server.common.services.implementations;

import com.kdimitrov.edentist.app.config.MQConfig;
import com.kdimitrov.edentist.server.common.services.MQSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Service;

@Service
public class MQSenderImpl<T> implements MQSender<T> {
    private final Logger logger = LoggerFactory.getLogger(MQSenderImpl.class);

    private final AmqpTemplate rabbitTemplate;
    private final MQConfig mqConfig;

    public MQSenderImpl(AmqpTemplate rabbitTemplate, MQConfig mqConfig) {
        this.rabbitTemplate = rabbitTemplate;
        this.mqConfig = mqConfig;
    }

    @Override
    public void send(T entity) {
        rabbitTemplate.convertAndSend(mqConfig.getExchange(), mqConfig.getRoutingkey(), entity);
        logger.info("Send msg = " + entity);
    }
}