package com.bookie.bookie.controllers;

import com.bookie.bookie.dtos.booking.BookingDto;
import com.bookie.bookie.dtos.booking.BookingRequestDto;
import com.bookie.bookie.dtos.guest.GuestDto;
import com.bookie.bookie.entities.User;
import com.bookie.bookie.services.BookingService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("bookings")
@AllArgsConstructor
public class HotelBookingController {

    private final BookingService bookingService;

    @PostMapping("init")
    public ResponseEntity<BookingDto> initialiseBooking(@RequestBody @Valid BookingRequestDto bookingRequestDto, @AuthenticationPrincipal User authenticatedUser){
        return ResponseEntity.ok(bookingService.initialiseBooking(bookingRequestDto, authenticatedUser));
    }

    @PostMapping("/{bookingId}/addGuests")
    public ResponseEntity<BookingDto> addGuests(@RequestBody Set<GuestDto> guests, @PathVariable Long bookingId, @AuthenticationPrincipal User authenticatedUser){
        return ResponseEntity.ok(bookingService.addGuests(guests, bookingId, authenticatedUser));
    }

    @GetMapping("{id}")
    public ResponseEntity<BookingDto> getBookingById(@PathVariable Long id){
        return ResponseEntity.ok(bookingService.getBookingById(id));
    }

}
