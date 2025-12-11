package com.bookie.bookie.services.impl;

import com.bookie.bookie.dtos.hotel.CreateHotelDto;
import com.bookie.bookie.dtos.hotel.HotelDto;
import com.bookie.bookie.dtos.hotel.HotelSearchDto;
import com.bookie.bookie.entities.Hotel;
import com.bookie.bookie.entities.Room;
import com.bookie.bookie.entities.User;
import com.bookie.bookie.entities.enums.Role;
import com.bookie.bookie.exceptions.ResourceNotFoundException;
import com.bookie.bookie.exceptions.UnauthorizedException;
import com.bookie.bookie.mappers.HotelMapper;
import com.bookie.bookie.repositories.HotelRepository;
import com.bookie.bookie.repositories.RoomRepository;
import com.bookie.bookie.services.HotelService;
import com.bookie.bookie.services.InventoryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class HotelServiceImpl implements HotelService {

    private final HotelRepository hotelRepository;
    private final HotelMapper hotelMapper;
    private final InventoryService inventoryService;
    private final RoomRepository roomRepository;
    private static final String HOTEL_NOT_FOUND_ERROR_MESSAGE = "Hotel not found with id : ";
    @Override
    @Transactional
    public HotelDto createNewHotel(CreateHotelDto hotelDto, User authenticatedUser) {
        Hotel hotel = hotelMapper.toEntity(hotelDto);
        hotel.setActive(false);
        hotel.setOwner(authenticatedUser);
        Hotel savedHotel = hotelRepository.save(hotel);
        return hotelMapper.toDto(savedHotel);
    }

    @Override
    public HotelDto getHotelById(Long id, User authenticatedUser) {
        Hotel hotel = hotelRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(HOTEL_NOT_FOUND_ERROR_MESSAGE + id));
        if (!authenticatedUser.equals(hotel.getOwner())){
            throw new UnauthorizedException("the user with email : " + authenticatedUser.getEmail() + " is not the owner of the hotel " + hotel.getName());
        }
        return hotelMapper.toDto(hotel);
    }

    @Override
    public List<HotelDto> getAllHotels(User authenticatedUser) {
        List<Hotel> hotels = hotelRepository.findAll();
        return hotels.stream().map(hotelMapper::toDto).toList();
    }

    @Override
    public List<HotelDto> getAllHotelsByActive(Boolean active) {
        List<Hotel> hotels = hotelRepository.findAllByActiveTrue(active);
        return hotels.stream().map(hotelMapper::toDto).toList();
    }

    @Override
    @Transactional
    public HotelDto updateHotelById(Long id, CreateHotelDto hotelDto, User authenticatedUser) {
        Hotel hotel = hotelRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(HOTEL_NOT_FOUND_ERROR_MESSAGE + id));
        hotelMapper.updateEntityFromDto(hotelDto, hotel);
        if (!authenticatedUser.equals(hotel.getOwner())){
            throw new UnauthorizedException("the user with email : " + authenticatedUser.getEmail() + " is not the owner of the hotel " + hotel.getName());
        }
        return hotelMapper.toDto(hotel);
    }

    @Override
    @Transactional
    public void deleteHotelById(Long id, User authenticatedUser) {
        Hotel hotel = hotelRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(HOTEL_NOT_FOUND_ERROR_MESSAGE + id));
        if (!authenticatedUser.equals(hotel.getOwner())){
            throw new UnauthorizedException("the user with email : " + authenticatedUser.getEmail() + " is not the owner of the hotel " + hotel.getName());
        }
        hotel.setActive(false);
        roomRepository.deleteAll(hotel.getRooms());
    }
    @Override
    @Transactional
    public HotelDto activateHotel(Long id, User authenticatedUser){
        Hotel hotel = hotelRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(HOTEL_NOT_FOUND_ERROR_MESSAGE + id));
        if (!authenticatedUser.equals(hotel.getOwner())){
            throw new UnauthorizedException("the user with email : " + authenticatedUser.getEmail() + " is not the owner of the hotel " + hotel.getName());
        }
        hotel.setActive(true);
        for (Room room : hotel.getRooms()){
            inventoryService.initializeRoomForAYear(room);
        }
        return hotelMapper.toDto(hotel);
    }

    @Override
    @Transactional
    public HotelSearchDto getHotelInfo(Long id) {
        Hotel hotel = hotelRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(HOTEL_NOT_FOUND_ERROR_MESSAGE + id));
        return hotelMapper.toSearchDto(hotel);
    }


}
