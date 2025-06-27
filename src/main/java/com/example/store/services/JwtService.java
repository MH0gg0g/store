package com.example.store.services;

import java.util.Date;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.store.config.JwtConfig;
import com.example.store.entities.Token;
import com.example.store.entities.User;
import com.example.store.exceptions.InvalidJwtToken;
import com.example.store.repositories.TokenRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.Cookie;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class JwtService {

    private final JwtConfig jwtConfig;
    private final TokenRepository tokenRepository;

    private Jwt generateToken(User user, final long tokenExpiration) {

        var claims = Jwts.claims()
                .subject(user.getId().toString())
                .add("email", user.getEmail())
                .add("name", user.getName())
                .add("role", user.getRole())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * tokenExpiration))
                .build();

        return new Jwt(claims, jwtConfig.getSecretKey());
    }

    public Jwt parseToken(String token) {
        try {
            var claims = getClamis(token);
            return new Jwt(claims, jwtConfig.getSecretKey());
        } catch (Exception e) {
            throw new InvalidJwtToken();
        }
    }

    private Claims getClamis(String token) {
        return Jwts.parser()
                .verifyWith(jwtConfig.getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public Jwt generateAccessToken(User user) {
        return generateToken(user, jwtConfig.getAccessTokenExpiration());
    }

    public Cookie generateRefreshToken(User user) {
        var refreshToken = generateToken(user, jwtConfig.getRefreshTokenExpiration());

        var cookie = new Cookie("refreshToken", refreshToken.toString());
        cookie.setPath("/auth/refresh");
        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(jwtConfig.getRefreshTokenExpiration());

        return cookie;
    }

    public void saveToken(String accessToken, String refreshToken, User user) {
        var existingToken = tokenRepository.findByUserId(user.getId()).orElse(null);
        if (existingToken != null) {
            tokenRepository.delete(existingToken);
        }
        tokenRepository.save(new Token(accessToken, refreshToken, user));
    }

    @Transactional
    public void deleteToken(Long userId) {
        tokenRepository.deleteByUserId(userId);
    }

    public boolean TokenExists(Jwt jwt) {
        tokenRepository.findAll();
        return (tokenRepository.findByUserId(jwt.getUserID()).orElse(null) != null);
        
    }

}
