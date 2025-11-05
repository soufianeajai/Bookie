package com.bookie.bookie.dtos.hotel;

import com.bookie.bookie.entities.Embeddable.HotelContactInfo;
import com.bookie.bookie.entities.Room;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
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
    private HotelContactInfo contactInfo;
    private Boolean active;
}
