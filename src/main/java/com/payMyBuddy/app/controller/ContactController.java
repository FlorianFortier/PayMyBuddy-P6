package com.payMyBuddy.app.controller;

import com.payMyBuddy.app.exception.ContactUserNotFoundException;
import com.payMyBuddy.app.model.Contact;
import com.payMyBuddy.app.model.User;
import com.payMyBuddy.app.repository.UserRepository;
import com.payMyBuddy.app.service.ContactService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/contacts")
@RequiredArgsConstructor
public class ContactController {

    private final ContactService contactService;
    private final UserRepository userRepository;

    @GetMapping
    public String showContacts(Model model, Authentication authentication) {
        User currentUser = getUserFromAuthentication(authentication);
        List<Contact> contacts = contactService.getContactsByUserId(currentUser.getId());
        model.addAttribute("contacts", contacts);
        return "contacts"; // Assuming you have a contacts.html Thymeleaf template
    }

    @PostMapping("/addContact")
    public String addContact(@RequestParam("contactEmail") String contactEmail, Authentication authentication, Model model) {
        try {
            User currentUser = getUserFromAuthentication(authentication);
            contactService.addContact(currentUser, contactEmail);
            model.addAttribute("successMessage", "Contact added successfully");
        } catch (ContactUserNotFoundException e) {
            model.addAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/index.html";
    }


    private User getUserFromAuthentication(Authentication authentication) {
        return userRepository.findByEmail(authentication.getName());
    }
}

