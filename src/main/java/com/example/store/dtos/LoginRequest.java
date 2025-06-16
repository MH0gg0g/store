package com.example.store.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {
    @NotBlank(message = "Email is required")
    @Email(message = "Email Not Valid")
    private String email;

    @NotBlank(message = "Password is required")
    private String Password;

}
