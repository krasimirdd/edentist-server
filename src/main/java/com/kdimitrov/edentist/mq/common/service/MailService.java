package com.kdimitrov.edentist.mq.common.service;

import javax.mail.MessagingException;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.nio.file.Path;

public interface MailService {
    void sendEmail(String subject, String messageText, String to) throws MessagingException;

    void sendEmail(String subject, Path file, String to,@NotNull String code) throws IOException, MessagingException;
}
