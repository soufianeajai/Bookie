package com.bookie.bookie.dtos.room;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CreateRoomDto {
    @NotBlank
    private String type;
    @NotNull
    @PositiveOrZero
    @Digits(integer = 10, fraction = 2)
    private BigDecimal basePrice;
    private List<String> photos;
    private List<String> amenities;
    @NotNull
    @PositiveOrZero
    private Integer totalCount;
    @NotNull
    @PositiveOrZero
    private Integer capacity;
}
