package com.payMyBuddy.app.controller;

import com.payMyBuddy.app.model.Contact;
import com.payMyBuddy.app.model.Transaction;
import com.payMyBuddy.app.model.User;
import com.payMyBuddy.app.service.ContactService;
import com.payMyBuddy.app.service.TransactionService;
import com.payMyBuddy.app.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
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

import java.util.List;

@Controller
public class LoginController {

    private final AuthenticationManager authenticationManager;

    private final TransactionService transactionService;
    private final ContactService contactService;

    private final UserService userService;

    LoginController(AuthenticationManager authenticationManager, TransactionService transactionService, ContactService contactService, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.transactionService = transactionService;
        this.contactService = contactService;
        this.userService = userService;
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
    @PostMapping("/perform_login")
    public String performLogin(@RequestParam String username, @RequestParam String password, RedirectAttributes redirectAttribute, HttpServletRequest request) {
        // Créer une instance d'UsernamePasswordAuthenticationToken avec les informations d'identification
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);

        // Authentifier l'utilisateur
        Authentication authentication = authenticationManager.authenticate(token);

        // Vérifier si l'utilisateur est activé
        User user = (User) authentication.getPrincipal();

        if (user.isEnabled()) {
            // Rediriger vers la page d'accueil après une connexion réussie
            return "redirect:/index.html";
        } else {
            // Si l'utilisateur n'a pas confirmé son email, on le notifie
            redirectAttribute.addFlashAttribute("emailNotConfirmed", true);
            return "redirect:/login.html";
        }
    }

    /**
     *
     * @return view "index"
     */
    @GetMapping("/index.html")
    public String home(Model model, RedirectAttributes redirectAttribute) {
        // Récupération de l'utilisateur connecté
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        org.springframework.security.core.userdetails.User loggedInUser = (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
        User customUser = userService.getUserByEmail(loggedInUser.getUsername());
        List<Contact> contacts = contactService.getContactsByUserId(customUser.getId());
        List<Transaction> transactions = transactionService.getTransactionsByUserId(customUser.getId());

        // Passer à la vue les contacts et transactions.
        model.addAttribute("contacts", contacts);
        model.addAttribute("transfers", transactions);
        return "index";
    }
}
