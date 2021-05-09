package com.kdimitrov.edentist.app.config;

import com.kdimitrov.edentist.mq.common.service.MailService;
import com.kdimitrov.edentist.mq.common.service.MailServiceImpl;
import com.kdimitrov.edentist.mq.common.service.RabbitMQListener;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;

@Configuration
@ConfigurationProperties(prefix = "edentist.mq")
public class MQConfig {

    private String exchange;
    private String routingkey;
    private String username;
    private String password;
    private String queueName;
    private String mailSender;

    public String getMailSender() {
        return mailSender;
    }

    public void setMailSender(String mailSender) {
        this.mailSender = mailSender;
    }

    public String getQueueName() {
        return queueName;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public String getRoutingkey() {
        return routingkey;
    }

    public void setRoutingkey(String routingkey) {
        this.routingkey = routingkey;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Bean
    Queue queue() {
        return new Queue(queueName, false);
    }

    @Bean
    DirectExchange exchange() {
        return new DirectExchange(exchange);
    }

    @Bean
    Binding binding(Queue queue, DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(routingkey);
    }

    @Bean
    MessageListenerContainer messageListenerContainer(ConnectionFactory connectionFactory, JavaMailSender mailSender) {
        SimpleMessageListenerContainer simpleMessageListenerContainer = new SimpleMessageListenerContainer();
        simpleMessageListenerContainer.setConnectionFactory(connectionFactory);
        simpleMessageListenerContainer.setQueues(queue());
        simpleMessageListenerContainer.setMessageListener(new RabbitMQListener(mailService(mailSender)));
        return simpleMessageListenerContainer;
    }

    @Bean
    MailService mailService(JavaMailSender mailSender) {
        MailServiceImpl mailService = new MailServiceImpl(mailSender);
        mailService.setFrom(this.mailSender);
        return mailService;
    }

    @Bean
    public AmqpTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

//    @Bean
//    public JavaMailSender mailSender(MailProperties mailProperties) {
//        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
//
//        mailSender.setHost(mailProperties.getHost());
//        mailSender.setPort(mailProperties.getPort());
//
//        mailSender.setUsername(mailProperties.getUsername());
//        mailSender.setPassword(mailProperties.getPassword());
//
//        Properties props = mailSender.getJavaMailProperties();
//        Map<String, String> properties = mailProperties.getProperties();
//        props.putAll(properties);
//
//        return mailSender;
//    }
}