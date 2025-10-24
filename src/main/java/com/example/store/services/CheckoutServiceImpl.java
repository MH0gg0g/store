package com.example.store.services;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.store.dtos.CheckoutResponse;
import com.example.store.entities.Order;
import com.example.store.exceptions.CartEmptyException;
import com.example.store.exceptions.CartNotFoundException;
import com.example.store.repositories.CartRepository;
import com.example.store.repositories.OrderRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CheckoutServiceImpl implements CheckoutService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final CartService cartService;
    private final AuthService authService;

    public CheckoutResponse checkout(UUID cartId) {
        var cart = cartRepository.findById(cartId).orElseThrow(CartNotFoundException::new);
        var currentUser = authService.getCurrentUser();

        if (cart.isEmpty()) {
            throw new CartEmptyException();
        }

        var order = Order.fromCart(cart, currentUser);

        orderRepository.save(order);
        cartService.clearCart(cart.getId());

        return new CheckoutResponse(order.getId());
    }

}
