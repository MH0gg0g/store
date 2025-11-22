package com.example.store.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.store.dtos.OrderDto;
import com.example.store.services.OrderService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    @GetMapping
    public List<OrderDto> getAllorders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/{orderId}")
    public OrderDto getOrder(@PathVariable("orderId") Long orderId) {
        return orderService.getOrder(orderId);
    }

}
