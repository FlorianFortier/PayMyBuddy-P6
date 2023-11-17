package com.payMyBuddy.app.controller;

import com.payMyBuddy.app.model.User;
import org.springframework.beans.factory.annotation.Autowired;
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

import static org.springframework.security.core.context.SecurityContextHolder.*;


@Controller
public class LoginController {

    @Autowired
    private AuthenticationManager authenticationManager;
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
    public String performLogin(@RequestParam String username, @RequestParam String password) {
        try {

            // Créer une instance d'UsernamePasswordAuthenticationToken avec les informations d'identification
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);

            // Authentifier l'utilisateur
            Authentication authentication = authenticationManager.authenticate(token);

            // Vérifier si l'utilisateur est activé
            User user = (User) authentication.getPrincipal();
            if (user.isEnabled()) {
                // L'utilisateur n'est pas activé, géré cette situation comme une erreur
                // Mettre à jour le contexte de sécurité
                getContext().setAuthentication(authentication);

                // Rediriger vers la page d'accueil après une connexion réussie
                return "redirect:/index.html";
            } else {
                return "redirect:/error.html";
            }
        } catch (Exception e) {
            e.printStackTrace(); // Log the exception
            // Gérer l'erreur, par exemple, rediriger vers une page d'erreur
            return "redirect:/error.html";
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
