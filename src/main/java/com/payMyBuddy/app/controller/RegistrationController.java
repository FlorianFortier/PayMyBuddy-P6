package com.payMyBuddy.app.controller;

import com.payMyBuddy.app.dto.UserDto;
import com.payMyBuddy.app.exception.UserAlreadyExistException;
import com.payMyBuddy.app.model.User;
import com.payMyBuddy.app.service.MyUserDetailsService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
public class RegistrationController {

    private final MyUserDetailsService userService;

    public RegistrationController(MyUserDetailsService userService) {
        this.userService = userService;
    }

    @GetMapping("/registration")
    public String showRegistrationForm(WebRequest request, Model model) {
        UserDto userDto = new UserDto();
        model.addAttribute("user", userDto);
        return "registration"; // Assurez-vous d'avoir une vue nommée "registration"
    }

    @PostMapping("/user/registration")
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
            mav.setViewName("successRegister"); // Assurez-vous d'avoir une vue nommée "successRegister"
            mav.addObject("user", userDto);
        } catch (UserAlreadyExistException uaeEx) {
            mav.setViewName("registration"); // Renvoyer l'utilisateur au formulaire d'inscription en cas d'erreur
            mav.addObject("message", "An account for that username/email already exists.");
        }
        return new ModelAndView("successRegister", "user", userDto);
    }
}
