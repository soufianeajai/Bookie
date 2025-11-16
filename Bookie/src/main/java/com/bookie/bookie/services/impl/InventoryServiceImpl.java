package com.bookie.bookie.services.impl;

import com.bookie.bookie.dtos.hotel.HotelPriceDto;
import com.bookie.bookie.dtos.hotel.HotelSearchCriteria;
import com.bookie.bookie.dtos.hotel.HotelSearchDto;
import com.bookie.bookie.entities.Hotel;
import com.bookie.bookie.entities.HotelMinPrice;
import com.bookie.bookie.entities.Inventory;
import com.bookie.bookie.entities.Room;
import com.bookie.bookie.mappers.HotelMapper;
import com.bookie.bookie.mappers.HotelPriceMapper;
import com.bookie.bookie.repositories.HotelMinPriceRepository;
import com.bookie.bookie.repositories.InventoryRepository;
import com.bookie.bookie.services.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;
    private final HotelMinPriceRepository hotelMinPriceRepository;
    private final HotelMapper hotelMapper;
    private final HotelPriceMapper hotelPriceMapper;
    @Override
    @Transactional
    public void initializeRoomForAYear(Room room) {
        List<Inventory> inventories = new ArrayList<>();

        LocalDate today = LocalDate.now();
        LocalDate end = today.plusYears(1L);
        for (LocalDate date = today; !date.isAfter(end) ; date = date.plusDays(1L)){
            Inventory inventory = Inventory.builder()
                    .hotel(room.getHotel())
                    .room(room)
                    .date(date)
                    .price(room.getBasePrice())
                    .surgeFactor(BigDecimal.ONE)
                    .totalCount(room.getTotalCount())
                    .bookedCount(0)
                    .reservedCount(0)
                    .city(room.getHotel().getCity())
                    .closed(false)
                    .build();
            inventories.add(inventory);
        }
        inventoryRepository.saveAll(inventories);
    }

    @Override
    @Transactional()
    public Page<HotelSearchDto> searchAvailableHotels(HotelSearchCriteria criteria, Pageable pageable) {
        Page<Hotel> hotels = inventoryRepository.findWithDynamicAvailability(criteria, pageable);
        return Boolean.TRUE.equals(criteria.getWithRooms()) ? hotels.map(hotelMapper::toSearchDto) : hotels.map(hotelMapper::toDtoWithoutRooms);
    }

    @Override
    @Transactional
    public Page<HotelPriceDto> searcheHotelsByprice(HotelSearchCriteria criteria, Pageable pageable) {
        return hotelMinPriceRepository.findHotelByMinPrice(criteria.getCity(), criteria.getStartDate(), criteria.getEndDate(), pageable);
    }


}
