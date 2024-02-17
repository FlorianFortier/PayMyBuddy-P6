package com.payMyBuddy.app.exception;

public class RecipientUserDoesNotExist extends RuntimeException{
    public RecipientUserDoesNotExist(String message) {
        super(message);
    }
}
