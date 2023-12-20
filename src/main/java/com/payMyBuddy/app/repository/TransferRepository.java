package com.payMyBuddy.app.repository;

import com.payMyBuddy.app.model.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransferRepository extends JpaRepository<Transfer, Long> {
    List<Transfer> findByEmitterUserId_IdOrReceiverUserId_Id(Long emitterUserId, Long receiverUserId);
}
