package com.bookie.bookie.services;


import com.bookie.bookie.dtos.booking.BookingDto;
import com.bookie.bookie.dtos.booking.BookingRequestDto;
import com.bookie.bookie.dtos.guest.GuestDto;
import com.bookie.bookie.entities.User;

import java.util.List;
import java.util.Set;

public interface BookingService {
    BookingDto initialiseBooking(BookingRequestDto bookingRequestDto, User user);
    BookingDto addGuests(Set<GuestDto> guests, Long id, User user);

    BookingDto getBookingById(Long id);
}
