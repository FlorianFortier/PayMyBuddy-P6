package com.payMyBuddy.app.util;

import jakarta.mail.MessagingException;
import org.thymeleaf.context.Context;

public interface IEmailService {

     void sendSimpleMessage(String to, String subject, String text);

     void sendMessageWithAttachment(
             String to, String subject, String text, String pathToAttachment) throws MessagingException;

     void sendHtmlMessage(String to, String subject, String templateName, Context context) throws MessagingException;
}
