package com.bookie.bookie.repositories;

import com.bookie.bookie.entities.Inventory;
import com.bookie.bookie.entities.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface InventoryRepository extends JpaRepository<Inventory, Long>, InventoryRepositoryCustom {
    void deleteByDateAfterAndRoom(LocalDate date, Room room);
}