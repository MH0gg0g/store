package com.example.store.services;

import com.example.store.dtos.CheckoutRequest;
import com.example.store.dtos.CheckoutResponse;

public interface CheckoutService {
    CheckoutResponse checkout(CheckoutRequest request);
}
