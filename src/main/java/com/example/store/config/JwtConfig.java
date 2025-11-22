package com.example.store.config;

import javax.crypto.SecretKey;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import io.jsonwebtoken.security.Keys;
import lombok.Data;

@ConfigurationProperties(prefix = "spring.jwt")
@Configuration
@Data
public class JwtConfig {
    private String secret;
    private String jwtBlacklistprefix;
    private int refreshTokenExpiration;
    private int accessTokenExpiration;

    public SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }
}
