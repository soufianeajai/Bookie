package com.bookie.bookie.entities.Embeddable;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter @Setter
public class HotelContactInfo {
    private String address;
    @Pattern(regexp = "^[+]?\\d{9,15}$", message = "Invalid phone number")
    private String phoneNumber;
    @Email
    private String email;
    private String location;
}
