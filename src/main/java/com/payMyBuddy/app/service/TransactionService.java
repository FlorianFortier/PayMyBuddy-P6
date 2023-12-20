package com.payMyBuddy.app.service;

import com.payMyBuddy.app.dto.TransactionDTO;
import com.payMyBuddy.app.dto.UserDTO;
import org.springframework.security.core.Authentication;

public interface TransactionService {

    UserDTO transferMoneyToUser(TransactionDTO transactionDTO, Authentication authentication);

}
