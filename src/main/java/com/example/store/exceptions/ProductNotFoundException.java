package com.example.store.exceptions;

public class ProductNotFoundException extends RuntimeException{
    public ProductNotFoundException(Long productId) {
        super("product " + productId + "not found");
    }
}
