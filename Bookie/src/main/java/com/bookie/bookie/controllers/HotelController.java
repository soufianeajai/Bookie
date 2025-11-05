package com.bookie.bookie.controllers;

import com.bookie.bookie.dtos.hotel.HotelDto;
import com.bookie.bookie.services.HotelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Controller
@RestController()
@RequestMapping("/admin/hotels")
@RequiredArgsConstructor
public class HotelController {

    private final HotelService hotelService;

    @GetMapping
    public List<HotelDto> getAllHotels(){
        return hotelService.getAllHotels();
    }
}
