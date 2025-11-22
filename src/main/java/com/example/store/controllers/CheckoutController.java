package com.example.store.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.store.dtos.CheckoutRequest;
import com.example.store.dtos.CheckoutResponse;
import com.example.store.services.CheckoutService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/checkout")
public class CheckoutController {
    private final CheckoutService checkoutService;

    @PostMapping
    public CheckoutResponse checkout(@Valid @RequestBody CheckoutRequest request) {
        return checkoutService.checkout(request.getCartId());
    }

}
