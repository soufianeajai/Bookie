package com.bookie.bookie.controllers;

import com.bookie.bookie.dtos.hotel.HotelDto;
import com.bookie.bookie.dtos.hotel.HotelSearchCriteria;
import com.bookie.bookie.dtos.hotel.HotelSearchDto;
import com.bookie.bookie.services.HotelService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("hotels")
@RequiredArgsConstructor
@Validated
public class HotelBrowseController {

    private final HotelService hotelService;


    @GetMapping("search")
    public ResponseEntity<Page<HotelSearchDto>> searchHotels(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(required = false) String roomType,
            @RequestParam(required = false) Integer roomsCount,
            Pageable pageable
            )
    {
        HotelSearchCriteria criteria = new HotelSearchCriteria(city, startDate, endDate, roomType, roomsCount);
        Page<HotelSearchDto> results = hotelService.searchHotels(criteria, pageable);
        return ResponseEntity.ok(results);
    }
}
