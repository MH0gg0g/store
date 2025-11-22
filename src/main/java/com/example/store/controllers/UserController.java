package com.example.store.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.store.dtos.UserDto;
import com.example.store.dtos.UserRegisterationRequest;
import com.example.store.dtos.changePasswordRequest;
import com.example.store.dtos.updateUserRequest;
import com.example.store.services.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<UserDto> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{userId}")
    public UserDto getUserById(@PathVariable Long userId) {
        return userService.getUserById(userId);
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserRegisterationRequest userRequest,
            UriComponentsBuilder uriBuilder) {

        var userDto = userService.createUser(userRequest);

        var uri = uriBuilder.path("/users/{id}").buildAndExpand(userDto.getId()).toUri();
        return ResponseEntity.created(uri).body(userDto);
    }

    @PostMapping("/{userId}/change-password")
    public ResponseEntity<Void> changePassword(@PathVariable Long userId,
            @RequestBody changePasswordRequest userRequest) {

        userService.changePassword(userId, userRequest);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{userId}")
    public UserDto updateUser(@PathVariable Long userId,
            @RequestBody updateUserRequest userRequest) {
        return userService.updateUser(userId, userRequest);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        userService.removeUser(userId);
        return ResponseEntity.noContent().build();
    }
}