package com.example.store.exceptions;

public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException() {
        super("The provided token is invalid.");
    }

    public InvalidTokenException(String message) {
        super(message);
    }

}