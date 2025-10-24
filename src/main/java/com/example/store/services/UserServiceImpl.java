package com.example.store.services;

import java.util.List;

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

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream().map(userMapper::toDto).toList();
    }

    public UserDto getUserById(Long userId) {
        var user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        return userMapper.toDto(user);
    }

    public UserDto createUser(UserRegisterationRequest userRequest) {
        if (userRepository.existsByEmail(userRequest.getEmail())) {
            throw new DublicateEmailException();
        }
        var user = userMapper.toEntity(userRequest);
        user.setRole(Role.USER);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        return userMapper.toDto(user);
    }

    public UserDto updateUser(Long userId, updateUserRequest userRequest) {
        var user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        userMapper.update(userRequest, user);
        userRepository.save(user);

        return userMapper.toDto(user);
    }

    public void changePassword(Long userId, changePasswordRequest userRequest) {
        var user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        if (!passwordEncoder.matches(userRequest.getOldPassword(), user.getPassword())) {
            throw new InvalidPasswordException();
        }

        user.setPassword(passwordEncoder.encode(userRequest.getNewPassword()));
        userRepository.save(user);
    }

    public void removeUser(Long userId) {
        var user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        userRepository.delete(user);
    }
}
