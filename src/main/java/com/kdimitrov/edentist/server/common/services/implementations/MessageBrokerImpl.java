package com.kdimitrov.edentist.server.common.services.implementations;

import com.kdimitrov.edentist.server.common.services.MessageBroker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static java.nio.charset.StandardCharsets.UTF_8;

@Service
public class MessageBrokerImpl implements MessageBroker {
    private final Logger logger = LoggerFactory.getLogger(MessageBrokerImpl.class);

    private final AmqpTemplate rabbitTemplate;

    @Value("${edentist.mq.exchange}")
    private String exchange;

    @Value("${edentist.mq.routingkey}")
    private String routingkey;

    public MessageBrokerImpl(AmqpTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public <T> void translate(T entity) {
        Message message = new Message(entity.toString().getBytes(UTF_8), new MessageProperties());
        rabbitTemplate.send(exchange, routingkey, message);
        logger.info("Send msg = {}", entity);
    }
}
