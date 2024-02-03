package com.payMyBuddy.app.exception;

import lombok.Getter;

@Getter
public class InsufficientFundsException extends RuntimeException {
    private final double balance;

    private final double amount;

    public InsufficientFundsException(double balance, double amount) {
        super("You have not enough money on balance to transfer this specific amount");
        this.balance = balance;
        this.amount = amount;
    }
    public InsufficientFundsException(String message, double balance, double amount) {
        super(message);
        this.balance = balance;
        this.amount = amount;
    }

}
