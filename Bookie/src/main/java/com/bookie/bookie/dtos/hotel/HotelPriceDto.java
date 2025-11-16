package com.bookie.bookie.dtos.hotel;

import com.bookie.bookie.entities.Hotel;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HotelPriceDto {
    private Hotel hotel;
    private Double price;
}
