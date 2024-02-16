package com.payMyBuddy.app.controller;

import com.payMyBuddy.app.dto.BankDTO;
import com.payMyBuddy.app.model.Bank;
import com.payMyBuddy.app.model.User;
import com.payMyBuddy.app.service.BankService;
import com.payMyBuddy.app.service.CustomUserDetailsService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

@Controller
public class BankController {

    private final BankService bankService;

    private final CustomUserDetailsService customUserDetailsService;

    public BankController(BankService bankService, CustomUserDetailsService customUserDetailsService) {
        this.bankService = bankService;
        this.customUserDetailsService = customUserDetailsService;
    }


    @GetMapping("/bank")
    public String bank(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal() instanceof String) {
            // Rediriger vers la page de connexion
            return "redirect:/login";
        }
        else {
            return "bank";
        }
    }

    @PostMapping("/create-bank")
    public String createBank(@RequestParam String name, @RequestParam String address, HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        BankDTO bankDTO = new BankDTO();
        bankDTO.setName(name);
        bankDTO.setAddress(address);
        bankDTO.setBalance(1000);
        org.springframework.security.core.userdetails.User currentUser = (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
        Long userId = customUserDetailsService.getUserIdByEmail(currentUser.getUsername());
        bankDTO.setUserId(userId);

        Bank bank = convertToBank(bankDTO, response, request, authentication);

        bankService.createBank(bank);

        return "redirect:/transfer.html";
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
