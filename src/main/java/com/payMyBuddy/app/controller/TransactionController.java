package com.payMyBuddy.app.controller;

import com.payMyBuddy.app.dto.TransactionDTO;
import com.payMyBuddy.app.model.Transaction;
import com.payMyBuddy.app.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping("/{userId}")
    public List<Transaction> getTransfersByUserId(@PathVariable Long userId) {
        return transactionService.getTransactionsByUserId(userId);
    }

    @PostMapping("/transfer")
    public String transferMoney(@ModelAttribute("transactionDTO") TransactionDTO transactionDTO,
                                Authentication authentication,
                                @RequestParam String email) {
        transactionService.transferMoneyToUser(authentication, transactionDTO.getAmount(), email);

        // Redirect to index.html after the transfer is completed
        return "redirect:/index.html";
    }
}

