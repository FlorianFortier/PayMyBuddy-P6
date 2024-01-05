package com.payMyBuddy.app.service;

import com.payMyBuddy.app.dto.TransactionDTO;
import com.payMyBuddy.app.dto.UserDTO;
import com.payMyBuddy.app.exception.InsufficientFundsException;
import com.payMyBuddy.app.model.Bank;
import com.payMyBuddy.app.model.Transaction;
import com.payMyBuddy.app.model.User;
import com.payMyBuddy.app.repository.BankRepository;
import com.payMyBuddy.app.repository.TransactionRepository;
import com.payMyBuddy.app.repository.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class TransactionService {

    private final Logger logger = LogManager.getLogger(TransactionService.class);
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final BankRepository bankRepository;

    public TransactionService(TransactionRepository transactionRepository, UserRepository userRepository, BankRepository bankRepository) {
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
        this.bankRepository = bankRepository;
    }

    public List<Transaction> getTransactionsByUserId(Long userId) {
        return transactionRepository.findByEmitterUserId_IdOrReceiverUserId_Id(userId, userId);
    }

    public UserDTO transferMoneyToBank(Authentication authentication, double amount) {
        // Récupérer l'utilisateur authentifié
        User currentUser = userRepository.findByEmail(authentication.getName());

        // Récupérer la banque associée à l'utilisateur
        Bank userBank = currentUser.getBank();

        // Vérifier si l'utilisateur a suffisamment d'argent pour le transfert
        if (currentUser.getBalance() >= amount) {
            // Mettre à jour le solde de l'utilisateur
            currentUser.setBalance(currentUser.getBalance() - amount);
            userRepository.save(currentUser);

            // Créer une transaction
            Transaction transaction = new Transaction();
            transaction.setEmitterUserId(currentUser);
            transaction.setAmount(amount);
            transaction.setEmitterBankId(userBank);
            transactionRepository.save(transaction);

            // Mettre à jour le solde de la banque
            userBank.setBalance(userBank.getBalance() + amount);
            bankRepository.save(userBank);

            return new UserDTO();
        } else {
            // Gérer le cas où l'utilisateur n'a pas assez d'argent
            logger.error("Transfert échoué : Solde insuffisant.");
            throw new InsufficientFundsException(currentUser.getBalance(), amount);
        }
    }

    public UserDTO transferMoneyFromBank(Authentication authentication, double amount) {
        User currentUser = userRepository.findByEmail(authentication.getName());
        Bank userBank = currentUser.getBank();

        // Vérifier si la banque a suffisamment d'argent pour le transfert
        if (userBank.getBalance() >= amount) {
            // Créer une transaction
            Transaction transaction = new Transaction();
            transaction.setEmitterBankId(userBank);
            transaction.setAmount(amount);
            transaction.setReceiverUserId(currentUser);
            transactionRepository.save(transaction);

            // Mettre à jour le solde de la banque
            userBank.setBalance(userBank.getBalance() - amount);
            bankRepository.save(userBank);

            // Mettre à jour le solde de l'utilisateur
            currentUser.setBalance(currentUser.getBalance() + amount);
            userRepository.save(currentUser);

            return new UserDTO();
        } else {
            logger.error("Transfert échoué : Solde insuffisant dans la banque.");
            throw new InsufficientFundsException(userBank.getBalance(), amount);
        }
    }

    public UserDTO transferMoneyToUser(Authentication authentication, double amount, String recipientEmail) {
        User currentUser = userRepository.findByEmail(authentication.getName());
        User recipientUser = userRepository.findByEmail(recipientEmail);

        // Vérifier si l'utilisateur a suffisamment d'argent pour le transfert
        if (currentUser.getBalance() >= amount) {
            // Créer une transaction
            Transaction transaction = new Transaction();
            transaction.setEmitterUserId(currentUser);
            transaction.setAmount(amount);
            transaction.setReceiverUserId(recipientUser);
            transactionRepository.save(transaction);

            // Mettre à jour le solde de l'émetteur
            currentUser.setBalance(currentUser.getBalance() - amount);
            userRepository.save(currentUser);

            // Mettre à jour le solde du destinataire
            recipientUser.setBalance(recipientUser.getBalance() + amount);
            userRepository.save(recipientUser);

            return new UserDTO();
        } else {
            logger.error("Transfert échoué : Solde insuffisant.");
            throw new InsufficientFundsException(currentUser.getBalance(), amount);
        }
    }

}
