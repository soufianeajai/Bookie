package com.bookie.bookie.mappers;

import com.bookie.bookie.dtos.user.AuthResponseDto;
import com.bookie.bookie.dtos.user.SignupDto;
import com.bookie.bookie.dtos.user.UserDto;
import com.bookie.bookie.entities.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toEntity(SignupDto signupDto);
    AuthResponseDto toAuthResponseDto(User user);
    UserDto toDto(User user);
}
