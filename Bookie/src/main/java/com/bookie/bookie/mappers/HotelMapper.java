package com.bookie.bookie.mappers;

import com.bookie.bookie.dtos.hotel.CreateHotelDto;
import com.bookie.bookie.dtos.hotel.HotelDto;
import com.bookie.bookie.dtos.hotel.HotelSearchDto;
import com.bookie.bookie.entities.Hotel;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface HotelMapper {
    HotelDto toDto(Hotel hotel);
    Hotel toEntity(CreateHotelDto hotelDto);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(CreateHotelDto hotelDto, @MappingTarget Hotel hotel);
    HotelSearchDto toSearchDto(Hotel hotel);
    @Mapping(target = "rooms", ignore = true)
    HotelSearchDto toDtoWithoutRooms(Hotel hotel);
}
