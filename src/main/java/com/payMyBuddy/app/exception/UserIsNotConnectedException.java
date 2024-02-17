package com.payMyBuddy.app.exception;

public class UserIsNotConnectedException extends RuntimeException
{
    public UserIsNotConnectedException(String message) {
        super(message);
    }
}
