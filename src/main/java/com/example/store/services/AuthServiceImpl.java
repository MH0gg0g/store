package com.example.store.services;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.store.aop.Loggable;
import com.example.store.dtos.LoginRequest;
import com.example.store.dtos.LoginResponse;
import com.example.store.entities.User;
import com.example.store.exceptions.InvalidTokenException;
import com.example.store.exceptions.UserNotFoundException;
import com.example.store.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Loggable
    public LoginResponse login(LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> {

                    return new UserNotFoundException(request.getEmail());
                });
        var refreshToken = jwtService.generateRefreshToken(user);
        var accessToken = jwtService.generateAccessToken(user);

        return new LoginResponse(accessToken, refreshToken);
    }

    @Loggable
    public Jwt refreshToken(String refreshToken) {
        var jwt = jwtService.parseToken(refreshToken);

        if (jwt == null || jwt.isExpired()) {
            throw new InvalidTokenException();
        }

        var user = userRepository.findById(jwt.getUserID()).orElseThrow();

        return jwtService.generateAccessToken(user);
    }

    @Loggable
    public User getCurrentUser() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            throw new InvalidTokenException("Token Expired or Invalid");
        }

        var userId = (Long) auth.getPrincipal();
        var user = userRepository.findById(userId).orElseThrow(() -> {

            return new InvalidTokenException("Token Expired or Invalid");
        });

        return user;
    }
}