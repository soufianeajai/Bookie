package com.bookie.bookie.utils;

import com.bookie.bookie.annotations.CheckOutAfterCheckIn;
import com.bookie.bookie.dtos.booking.BookingRequestDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CheckOutAfterCheckInValidator implements ConstraintValidator<CheckOutAfterCheckIn, BookingRequestDto> {

    @Override
    public boolean isValid(BookingRequestDto dto, ConstraintValidatorContext context) {
        if (dto.getCheckInDate() == null || dto.getCheckOutDate() == null) {
            return true;
        }
        return dto.getCheckOutDate().isAfter(dto.getCheckInDate());
    }
}
