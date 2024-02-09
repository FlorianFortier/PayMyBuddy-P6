package com.payMyBuddy.app.controller;

import com.payMyBuddy.app.exception.ContactUserNotFoundException;
import com.payMyBuddy.app.model.Contact;
import com.payMyBuddy.app.model.User;
import com.payMyBuddy.app.repository.UserRepository;
import com.payMyBuddy.app.service.ContactService;
import com.payMyBuddy.app.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/contacts")
@RequiredArgsConstructor
public class ContactController {

    private final ContactService contactService;

    private final CustomUserDetailsService customUserDetailsService;

    Authentication authentication;
    @GetMapping
    public String showContacts(Model model, Authentication authentication) {
        org.springframework.security.core.userdetails.User currentUser = (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
        User user = customUserDetailsService.getUserIdByUsername(currentUser.getUsername());
        Long currentUserId = user.getId();
        List<Contact> contacts = contactService.getContactsByUserId(currentUserId);
        model.addAttribute("contacts", contacts);
        return "contacts"; // Assuming you have a contacts.html Thymeleaf template
    }
    @PostMapping("/addContact")
    public String addContact(@RequestParam("contactEmail") String contactEmail, Authentication authentication, Model model) {
        try {
            org.springframework.security.core.userdetails.User currentUser = (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
            User user = customUserDetailsService.getUserIdByUsername(currentUser.getUsername());

            contactService.addContact(user, contactEmail);
            model.addAttribute("successMessage", "Contact added successfully");
        } catch (ContactUserNotFoundException e) {
            model.addAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/contacts"; // Redirect to the contacts page
    }
}

