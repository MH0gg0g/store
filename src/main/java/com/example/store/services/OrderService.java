package com.example.store.services;

import java.util.List;

import com.example.store.dtos.OrderDto;

public interface OrderService {
    List<OrderDto> getAllOrders();

    OrderDto getOrder(Long userId, Long orderId);

}
