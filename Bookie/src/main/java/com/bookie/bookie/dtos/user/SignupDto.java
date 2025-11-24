package com.bookie.bookie.dtos.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
public class SignupDto {
    @Email
    private String email;
    @Size(min = 6)
    private String password;
    private String name;
}
