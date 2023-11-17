package com.payMyBuddy.app.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorController {
    @GetMapping("/error.html")
    public String handleError(Model model, HttpSession session) {
        // Retrieve the error message from the session
        String errorMessage = (String) session.getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);

        // Add the error message to the model
        model.addAttribute("message", errorMessage);
        return "error";
    }
}
