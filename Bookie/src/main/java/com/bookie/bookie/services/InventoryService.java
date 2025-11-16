package com.bookie.bookie.services;

import com.bookie.bookie.dtos.hotel.HotelPriceDto;
import com.bookie.bookie.dtos.hotel.HotelSearchCriteria;
import com.bookie.bookie.dtos.hotel.HotelSearchDto;
import com.bookie.bookie.entities.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface InventoryService {
    void initializeRoomForAYear(Room room);
    Page<HotelSearchDto> searchAvailableHotels(HotelSearchCriteria criteria, Pageable pageable);
    Page<HotelPriceDto> searcheHotelsByprice(HotelSearchCriteria criteria, Pageable pageable);

    }
