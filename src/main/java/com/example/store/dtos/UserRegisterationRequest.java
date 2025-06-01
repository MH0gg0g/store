package com.example.store.dtos;

import lombok.Data;

@Data
public class UserRegisterationRequest {

    private String name;
    private String email;
    private String password;

}
