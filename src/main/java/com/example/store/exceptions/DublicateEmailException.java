package com.example.store.exceptions;

public class DublicateEmailException extends RuntimeException{
    public DublicateEmailException(String email) {
        super("The email '" + email + "' is already in use.");
    } 
}
