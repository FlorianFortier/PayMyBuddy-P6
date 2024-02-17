package com.payMyBuddy.app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class LoginController {



    @GetMapping("/login")
    public String login(Model model, String error, String logout) {
        if (error != null && error.contains("email")) {
            model.addAttribute("emailNotConfirmed", true);
        } else if (error !=null){
            model.addAttribute("userDoesNotExist", true);
        }

        if (logout != null) {
            model.addAttribute("message", true);
        }

        return "login";
    }
}
