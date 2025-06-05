package com.example.store.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.store.dtos.UserDto;
import com.example.store.dtos.UserRegisterationRequest;
import com.example.store.dtos.changePasswordRequest;
import com.example.store.dtos.updateUserRequest;
import com.example.store.exceptions.InvalidPasswordException;
import com.example.store.exceptions.UserNotFoundException;
import com.example.store.mappers.UserMapper;
import com.example.store.repositories.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream().map(userMapper::toDto).toList();
    }

    public UserDto getUser(Long userId) {
        var user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new UserNotFoundException();
        }

        return userMapper.toDto(user);
    }

    public UserDto createUser(UserRegisterationRequest userRequest) {
        var user = userMapper.toEntity(userRequest);

        userRepository.save(user);

        return userMapper.toDto(user);
    }

    public UserDto updateUser(Long userId, updateUserRequest userRequest) {
        var user = userRepository.findById(userId).orElse(null);

        if (user == null) {
            throw new UserNotFoundException();
        }

        userMapper.update(userRequest, user);
        userRepository.save(user);

        return userMapper.toDto(user);
    }

    public void changePassword(Long userId, changePasswordRequest userRequest) {
        var user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new UserNotFoundException();
        }

        if (!user.getPassword().equals(userRequest.getOldPassword())) {
            throw new InvalidPasswordException();
        }

        user.setPassword(userRequest.getNewPassword());
        userRepository.save(user);
    }

    public void removeUser(Long userId) {
        var user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new UserNotFoundException();
        }

        userRepository.delete(user);
    }

}
