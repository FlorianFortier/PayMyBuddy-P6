package com.payMyBuddy.app.controller;

import com.payMyBuddy.app.dto.TransactionDTO;
import com.payMyBuddy.app.model.Contact;
import com.payMyBuddy.app.model.Transaction;
import com.payMyBuddy.app.model.User;
import com.payMyBuddy.app.service.ContactService;
import com.payMyBuddy.app.service.TransactionService;
import com.payMyBuddy.app.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;
    private final ContactService contactService;

    private final UserService userService;

    @GetMapping("/{userId}")
    public List<Transaction> getTransfersByUserId(@PathVariable Long userId) {
        return transactionService.getTransactionsByUserId(userId);
    }

    /**
     *
     * @return view "index"
     */
    @GetMapping("/transfer.html")
    public String transfer(Model model, RedirectAttributes redirectAttribute, HttpServletRequest request) {

        // Récupération de l'utilisateur connecté
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        org.springframework.security.core.userdetails.User loggedInUser = (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
        User customUser = userService.getUserByEmail(loggedInUser.getUsername());
        List<Contact> contacts = contactService.getContactsByUserId(customUser.getId());
        List<Transaction> transactions = transactionService.getTransactionsByUserId(customUser.getId());
        Double solde = customUser.getBalance();

        // Passer à la vue les contacts et transactions.
        model.addAttribute("contacts", contacts);
        model.addAttribute("transfers", transactions);
        model.addAttribute("solde", solde);
        return "transfer";
    }

    @PostMapping("/transfer")
    public String transferMoney(@ModelAttribute("transactionDTO") TransactionDTO transactionDTO,
                                Authentication authentication,
                                @RequestParam String email) {
        transactionService.transferMoneyToUser(authentication, transactionDTO.getAmount(), email);

        // Redirect to transfer.html after the transfer is completed
        return "redirect:/transfer.html";
    }
}

