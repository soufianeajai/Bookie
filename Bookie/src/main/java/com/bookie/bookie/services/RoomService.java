package com.bookie.bookie.services;


import com.bookie.bookie.dtos.room.CreateRoomDto;
import com.bookie.bookie.dtos.room.RoomDto;

import java.util.List;
import java.util.Map;

public interface RoomService {
    RoomDto createNewRoom(CreateRoomDto createRoomDto, Long hotelId);
    List<RoomDto> getAllRoomsInHotel(Long hotelId);
    RoomDto getRoomById(Long id);
    RoomDto updateRoomById(CreateRoomDto createRoomDto, Long id);
    RoomDto patchRoomById(Map<String, Object> patchValues, Long id);
    void deleteRoomById(Long id);
}
