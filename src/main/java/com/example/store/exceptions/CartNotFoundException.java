package com.example.store.exceptions;

import java.util.UUID;

public class CartNotFoundException extends RuntimeException {
    public CartNotFoundException(UUID cartId) {
        super("Cart with ID " + cartId + " not found.");
    }
}
