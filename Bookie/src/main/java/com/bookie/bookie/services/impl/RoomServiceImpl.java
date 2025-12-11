package com.bookie.bookie.services.impl;

import com.bookie.bookie.dtos.room.CreateRoomDto;
import com.bookie.bookie.dtos.room.RoomDto;
import com.bookie.bookie.entities.Hotel;
import com.bookie.bookie.entities.Room;
import com.bookie.bookie.entities.User;
import com.bookie.bookie.exceptions.ResourceNotFoundException;
import com.bookie.bookie.exceptions.UnauthorizedException;
import com.bookie.bookie.mappers.RoomMapper;
import com.bookie.bookie.repositories.HotelRepository;
import com.bookie.bookie.repositories.RoomRepository;
import com.bookie.bookie.services.InventoryService;
import com.bookie.bookie.services.RoomService;
import com.bookie.bookie.utils.PatchHelper;
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
    private final PatchHelper patchHelper;
    private final InventoryService inventoryService;
    private static final String ROOM_NOT_FOUND_ERROR_MESSAGE = "Room not found with id : ";
    private static final String HOTEL_NOT_FOUND_ERROR_MESSAGE = "Hotel not found with id : ";


    @Override
    @Transactional
    public RoomDto createNewRoom(CreateRoomDto createRoomDto, Long hotelId, User authenticatedUser) {
        Hotel hotel = hotelRepository.findById(hotelId).orElseThrow(() -> new ResourceNotFoundException(HOTEL_NOT_FOUND_ERROR_MESSAGE + hotelId));
        if (!authenticatedUser.equals(hotel.getOwner())){
            throw new UnauthorizedException("the user with email : " + authenticatedUser.getEmail() + " is not the owner of the hotel " + hotel.getName());
        }
        Room room = roomMapper.toEntity(createRoomDto);
        room.setHotel(hotel);

        if(Boolean.TRUE.equals(hotel.getActive())){
            inventoryService.initializeRoomForAYear(room);
        }
        return roomMapper.toDto(roomRepository.save(room));
    }

    @Override
    @Transactional
    public List<RoomDto> getAllRoomsInHotel(Long hotelId) {
        Hotel hotel = hotelRepository.findById(hotelId).orElseThrow(() -> new ResourceNotFoundException(HOTEL_NOT_FOUND_ERROR_MESSAGE + hotelId));
        List<Room> rooms = hotel.getRooms();
        return rooms.stream().map(roomMapper::toDto).toList();
    }

    @Override
    public RoomDto getRoomById(Long id) {
        Room room = roomRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(ROOM_NOT_FOUND_ERROR_MESSAGE + id));
        return roomMapper.toDto(room);
    }

    @Override
    @Transactional
    public RoomDto updateRoomById(CreateRoomDto createRoomDto, Long id, User authenticatedUser) {
        Room room = roomRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(ROOM_NOT_FOUND_ERROR_MESSAGE + id));
        if (!authenticatedUser.equals(room.getHotel().getOwner())){
            throw new UnauthorizedException("the user with email : " + authenticatedUser.getEmail() + " is not the owner of the hotel " + room.getHotel().getName());
        }
        roomMapper.updateEntityFromDto(createRoomDto, room);
        return roomMapper.toDto(room);
    }

    @Override
    @Transactional
    public RoomDto patchRoomById(Map<String, Object> patchValues, Long id, User authenticatedUser) {
        Room room = roomRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(ROOM_NOT_FOUND_ERROR_MESSAGE + id));
        if (!authenticatedUser.equals(room.getHotel().getOwner())){
            throw new UnauthorizedException("the user with email : " + authenticatedUser.getEmail() + " is not the owner of the hotel " + room.getHotel().getName());
        }
        CreateRoomDto createRoomDto = roomMapper.toCreateRoomDto(room);
        CreateRoomDto mergedDto = patchHelper.mergeAndValidate(createRoomDto, patchValues);
        roomMapper.updateEntityFromDto(mergedDto, room);
        return roomMapper.toDto(room);
    }


    @Override
    @Transactional
    public void deleteRoomById(Long id, User authenticatedUser) {
        Room room = roomRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(ROOM_NOT_FOUND_ERROR_MESSAGE + id));
        if (!authenticatedUser.equals(room.getHotel().getOwner())){
            throw new UnauthorizedException("the user with email : " + authenticatedUser.getEmail() + " is not the owner of the hotel " + room.getHotel().getName());
        }
        roomRepository.delete(room);
    }
}
