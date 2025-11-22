package com.example.store.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.store.dtos.UserDto;
import com.example.store.dtos.UserRegisterationRequest;
import com.example.store.dtos.changePasswordRequest;
import com.example.store.dtos.updateUserRequest;
import com.example.store.entities.Role;
import com.example.store.exceptions.DublicateEmailException;
import com.example.store.exceptions.InvalidPasswordException;
import com.example.store.exceptions.UserNotFoundException;
import com.example.store.mappers.UserMapper;
import com.example.store.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PreAuthorize("hasRole('ADMIN')")
    public List<UserDto> getAllUsers() {
        logger.debug("getAllUsers: fetching all users");
        var users = userRepository.findAll().stream().map(userMapper::toDto).toList();
        return users;
    }

    @Cacheable(value = "users", key = "#userId")
    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal")
    public UserDto getUserById(Long userId) {
        var user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        return userMapper.toDto(user);
    }

    public UserDto createUser(UserRegisterationRequest userRequest) {
        if (userRepository.existsByEmail(userRequest.getEmail())) {
            logger.warn("duplicate email found {}", userRequest.getEmail());
            throw new DublicateEmailException(userRequest.getEmail());
        }
        var user = userMapper.toEntity(userRequest);
        user.setRole(Role.USER);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        logger.info("createUser: created user id={} email={}", user.getId(), user.getEmail());
        return userMapper.toDto(user);
    }

    @PreAuthorize("#userId == authentication.principal")
    public UserDto updateUser(Long userId, updateUserRequest userRequest) {
        var user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));

        userMapper.update(userRequest, user);
        userRepository.save(user);
        logger.info("updateUser: updated user id={}", userId);
        return userMapper.toDto(user);
    }

    @PreAuthorize("#userId == authentication.principal")
    public void changePassword(Long userId, changePasswordRequest userRequest) {
        var user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        if (!passwordEncoder.matches(userRequest.getOldPassword(), user.getPassword())) {
            logger.warn("invalid old password for userId={}", userId);
            throw new InvalidPasswordException();
        }

        user.setPassword(passwordEncoder.encode(userRequest.getNewPassword()));
        userRepository.save(user);
        logger.info("changePassword: password changed for userId={}", userId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void removeUser(Long userId) {
        var user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        userRepository.delete(user);
        logger.info("removeUser: deleted user id={}", userId);
    }
}
