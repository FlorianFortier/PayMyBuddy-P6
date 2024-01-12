package com.payMyBuddy.app.service;

import com.payMyBuddy.app.dto.ContactDTO;
import com.payMyBuddy.app.exception.ContactUserNotFoundException;
import com.payMyBuddy.app.model.Contact;
import com.payMyBuddy.app.model.User;
import com.payMyBuddy.app.repository.ContactRepository;
import com.payMyBuddy.app.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

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

    public void addContact(User user, String email) {
        User contactUser = userRepository.findByEmail(email);

        if (contactUser != null) {
            Contact newContact = new Contact(user.getId(), contactUser.getId());
            contactRepository.save(newContact);
        } else {
            // Handle the case where the contact user is not found
            log.error("Contact user not found for email: " + email);
            throw new ContactUserNotFoundException("Contact user not found for email: " + email);
        }
    }

}
