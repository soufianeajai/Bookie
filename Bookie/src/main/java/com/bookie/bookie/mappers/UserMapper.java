package com.bookie.bookie.mappers;

import com.bookie.bookie.dtos.user.AuthDto;
import com.bookie.bookie.dtos.user.AuthResponseDto;
import com.bookie.bookie.dtos.user.SignupDto;
import com.bookie.bookie.dtos.user.UserDto;
import com.bookie.bookie.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toEntity(SignupDto signupDto);
    AuthResponseDto toAuthResponseDto(User user);
    UserDto toDto(User user);
    @Mapping(target = "accessToken", source = "jwtAccessToken")
    AuthDto toAuthDto(AuthResponseDto authResponseDto);
}
