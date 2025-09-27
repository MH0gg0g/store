package com.example.store.services;

import java.util.Date;

import javax.crypto.SecretKey;

import com.example.store.entities.Role;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Jwt {

    private final Claims claims;
    private final SecretKey secretKey;

    public boolean isExpired() {
        return claims.getExpiration().before(new Date());
    }

    public long getRemainingExpirationTime() {
        long expiration = claims.getExpiration().getTime() - System.currentTimeMillis();
        return Math.max(0, expiration);
    }

    public Long getUserID() {
        return Long.valueOf(claims.getSubject());
    }
    
    public String getTokenID() {
        return claims.getId();
    }

    public Role getRole() {
        return Role.valueOf(claims.get("role", String.class));
    }

    public String toString() {
        return Jwts.builder().claims(claims).signWith(secretKey).compact();
    }

}
