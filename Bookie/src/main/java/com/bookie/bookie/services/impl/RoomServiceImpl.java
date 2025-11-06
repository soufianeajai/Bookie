package com.bookie.bookie.services.impl;

import com.bookie.bookie.dtos.room.CreateRoomDto;
import com.bookie.bookie.dtos.room.RoomDto;
import com.bookie.bookie.entities.Hotel;
import com.bookie.bookie.entities.Room;
import com.bookie.bookie.exceptions.ResourceNotFoundException;
import com.bookie.bookie.mappers.HotelMapper;
import com.bookie.bookie.mappers.RoomMapper;
import com.bookie.bookie.repositories.HotelRepository;
import com.bookie.bookie.repositories.RoomRepository;
import com.bookie.bookie.services.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final RoomMapper roomMapper;
    private final HotelRepository hotelRepository;
    private final HotelMapper hotelMapper;

    @Override
    @Transactional
    @Modifying
    public RoomDto createNewRoom(CreateRoomDto createRoomDto, Long hotelId) {
        Hotel hotel = hotelRepository.findById(hotelId).orElseThrow(() -> new ResourceNotFoundException("Hotel with Id " + hotelId + " not found"));
        Room room = roomMapper.toEntity(createRoomDto);
        room.setHotel(hotel);

        // create inventory for it when room created and hotel is active

        return roomMapper.toDto(roomRepository.save(room));
    }

    @Override
    public List<RoomDto> getAllRoomsInHotel(Long hotelId) {
        Hotel hotel = hotelRepository.findById(hotelId).orElseThrow(() -> new ResourceNotFoundException("Hotel with Id " + hotelId + " not found"));
        List<Room> rooms = hotel.getRooms();
        return rooms.stream().map(roomMapper::toDto).toList();
    }

    @Override
    public RoomDto getRoomById(Long id) {
        Room room = roomRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Room with id " + id + " not found"));
        return roomMapper.toDto(room);
    }

    @Override
    @Transactional
    @Modifying
    public RoomDto updateRoomById(CreateRoomDto createRoomDto, Long id) {
        Room room = roomRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Room with id " + id + " not found"));
        roomMapper.updateEntityFromDto(createRoomDto, room);
        return roomMapper.toDto(room);
    }

    @Override
    public void deleteRoomById(Long id) {
        Room room = roomRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Room with id " + id + " not found"));

//        delete inventory for the room then delete it
        roomRepository.delete(room);
    }
}
