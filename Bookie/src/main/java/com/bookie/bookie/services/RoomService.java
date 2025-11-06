package com.bookie.bookie.services;


import com.bookie.bookie.dtos.room.CreateRoomDto;
import com.bookie.bookie.dtos.room.RoomDto;

import java.util.List;

public interface RoomService {
    RoomDto createNewRoom(CreateRoomDto createRoomDto, Long hotelId);
    List<RoomDto> getAllRoomsInHotel(Long hotelId);
    RoomDto getRoomById(Long id);
    RoomDto updateRoomById(CreateRoomDto createRoomDto, Long id);
    void deleteRoomById(Long id);
}
