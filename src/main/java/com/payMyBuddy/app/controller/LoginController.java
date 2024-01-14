package com.payMyBuddy.app.controller;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    LoginController(AuthenticationManager authenticationManager) {
    }

    @GetMapping("/login")
    public String login(Model model, String error, String logout) {
        if (error != null) {
            model.addAttribute("emailNotConfirmed", true);
        }

        if (logout != null) {
            model.addAttribute("message", true);
        }

        return "login";
    }
}
