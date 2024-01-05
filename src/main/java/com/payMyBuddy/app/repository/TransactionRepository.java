package com.payMyBuddy.app.repository;

import com.payMyBuddy.app.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByEmitterUserId_IdOrReceiverUserId_Id(Long emitterUserId, Long receiverUserId);
}
