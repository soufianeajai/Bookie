package com.bookie.bookie.dtos.guest;

import com.bookie.bookie.entities.Booking;
import com.bookie.bookie.entities.User;
import com.bookie.bookie.entities.enums.Gender;
import lombok.Data;

import java.util.Set;

@Data
public class GuestDto {
    private Long id;
    private String name;
    private Integer age;
    private Gender gender;
}
