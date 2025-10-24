package com.example.store.services;

import java.util.UUID;

import com.example.store.dtos.CheckoutResponse;

public interface CheckoutService {
    CheckoutResponse checkout(UUID cartId);
}
