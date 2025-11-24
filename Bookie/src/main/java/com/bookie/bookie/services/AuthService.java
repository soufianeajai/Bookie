package com.bookie.bookie.services;

import com.bookie.bookie.dtos.user.AuthResponseDto;
import com.bookie.bookie.dtos.user.LoginDto;
import com.bookie.bookie.dtos.user.SignupDto;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {
    AuthResponseDto signUp(SignupDto signupDto);
    AuthResponseDto login(LoginDto loginDto, HttpServletResponse response);

    AuthResponseDto refresh(String refreshToken);
}
