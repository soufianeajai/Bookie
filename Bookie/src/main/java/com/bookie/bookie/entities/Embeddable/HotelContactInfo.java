package com.bookie.bookie.entities.Embeddable;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter @Setter
public class HotelContactInfo {
    @NotBlank
    private String address;
    private String phoneNumber;
    @Email
    private String email;
    private String location;
}
