package com.example.store.dtos;

import com.example.store.services.Jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponse {

    private Jwt accessToken;
    private Jwt refreshToken;

}