package com.bookie.bookie.services.impl;

import com.bookie.bookie.dtos.booking.BookingDto;
import com.bookie.bookie.dtos.booking.BookingRequestDto;
import com.bookie.bookie.dtos.guest.GuestDto;
import com.bookie.bookie.entities.*;
import com.bookie.bookie.entities.enums.BookingStatus;
import com.bookie.bookie.exceptions.ResourceNotFoundException;
import com.bookie.bookie.mappers.BookingMapper;
import com.bookie.bookie.mappers.GuestMapper;
import com.bookie.bookie.repositories.BookingRepository;
import com.bookie.bookie.repositories.HotelRepository;
import com.bookie.bookie.repositories.InventoryRepository;
import com.bookie.bookie.repositories.RoomRepository;
import com.bookie.bookie.services.BookingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final HotelRepository hotelRepository;
    private final RoomRepository roomRepository;
    private  final InventoryRepository inventoryRepository;
    private final BookingMapper bookingMapper;
    private final GuestMapper guestMapper;
    private static final String HOTEL_NOT_FOUND_ERROR_MESSAGE = "Hotel not found with id : ";
    private static final String ROOM_NOT_FOUND_ERROR_MESSAGE = "Room not found with id : ";
    private static final String BOOKING_NOT_FOUND_ERROR_MESSAGE = "Booking not found with id : ";

    @Override
    @Transactional
    public BookingDto initialiseBooking(BookingRequestDto bookingRequestDto) {
        Hotel hotel = hotelRepository.findById(bookingRequestDto.getHotelId()).orElseThrow(() -> new ResourceNotFoundException(HOTEL_NOT_FOUND_ERROR_MESSAGE + bookingRequestDto.getHotelId()));
        Room room = roomRepository.findByIdAndHotel(bookingRequestDto.getRoomId(), hotel).orElseThrow(() -> new ResourceNotFoundException(ROOM_NOT_FOUND_ERROR_MESSAGE + bookingRequestDto.getRoomId()));

        LocalDate checkIn = bookingRequestDto.getCheckInDate();
        LocalDate checkOut = bookingRequestDto.getCheckOutDate();
        if (!checkIn.isBefore(checkOut)) {
            throw new IllegalStateException("Check-out date must be after check-in date");
        }
        LocalDate inventoryEndDate = checkOut.minusDays(1);
        long daysCount = ChronoUnit.DAYS.between(checkIn, checkOut);

        List<Inventory> inventories = inventoryRepository.findAndLockAvailableInventories(
                bookingRequestDto.getRoomId(),
                checkIn,
                inventoryEndDate,
                bookingRequestDto.getRoomsCount()
        );
        if (inventories.size() != daysCount) {
            String error = new StringBuilder("Room with Id = ").append(bookingRequestDto.getRoomId()).append(" is not available for all nights between ").append(checkIn).append(" and ").append(checkOut).toString();
            throw new ResourceNotFoundException(error);
        }

        for (Inventory inventory : inventories) {
            inventory.setReservedCount(inventory.getReservedCount() + bookingRequestDto.getRoomsCount());
        }
        inventoryRepository.saveAll(inventories);
        Booking booking = Booking.builder()
                .bookingStatus(BookingStatus.RESERVED)
                .hotel(hotel).room(room)
                .roomsCount(bookingRequestDto.getRoomsCount())
                .checkInDate(checkIn)
                .checkOutDate(checkOut)
                .user(getCurrentUser())
                .amount(BigDecimal.TEN).build();
        // TODO: Calculate price

        return bookingMapper.toDto(bookingRepository.save(booking));
    }

    @Override
    @Transactional
    public BookingDto addGuests(Set<GuestDto> guests, Long id) {
        Booking booking = bookingRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(BOOKING_NOT_FOUND_ERROR_MESSAGE + id));
        if(booking.getBookingStatus() == BookingStatus.EXPIRED){
            throw new  IllegalStateException("booking is expired");
        }
        if (booking.getBookingStatus() != BookingStatus.RESERVED)
            throw  new IllegalStateException("booking is not in reserved state, cannot add guests");
        for (GuestDto guest : guests){
            Guest g = guestMapper.toEntity(guest);
            g.setUser(getCurrentUser());
            booking.getGuests().add(g);
        }
        booking.setBookingStatus(BookingStatus.GUESTS_ADDED);
        booking = bookingRepository.save(booking);
        return bookingMapper.toDto(booking);
    }

    @Override
    @Transactional
    public BookingDto getBookingById(Long id) {
        Booking booking = bookingRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(BOOKING_NOT_FOUND_ERROR_MESSAGE + id));
        return bookingMapper.toDto(booking);
    }

    private User getCurrentUser(){
        User user = User.builder().id(1L).name(String.valueOf(ThreadLocalRandom.current().nextLong())).email("soufiane.ajaite@gmail.com").build();
        return user;
    }
}
