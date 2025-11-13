package com.bookie.bookie.services;


import com.bookie.bookie.dtos.booking.BookingDto;
import com.bookie.bookie.dtos.booking.BookingRequestDto;
import com.bookie.bookie.dtos.guest.GuestDto;

import java.util.List;
import java.util.Set;

public interface BookingService {
    BookingDto initialiseBooking(BookingRequestDto bookingRequestDto);
    BookingDto addGuests(Set<GuestDto> guests, Long id);

    BookingDto getBookingById(Long id);
}
