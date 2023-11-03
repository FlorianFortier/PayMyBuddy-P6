package com.payMyBuddy.app.exception;

public class UserNotFoundExeption extends  RuntimeException {
    public UserNotFoundExeption(String message, Throwable cause) {
        super(message, cause);
    }
}
