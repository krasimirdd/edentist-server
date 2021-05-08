package com.kdimitrov.edentist.mq.common.processors;

import com.kdimitrov.edentist.mq.common.model.VisitRequest;
import com.kdimitrov.edentist.mq.common.service.MailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.MessagingException;
import java.io.IOException;
import java.nio.file.Paths;

public class MQTask implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(MQTask.class);

    private final VisitRequest visitRequest;
    private final MailService mailService;
    private static final String SUBJECT = "Visit Request";

    public MQTask(VisitRequest visitRequest, MailService mailService) {
        this.visitRequest = visitRequest;
        this.mailService = mailService;
    }

    @Override
    public void run() {
        try {
            mailService.sendEmail(SUBJECT,
                                  Paths.get("rabbitmq\\src\\main\\resources\\email_doctor.html"),
                                  visitRequest.getDoctor().getEmail(),
                                  visitRequest.getVisitCode());
        } catch (IOException | MessagingException e) {
            LOGGER.error("Error when sending mail to the remedial", e);
        }

        try {
            mailService.sendEmail(SUBJECT,
                                  Paths.get("rabbitmq\\src\\main\\resources\\email_patient.html"),
                                  visitRequest.getPatient().getEmail(),
                                  visitRequest.getVisitCode());
        } catch (IOException | MessagingException e) {
            LOGGER.error("Error when sending mail to the patient", e);
        }
    }
}
