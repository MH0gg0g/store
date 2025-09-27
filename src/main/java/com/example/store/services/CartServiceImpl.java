package com.example.store.services;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.store.dtos.CartDto;
import com.example.store.dtos.CartItemDto;
import com.example.store.entities.Cart;
import com.example.store.exceptions.CartNotFoundException;
import com.example.store.exceptions.ProductNotFoundException;
import com.example.store.mappers.CartMapper;
import com.example.store.repositories.CartRepository;
import com.example.store.repositories.ProductRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartMapper cartMapper;
    private final ProductRepository productRepository;

    public CartDto createCart() {
        var cart = new Cart();
        cartRepository.save(cart);

        return cartMapper.toDto(cart);
    }

    public CartItemDto addToCart(UUID cartId, Long productId) {
        var cart = cartRepository.findById(cartId).orElseThrow(() -> new CartNotFoundException());

        var product = productRepository.findById(productId).orElseThrow(() -> new ProductNotFoundException());

        var cartItem = cart.addItem(product);
        cartRepository.save(cart);

        return cartMapper.toDto(cartItem);
    }

    public CartDto getCart(UUID cartId) {
        var cart = cartRepository.findById(cartId).orElseThrow(CartNotFoundException::new);
        return cartMapper.toDto(cart);
    }

    public CartItemDto updateItem(UUID cartId, Long productId, Long quantity) {
        var cart = cartRepository.findById(cartId).orElseThrow(CartNotFoundException::new);

        var cartItem = cart.getItem(productId);

        if (cartItem == null) {
            throw new ProductNotFoundException();
        }

        cartItem.setQuantity(quantity);
        cartRepository.save(cart);

        return cartMapper.toDto(cartItem);
    }

    public void removeItem(UUID cartId, Long productId) {
        var cart = cartRepository.findById(cartId).orElseThrow(CartNotFoundException::new);

        cart.removeItem(productId);
        cartRepository.save(cart);
    }

    public void clearCart(UUID cartId) {
        var cart = cartRepository.findById(cartId).orElseThrow(CartNotFoundException::new);

        cart.clearCart();
        cartRepository.save(cart);
    }

    public List<CartDto> getAllCarts() {
        return cartRepository.findAll().stream().map(cartMapper::toDto).toList();
    }

    public void removeCart(UUID cartId) {
        var cart = cartRepository.findById(cartId).orElseThrow(CartNotFoundException::new);

        cartRepository.delete(cart);
    }

}
