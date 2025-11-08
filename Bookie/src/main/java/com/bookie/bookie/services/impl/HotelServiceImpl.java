package com.bookie.bookie.services.impl;

import com.bookie.bookie.dtos.hotel.CreateHotelDto;
import com.bookie.bookie.dtos.hotel.HotelDto;
import com.bookie.bookie.dtos.hotel.HotelSearchCriteria;
import com.bookie.bookie.dtos.hotel.HotelSearchDto;
import com.bookie.bookie.entities.Hotel;
import com.bookie.bookie.entities.Room;
import com.bookie.bookie.exceptions.ResourceNotFoundException;
import com.bookie.bookie.mappers.HotelMapper;
import com.bookie.bookie.repositories.HotelRepository;
import com.bookie.bookie.repositories.RoomRepository;
import com.bookie.bookie.services.HotelService;
import com.bookie.bookie.services.InventoryService;
import com.bookie.bookie.utils.HotelSpecifications;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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
    public HotelDto createNewHotel(CreateHotelDto hotelDto) {
        Hotel hotel = hotelRepository.save(hotelMapper.toEntity(hotelDto));
        return hotelMapper.toDto(hotel);
    }

    @Override
    public HotelDto getHotelById(Long id) {
        Hotel hotel = hotelRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(HOTEL_NOT_FOUND_ERROR_MESSAGE + id));
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
        Hotel hotel = hotelRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(HOTEL_NOT_FOUND_ERROR_MESSAGE + id));
        hotelMapper.updateEntityFromDto(hotelDto, hotel);
        return hotelMapper.toDto(hotel);
    }

    @Override
    @Transactional
    public void deleteHotelById(Long id) {
        Hotel hotel = hotelRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(HOTEL_NOT_FOUND_ERROR_MESSAGE + id));
        hotel.setActive(false);
        roomRepository.deleteAll(hotel.getRooms());
    }
    @Override
    @Transactional
    public HotelDto activateHotel(Long id){
        Hotel hotel = hotelRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(HOTEL_NOT_FOUND_ERROR_MESSAGE + id));
        hotel.setActive(true);
        for (Room room : hotel.getRooms()){
            inventoryService.initializeRoomForAYear(room);
        }
        return hotelMapper.toDto(hotel);
    }

    @Override
    @Transactional
    public Page<HotelSearchDto> searchHotels(HotelSearchCriteria criteria, Pageable pageable){
        Specification<Hotel> spec = HotelSpecifications.withCriteria(criteria);
        Page<Hotel> hotels = hotelRepository.findAll(spec, pageable);
        return hotels.map(hotelMapper::toSearchDto);
    }
}
