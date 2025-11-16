package com.bookie.bookie.services.impl;

import com.bookie.bookie.exceptions.ResourceNotFoundException;
import com.bookie.bookie.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private static final String ERROR_MESSAGE = "User not found with Email, Or wrong Password ";

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email).orElseThrow(()-> new ResourceNotFoundException(ERROR_MESSAGE + email));
    }


}
