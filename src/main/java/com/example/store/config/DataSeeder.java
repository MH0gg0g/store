package com.example.store.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.store.entities.Role;
import com.example.store.entities.User;
import com.example.store.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Order(1)
@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {
    private final Logger logger = LoggerFactory.getLogger(DataSeeder.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        logger.info("Seeding initial data...");
        if (userRepository.findByName("Admin").isEmpty()) {
            User admin = new User();
            admin.setName("Admin");
            admin.setEmail("admin@mail.com");
            admin.setPassword(passwordEncoder.encode("password"));
            admin.setRole(Role.ADMIN);
            userRepository.save(admin);
            logger.info("Admin user created.");
        }
        else {
            logger.info("Admin already exists.");
        }
    }
}
