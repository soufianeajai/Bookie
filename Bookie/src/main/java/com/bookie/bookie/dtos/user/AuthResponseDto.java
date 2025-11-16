package com.bookie.bookie.dtos.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponseDto {
    private UserDto user;
    private String jwtToken;
}
