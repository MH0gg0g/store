package com.example.store.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.store.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Boolean existsByEmail(String email);

    Optional <User> findByEmail(String email);

    Optional <User> findByName(String email);

}
