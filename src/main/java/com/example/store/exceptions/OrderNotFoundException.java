package com.example.store.exceptions;

public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException(Long Id) {
        super("Order ID" + Id + "Not found");
    }
}
