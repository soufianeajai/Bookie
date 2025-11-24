package com.bookie.bookie.services.impl;

import com.bookie.bookie.dtos.user.UserDto;
import com.bookie.bookie.entities.User;
import com.bookie.bookie.exceptions.ResourceNotFoundException;
import com.bookie.bookie.mappers.UserMapper;
import com.bookie.bookie.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private static final String ERROR_MESSAGE = "User not found with Email, Or wrong Password ";

    @Override
    public UserDetails loadUserByUsername(String email) throws BadCredentialsException {
        return userRepository.findByEmail(email).orElseThrow(()-> new BadCredentialsException(ERROR_MESSAGE + email));
    }


    public List<UserDto> findAllUsers() {
        List<User> users =  userRepository.findAll();
        return users.stream().map(userMapper::toDto).toList();
    }
}
