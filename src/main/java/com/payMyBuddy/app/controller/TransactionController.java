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

import java.text.DecimalFormat;
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
     * @return view "index"
     */
    @GetMapping("/transfer.html")
    public String transfer(Model model, RedirectAttributes redirectAttribute, HttpServletRequest request) {

        // Récupération de l'utilisateur connecté
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal() instanceof String) {
            // Rediriger vers la page de connexion
            return "redirect:/login";
        }
        org.springframework.security.core.userdetails.User loggedInUser = (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
        User customUser = userService.getUserByEmail(loggedInUser.getUsername());
        List<Contact> contacts = contactService.getContactsByUserId(customUser.getId());
        List<Transaction> transactions = transactionService.getTransactionsByUserId(customUser.getId());
        Double solde = customUser.getBalance();

        // Formatter le solde avec deux chiffres après la virgule
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        String formattedSolde = decimalFormat.format(solde);

        // Passer à la vue les contacts et transactions.
        model.addAttribute("contacts", contacts);
        model.addAttribute("transfers", transactions);
        model.addAttribute("solde", formattedSolde);
        return "transfer";
    }

    @PostMapping("/transfer")
    public String transferMoney(@ModelAttribute("transactionDTO") TransactionDTO transactionDTO,
                                Authentication authentication,
                                @RequestParam String email, RedirectAttributes redirectAttributes) {

        transactionService.transferMoneyToUser(authentication, transactionDTO.getAmount(), email);

        // Redirect to transfer.html after the transfer is completed
        redirectAttributes.addFlashAttribute("successMessage", "Transfert d'argent réussi!");

        return "redirect:/transfer.html";
    }

    @PostMapping("/transferFromBank")
    public String transferFromBank(@ModelAttribute("transactionDTO") TransactionDTO transactionDTO,
                                   Authentication authentication,
                                   RedirectAttributes redirectAttributes) {
        // Récupérer l'utilisateur connecté
        org.springframework.security.core.userdetails.User loggedInUser = (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
        User customUser = userService.getUserByEmail(loggedInUser.getUsername());

        // Effectuer le transfert depuis la banque vers le solde de l'utilisateur
        transactionService.transferMoneyToUserFromBank(customUser, transactionDTO.getAmount());

        // Ajouter un message pour afficher le succès du transfert depuis la banque
        redirectAttributes.addFlashAttribute("successMessage", "Transfert depuis la banque réussi!");

        // Rediriger vers la page de transfert
        return "redirect:/transfer.html";
    }
}

