package com.example.store.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.store.dtos.RegisterUserRequest;
import com.example.store.dtos.UpdateEmailRequest;
import com.example.store.dtos.UserDto;
import com.example.store.entities.Role;
import com.example.store.entities.User;

@Mapper(componentModel = "spring", imports = Role.class)
public abstract class UserMapper {

    @Autowired
    protected PasswordEncoder passwordEncoder;

    public abstract UserDto toDto(User user);

    @Mappings({
            @Mapping(target = "password", expression = "java(passwordEncoder.encode(request.getPassword()))"),
            @Mapping(target = "role", expression = "java(Role.USER)")
    })
    public abstract User toEntity(RegisterUserRequest request);

    @Mapping(source = "email", target = "email")
    public abstract void update(UpdateEmailRequest request, @MappingTarget User user);

}
