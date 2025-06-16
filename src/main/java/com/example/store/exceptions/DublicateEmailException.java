package com.example.store.exceptions;

public class DublicateEmailException extends RuntimeException{
    public DublicateEmailException() {
        super("Email Already Exists");
    } 
}
