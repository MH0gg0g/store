package com.example.store.exceptions;

public class InvalidPasswordException extends RuntimeException{
    public InvalidPasswordException() {
        super("The provided password is invalid.");
    }

}
