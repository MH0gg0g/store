package com.example.store.dtos;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CheckoutRequest {

    @NotNull(message = "Cart Id is Required")
    private UUID cartId;
}
