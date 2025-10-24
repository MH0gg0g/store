package com.example.store.services;

import com.example.store.dtos.LoginRequest;
import com.example.store.dtos.LoginResponse;
import com.example.store.entities.User;

public interface AuthService {
    User getCurrentUser();

    Jwt refreshToken(String refreshToken);

    LoginResponse login(LoginRequest request);
}