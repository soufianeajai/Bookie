package com.bookie.bookie.repositories;

import com.bookie.bookie.entities.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long> {
}
