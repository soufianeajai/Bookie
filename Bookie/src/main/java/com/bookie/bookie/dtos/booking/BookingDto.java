package com.bookie.bookie.dtos.booking;

import com.bookie.bookie.dtos.guest.GuestDto;
import com.bookie.bookie.dtos.hotel.HotelDto;
import com.bookie.bookie.dtos.room.RoomDto;
import com.bookie.bookie.entities.enums.BookingStatus;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Data
public class BookingDto {
    private Long id;
    private HotelDto hotel;
    private RoomDto room;
    private Integer roomsCount;
    private LocalDate checkOutDate;
    private LocalDate checkInDate;
    private BookingStatus bookingStatus;
    private Set<GuestDto> guests;
}
