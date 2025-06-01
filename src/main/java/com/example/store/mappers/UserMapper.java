package com.example.store.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.example.store.dtos.UserDto;
import com.example.store.dtos.UserRegisterationRequest;
import com.example.store.dtos.updateUserRequest;
import com.example.store.entities.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toDto(User user);

    User toEntity(UserRegisterationRequest request);

    void update(updateUserRequest request, @MappingTarget User user);

}
