package com.bookie.bookie.mappers;

import com.bookie.bookie.dtos.guest.GuestDto;
import com.bookie.bookie.entities.Guest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GuestMapper {
    Guest toEntity(GuestDto dto);
    GuestDto toDto(Guest guest);
}
