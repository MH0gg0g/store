package com.example.store.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.store.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {

}
