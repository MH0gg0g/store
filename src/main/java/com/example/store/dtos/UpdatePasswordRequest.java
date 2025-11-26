package com.example.store.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdatePasswordRequest {

    @NotBlank(message = "Password cannot be blank")
    private String oldPassword;
    @NotBlank(message = "Password cannot be blank")
    private String newPassword;
}