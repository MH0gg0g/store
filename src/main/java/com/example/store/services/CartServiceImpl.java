package com.example.store.services;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.example.store.dtos.CartDto;
import com.example.store.dtos.CartItemDto;
import com.example.store.entities.Cart;
import com.example.store.exceptions.CartNotFoundException;
import com.example.store.exceptions.ProductNotFoundException;
import com.example.store.mappers.CartMapper;
import com.example.store.repositories.CartRepository;
import com.example.store.repositories.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private static final Logger logger = LoggerFactory.getLogger(CartServiceImpl.class);

    private final CartRepository cartRepository;
    private final CartMapper cartMapper;
    private final ProductRepository productRepository;

    public CartDto createCart() {
        logger.debug("createCart: creating new cart");
        var cart = new Cart();
        cartRepository.save(cart);
        return cartMapper.toDto(cart);
    }

    public CartItemDto addToCart(UUID cartId, Long productId) {
        var cart = cartRepository.getCartWithItems(cartId).orElseThrow(
                () -> {
                    logger.warn("addToCart: cart not found cartId={}", cartId);
                    return new CartNotFoundException(cartId);
                });
        var product = productRepository.findById(productId).orElseThrow(
                () -> {
                    logger.warn("addToCart: product not found productId={}", productId);
                    return new ProductNotFoundException(productId);
                });

        var cartItem = cart.addItem(product);
        cartRepository.save(cart);
        return cartMapper.toDto(cartItem);
    }

    public CartDto getCart(UUID cartId) {
        var cart = cartRepository.getCartWithItems(cartId).orElseThrow(
                () -> {
                    logger.warn("getCart: cart not found cartId={}", cartId);
                    return new CartNotFoundException(cartId);
                });
        return cartMapper.toDto(cart);
    }

    public CartItemDto updateItem(UUID cartId, Long productId, Long quantity) {
        var cart = cartRepository.getCartWithItems(cartId).orElseThrow(
                () -> {
                    logger.warn("updateItem: cart not fond cartId={}", cartId);
                    return new CartNotFoundException(cartId);
                });
        var cartItem = cart.getItem(productId);

        if (cartItem == null) {
            logger.warn("updateItem: product not fond in cart productId={} cartId={}", productId, cartId);
            throw new ProductNotFoundException(productId);
        }

        cartItem.setQuantity(quantity);
        cartRepository.save(cart);
        return cartMapper.toDto(cartItem);
    }

    public void removeItem(UUID cartId, Long productId) {
        var cart = cartRepository.getCartWithItems(cartId).orElseThrow(
                () -> {
                    logger.warn("removeItem: cart not found cartId={}", cartId);
                    return new CartNotFoundException(cartId);
                });
        cart.removeItem(productId);
        cartRepository.save(cart);
    }

    public void clearCart(UUID cartId) {
        var cart = cartRepository.getCartWithItems(cartId).orElseThrow(
                () -> {
                    logger.warn("clearCart: cart not found cartId={}", cartId);
                    return new CartNotFoundException(cartId);
                });
        cart.clearCart();
        cartRepository.save(cart);
    }

    public List<CartDto> getAllCarts() {
        var dtos = cartRepository.findAll().stream().map(cartMapper::toDto).toList();
        logger.debug("getAllCarts: returning {} carts", dtos.size());
        return dtos;
    }

    public void removeCart(UUID cartId) {
        logger.debug("removeCart: cartId={}", cartId);
        var cart = cartRepository.getCartWithItems(cartId).orElseThrow(
                () -> {
                    logger.warn("removeCart: cart not fond cartId={}", cartId);
                    return new CartNotFoundException(cartId);
                });
        cartRepository.delete(cart);
    }

}