package com.payMyBuddy.app.service;

import com.payMyBuddy.app.exception.UserHaveAlreadyABank;
import com.payMyBuddy.app.model.Bank;
import com.payMyBuddy.app.model.User;
import com.payMyBuddy.app.repository.BankRepository;
import com.payMyBuddy.app.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BankService {


    private final BankRepository bankRepository;
    private final UserRepository userRepository;

    public BankService(BankRepository bankRepository, UserRepository userRepository) {
        this.bankRepository = bankRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public void createBank(Bank bank) {
        Long userId = bank.getUser().getId();
        Optional<User> customUser = userRepository.findById(userId);
        if (customUser.get().getBank() == null) {
            bankRepository.save(bank); // Enregistrement de la banque dans la base de données
        } else {
            throw new UserHaveAlreadyABank("Vous avez déjà une banque liée à votre compte");
        }
    }
    public Optional<Bank> getUserBank(Authentication authentication) {
        Optional<User> user = Optional.ofNullable(userRepository.findByEmail(authentication.getName()));
        if (user.isPresent()) {
            Optional<Bank> bank = bankRepository.findById(user.get().getId());
            if (bank.isPresent()) {
                Optional<User> customUser = userRepository.findById(bank.get().getUser().getId());
                return bankRepository.findById(customUser.get().getId());
            }
        }
        return Optional.empty(); // Retourne un Optional vide si la banque n'existe pas
    }
}