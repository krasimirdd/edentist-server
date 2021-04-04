package com.kdimitrov.rabbitmq.common.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class MailService {
    public static final String NAME = "mailFacade";

    private static final Logger logger = LogManager.getLogger(MailService.class);

    @Value("${spring.mail.username}")
    private String emailAddress;

    private final JavaMailSender mailSender;

    public MailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendEmail(String subject, String messageText, String to) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

        helper.setFrom(this.emailAddress);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(messageText, true);
        mailSender.send(mimeMessage);
    }

    public void sendEmail(String subject, Path file, String to, String code) throws IOException, MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

        String text = new String(Files.readAllBytes(file)).replaceAll("@code@", code);
        helper.setFrom(this.emailAddress);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text, true);
        mailSender.send(mimeMessage);
    }
}
