package com.payMyBuddy.app.exception;

public class ContactUserNotFoundException extends RuntimeException {

    public ContactUserNotFoundException(String message) {
        super(message);
    }

    public ContactUserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}

