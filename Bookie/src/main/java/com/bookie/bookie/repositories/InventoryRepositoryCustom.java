package com.bookie.bookie.repositories;

import com.bookie.bookie.dtos.hotel.HotelSearchCriteria;
import com.bookie.bookie.entities.Hotel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface InventoryRepositoryCustom {
    Page<Hotel> findWithDynamicAvailability(HotelSearchCriteria criteria, Pageable pageable);
}