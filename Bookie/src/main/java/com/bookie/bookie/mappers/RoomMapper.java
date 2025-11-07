package com.bookie.bookie.mappers;

import com.bookie.bookie.dtos.room.CreateRoomDto;
import com.bookie.bookie.dtos.room.RoomDto;
import com.bookie.bookie.entities.Room;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface RoomMapper {
    RoomDto toDto(Room room);
    Room toEntity(CreateRoomDto room);
    void updateEntityFromDto(CreateRoomDto createRoomDto, @MappingTarget Room room);
    CreateRoomDto toCreateRoomDto(Room room);
}
