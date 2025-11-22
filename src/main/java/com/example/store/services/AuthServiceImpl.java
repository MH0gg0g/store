package com.example.store.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

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

    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public LoginResponse login(LoginRequest request) {
        logger.debug("login: attempt for email={}", request.getEmail());
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> {
                    logger.warn(" user not found email={}", request.getEmail());
                    return new UserNotFoundException(request.getEmail());
                });
        var refreshToken = jwtService.generateRefreshToken(user);
        var accessToken = jwtService.generateAccessToken(user);

        logger.info("login: successful for email={}", user.getEmail());
        return new LoginResponse(accessToken, refreshToken);
    }

    public Jwt refreshToken(String refreshToken) {
        var jwt = jwtService.parseToken(refreshToken);

        if (jwt == null || jwt.isExpired()) {
            throw new InvalidTokenException();
        }

        var user = userRepository.findById(jwt.getUserID()).orElseThrow();
        logger.info("refreshToken: generating new access token for userId={}", user.getId());
        return jwtService.generateAccessToken(user);
    }

    public User getCurrentUser() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new InvalidTokenException("Token Expired or Invalid");
        }

        var userId = (Long) authentication.getPrincipal();
        var user = userRepository.findById(userId).orElseThrow(() -> {
            logger.warn("user not found id={}", userId);
            return new InvalidTokenException("Token Expired or Invalid");
        });
        logger.debug("getCurrentUser: returning userId={}", userId);
        return user;
    }
}