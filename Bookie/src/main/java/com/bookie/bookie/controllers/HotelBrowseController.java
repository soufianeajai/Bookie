package com.bookie.bookie.controllers;

import com.bookie.bookie.dtos.hotel.HotelSearchCriteria;
import com.bookie.bookie.dtos.hotel.HotelSearchDto;
import com.bookie.bookie.services.HotelService;
import com.bookie.bookie.services.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("hotels")
@RequiredArgsConstructor
@Validated
public class HotelBrowseController {

    private final InventoryService inventoryService;
    private final HotelService hotelService;

    @GetMapping("search")
    public ResponseEntity<Page<HotelSearchDto>> searchHotels(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(required = false) String roomType,
            @RequestParam(required = false) Integer roomsCount,
            @RequestParam(required = false, defaultValue = "false") Boolean withRooms,
            Pageable pageable
            )
    {
        HotelSearchCriteria criteria = new HotelSearchCriteria(city, startDate, endDate, roomType, roomsCount, withRooms);
        Page<HotelSearchDto> results = inventoryService.searchAvailableHotels(criteria, pageable);
        return ResponseEntity.ok(results);
    }
    @GetMapping("/{id}/infos")
    public ResponseEntity<HotelSearchDto> getHotelInfo(@PathVariable Long id)
    {
        return ResponseEntity.ok(hotelService.getHotelInfo(id));
    }
}
