package com.kdimitrov.edentist.mq.common.processors;

import com.kdimitrov.edentist.mq.common.model.User;
import com.kdimitrov.edentist.mq.common.model.VisitRequest;
import com.kdimitrov.edentist.mq.common.service.MailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.MessagingException;
import java.io.IOException;
import java.io.InputStream;

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
                sendMailTo(getFileFromResourceAsStream("email-templates/email_doctor.html"),
                           visitRequest.getDoctor(),
                           ERROR_MSG_DOCTOR);
                sendMailTo(getFileFromResourceAsStream("email-templates/email_patient.html"),
                           visitRequest.getPatient(),
                           ERROR_MSG_PATIENT);
                break;
            case EDIT:
                sendMailTo(getFileFromResourceAsStream("email-templates/email_patient_edit.html"),
                           visitRequest.getPatient(),
                           ERROR_MSG_PATIENT);
                break;
            default:
                logger.info("No action provided! Will skip sending mail.");
        }
    }

    private void sendMailTo(InputStream path, User user, String errorMsg) {

        try {
            mailService.sendEmail(SUBJECT,
                                  path,
                                  user.getEmail(),
                                  visitRequest.getVisitCode());
        } catch (IOException | MessagingException e) {
            logger.info(errorMsg, e);
        }
    }

    // get a file from the resources folder
    // works everywhere, IDEA, unit test and JAR file.
    private InputStream getFileFromResourceAsStream(String fileName) {

        // The class loader that loaded the class
        ClassLoader classLoader = this.getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(fileName);

        // the stream holding the file content
        if (inputStream == null) {
            logger.info("file not found");
            throw new IllegalArgumentException("file not found! " + fileName);
        } else {
            return inputStream;
        }

    }
}
