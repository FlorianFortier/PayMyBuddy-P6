package com.payMyBuddy.app.exception;

public class UserHaveAlreadyABank extends RuntimeException{
    public UserHaveAlreadyABank(String message) {
        super(message);
    }
}
