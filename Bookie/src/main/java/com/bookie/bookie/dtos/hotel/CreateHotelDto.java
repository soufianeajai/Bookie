package com.bookie.bookie.dtos.hotel;

import com.bookie.bookie.entities.Embeddable.HotelContactInfo;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class CreateHotelDto {
    @NotBlank
    private String name;
    @NotBlank
    private String city;
    private List<String> photos;
    private List<String> amenities;
    @Valid
    private HotelContactInfo contactInfo;
    @NotNull
    private Boolean active;
}
