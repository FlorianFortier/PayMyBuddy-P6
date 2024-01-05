package com.payMyBuddy.app.controller;

import com.payMyBuddy.app.dto.TransactionDTO;
import com.payMyBuddy.app.exception.InsufficientFundsException;
import com.payMyBuddy.app.model.Transaction;
import com.payMyBuddy.app.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping("/{userId}")
    public ResponseEntity<List<Transaction>> getTransfersByUserId(@PathVariable Long userId) {
        List<Transaction> transactions = transactionService.getTransactionsByUserId(userId);
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

    @PostMapping("/transfer")
    public ResponseEntity<String> transferMoney(@RequestBody TransactionDTO transactionDTO, Authentication authentication) {
        try {
            // Vous devrez implémenter la logique de transfert d'argent ici en utilisant les données de transactionDTO.
            transactionService.transferMoneyToUser(authentication, transactionDTO.getAmount());

            // Pour l'exemple, nous renvoyons simplement une réponse réussie.
            return new ResponseEntity<>("Transfer successful", HttpStatus.OK);
        } catch (InsufficientFundsException e) {
            // Gérer l'exception de solde insuffisant
            return new ResponseEntity<>("Transfer failed: Insufficient funds", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            // Gérer les autres exceptions
            return new ResponseEntity<>("Transfer failed: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

