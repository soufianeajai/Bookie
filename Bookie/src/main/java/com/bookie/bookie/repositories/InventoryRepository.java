package com.bookie.bookie.repositories;

import com.bookie.bookie.entities.Inventory;
import com.bookie.bookie.entities.Room;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface InventoryRepository extends JpaRepository<Inventory, Long>, InventoryRepositoryCustom {

    @Query("SELECT i FROM Inventory i WHERE i.room.id = :roomId AND (i.totalCount - i.bookedCount - i.reservedCount) >= :roomsCount AND i.date BETWEEN :startDate AND :endDate")
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Inventory> findAndLockAvailableInventories(
            @Param("roomId") Long roomId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("roomsCount") Integer roomsCount
            );

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT i FROM Inventory i " +
            "WHERE i.room.id = :roomId " +
            "AND i.date BETWEEN :startDate AND :endDate")
    List<Inventory> findAndLockByDateRange(
            @Param("roomId") Long roomId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}