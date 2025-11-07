package com.bookie.bookie.services;

import com.bookie.bookie.entities.Room;

public interface InventoryService {
    void initializeRoomForAYear(Room room);
    void deleteFutureInventories(Room room);
}
