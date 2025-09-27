package com.example.store.services;

import java.util.List;

import com.example.store.dtos.OrderDto;

public interface OrderService {
    OrderDto getOrder(Long id);
    List<OrderDto> getAllOrders();
}
