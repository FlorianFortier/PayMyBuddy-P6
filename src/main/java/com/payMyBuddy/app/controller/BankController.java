package com.payMyBuddy.app.controller;

import com.payMyBuddy.app.dto.BankDTO;
import com.payMyBuddy.app.exception.UserHaveAlreadyABank;
import com.payMyBuddy.app.model.Bank;
import com.payMyBuddy.app.model.User;
import com.payMyBuddy.app.service.BankService;
import com.payMyBuddy.app.service.CustomUserDetailsService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.Optional;
import java.util.ResourceBundle;

@Controller
public class BankController {

    private final BankService bankService;

    private final CustomUserDetailsService customUserDetailsService;

    public BankController(BankService bankService, CustomUserDetailsService customUserDetailsService) {
        this.bankService = bankService;
        this.customUserDetailsService = customUserDetailsService;
    }


    @GetMapping("/bank")
    public String bank(Authentication authentication, Model model) {
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal() instanceof String) {
            // Rediriger vers la page de connexion
            return "redirect:/login";
        }
        else {
            customUserDetailsService.getUserIdByEmail(authentication.getName());
            Optional<Bank> bankOptional = bankService.getUserBank(authentication);
            if(bankOptional.isPresent()) {
                Bank bank = bankOptional.get();
                model.addAttribute("bank", bank);
                model.addAttribute("bankPresent", true);
            } else {
                model.addAttribute("bankPresent", false);
            }
            return "bank";
        }
    }

    @PostMapping("/create-bank")
    public String createBank(@RequestParam String name, @RequestParam String address, HttpServletRequest request, HttpServletResponse response, Authentication authentication, RedirectAttributes redirectAttributes) throws IOException {
        ResourceBundle bundle = ResourceBundle.getBundle("messages");

        try {
            BankDTO bankDTO = new BankDTO();
            bankDTO.setName(name);
            bankDTO.setAddress(address);
            bankDTO.setBalance(1000);
            org.springframework.security.core.userdetails.User currentUser = (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
            Long userId = customUserDetailsService.getUserIdByEmail(currentUser.getUsername());
            bankDTO.setUserId(userId);

            Bank bank = convertToBank(bankDTO, response, request, authentication);
            if (bank !=null) {
                bankService.createBank(bank);
            }
            redirectAttributes.addFlashAttribute("successMessage", bundle.getString("bank.create.success"));

        } catch (UserHaveAlreadyABank e) {
            redirectAttributes.addFlashAttribute("errorMessage", bundle.getString("bank.create.error.already.have.bank"));

            return "redirect:/bank";
        }


        return "redirect:/bank";
    }

    private Bank convertToBank(BankDTO bankDTO, HttpServletResponse response, HttpServletRequest request, Authentication authentication) throws IOException {
        Bank bank = new Bank();
        bank.setName(bankDTO.getName());
        bank.setAddress(bankDTO.getAddress());
        bank.setBalance(bankDTO.getBalance());

        if (authentication.isAuthenticated() || authentication.getPrincipal() instanceof String) {
            org.springframework.security.core.userdetails.User currentUser = (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
            User user = new User();
            user.setId(customUserDetailsService.getUserIdByEmail(currentUser.getUsername()));
            bank.setUser(user);
        } else {
            String loginUrl = "/login";
            String redirectUrl = loginUrl + "?redirect=" + UrlUtils.buildRequestUrl(request);
            response.sendRedirect(redirectUrl);
            return null;
        }
        return bank;
    }
}
