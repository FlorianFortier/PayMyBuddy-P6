package com.payMyBuddy.app.exeption;


public class UserAlreadyExistException extends RuntimeException {
    public UserAlreadyExistException(String message) {
        super(message, cause);
    }
}
