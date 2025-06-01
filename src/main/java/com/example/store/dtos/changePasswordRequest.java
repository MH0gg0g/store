package com.example.store.dtos;

import lombok.Data;

@Data
public class changePasswordRequest {

    private String oldPassword;
    private String newPassword;
}