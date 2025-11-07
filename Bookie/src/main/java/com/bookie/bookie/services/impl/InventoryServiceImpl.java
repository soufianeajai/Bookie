package com.bookie.bookie.services.impl;

import com.bookie.bookie.entities.Inventory;
import com.bookie.bookie.entities.Room;
import com.bookie.bookie.repositories.InventoryRepository;
import com.bookie.bookie.services.InventoryService;
import lombok.RequiredArgsConstructor;
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
                    .city(room.getHotel().getCity())
                    .closed(false)
                    .build();
            inventories.add(inventory);
        }
        inventoryRepository.saveAll(inventories);
    }

    @Override
    public void deleteFutureInventories(Room room) {
        inventoryRepository.deleteByDateAfterAndRoom(LocalDate.now(), room);
        inventoryRepository.flush();
    }
}
