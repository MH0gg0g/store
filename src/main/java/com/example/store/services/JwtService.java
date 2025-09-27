package com.example.store.services;

import com.example.store.entities.User;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

public interface JwtService {

    Jwt generateAccessToken(User user);

    Cookie generateRefreshToken(User user);

    Jwt parseToken(String token);

    Jwt ExtractJwtFromRequest(HttpServletRequest request);

    String blacklistToken(Jwt jwt);

    boolean isTokenBlacklisted(String jti);

}
