package com.bookie.bookie.services;


import com.bookie.bookie.dtos.room.CreateRoomDto;
import com.bookie.bookie.dtos.room.RoomDto;
import com.bookie.bookie.entities.User;

import java.util.List;
import java.util.Map;

public interface RoomService {
    RoomDto createNewRoom(CreateRoomDto createRoomDto, Long hotelId, User user);
    List<RoomDto> getAllRoomsInHotel(Long hotelId);
    RoomDto getRoomById(Long id);
    RoomDto updateRoomById(CreateRoomDto createRoomDto, Long id, User user);
    RoomDto patchRoomById(Map<String, Object> patchValues, Long id, User user);
    void deleteRoomById(Long id, User user);
}
