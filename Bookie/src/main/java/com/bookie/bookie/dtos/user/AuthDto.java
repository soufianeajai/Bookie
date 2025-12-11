package com.bookie.bookie.dtos.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthDto {
    private UserDto user;
    private String accessToken;
}
