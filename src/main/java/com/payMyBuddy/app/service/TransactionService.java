package com.payMyBuddy.app.service;

import com.payMyBuddy.app.dto.UserDTO;
import com.payMyBuddy.app.exception.InsufficientFundsException;
import com.payMyBuddy.app.model.Bank;
import com.payMyBuddy.app.model.Transaction;
import com.payMyBuddy.app.model.User;
import com.payMyBuddy.app.repository.BankRepository;
import com.payMyBuddy.app.repository.TransactionRepository;
import com.payMyBuddy.app.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.Authentication;
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


    public void transferMoneyToUserFromBank(User user, Double amount) {
        // Récupérer la banque de l'utilisateur
        Bank userBank = user.getBank();

        // Vérifier si la banque et l'utilisateur existent
        if (userBank != null) {
            // Effectuer la logique de transfert depuis la banque vers le solde de l'utilisateur
            double userBalance = user.getBalance();
            double bankBalance = userBank.getBalance();

            // Vérifier si la banque a suffisamment de fonds
            if (bankBalance >= amount) {
                // Déduire le montant de la banque
                userBank.setBalance(bankBalance - amount);
                bankRepository.save(userBank);

                // Ajouter le montant au solde de l'utilisateur
                user.setBalance(userBalance + amount);
                userRepository.save(user);

                // Enregistrer la transaction si nécessaire
                Transaction transaction = new Transaction();
                // Paramètres de la transaction...
                transactionRepository.save(transaction);
            } else {
                throw new InsufficientFundsException("Vous semblez ne pas avoir le solde suffisant vérifier votre moyen de paiement.", bankBalance, amount);
            }
        } else {
            throw new EntityNotFoundException("La banque ou l'utilisateur n'existe pas");
        }
    }

    public void transferMoneyToUser(Authentication authentication, Double amount, String recipientEmail) {
        User currentUser = userRepository.findByEmail(authentication.getName());
        User recipientUser = userRepository.findByEmail(recipientEmail);

        // Vérifier si l'utilisateur a suffisamment d'argent pour le transfert
        if (currentUser.getBalance() >= amount) {
            // Calculer la commission (0.5%)
            Double fee = calculateFee(amount);

            // Créer une transaction
            Transaction transaction = new Transaction();
            transaction.setEmitterUserId(currentUser);
            transaction.setAmount(amount - fee); // Deduct the fee from the transfer amount
            transaction.setReceiverUserId(recipientUser);
            transactionRepository.save(transaction);

            // Mettre à jour le solde de l'émetteur (déduire le montant total avec la commission)
            currentUser.setBalance(currentUser.getBalance() - (amount - fee));
            userRepository.save(currentUser);

            // Mettre à jour le solde du destinataire
            recipientUser.setBalance(recipientUser.getBalance() + (amount - fee));
            userRepository.save(recipientUser);

        } else {
            logger.error("Transfert échoué : Solde insuffisant.");
            throw new InsufficientFundsException(currentUser.getBalance(), amount);
        }
    }

    // Méthode pour calculer la commission (0.5%)
    private double calculateFee(Double amount) {
        // 0.5% of the transfer amount
        double feePercentage = 0.005;
        return amount * feePercentage;
    }
}
