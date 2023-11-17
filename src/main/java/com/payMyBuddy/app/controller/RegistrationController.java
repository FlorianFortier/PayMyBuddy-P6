package com.payMyBuddy.app.controller;

import com.payMyBuddy.app.dto.UserDto;
import com.payMyBuddy.app.exception.UserAlreadyExistException;
import com.payMyBuddy.app.model.User;
import com.payMyBuddy.app.model.VerificationToken;
import com.payMyBuddy.app.security.OnRegistrationCompleteEvent;

import com.payMyBuddy.app.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;


import java.util.Calendar;
import java.util.Locale;
import java.util.UUID;

@Controller
public class RegistrationController {

    private final UserService userService;
    private final MessageSource messages;

    private final ApplicationEventPublisher eventPublisher;

    @Autowired
    public RegistrationController(UserService userService, MessageSource messages, ApplicationEventPublisher eventPublisher) {
        this.userService = userService;
        this.messages = messages;
        this.eventPublisher = eventPublisher;
    }

    @GetMapping("/registration")
    public String showRegistrationForm(WebRequest request, Model model) {
        UserDto userDto = new UserDto();
        model.addAttribute("user", userDto);
        return "registration"; // Assurez-vous d'avoir une vue nommée "registration"
    }

    @PostMapping("/registration")
    public String registerUserAccount(
            @ModelAttribute("user") @Valid UserDto userDto,
            BindingResult bindingResult,
            HttpServletRequest request,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "registration";
        }

        try {
            User registered = userService.registerNewUserAccount(userDto);

            String appUrl = request.getContextPath();
            String generatedToken = UUID.randomUUID().toString();

            eventPublisher.publishEvent(new OnRegistrationCompleteEvent(registered,
                    request.getLocale(), appUrl, generatedToken));

            redirectAttributes.addFlashAttribute("registrationSuccessMessage", messages.getMessage("success.register.message", null, request.getLocale()));
        } catch (UserAlreadyExistException uaeEx) {
            redirectAttributes.addFlashAttribute("message", "An account for that username/email already exists.");
            return "redirect:/registration";
        }

        return "redirect:/login";
    }

    @GetMapping("/registrationConfirm")
    public String confirmRegistration(WebRequest request, @RequestParam("token") String token, RedirectAttributes redirectAttributes) throws MessagingException {
        Locale locale = request.getLocale();

        VerificationToken verificationToken = userService.getVerificationToken(token);
        if (verificationToken == null) {
            redirectAttributes.addFlashAttribute("message", messages.getMessage("auth.message.invalidToken", null, locale));
            return "error" + locale.getLanguage();
        }

        User user = verificationToken.getUser();
        Calendar cal = Calendar.getInstance();
        if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            redirectAttributes.addFlashAttribute("message", messages.getMessage("auth.message.expired", null, locale));
            return "error" + locale.getLanguage();
        }

        user.setEnabled(true);
        userService.saveRegisteredUser(user);

        return "redirect:/login?lang=" + request.getLocale().getLanguage();
    }
}

