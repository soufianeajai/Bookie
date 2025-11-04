package com.bookie.bookie.services;


import com.bookie.bookie.entities.User;
import com.bookie.bookie.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    UserRepository userRepository;


    public void addUser(){

    }
}
