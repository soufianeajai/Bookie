package com.bookie.bookie.mappers;

import com.bookie.bookie.dtos.hotel.HotelPriceDto;
import com.bookie.bookie.entities.HotelMinPrice;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {HotelMapper.class})
public interface HotelPriceMapper {
    HotelPriceDto toDto(HotelMinPrice hotelMinPrice);
}
