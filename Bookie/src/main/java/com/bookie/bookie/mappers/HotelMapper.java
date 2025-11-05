package com.bookie.bookie.mappers;

import com.bookie.bookie.dtos.hotel.HotelDto;
import com.bookie.bookie.entities.Hotel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface HotelMapper {
    HotelDto toDto(Hotel hotel);
    Hotel toEntity(HotelDto hotelDto);
}
