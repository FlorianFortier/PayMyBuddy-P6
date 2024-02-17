package com.payMyBuddy.app.controller;

import com.payMyBuddy.app.dto.TransactionDTO;
import com.payMyBuddy.app.exception.InsufficientFundsException;
import com.payMyBuddy.app.model.Contact;
import com.payMyBuddy.app.model.Transaction;
import com.payMyBuddy.app.model.User;
import com.payMyBuddy.app.service.ContactService;
import com.payMyBuddy.app.service.CustomUserDetailsService;
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

import java.text.DecimalFormat;
import java.util.List;
import java.util.ResourceBundle;

@Controller
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;
    private final ContactService contactService;

    private final UserService userService;
    private final CustomUserDetailsService customUserDetailsService;

    @GetMapping("/{userId}")
    public List<Transaction> getTransfersByUserId(@PathVariable Long userId) {
        return transactionService.getTransactionsByUserId(userId);
    }

    /**
     * @return view "index"
     */
    @GetMapping("/transfer.html")
    public String transfer(Model model, RedirectAttributes redirectAttribute, HttpServletRequest request, Authentication authentication) {

        // Récupération de l'utilisateur connecté
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal() instanceof String) {
            // Rediriger vers la page de connexion
            return "redirect:/login";
        } else {
            org.springframework.security.core.userdetails.User currentUser = (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
            Long userId = customUserDetailsService.getUserIdByEmail(currentUser.getUsername());
            List<Contact> contacts = contactService.getContactsByUserId(userId);
            List<Transaction> transactions = transactionService.getTransactionsByUserId(userId);
            Double solde = userService.getUserByEmail(currentUser.getUsername()).getBalance();

            // Formatter le solde avec deux chiffres après la virgule
            DecimalFormat decimalFormat = new DecimalFormat("#0.00");
            String formattedSolde = decimalFormat.format(solde);
            // Passer à la vue les contacts et transactions.
            model.addAttribute("contacts", contacts);
            model.addAttribute("transfers", transactions);
            model.addAttribute("solde", formattedSolde);
        }
        return "transfer";
    }

    @PostMapping("/transfer")
    public String transferMoney(@ModelAttribute("transactionDTO") TransactionDTO transactionDTO,
                                Authentication authentication,
                                @RequestParam String email, RedirectAttributes redirectAttributes) {
        ResourceBundle bundle = ResourceBundle.getBundle("messages");

        try {
            transactionService.transferMoneyToUser(authentication, transactionDTO.getAmount(), email);

            // Redirect to transfer.html after the transfer is completed
            redirectAttributes.addFlashAttribute("successMessage", bundle.getString("transfer.toUser.success"));
        } catch (InsufficientFundsException e) {
            redirectAttributes.addFlashAttribute("errorMessage", bundle.getString("transfer.toUser.error"));

        }
        return "redirect:/transfer.html";
    }

    @PostMapping("/transferFromBank")
    public String transferFromBank(@ModelAttribute("transactionDTO") TransactionDTO transactionDTO,
                                   Authentication authentication,
                                   RedirectAttributes redirectAttributes) {
        ResourceBundle bundle = ResourceBundle.getBundle("messages");

        try {
            // Récupérer l'utilisateur connecté
            org.springframework.security.core.userdetails.User loggedInUser = (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
            User customUser = userService.getUserByEmail(loggedInUser.getUsername());

            // Effectuer le transfert depuis la banque vers le solde de l'utilisateur
            transactionService.transferMoneyToUserFromBank(customUser, transactionDTO.getAmount());

            // Ajouter un message pour afficher le succès du transfert depuis la banque
            redirectAttributes.addFlashAttribute("successMessage", bundle.getString("transfer.toAccount.success"));
        } catch (InsufficientFundsException e) {
            redirectAttributes.addFlashAttribute("errorMessage", bundle.getString("transfer.toAccount.error.not.enough.funds"));

        }


        // Rediriger vers la page de transfert
        return "redirect:/transfer.html";
    }
    @PostMapping("/transferToBank")
    public String transferToBank(@ModelAttribute("transactionDTO") TransactionDTO transactionDTO,
                                 Authentication authentication,
                                 RedirectAttributes redirectAttributes) {
        ResourceBundle bundle = ResourceBundle.getBundle("messages");

        try {
            // Récupérer l'utilisateur connecté
            org.springframework.security.core.userdetails.User loggedInUser = (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
            User customUser = userService.getUserByEmail(loggedInUser.getUsername());

            // Effectuer le transfert depuis le solde de l'utilisateur vers sa banque
            transactionService.transferMoneyFromAccountToBank(customUser, transactionDTO.getAmount());

            // Ajouter un message pour afficher le succès du transfert vers la banque
            redirectAttributes.addFlashAttribute("successMessage", bundle.getString("transfer.toBank.success"));
        } catch (InsufficientFundsException e) {
            redirectAttributes.addFlashAttribute("errorMessage", bundle.getString("transfer.toBank.error"));
        }

        // Rediriger vers la page de transfert
        return "redirect:/transfer.html";
    }
}

