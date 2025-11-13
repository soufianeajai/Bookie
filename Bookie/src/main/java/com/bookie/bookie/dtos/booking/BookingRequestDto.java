package com.bookie.bookie.dtos.booking;

import com.bookie.bookie.annotations.CheckOutAfterCheckIn;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.LocalDate;

@Data
@CheckOutAfterCheckIn
public class BookingRequestDto {
    private Long hotelId;
    private Long roomId;
    @Positive
    private Integer roomsCount;
    @FutureOrPresent
    private LocalDate checkInDate;
    @Future
    private LocalDate checkOutDate;
}
