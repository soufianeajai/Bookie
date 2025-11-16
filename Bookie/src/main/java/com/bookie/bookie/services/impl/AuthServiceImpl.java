package com.bookie.bookie.services.impl;

import com.bookie.bookie.dtos.user.AuthResponseDto;
import com.bookie.bookie.dtos.user.LoginDto;
import com.bookie.bookie.dtos.user.SignupDto;
import com.bookie.bookie.entities.User;
import com.bookie.bookie.mappers.UserMapper;
import com.bookie.bookie.repositories.UserRepository;
import com.bookie.bookie.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private static final String ERROR_MESSAGE = "User not found with Email, Or wrong Password ";

    public AuthResponseDto signUp(SignupDto signupDto) {
        Optional<User> userExists = userRepository.findByEmail(signupDto.getEmail());
        if (userExists.isPresent()){
            throw new BadCredentialsException("the User already exists with email " + signupDto.getEmail());
        }
        User user = userMapper.toEntity(signupDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return AuthResponseDto.builder().user(userMapper.toDto(user)).jwtToken(jwtService.generateToken(user)).build();
    }

    @Transactional
    public AuthResponseDto login(LoginDto loginDto) {
        Authentication authentication =  authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));
        User user = (User)authentication.getPrincipal();
        return AuthResponseDto.builder().user(userMapper.toDto(user)).jwtToken(jwtService.generateToken(user)).build();
    }
}
