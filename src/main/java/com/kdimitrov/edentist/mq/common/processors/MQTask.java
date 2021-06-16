package com.kdimitrov.edentist.mq.common.processors;

import com.kdimitrov.edentist.mq.common.model.User;
import com.kdimitrov.edentist.mq.common.model.VisitRequest;
import com.kdimitrov.edentist.mq.common.service.MailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.MessagingException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MQTask implements Runnable {
    private static final String ERROR_MSG_DOCTOR = "Error when sending mail to the doctor";
    private static final String ERROR_MSG_PATIENT = "Error when sending mail to the patient";
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

        switch (visitRequest.getAction()) {
            case ADD_NEW:
                sendMailTo(Paths.get("src/main/resources/email-templates/email_doctor.html"),
                           visitRequest.getDoctor(),
                           ERROR_MSG_DOCTOR);
                sendMailTo(Paths.get("src/main/resources/email-templates/email_patient.html"),
                           visitRequest.getPatient(),
                           ERROR_MSG_PATIENT);
                break;
            case EDIT:
                sendMailTo(Paths.get("src/main/resources/email-templates/email_patient_edit.html"),
                           visitRequest.getPatient(),
                           ERROR_MSG_PATIENT);
                break;
            default:
                logger.info("No action provided! Will skip sending mail.");
        }
    }

    private void sendMailTo(Path path, User user, String errorMsg) {
        try {
            mailService.sendEmail(SUBJECT,
                                  path,
                                  user.getEmail(),
                                  visitRequest.getVisitCode());
        } catch (IOException | MessagingException e) {
            logger.info(errorMsg, e);
        }
    }
}
