package com.payMyBuddy.app.controller;

import com.payMyBuddy.app.dto.UserDto;
import com.payMyBuddy.app.exeption.UserAlreadyExistException;
import com.payMyBuddy.app.model.User;
import com.payMyBuddy.app.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

public class RegistrationController {

    private UserService userService;

    /**
     *
     * @param request
     * @param model
     * @return
     */
    @GetMapping("/registration")
    public String showRegistrationForm(WebRequest request, Model model) {
        UserDto userDto = new UserDto();
        model.addAttribute("user", userDto);
        return "registration";
    }

    /**
     *
     * @param userDto
     * @param request
     * @param errors
     * @return
     */
    @PostMapping("/user/registration")
    public ModelAndView registerUserAccount(
            @ModelAttribute("user") @Valid UserDto userDto,
            BindingResult bindingResult,
            HttpServletRequest request) {

        ModelAndView mav = new ModelAndView();

        if (bindingResult.hasErrors()) {
            // Si des erreurs de validation sont présentes, renvoyer l'utilisateur au formulaire d'inscription
            mav.setViewName("registrationForm"); // Assurez-vous d'avoir une vue nommée "registrationForm"
            return mav;
        }
        try {
            User registered = userService.registerNewUserAccount(userDto);
            mav.setViewName("successRegister"); // Assurez-vous d'avoir une vue nommée "successRegister"
            mav.addObject("user", userDto);
        } catch (UserAlreadyExistException uaeEx) {
            mav.setViewName("registrationForm"); // Renvoyer l'utilisateur au formulaire d'inscription en cas d'erreur
            mav.addObject("message", "An account for that username/email already exists.");
        }
        return mav;
    }
}
