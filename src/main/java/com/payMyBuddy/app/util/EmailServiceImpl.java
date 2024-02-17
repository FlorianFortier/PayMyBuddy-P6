package com.payMyBuddy.app.util;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;


@Component
@RequiredArgsConstructor
public class EmailServiceImpl implements IEmailService {

    private static final String NO_REPLY_EMAIL = "noreply@payMyBuddy.com";

    private JavaMailSender emailSender;
    private TemplateEngine templateEngine;

    private Logger logger;

    public EmailServiceImpl(JavaMailSender emailSender, TemplateEngine templateEngine, Logger logger) {
        this.emailSender = emailSender;
        this.templateEngine = templateEngine;
        this.logger = logger;
    }

    @Override
    public void sendSimpleMessage(
            String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(NO_REPLY_EMAIL);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);

    }

    @Override
    public void sendMessageWithAttachment(
            String to, String subject, String text, String pathToAttachment) throws MessagingException {

        MimeMessage message = emailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom(NO_REPLY_EMAIL);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text);

        FileSystemResource file
                = new FileSystemResource(new File(pathToAttachment));
        helper.addAttachment("Invoice", file);

        emailSender.send(message);
    }
    @Override
    public void sendHtmlMessage(String to, String subject, String templateName, Context context) throws MessagingException {
        try {
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(NO_REPLY_EMAIL);
            helper.setTo(to);
            helper.setSubject(subject);

            String htmlContent = templateEngine.process(templateName, context);
            helper.setText(htmlContent, true);

            emailSender.send(message);

            logger.atInfo().log("Email sent successfully.");
        } catch (MessagingException e) {
            logger.atError().log("Error sending email: " + e.getMessage());
            throw e;
        }
    }
}