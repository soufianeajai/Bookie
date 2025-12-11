package com.bookie.bookie.controllers;

import com.bookie.bookie.dtos.hotel.CreateHotelDto;
import com.bookie.bookie.dtos.hotel.HotelDto;
import com.bookie.bookie.entities.User;
import com.bookie.bookie.services.HotelService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public ResponseEntity<List<HotelDto>> getAllHotels(@AuthenticationPrincipal User authenticatedUser){
        return ResponseEntity.ok(hotelService.getAllHotels(authenticatedUser));
    }
    @GetMapping("/available")
    public ResponseEntity<List<HotelDto>> getAllHotelsByActive(@RequestParam Boolean active){
        return ResponseEntity.ok(hotelService.getAllHotelsByActive(active));
    }

    @PostMapping
    public ResponseEntity<HotelDto> createHotel(@RequestBody @Valid CreateHotelDto createHotelDto, @AuthenticationPrincipal User authenticatedUser){
        HotelDto hotelDto = hotelService.createNewHotel(createHotelDto, authenticatedUser);
        URI location = URI.create("/api/v1/hotels/" + hotelDto.getId());
        return ResponseEntity.created(location).body(hotelDto);
    }

    @GetMapping("{id}")
    public ResponseEntity<HotelDto> getHotelById(@PathVariable @Positive Long id, @AuthenticationPrincipal User authenticatedUser){
        return ResponseEntity.ok(hotelService.getHotelById(id, authenticatedUser));
    }

    @PutMapping("{id}")
    public ResponseEntity<HotelDto> updateHotelById(@PathVariable Long id, @RequestBody @Valid CreateHotelDto createHotelDto, @AuthenticationPrincipal User authenticatedUser){
        return ResponseEntity.ok(hotelService.updateHotelById(id, createHotelDto, authenticatedUser));
    }

    @PatchMapping("{id}")
    public ResponseEntity<HotelDto> activateHotelById(@PathVariable Long id, @AuthenticationPrincipal User authenticatedUser){
        return ResponseEntity.ok(hotelService.activateHotel(id, authenticatedUser));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteHotelById(@PathVariable Long id, @AuthenticationPrincipal User authenticatedUser){
        hotelService.deleteHotelById(id, authenticatedUser);
        return ResponseEntity.noContent().build();
    }
}
