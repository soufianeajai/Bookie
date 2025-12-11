package com.bookie.bookie.services;

import com.bookie.bookie.dtos.hotel.CreateHotelDto;
import com.bookie.bookie.dtos.hotel.HotelDto;
import com.bookie.bookie.dtos.hotel.HotelSearchDto;
import com.bookie.bookie.entities.User;

import java.util.List;

public interface HotelService {
    HotelDto createNewHotel(CreateHotelDto hotelDto, User user);
    HotelDto getHotelById(Long id, User authenticatedUser);
    List<HotelDto> getAllHotels(User authenticatedUser);
    List<HotelDto> getAllHotelsByActive(Boolean active);
    HotelDto updateHotelById(Long id, CreateHotelDto hotelDto, User authenticatedUser);
    void deleteHotelById(Long id, User authenticatedUser);
    HotelDto activateHotel(Long id, User authenticatedUser);
    HotelSearchDto getHotelInfo(Long id);
}
