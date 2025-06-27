package com.example.store.exceptions;

public class InvalidJwtToken extends RuntimeException {
    public InvalidJwtToken() {
        super("Invalid JWT Token ");
    }

}
