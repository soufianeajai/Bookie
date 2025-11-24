package com.bookie.bookie.controllers;


import com.bookie.bookie.dtos.user.UserDto;
import com.bookie.bookie.services.impl.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Controller
@RestController
@RequestMapping("users")
@AllArgsConstructor
public class UserController {


    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers (){
        return ResponseEntity.ok(userService.findAllUsers());
    }
}
