package com.bookie.bookie.controllers;


import com.bookie.bookie.dtos.user.*;
import com.bookie.bookie.mappers.UserMapper;
import com.bookie.bookie.services.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RestController
@RequiredArgsConstructor
@RequestMapping("auth")
public class UserAuthController {

    private final AuthService authService;
    private final UserMapper userMapper;

    @PostMapping("/signup")
    public ResponseEntity<UserDto> signup(@RequestBody SignupDto signupDto) {
        return new ResponseEntity<>(authService.signUp(signupDto), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthDto> login(@RequestBody LoginDto loginDto, HttpServletResponse response) {
        AuthResponseDto authResponseDto = authService.login(loginDto);
        Cookie cookie = new Cookie("refresh_token", authResponseDto.getJwtRefreshToken());
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
        return ResponseEntity.ok(userMapper.toAuthDto(authResponseDto));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthDto> refresh(@CookieValue(name = "refresh_token") String refreshToken){
        AuthResponseDto authResponseDto = authService.refresh(refreshToken);
        return ResponseEntity.ok(userMapper.toAuthDto(authResponseDto));
    }
}