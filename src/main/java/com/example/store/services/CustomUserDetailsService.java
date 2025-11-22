package com.example.store.services;

import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.store.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);
    
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        logger.debug("loadUserByUsername: email={}", email);
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    logger.warn("loadUserByUsername: user not found email={}", email);
                    return new UsernameNotFoundException("User not found with email: " + email);
                });

        var userDetails = User.withUsername(user.getEmail())
                .password(user.getPassword())
                .authorities(Collections.emptyList())
                .build();

        logger.debug("loadUserByUsername: loaded user email={}", email);
        return userDetails;
    }

}
