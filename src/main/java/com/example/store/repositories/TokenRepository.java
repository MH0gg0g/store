package com.example.store.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.store.entities.Token;

public interface TokenRepository extends JpaRepository<Token, Long> {

    Optional<Token> findByUserId(Long id);

    void deleteByUserId(Long userId);
}