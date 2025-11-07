package com.bookie.bookie.services.impl;

import com.bookie.bookie.dtos.hotel.CreateHotelDto;
import com.bookie.bookie.dtos.hotel.HotelDto;
import com.bookie.bookie.entities.Hotel;
import com.bookie.bookie.entities.Room;
import com.bookie.bookie.exceptions.ResourceNotFoundException;
import com.bookie.bookie.mappers.HotelMapper;
import com.bookie.bookie.repositories.HotelRepository;
import com.bookie.bookie.repositories.RoomRepository;
import com.bookie.bookie.services.HotelService;
import com.bookie.bookie.services.InventoryService;
import com.bookie.bookie.services.RoomService;
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

    @Override
    @Transactional
    public HotelDto createNewHotel(CreateHotelDto hotelDto) {
        Hotel hotel = hotelRepository.save(hotelMapper.toEntity(hotelDto));
        return hotelMapper.toDto(hotel);
    }

    @Override
    public HotelDto getHotelById(Long id) {
        Hotel hotel = hotelRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Hotel with id " + id + " not found"));
        return hotelMapper.toDto(hotel);
    }

    @Override
    public List<HotelDto> getAllHotels() {
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
    public HotelDto updateHotelById(Long id, CreateHotelDto hotelDto) {
        Hotel hotel = hotelRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Hotel with id " + id + " not found"));
        hotelMapper.updateEntityFromDto(hotelDto, hotel);
        return hotelMapper.toDto(hotel);
    }

    @Override
    @Transactional
    public void deleteHotelById(Long id) {
        Hotel hotel = hotelRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Hotel with id " + id + " not found"));
        hotel.setActive(false);
        roomRepository.deleteAll(hotel.getRooms());
    }
    @Override
    @Transactional
    public HotelDto activateHotel(Long id){
        Hotel hotel = hotelRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Hotel with id " + id + " not found"));
        hotel.setActive(true);
        for (Room room : hotel.getRooms()){
            inventoryService.initializeRoomForAYear(room);
        }
        return hotelMapper.toDto(hotel);
    }
}
