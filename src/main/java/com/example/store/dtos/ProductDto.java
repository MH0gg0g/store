package com.example.store.dtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {

    private Long id;
    @NotBlank(message = "Name cannot be blank")
    private String name;
    @NotNull(message = "Description cannot be null")
    private String description;
    @Min(value = 1, message = "Price must be at least 1")
    @Max(value = 100, message = "Price must not exceed 100")
    private Long price;
    @NotNull(message = "Category ID cannot be null")
    private Long categoryId;

}