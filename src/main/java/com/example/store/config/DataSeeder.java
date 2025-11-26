package com.example.store.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.store.entities.Role;
import com.example.store.entities.User;
import com.example.store.repositories.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Order(1)
@Component
@RequiredArgsConstructor
@Slf4j
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public void run(String... args) throws Exception {
        log.info("Seeding initial data...");
        if (userRepository.findByName("Admin").isEmpty()) {
            User admin = new User();
            admin.setName("Admin");
            admin.setEmail("admin@mail.com");
            admin.setPassword(passwordEncoder.encode("password"));
            admin.setRole(Role.ADMIN);
            userRepository.save(admin);
            log.info("Admin created.");
        } else {
            log.info("Admin already exists.");
        }
    }
}
