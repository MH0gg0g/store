package com.example.store.dtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class updateCartItemRequest {

    @NotNull(message = "Qunatity cannot be null")
    @Min(value = 1, message = "Quantity must be at least 1")
    @Max(value = 100, message = "Quantity must not exceed 100")
    private Long quantity;


}
