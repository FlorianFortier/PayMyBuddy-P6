package com.payMyBuddy.app.util;

import jakarta.mail.MessagingException;

public interface IEmailService {

     void sendSimpleMessage(String to, String subject, String text);

     void sendMessageWithAttachment(
             String to, String subject, String text, String pathToAttachment) throws MessagingException;
}
