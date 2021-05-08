package com.kdimitrov.edentist.server.common.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static java.nio.charset.StandardCharsets.UTF_8;

@Service
public class MessageBroker {

    private final Logger logger = LoggerFactory.getLogger(MessageBroker.class);
    private final AmqpTemplate rabbitTemplate;

    @Value("${edentist.mq.exchange}")
    private String exchange;

    @Value("${edentist.mq.routingkey}")
    private String routingkey;

    public MessageBroker(AmqpTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public <T> void translate(T entity) {
        Message message = new Message(entity.toString().getBytes(UTF_8), new MessageProperties());
        rabbitTemplate.send(exchange, routingkey, message);
        logger.debug("Send msg = {}", entity);
    }
}
