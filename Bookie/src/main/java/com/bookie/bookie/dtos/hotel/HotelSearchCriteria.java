package com.bookie.bookie.dtos.hotel;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class HotelSearchCriteria {
    private String city;
    private LocalDate startDate;
    private LocalDate endDate;
    private String roomType;
    private Integer roomsCount;
    private Boolean withRooms;

}
