package com.example.store.services;

import com.example.store.entities.User;

import jakarta.servlet.http.HttpServletRequest;

public interface JwtService {

    Jwt generateAccessToken(User user);

    Jwt generateRefreshToken(User user);

    Jwt parseToken(String token);

    String blacklistToken(Jwt jwt);

    boolean isTokenBlacklisted(String jti);

    Jwt ExtractJwtFromRequest(HttpServletRequest request);

}