package com.example.store.services;

import java.util.List;

import com.example.store.dtos.UserDto;
import com.example.store.dtos.UserRegisterationRequest;
import com.example.store.dtos.changePasswordRequest;
import com.example.store.dtos.updateUserRequest;

public interface UserService {
    List<UserDto> getAllUsers();

    UserDto getUserById(Long userId);

    UserDto createUser(UserRegisterationRequest userRequest);

    UserDto updateUser(Long userId, updateUserRequest userRequest);

    void removeUser(Long userId);

    void changePassword(Long userId, changePasswordRequest userRequest);
}
