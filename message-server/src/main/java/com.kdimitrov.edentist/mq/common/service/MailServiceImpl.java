package com.kdimitrov.edentist.mq.common.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class MailServiceImpl implements MailService {
    private static final Logger logger = LogManager.getLogger(MailServiceImpl.class);

    private final JavaMailSender mailSender;
    private String sender;

    public MailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendEmail(String subject, String messageText, String to) throws MessagingException {
        logger.info("Sending mail.");

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

        helper.setFrom(sender);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(messageText, true);
        logger.info("=== >> Mail body << === \n From : {} \n To : {}", sender, mimeMessage.getAllRecipients()[0]);
        mailSender.send(mimeMessage);
    }

    @Override
    public void sendEmail(String subject, Path file, String to, @NotNull String code) throws IOException, MessagingException {
        logger.info("Sending mail.");

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

        if (code == null) {
            throw new IOException("Code must not be null!");
        }
        String text = new String(Files.readAllBytes(file)).replaceAll("@code@", code);
        helper.setFrom(sender);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text, true);
        logger.info("=== >> Mail body << === \n To : {}",
                    helper.getMimeMessage().getAllRecipients()[0]);

        mailSender.send(mimeMessage);
    }

    public void setFrom(String from) {
        this.sender = from;
    }
}
