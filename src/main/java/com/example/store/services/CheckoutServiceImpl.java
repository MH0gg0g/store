package com.example.store.services;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.example.store.dtos.CheckoutResponse;
import com.example.store.entities.Order;
import com.example.store.exceptions.CartEmptyException;
import com.example.store.exceptions.CartNotFoundException;
import com.example.store.repositories.CartRepository;
import com.example.store.repositories.OrderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CheckoutServiceImpl implements CheckoutService {

    private static final Logger logger = LoggerFactory.getLogger(CheckoutServiceImpl.class);

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final CartService cartService;
    private final AuthService authService;

    public CheckoutResponse checkout(UUID cartId) {
        logger.debug("checkout: cartId={}", cartId);
        var cart = cartRepository.findById(cartId).orElseThrow(
                () -> new CartNotFoundException(cartId));
        var currentUser = authService.getCurrentUser();

        if (cart.isEmpty()) {
            throw new CartEmptyException();
        }

        var order = Order.fromCart(cart, currentUser);

        orderRepository.save(order);
        cartService.clearCart(cart.getId());
        logger.info("checkout: order created orderId={} cartId={} userId={}", order.getId(), cartId,
                currentUser.getId());

        return new CheckoutResponse(order.getId());
    }

}
