package com.bookie.bookie.dtos.hotel;

import lombok.Data;

@Data
public class HotelPriceDtoResponse {
    private HotelDto hotel;
    private Double price;
}
