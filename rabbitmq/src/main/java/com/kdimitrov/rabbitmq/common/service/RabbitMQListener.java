package com.kdimitrov.rabbitmq.common.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.kdimitrov.rabbitmq.common.model.VisitRequest;
import com.kdimitrov.rabbitmq.common.processors.MQTask;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Service
public class RabbitMQListener implements MessageListener {
    private final Logger logger = LoggerFactory.getLogger(MQTask.class);

    private final MailService mailService;

    private final ObjectMapper mapper = new Jackson2ObjectMapperBuilder()
            .modulesToInstall(new JavaTimeModule(),new Jdk8Module()).build()
            .enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    private final ExecutorService executor = new ThreadPoolExecutor(
            5, 5,
            60L, TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(20),
            new BasicThreadFactory.Builder().namingPattern(
                    "MailManagerThreadPool-%s").daemon(true).build());

    public RabbitMQListener(MailService mailService) {
        this.mailService = mailService;
    }

    @RabbitListener(queues = "edentist.queue")
    public void onMessage(final Message message) {
        logger.debug("Consuming Message - {}", message);

        try {
            VisitRequest value = mapper.readValue(message.getBody(), VisitRequest.class);
            executor.submit(new MQTask(value, mailService));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
