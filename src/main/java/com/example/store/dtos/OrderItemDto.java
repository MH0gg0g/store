package com.example.store.dtos;

import lombok.Data;

@Data
public class OrderItemDto {
    private OrderProductDto product;
    private Long quantity;
    private Long totalPrice;

}
