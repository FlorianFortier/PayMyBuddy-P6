package com.payMyBuddy.app.exeption;

public class UserNotFoundExeption extends  RuntimeException {
    public UserNotFoundExeption(String message, Throwable cause) {
        super(message, cause);
    }
}
