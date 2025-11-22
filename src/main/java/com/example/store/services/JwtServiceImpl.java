package com.example.store.services;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.example.store.config.JwtConfig;
import com.example.store.entities.User;
import com.example.store.exceptions.InvalidTokenException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {

    private static final Logger logger = LoggerFactory.getLogger(JwtServiceImpl.class);

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

        var jwt = new Jwt(claims, jwtConfig.getSecretKey());
        logger.debug("generateToken: tokenId={} userId={}", jwt.getTokenID(), user.getId());
        return jwt;
    }

    public Jwt generateAccessToken(User user) {
        logger.debug("generateAccessToken: userId={}", user.getId());
        return generateToken(user, jwtConfig.getAccessTokenExpiration());
    }

    public Jwt generateRefreshToken(User user) {
        logger.debug("generateRefreshToken: userId={}", user.getId());
        return generateToken(user, jwtConfig.getRefreshTokenExpiration());
    }

    public Jwt parseToken(String token) {
        try {
            var claims = getClamis(token);
            var jwt = new Jwt(claims, jwtConfig.getSecretKey());
            return jwt;
        } catch (ExpiredJwtException | InvalidTokenException e) {
            logger.warn("parseToken: token is expired or invalid", e);
            throw new InvalidTokenException();
        } catch (Exception e) {
            logger.error("parseToken: unexpcted error whle parsing token", e);
            throw new InvalidTokenException();
        }
    }

    public Jwt ExtractJwtFromRequest(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            var token = authHeader.replace("Bearer ", "");
            return parseToken(token);
        }
        logger.trace("ExtractJwtFromRequest: no bearer header found");
        return null;
    }

    private Claims getClamis(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(jwtConfig.getSecretKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (Exception e) {
            logger.error("getClamis: error parsing claims", e);
            throw new InvalidTokenException(e.getMessage());
        }
    }

    public String blacklistToken(Jwt jwt) {
        try {
            var key = jwtConfig.getJwtBlacklistprefix() + jwt.getTokenID();
            var remainingMillis = jwt.getRemainingExpirationTime();
            redisTemplate.opsForValue().set(key, "BlackListed", remainingMillis, TimeUnit.MILLISECONDS);
            logger.info("blacklistToken: tokenId={} ttlSec={}", jwt.getTokenID(), remainingMillis / 1000);
            return "Logged out successfully";
        } catch (Exception e) {
            logger.error("blacklistToken: error blacklisting tokenId={}", jwt.getTokenID(), e);
            throw new InvalidTokenException(e.getMessage());
        }
    }

    public boolean isTokenBlacklisted(String jti) {
        var key = jwtConfig.getJwtBlacklistprefix() + jti;
        boolean exists = redisTemplate.hasKey(key);
        logger.debug("isTokenBlacklisted: tokenId={} blacklisted={}", jti, exists);
        return exists;
    }

}