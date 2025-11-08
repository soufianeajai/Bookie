package com.bookie.bookie.dtos.hotel;

import com.bookie.bookie.dtos.room.RoomDto;
import com.bookie.bookie.entities.Room;
import com.bookie.bookie.entities.embeddable.HotelContactInfo;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class HotelSearchDto {
    private Long id;
    private String name;
    private String city;
    private List<String> photos;
    private List<String> amenities;
    private HotelContactInfo contactInfo;
    private Boolean active;
    private List<RoomDto> rooms;
}
