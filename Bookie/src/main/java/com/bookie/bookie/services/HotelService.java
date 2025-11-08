package com.bookie.bookie.services;

import com.bookie.bookie.dtos.hotel.CreateHotelDto;
import com.bookie.bookie.dtos.hotel.HotelDto;
import com.bookie.bookie.dtos.hotel.HotelSearchCriteria;
import com.bookie.bookie.dtos.hotel.HotelSearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface HotelService {
    HotelDto createNewHotel(CreateHotelDto hotelDto);
    HotelDto getHotelById(Long id);
    List<HotelDto> getAllHotels();
    List<HotelDto> getAllHotelsByActive(Boolean active);
    HotelDto updateHotelById(Long id, CreateHotelDto hotelDto);
    void deleteHotelById(Long id);
    HotelDto activateHotel(Long id);
    Page<HotelSearchDto> searchHotels(HotelSearchCriteria criteria, Pageable pageable);
}
