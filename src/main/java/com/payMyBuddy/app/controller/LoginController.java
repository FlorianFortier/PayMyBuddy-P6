package com.payMyBuddy.app.controller;

import com.payMyBuddy.app.model.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Locale;

import static org.springframework.security.core.context.SecurityContextHolder.*;


@Controller
public class LoginController {

    @Autowired
    private AuthenticationManager authenticationManager;

    private MessageSource message;
    @GetMapping("/login")
    public String login(Model model, String error, String logout) {
        if (error != null) {
            model.addAttribute("error", "Your username and password are invalid.");
        }

        if (logout != null) {
            model.addAttribute("message", "You have been logged out successfully.");
        }

        return "login";
    }
    @PostMapping("/perform_login")
    public String performLogin(@RequestParam String username, @RequestParam String password, RedirectAttributes redirectAttribute, HttpServletRequest request) {
        Locale locale = request.getLocale();

        // Créer une instance d'UsernamePasswordAuthenticationToken avec les informations d'identification
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);

        // Authentifier l'utilisateur
        Authentication authentication = authenticationManager.authenticate(token);

        // Vérifier si l'utilisateur est activé
        User user = (User) authentication.getPrincipal();

        if (user.isEnabled()) {
            // Mettre à jour le contexte de sécurité
            getContext().setAuthentication(authentication);

            // Rediriger vers la page d'accueil après une connexion réussie
            return "redirect:/index.html";
        } else {
            // Si l'utilisateur n'a pas confirmé son email, on le notifie
            boolean notConfirmed = true;
            redirectAttribute.addFlashAttribute("emailNotConfirmed", notConfirmed);
            return "redirect:/login.html";
        }
    }

    /**
     *
     * @return view "index"
     */
    @GetMapping("/index.html")
    public String home() {
        return "index";
    }

}
