package com.example.store.services;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import com.example.store.config.JwtConfig;
import com.example.store.entities.User;
import com.example.store.exceptions.InvalidJwtToken;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class JwtServiceImpl implements JwtService {

    private final JwtConfig jwtConfig;
    private final RedisTemplate<String, Object> redisTemplate;

    private Jwt generateToken(User user, final long tokenExpiration) {

        var claims = Jwts.claims()
                .issuedAt(new Date())
                .subject(user.getId().toString())
                .add("email", user.getEmail())
                .add("name", user.getName())
                .add("role", user.getRole())
                .id(UUID.randomUUID().toString())
                .expiration(new Date(System.currentTimeMillis() + 1000 * tokenExpiration))
                .build();

        return new Jwt(claims, jwtConfig.getSecretKey());
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

    public Jwt parseToken(String token) {
        try {
            var claims = getClamis(token);
            return new Jwt(claims, jwtConfig.getSecretKey());
        } catch (Exception e) {
            throw new InvalidJwtToken();
        }
    }

    public Jwt ExtractJwtFromRequest(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            var token = authHeader.replace("Bearer ", "");
            return parseToken(token);
        }
        return null;
    }

    private Claims getClamis(String token) {
        return Jwts.parser()
                .verifyWith(jwtConfig.getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String blacklistToken(Jwt jwt) {
        var key = jwtConfig.getJwtBlacklistprefix() + jwt.getTokenID();
        var remainingMillis = jwt.getRemainingExpirationTime();

        redisTemplate.opsForValue().set(key, "BlackListed", remainingMillis, TimeUnit.MILLISECONDS);
        return "Logged out successfully";
    }

    public boolean isTokenBlacklisted(String jti) {
        var key = jwtConfig.getJwtBlacklistprefix() + jti;
        return redisTemplate.hasKey(key);
    }

}
