package com.bookie.bookie.repositories;

import com.bookie.bookie.entities.Booking;
import com.bookie.bookie.entities.enums.BookingStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT b FROM Booking b " +
            "WHERE b.bookingStatus = :status AND b.createdDate < :expiryTime")
    List<Booking> findExpiredBookings(
            @Param("expiryTime") LocalDateTime expiryTime,
            @Param("status") BookingStatus status
    );
}
