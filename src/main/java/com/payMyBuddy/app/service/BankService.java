package com.payMyBuddy.app.service;

import com.payMyBuddy.app.model.Bank;
import com.payMyBuddy.app.repository.BankRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class BankService {


    private final BankRepository bankRepository;

    public BankService(BankRepository bankRepository) {
        this.bankRepository = bankRepository;
    }

    @Transactional
    public void createBank(Bank bank) {
        // Vous pouvez ajouter ici d'autres vérifications ou logiques métier avant de sauvegarder la banque
        bankRepository.save(bank); // Enregistrement de la banque dans la base de données
    }
}