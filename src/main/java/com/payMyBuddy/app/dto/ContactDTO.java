package com.payMyBuddy.app.dto;

import com.payMyBuddy.app.model.User;
import com.payMyBuddy.app.repository.UserRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class ContactDTO {

    private Long userId;
    private Long contactId;
    private final UserRepository userRepository;
    public String getEmail() {
        if (userId != null) {
            User user = userRepository.findById(userId).orElse(null);

            // If the user is found, return the email; otherwise, return null
            return user != null ? user.getEmail() : null;
        }
        return null;
    }
}
