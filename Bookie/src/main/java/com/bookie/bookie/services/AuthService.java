package com.bookie.bookie.services;

import com.bookie.bookie.dtos.user.AuthResponseDto;
import com.bookie.bookie.dtos.user.LoginDto;
import com.bookie.bookie.dtos.user.SignupDto;

public interface AuthService {
    AuthResponseDto signUp(SignupDto signupDto);
    AuthResponseDto login(LoginDto loginDto);

}
