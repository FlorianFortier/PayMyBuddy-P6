package com.payMyBuddy.app.security;

import com.payMyBuddy.app.model.User;

import com.payMyBuddy.app.service.UserService;
import com.payMyBuddy.app.util.EmailServiceImpl;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;


import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;

@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {


    private final UserService userService;
    private final MessageSource messages;
    private final EmailServiceImpl emailService;

    public RegistrationListener(UserService userService, @Qualifier("messageSource") MessageSource messages, EmailServiceImpl emailService) {
        this.userService = userService;
        this.messages = messages;
        this.emailService = emailService;
    }

    @Value("${app.base.url}")
    private String appBaseUrl;

    @Override
    public void onApplicationEvent(OnRegistrationCompleteEvent event) {
        this.confirmRegistration(event);
    }

    private void confirmRegistration(OnRegistrationCompleteEvent event) {
        User user = event.getUser();
        String token = event.getToken();
        userService.createVerificationToken(user, token);

        String recipientAddress = user.getEmail();
        String subject = "Registration Confirmation";
        String confirmationUrl = appBaseUrl + "/registrationConfirm?token=" + token;
        String confirmationMessage = messages.getMessage("success.register.message", null, event.getLocale());

        sendRegistrationConfirmationEmail(recipientAddress, subject, confirmationUrl, confirmationMessage);

    }

    private void sendRegistrationConfirmationEmail(String userEmail, String subject, String confirmationUrl, String confirmationMessage) {
        // Créez le contexte Thymeleaf avec les variables nécessaires
        Context context = new Context();
        context.setVariable("confirmationLink", confirmationUrl);
        context.setVariable("confirmationMessage", confirmationMessage);

        // Envoi de l'e-mail avec le template Thymeleaf
        try {
            emailService.sendHtmlMessage(userEmail, subject, "registrationConfirmationEmail", context);
        } catch (Exception e) {
            // Gérer les erreurs d'envoi d'e-mail
            e.printStackTrace();
        }
    }
}
