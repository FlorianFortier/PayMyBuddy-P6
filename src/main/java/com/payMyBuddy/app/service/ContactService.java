package com.payMyBuddy.app.service;

import com.payMyBuddy.app.exception.ContactUserNotFoundException;
import com.payMyBuddy.app.exception.UserIsNotConnectedException;
import com.payMyBuddy.app.model.Contact;
import com.payMyBuddy.app.model.User;
import com.payMyBuddy.app.repository.ContactRepository;
import com.payMyBuddy.app.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.ResourceBundle;

@Service
@Slf4j
public class ContactService {

    private final ContactRepository contactRepository;

    private final UserRepository userRepository;

    @Autowired
    public ContactService(ContactRepository contactRepository, UserRepository userRepository) {
        this.contactRepository = contactRepository;
        this.userRepository = userRepository;
    }

    public List<Contact> getContactsByUserId(Long userId) {
        return contactRepository.findByUserId(userId);
    }

    public void addContact(Long userId, String email) {
        User contactUser = userRepository.findByEmail(email);
        User user;
        if (userRepository.findById(userId).isPresent()) {
            user = userRepository.findById(userId).get();
        } else {
            log.error("user is not connected" + email);
            throw new UserIsNotConnectedException("User is not connected or disconnected");
        }
        
        if (contactUser != null) {
            Contact newContact = new Contact(user, contactUser);
            contactRepository.save(newContact);
        } else {
            ResourceBundle bundle = ResourceBundle.getBundle("messages");
            log.error("Contact user not found for email: " + email);
            throw new ContactUserNotFoundException(bundle.getString("contact.error.userNotFound") + " " + email);
        }
    }


}
