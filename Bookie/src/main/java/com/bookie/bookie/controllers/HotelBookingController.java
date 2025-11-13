package com.bookie.bookie.controllers;

import com.bookie.bookie.dtos.booking.BookingDto;
import com.bookie.bookie.dtos.booking.BookingRequestDto;
import com.bookie.bookie.dtos.guest.GuestDto;
import com.bookie.bookie.services.BookingService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("bookings")
@AllArgsConstructor
public class HotelBookingController {

    private final BookingService bookingService;

    @PostMapping("init")
    public ResponseEntity<BookingDto> initialiseBooking(@RequestBody @Valid BookingRequestDto bookingRequestDto){
        return ResponseEntity.ok(bookingService.initialiseBooking(bookingRequestDto));
    }

    @PostMapping("/{bookingId}/addGuests")
    public ResponseEntity<BookingDto> addGuests(@RequestBody Set<GuestDto> guests, @PathVariable Long bookingId){
        return ResponseEntity.ok(bookingService.addGuests(guests, bookingId));
    }

    @GetMapping("{id}")
    public ResponseEntity<BookingDto> getBookingById(@PathVariable Long id){
        return ResponseEntity.ok(bookingService.getBookingById(id));
    }

}
