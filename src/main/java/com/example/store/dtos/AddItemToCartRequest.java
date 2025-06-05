package com.example.store.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddItemToCartRequest {
    @NotNull
    @Min(value = 1, message = "Quantity must be at least 1")
    private Long productId;
}
