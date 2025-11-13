package com.bookie.bookie.repositories;

import com.bookie.bookie.entities.Hotel;
import com.bookie.bookie.entities.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> {
    Optional<Room> findByIdAndHotel(Long id, Hotel hotel);
}
