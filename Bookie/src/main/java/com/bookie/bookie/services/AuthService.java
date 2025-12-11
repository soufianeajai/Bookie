package com.bookie.bookie.services;

import com.bookie.bookie.dtos.user.AuthResponseDto;
import com.bookie.bookie.dtos.user.LoginDto;
import com.bookie.bookie.dtos.user.SignupDto;
import com.bookie.bookie.dtos.user.UserDto;

public interface AuthService {
    UserDto signUp(SignupDto signupDto);
    AuthResponseDto login(LoginDto loginDto);
    AuthResponseDto refresh(String refreshToken);
}
