package com.payMyBuddy.app.controller;

import com.payMyBuddy.app.dto.UserDto;
import com.payMyBuddy.app.model.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

public class RegistrationController {
    @GetMapping("/registration")
    public String showRegistrationForm(WebRequest request, Model model) {
        UserDto userDto = new UserDto();
        model.addAttribute("user", userDto);
        return "registration";
    }
//    @PostMapping("/user/registration")
//    public ModelAndView registerUserAccount(
//            @ModelAttribute("user") @Valid UserDto userDto,
//            HttpServletRequest request,
//            Errors errors) {
//
//        try {
//            User registered = userService.registerNewUserAccount(userDto);
//        } catch (UserAlreadyExistException uaeEx) {
//            mav.addObject("message", "An account for that username/email already exists.");
//            return mav;
//        }
//
//        return new ModelAndView("successRegister", "user", userDto);
//    }
    public ModelAndView registerUserAccount(@ModelAttribute("user") UserDto userDto,
                                            HttpServletRequest request, Errors errors) {
        //...
        return null;
    }
}
