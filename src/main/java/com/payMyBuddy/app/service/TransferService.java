package com.payMyBuddy.app.service;

import com.payMyBuddy.app.model.Transfer;
import com.payMyBuddy.app.repository.TransferRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransferService {

    private final TransferRepository transferRepository;

    public TransferService(TransferRepository transferRepository) {
        this.transferRepository = transferRepository;
    }

    public List<Transfer> getTransactionsByUserId(Long userId) {
        return transferRepository.findByEmitterUserId_IdOrReceiverUserId_Id(userId, userId);
    }

}
