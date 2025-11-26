package com.example.store.services;

import java.util.List;

import com.example.store.dtos.RegisterUserRequest;
import com.example.store.dtos.UpdateEmailRequest;
import com.example.store.dtos.UpdatePasswordRequest;
import com.example.store.dtos.UserDto;

public interface UserService {
    List<UserDto> getAllUsers();

    UserDto getUserById(Long userId);

    UserDto createUser(RegisterUserRequest request);

    UserDto updateUser(Long userId, UpdateEmailRequest request);

    void removeUser(Long userId);

    void changePassword(Long userId, UpdatePasswordRequest request);
}
