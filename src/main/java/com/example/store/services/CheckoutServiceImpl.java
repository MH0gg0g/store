package com.example.store.services;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.store.aop.Loggable;
import com.example.store.dtos.CheckoutResponse;
import com.example.store.entities.Order;
import com.example.store.entities.OrderStatus;
import com.example.store.exceptions.CartEmptyException;
import com.example.store.exceptions.CartNotFoundException;
import com.example.store.repositories.CartRepository;
import com.example.store.repositories.OrderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CheckoutServiceImpl implements CheckoutService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final CartService cartService;
    private final AuthService authService;

    @Loggable
    public CheckoutResponse checkout(UUID cartId) {
        var cart = cartRepository.findById(cartId).orElseThrow(
                () -> new CartNotFoundException(cartId));

        if (cart.isEmpty()) {
            throw new CartEmptyException();
        }

        var currentUser = authService.getCurrentUser();
        var order = Order.fromCart(cart, currentUser);

        orderRepository.save(order);
        cartService.clearCart(cart.getId());

        return new CheckoutResponse(order.getId(), order.getTotalAmount());
    }

    @Loggable
    public void cancelOrder(Long orderId) {
        var order = orderRepository.findById(orderId).orElseThrow(
                () -> new RuntimeException("Order not found: " + orderId));

        order.setStatus(OrderStatus.CANCELED);
        orderRepository.save(order);

    }

}
