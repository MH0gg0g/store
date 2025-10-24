package com.example.store.exceptions;

public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException() {
        super("Invalid Token ");
    }

}