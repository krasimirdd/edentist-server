package com.kdimitrov.rabbitmq;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class RabbitMQLauncher {
    public static void main(String[] args) {
        new SpringApplicationBuilder()
                .sources(RabbitMQLauncher.class)
                .run(args);
    }
}
