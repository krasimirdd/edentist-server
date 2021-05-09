package com.kdimitrov.edentist.mq.common.processors;

import com.kdimitrov.edentist.mq.common.model.VisitRequest;
import com.kdimitrov.edentist.mq.common.service.MailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.MessagingException;
import java.io.IOException;
import java.nio.file.Paths;

public class MQTask implements Runnable {
    private final Logger logger = LoggerFactory.getLogger(MQTask.class);

    private final MailService mailService;

    private final VisitRequest visitRequest;
    private static final String SUBJECT = "Visit Request";

    public MQTask(MailService mailService, VisitRequest visitRequest) {
        this.mailService = mailService;
        this.visitRequest = visitRequest;
    }

    @Override
    public void run() {
        try {
            mailService.sendEmail(SUBJECT,
                                  Paths.get("src/main/resources/email-templates/email_doctor.html"),
                                  visitRequest.getDoctor().getEmail(),
                                  visitRequest.getVisitCode());
        } catch (IOException | MessagingException e) {
            logger.info("Error when sending mail to the doctor", e);
        }

        try {
            mailService.sendEmail(SUBJECT,
                                  Paths.get("src/main/resources/email-templates/email_patient.html"),
                                  visitRequest.getPatient().getEmail(),
                                  visitRequest.getVisitCode());
        } catch (IOException | MessagingException e) {
            logger.info("Error when sending mail to the patient", e);
        }
    }
}
