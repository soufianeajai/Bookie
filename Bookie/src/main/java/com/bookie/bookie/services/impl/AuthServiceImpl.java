package com.bookie.bookie.services.impl;

import com.bookie.bookie.dtos.user.AuthResponseDto;
import com.bookie.bookie.dtos.user.LoginDto;
import com.bookie.bookie.dtos.user.SignupDto;
import com.bookie.bookie.dtos.user.UserDto;
import com.bookie.bookie.entities.User;
import com.bookie.bookie.entities.enums.Role;
import com.bookie.bookie.exceptions.ResourceNotFoundException;
import com.bookie.bookie.mappers.UserMapper;
import com.bookie.bookie.repositories.UserRepository;
import com.bookie.bookie.security.JwtService;
import com.bookie.bookie.services.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;


@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private static final String ERROR_MESSAGE = "User not found with Email, Or wrong Password ";

    @Transactional
    public UserDto signUp(SignupDto signupDto) {
        Optional<User> userExists = userRepository.findByEmail(signupDto.getEmail());
        if (userExists.isPresent())
            throw new BadCredentialsException("the User already exists with email " + signupDto.getEmail());
        User user = userMapper.toEntity(signupDto);
        user.setRoles(Set.of(Role.GUEST));
        user.setPassword(passwordEncoder.encode(signupDto.getPassword()));
        user.setCreatedBy(signupDto.getEmail());
        user.setLastModifiedBy(signupDto.getEmail());
        userRepository.save(user);
        return userMapper.toDto(user);
    }

    @Transactional
    public AuthResponseDto login(LoginDto loginDto) {
        Authentication authentication =  authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        User user = (User)authentication.getPrincipal();
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        return AuthResponseDto.builder().user(userMapper.toDto(user)).jwtAccessToken(accessToken).jwtRefreshToken(refreshToken).build();
    }

    @Override
    public AuthResponseDto refresh(String refreshToken) {
        if (!jwtService.isTokenValid(refreshToken)){
            throw new AuthenticationServiceException("refresh token is not valid");
        }
        String username = jwtService.extractUsername(refreshToken);
        User user = userRepository.findByEmail(username).orElseThrow(() -> new ResourceNotFoundException("user not found"));
        String accessToken = jwtService.generateAccessToken(user);
        return AuthResponseDto.builder().user(userMapper.toDto(user)).jwtAccessToken(accessToken).jwtRefreshToken(refreshToken).build();

    }
}
