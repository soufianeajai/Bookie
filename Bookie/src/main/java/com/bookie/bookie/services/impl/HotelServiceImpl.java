package com.bookie.bookie.services.impl;

import com.bookie.bookie.dtos.hotel.HotelDto;
import com.bookie.bookie.entities.Hotel;
import com.bookie.bookie.mappers.HotelMapper;
import com.bookie.bookie.repositories.HotelRepository;
import com.bookie.bookie.services.HotelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class HotelServiceImpl implements HotelService {

    private final HotelRepository hotelRepository;
    private final HotelMapper hotelMapper;
    @Override
    public HotelDto createNewHotel(HotelDto hotelDto) {
        return null;
    }

    @Override
    public HotelDto getHotelById(Long id) {
        return null;
    }

    @Override
    public List<HotelDto> getAllHotels() {
        List<Hotel> hotels = hotelRepository.findAll();
        return hotels.stream().map(hotelMapper::toDto).toList();
    }
}
