package com.example.store.services;

import java.util.List;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.example.store.dtos.OrderDto;
import com.example.store.exceptions.OrderNotFoundException;
import com.example.store.mappers.OrderMapper;
import com.example.store.repositories.OrderRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final AuthService authService;
    private final OrderMapper orderMapper;

    public List<OrderDto> getAllOrders() {
        var user = authService.getCurrentUser();
        var orders = orderRepository.findAllByCustomer(user);

        return orders.stream().map(orderMapper::toDto).toList();
    }

    public OrderDto getOrder(Long id) {
        var order = orderRepository
                .findById(id)
                .orElseThrow(() -> new OrderNotFoundException());

        var user = authService.getCurrentUser();
        if (!order.getCustomer().getId().equals(user.getId())) {
            throw new AccessDeniedException("Access Denied");
        }

        return orderMapper.toDto(order);
    }
}
