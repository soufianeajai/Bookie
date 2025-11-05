package com.bookie.bookie.controllers;

import com.bookie.bookie.dtos.hotel.CreateHotelDto;
import com.bookie.bookie.dtos.hotel.HotelDto;
import com.bookie.bookie.services.HotelService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@Controller
@RestController()
@RequestMapping("/admin/hotels")
@RequiredArgsConstructor
@Validated
public class HotelController {

    private final HotelService hotelService;

    @GetMapping
    public ResponseEntity<List<HotelDto>> getAllHotels(){
        return ResponseEntity.ok(hotelService.getAllHotels());
    }
    @GetMapping("/available")
    public ResponseEntity<List<HotelDto>> getAllHotelsByActive(@RequestParam Boolean active){
        return ResponseEntity.ok(hotelService.getAllHotelsByActive(active));
    }

    @PostMapping
    public ResponseEntity<HotelDto> createHotel(@RequestBody @Valid CreateHotelDto createHotelDto){
        HotelDto hotelDto = hotelService.createNewHotel(createHotelDto);
        URI location = URI.create("/api/v1/hotels/" + hotelDto.getId());
        return ResponseEntity.created(location).body(hotelDto);
    }

    @GetMapping("{id}")
    public ResponseEntity<HotelDto> getHotelById(@PathVariable @Positive Long id){
        return ResponseEntity.ok(hotelService.getHotelById(id));
    }

    @PutMapping("{id}")
    public ResponseEntity<HotelDto> updateHotelById(@PathVariable Long id, @RequestBody @Valid CreateHotelDto createHotelDto){
        return ResponseEntity.ok(hotelService.updateHotelById(id, createHotelDto));
    }

    @PatchMapping("{id}")
    public ResponseEntity<HotelDto> activateHotelById(@PathVariable Long id){
        return ResponseEntity.ok(hotelService.activateHotel(id));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteHotelById(@PathVariable Long id){
        hotelService.deleteHotelById(id);
        return ResponseEntity.noContent().build();
    }
}
