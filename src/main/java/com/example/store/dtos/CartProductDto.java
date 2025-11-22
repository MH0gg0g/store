package com.example.store.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CartProductDto {

    private Long id;
    @NotBlank(message = "Name cannot be blank")
    private String name;
    @NotBlank(message = "Price cannot be blank")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Long Price;
}
