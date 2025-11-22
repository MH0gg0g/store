package com.example.store.exceptions;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(Long Id) {
        super("user Id" + Id + "not found");
    }

    public UserNotFoundException(String email) {
        super("user Id" + email + "not found");
    }

}
