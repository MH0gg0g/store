package com.example.store.dtos;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CartDto {

    private UUID id;
    private List<CartItemDto> items = new ArrayList<>();
    @NotNull(message = "Total Price cannot be null")
    @Min(value = 1, message = "Total Price must be at least 1")
    private Long totalPrice;
}
