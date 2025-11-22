package com.example.store.exceptions;

public class CartEmptyException extends RuntimeException {
    public CartEmptyException() {
        super("The cart is empty.");
    }
}
