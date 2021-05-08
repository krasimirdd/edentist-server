package com.kdimitrov.edentist.mq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RabbitMQLauncher {
    public static void main(String[] args) {
        SpringApplication.run(RabbitMQLauncher.class, args);
    }
}
