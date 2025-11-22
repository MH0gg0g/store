package com.example.store.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.store.dtos.AddItemToCartRequest;
import com.example.store.dtos.CartDto;
import com.example.store.dtos.CartItemDto;
import com.example.store.dtos.updateCartItemRequest;
import com.example.store.services.CartService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/carts")
public class CartController {

    private final CartService cartService;

    @PostMapping
    public ResponseEntity<CartDto> createCart(UriComponentsBuilder uriBuilder) {
        var cartDto = cartService.createCart();
        var uri = uriBuilder.path("/carts/{id}").buildAndExpand(cartDto.getId()).toUri();

        return ResponseEntity.created(uri).body(cartDto);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<CartDto> getAllCarts() {
        return cartService.getAllCarts();
    }

    @GetMapping("/{cartId}")
    public CartDto getCart(@PathVariable UUID cartId) {
        return cartService.getCart(cartId);
    }

    @PostMapping("/{cartId}/items")
    public ResponseEntity<CartItemDto> addToCart(@PathVariable UUID cartId,
            @Valid @RequestBody AddItemToCartRequest request) {

        var cartItemDto = cartService.addToCart(cartId, request.getProductId());

        return ResponseEntity.status(HttpStatus.CREATED).body(cartItemDto);
    }

    @PutMapping("{cartId}/items/{productId}")
    public CartItemDto updateItem(@PathVariable("cartId") UUID cartId,
            @PathVariable("productId") Long productId,
            @Valid @RequestBody updateCartItemRequest request) {

        return cartService.updateItem(cartId, productId, request.getQuantity());
    }

    @DeleteMapping("/{cartId}/items/{productId}")
    public ResponseEntity<Void> removeItem(@PathVariable("cartId") UUID cartId,
            @PathVariable("productId") Long productId) {

        cartService.removeItem(cartId, productId);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{cartId}/items")
    public ResponseEntity<Void> clearCart(@PathVariable("cartId") UUID cartId) {
        cartService.clearCart(cartId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{cartId}")
    public ResponseEntity<Void> removeCart(@PathVariable UUID cartId) {
        cartService.removeCart(cartId);
        return ResponseEntity.noContent().build();
    }

}