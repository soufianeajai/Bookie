package com.bookie.bookie.dtos.user;

import jakarta.validation.constraints.Email;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class LoginDto {
    @Email
    private String email;
    private String password;
}
