package com.bookie.bookie.services.impl;

import com.bookie.bookie.entities.Booking;
import com.bookie.bookie.entities.Inventory;
import com.bookie.bookie.entities.enums.BookingStatus;
import com.bookie.bookie.repositories.BookingRepository;
import com.bookie.bookie.repositories.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingExpiryService {

    private final BookingRepository bookingRepository;
    private final InventoryRepository inventoryRepository;
    
    private static final int EXPIRY_MINUTES = 1;
    private static final String DELAY_TIME_IN_MILLISECONDS = "3000000";

    @Scheduled(fixedDelayString = DELAY_TIME_IN_MILLISECONDS)
    @Transactional
    public void expireReservedBookings() {
        log.info("Running booking expiry check...");
        LocalDateTime expiryTime = LocalDateTime.now().minusMinutes(EXPIRY_MINUTES);
        List<Booking> bookingsToExpire = bookingRepository.findExpiredBookings(
                expiryTime, BookingStatus.RESERVED
        );
        if (bookingsToExpire.isEmpty()) {
            log.info("No expired bookings found.");
            return;
        }
        log.warn("Found {} expired bookings. Reverting inventory...", bookingsToExpire.size());
        List<Inventory> allInventoriesToUpdate = new ArrayList<>();

        for (Booking booking : bookingsToExpire) {
            booking.setBookingStatus(BookingStatus.EXPIRED);
            LocalDate inventoryEndDate = booking.getCheckOutDate().minusDays(1);
            List<Inventory> inventories = inventoryRepository.findAndLockByDateRange(
                    booking.getRoom().getId(),
                    booking.getCheckInDate(),
                    inventoryEndDate
            );
            for (Inventory inventory : inventories) {
                inventory.setReservedCount(
                        inventory.getReservedCount() - booking.getRoomsCount()
                );
            }
            allInventoriesToUpdate.addAll(inventories);
        }
        bookingRepository.saveAll(bookingsToExpire);
        inventoryRepository.saveAll(allInventoriesToUpdate);
        log.info("Successfully expired {} bookings and reverted inventory.", bookingsToExpire.size());
    }
}