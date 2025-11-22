package com.example.store.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.example.store.dtos.OrderDto;
import com.example.store.exceptions.OrderNotFoundException;
import com.example.store.mappers.OrderMapper;
import com.example.store.repositories.OrderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    private final OrderRepository orderRepository;
    private final AuthService authService;
    private final OrderMapper orderMapper;

    public List<OrderDto> getAllOrders() {
        var user = authService.getCurrentUser();
        var orders = orderRepository.findAllByCustomer(user);

        var dtos = orders.stream().map(orderMapper::toDto).toList();
        logger.debug("getAllOrders: userId={} returning {} orders", user.getId(), dtos.size());
        return dtos;
    }

    public OrderDto getOrder(Long id) {
        var order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));

        var user = authService.getCurrentUser();
        if (!order.getCustomer().getId().equals(user.getId())) {
            logger.warn("getOrder: access denied userId={} attempted access to orderId={}", user.getId(), id);
            throw new AccessDeniedException("You do not have permission to access this order.");
        }

        logger.debug("getOrder: returning order id={} for userId={}", id, user.getId());
        return orderMapper.toDto(order);
    }
}