package com.bookie.bookie.services;

import com.bookie.bookie.dtos.hotel.HotelDto;

import java.util.List;

public interface HotelService {
    HotelDto createNewHotel(HotelDto hotelDto);
    HotelDto getHotelById(Long id);
    List<HotelDto> getAllHotels();
}
