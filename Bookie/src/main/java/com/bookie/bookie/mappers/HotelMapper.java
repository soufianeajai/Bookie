package com.bookie.bookie.mappers;

import com.bookie.bookie.dtos.hotel.CreateHotelDto;
import com.bookie.bookie.dtos.hotel.HotelDto;
import com.bookie.bookie.entities.Hotel;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface HotelMapper {
    HotelDto toDto(Hotel hotel);
    Hotel toEntity(CreateHotelDto hotelDto);
    void updateEntityFromDto(CreateHotelDto hotelDto, @MappingTarget Hotel hotel);
}
