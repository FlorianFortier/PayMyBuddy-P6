package com.payMyBuddy.app.repository;

import com.payMyBuddy.app.model.Bank;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankRepository extends JpaRepository<Bank, Long> {

    Bank findByName(String bank);
}
