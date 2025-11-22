package com.example.store.services;

import java.util.List;
import java.util.UUID;

import com.example.store.dtos.CartDto;
import com.example.store.dtos.CartItemDto;

public interface CartService {
    CartDto createCart();

    CartItemDto addToCart(UUID cartId, Long productId);

    CartDto getCart(UUID cartId);

    CartItemDto updateItem(UUID cartId, Long productId, Long quantity);

    void removeItem(UUID cartId, Long productId);

    List<CartDto> getAllCarts();

    void removeCart(UUID cartId);

    void clearCart(UUID cartId);
}
