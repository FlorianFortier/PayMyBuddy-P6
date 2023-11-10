package com.payMyBuddy.app.repository;

import com.payMyBuddy.app.model.User;
import com.payMyBuddy.app.model.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationTokenRepository
        extends JpaRepository<VerificationToken, Long> {

    VerificationToken findByToken(String token);

    VerificationToken findByUser(User user);
}