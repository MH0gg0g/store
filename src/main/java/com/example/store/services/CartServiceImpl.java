package com.example.store.services;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.store.aop.Loggable;
import com.example.store.dtos.CartDto;
import com.example.store.dtos.CartItemDto;
import com.example.store.entities.Cart;
import com.example.store.entities.Product;
import com.example.store.exceptions.CartNotFoundException;
import com.example.store.exceptions.InsufficientStockException;
import com.example.store.exceptions.ProductNotFoundException;
import com.example.store.mappers.CartMapper;
import com.example.store.repositories.CartRepository;
import com.example.store.repositories.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartMapper cartMapper;
    private final ProductRepository productRepository;

    @Loggable
    public CartItemDto addToCart(UUID cartId, Long productId, Long quantity) {
        var cart = getCart(cartId);

        var product = getProduct(productId);
        if (product.getQuantity() < quantity) {
            throw new InsufficientStockException(productId);
        }

        var cartItem = cart.addItem(product, quantity);
        cartRepository.save(cart);
        return cartMapper.toDto(cartItem);
    }

    @Loggable
    public CartItemDto updateItem(UUID cartId, Long productId, Long quantity) {
        var cart = getCart(cartId);

        var cartItem = cart.getItem(productId);
        if (cartItem == null) {

            throw new ProductNotFoundException(productId);
        }

        var product = getProduct(productId);
        if (product.getQuantity() < quantity) {
            throw new InsufficientStockException(productId);
        }

        cartItem.setQuantity(quantity);
        cartRepository.save(cart);
        return cartMapper.toDto(cartItem);
    }

    @Loggable
    private Product getProduct(Long productId) {
        return productRepository.findById(productId).orElseThrow(
                () -> new ProductNotFoundException(productId));
    }

    @Loggable
    public void removeItem(UUID cartId, Long productId) {
        var cart = getCart(cartId);

        if (cart.removeItem(productId) == false) {

            throw new ProductNotFoundException(productId);
        }
        cartRepository.save(cart);
    }

    @Loggable
    public List<CartDto> getAllCarts() {
        var cartsDto = cartRepository.findAll().stream().map(cartMapper::toDto).toList();

        return cartsDto;
    }

    @Loggable
    private Cart getCart(UUID cartId) {
        var cart = cartRepository.getCartWithItems(cartId).orElseThrow(
                () -> new CartNotFoundException(cartId));
        return cart;
    }

    @Loggable
    public CartDto getCartDto(UUID cartId) {
        return cartMapper.toDto(getCart(cartId));
    }

    @Loggable
    public CartDto createCart() {

        var cart = new Cart();
        cartRepository.save(cart);
        return cartMapper.toDto(cart);
    }

    @Loggable
    public void clearCart(UUID cartId) {

        var cart = getCart(cartId);
        cart.clearCart();
        cartRepository.save(cart);
    }

    @Loggable
    public void removeCart(UUID cartId) {

        var cart = getCart(cartId);
        cartRepository.delete(cart);
    }

}