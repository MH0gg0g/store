package com.example.store.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.store.config.JwtConfig;
import com.example.store.dtos.JwtResponse;
import com.example.store.dtos.LoginRequest;
import com.example.store.services.AuthService;
import com.example.store.services.JwtService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    private final JwtService jwtService;
    private final JwtConfig jwtConfig;

    @PostMapping("/login")
    public JwtResponse login(@Valid @RequestBody LoginRequest request, HttpServletResponse response) {

        var loginResult = authService.login(request);
        var accessToken = loginResult.getAccessToken().toString();

        var refreshToken = loginResult.getRefreshToken().toString();
        var cookie = new Cookie("refreshToken", refreshToken);
        cookie.setPath("/auth/refresh");
        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(jwtConfig.getRefreshTokenExpiration());
        
        response.addCookie(cookie);
        return new JwtResponse(accessToken);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        var jwt = jwtService.ExtractJwtFromRequest(request);
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok(jwtService.blacklistToken(jwt));
    }

    @PostMapping("/refresh")
    public JwtResponse refresh(
            @CookieValue(value = "refreshToken") String refreshToken) {

        var accessToken = authService.refreshToken(refreshToken);
        return new JwtResponse(accessToken.toString());
    }

}