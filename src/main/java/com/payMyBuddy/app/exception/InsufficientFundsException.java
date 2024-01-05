package com.payMyBuddy.app.exception;

public class InsufficientFundsException extends RuntimeException {
    private final double balance;

    private final double amount;

    public InsufficientFundsException(double balance, double amount) {
        super("You have not enough money on balance to transfer this specific amount");
        this.balance = balance;
        this.amount = amount;
    }

    public double getBalance() {
        return balance;
    }

    public double getAmount() {
        return amount;
    }
}
