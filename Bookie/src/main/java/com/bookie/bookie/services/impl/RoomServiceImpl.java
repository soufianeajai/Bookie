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
import com.bookie.bookie.utils.PatchHelper;
import com.fasterxml.jackson.databind.JsonMappingException;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final RoomMapper roomMapper;
    private final HotelRepository hotelRepository;
    private final HotelMapper hotelMapper;
    private final PatchHelper patchHelper;

    @Override
    @Transactional
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
    public RoomDto updateRoomById(CreateRoomDto createRoomDto, Long id) {
        Room room = roomRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Room with id " + id + " not found"));
        roomMapper.updateEntityFromDto(createRoomDto, room);
        return roomMapper.toDto(room);
    }

    @Override
    @Transactional
    public RoomDto patchRoomById(Map<String, Object> patchValues, Long id) {
        Room room = roomRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Room with id " + id + " not found"));
        CreateRoomDto createRoomDto = roomMapper.toCreateRoomDto(room);
        CreateRoomDto mergedDto = patchHelper.mergeAndValidate(createRoomDto, patchValues);
        roomMapper.updateEntityFromDto(mergedDto, room);
        return roomMapper.toDto(room);
    }


    @Override
    public void deleteRoomById(Long id) {
        Room room = roomRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Room with id " + id + " not found"));

//        delete inventory for the room then delete it
        roomRepository.delete(room);
    }
}
