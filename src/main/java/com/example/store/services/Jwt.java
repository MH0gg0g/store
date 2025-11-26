package com.example.store.services;

import java.util.Date;

import javax.crypto.SecretKey;

import com.example.store.entities.Role;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Jwt {

    private final Claims claims;
    private final SecretKey secretKey;


    public boolean isExpired() {
        Date expiration = claims.getExpiration();
        boolean expired = expiration.before(new Date());
        return expired;
    }

    public Long getRemainingExpirationTime() {
        Long expirationMs = claims.getExpiration().getTime() - System.currentTimeMillis();
        Long remaining = Math.max(0, expirationMs);
        return remaining;
    }

    public Long getUserID() {
        Long userId = Long.valueOf(claims.getSubject());
        return userId;
    }
    
    public String getTokenID() {
        String tid = claims.getId();
        return tid;
    }

    public Role getRole() {
        Role role = Role.valueOf(claims.get("role", String.class));
        return role;
    }

    public String toString() {
        String token = Jwts.builder().claims(claims).signWith(secretKey).compact();
        return token;
    }

}