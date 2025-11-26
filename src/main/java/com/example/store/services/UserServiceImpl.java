package com.example.store.services;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.store.aop.Loggable;
import com.example.store.dtos.RegisterUserRequest;
import com.example.store.dtos.UpdateEmailRequest;
import com.example.store.dtos.UpdatePasswordRequest;
import com.example.store.dtos.UserDto;
import com.example.store.exceptions.DublicateEmailException;
import com.example.store.exceptions.InvalidPasswordException;
import com.example.store.exceptions.UserNotFoundException;
import com.example.store.mappers.UserMapper;
import com.example.store.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PreAuthorize("hasRole('ADMIN')")
    public List<UserDto> getAllUsers() {

        var users = userRepository.findAll().stream().map(userMapper::toDto).toList();
        return users;
    }

    @Loggable
    @Cacheable(value = "users", key = "#userId")
    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal")
    public UserDto getUserById(Long userId) {
        var user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        return userMapper.toDto(user);
    }

    @Loggable
    public UserDto createUser(RegisterUserRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new DublicateEmailException(request.getEmail());
        }
        var user = userMapper.toEntity(request);
        userRepository.save(user);

        return userMapper.toDto(user);
    }

    @Loggable
    @PreAuthorize("#userId == authentication.principal")
    public UserDto updateUser(Long userId, UpdateEmailRequest request) {
        var user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        userMapper.update(request, user);
        userRepository.save(user);

        return userMapper.toDto(user);
    }

    @Loggable
    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal")
    public void changePassword(Long userId, UpdatePasswordRequest request) {
        var user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {

            throw new InvalidPasswordException();
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

    }

    @Loggable
    @PreAuthorize("hasRole('ADMIN')")
    public void removeUser(Long userId) {
        var user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        userRepository.delete(user);
    }
}
