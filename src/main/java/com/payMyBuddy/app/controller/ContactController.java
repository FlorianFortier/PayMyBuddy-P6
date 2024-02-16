package com.payMyBuddy.app.controller;

import com.payMyBuddy.app.exception.ContactUserNotFoundException;
import com.payMyBuddy.app.model.Contact;
import com.payMyBuddy.app.service.ContactService;
import com.payMyBuddy.app.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/contacts")
@RequiredArgsConstructor
public class ContactController {

    private final ContactService contactService;

    private final CustomUserDetailsService customUserDetailsService;


    @GetMapping
    public String showContacts(Model model, Authentication authentication) {
        org.springframework.security.core.userdetails.User currentUser = (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
        Long userId = customUserDetailsService.getUserIdByUsername(currentUser.getUsername());

        List<Contact> contacts = contactService.getContactsByUserId(userId);
        model.addAttribute("contacts", contacts);
        return "contacts";
    }
    @PostMapping("/addContact")
    public String addContact(@RequestParam("contactEmail") String contactEmail, Authentication authentication, Model model) {
        try {
            org.springframework.security.core.userdetails.User currentUser = (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
            Long userId = customUserDetailsService.getUserIdByUsername(currentUser.getUsername());

            contactService.addContact(userId, contactEmail);
            model.addAttribute("successMessage", "Contact added successfully");
        } catch (ContactUserNotFoundException e) {
            model.addAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/contacts";
    }
}

