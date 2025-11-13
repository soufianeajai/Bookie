package com.bookie.bookie.mappers;

import com.bookie.bookie.dtos.booking.BookingDto;
import com.bookie.bookie.entities.Booking;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {GuestMapper.class, HotelMapper.class, RoomMapper.class})

public interface BookingMapper {

    BookingDto toDto(Booking booking);
}
