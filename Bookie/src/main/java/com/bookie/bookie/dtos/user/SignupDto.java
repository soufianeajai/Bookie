package com.bookie.bookie.dtos.user;

import com.bookie.bookie.entities.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

@Data
public class SignupDto {
    @Email
    private String email;
    @Size(min = 6)
    private String password;
    private String name;
    private Set<Role> roles;
}
