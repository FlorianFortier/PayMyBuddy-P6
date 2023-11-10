package com.payMyBuddy.app.controller;

import com.payMyBuddy.app.dto.UserDto;
import com.payMyBuddy.app.exception.UserAlreadyExistException;
import com.payMyBuddy.app.model.User;
import com.payMyBuddy.app.model.VerificationToken;
import com.payMyBuddy.app.security.OnRegistrationCompleteEvent;
import com.payMyBuddy.app.service.MyUserDetailsService;
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
import org.springframework.web.servlet.ModelAndView;


import java.util.Calendar;
import java.util.Locale;

@Controller
public class RegistrationController {

    private final MyUserDetailsService userService;

    @Autowired
    private MessageSource messages;


    @Autowired
    ApplicationEventPublisher eventPublisher;
    public RegistrationController(MyUserDetailsService userService) {
        this.userService = userService;
    }

    @GetMapping("/registration")
    public String showRegistrationForm(WebRequest request, Model model) {
        UserDto userDto = new UserDto();
        model.addAttribute("user", userDto);
        return "registration"; // Assurez-vous d'avoir une vue nommée "registration"
    }

    @PostMapping("/registration")
    public ModelAndView registerUserAccount(
            @ModelAttribute("user") @Valid UserDto userDto,
            BindingResult bindingResult,
            HttpServletRequest request) {

        ModelAndView mav = new ModelAndView();

        if (bindingResult.hasErrors()) {
            // Si des erreurs de validation sont présentes, renvoyer l'utilisateur au formulaire d'inscription
            mav.setViewName("registration"); // Assurez-vous d'avoir une vue nommée "registration"
            return mav;
        }
        try {
            User registered = userService.registerNewUserAccount(userDto);

            String appUrl = request.getContextPath();
            eventPublisher.publishEvent(new OnRegistrationCompleteEvent(registered,
                    request.getLocale(), appUrl));


            mav.setViewName("successRegister"); // Assurez-vous d'avoir une vue nommée "successRegister"
            mav.addObject("user", userDto);
        } catch (UserAlreadyExistException uaeEx) {
            mav.setViewName("registration"); // Renvoyer l'utilisateur au formulaire d'inscription en cas d'erreur
            mav.addObject("message", "An account for that username/email already exists.");
        }
        return new ModelAndView("successRegister", "user", userDto);
    }

    @GetMapping("/regitrationConfirm")
    public String confirmRegistration
            (WebRequest request, Model model, @RequestParam("token") String token) {

        Locale locale = request.getLocale();

        VerificationToken verificationToken = userService.getVerificationToken(token);
        if (verificationToken == null) {
            String message = messages.getMessage("auth.message.invalidToken", null, locale);
            model.addAttribute("message", message);
            return "redirect:/badUser.html?lang=" + locale.getLanguage();
        }

        User user = verificationToken.getUser();
        Calendar cal = Calendar.getInstance();
        if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            String messageValue = messages.getMessage("auth.message.expired", null, locale);
            model.addAttribute("message", messageValue);
            return "redirect:/badUser.html?lang=" + locale.getLanguage();
        }

        user.setEnabled(true);
        userService.saveRegisteredUser(user);
        return "redirect:/login.html?lang=" + request.getLocale().getLanguage();
    }
}
