package com.bookie.bookie.repositories;

import com.bookie.bookie.entities.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HotelRepository extends JpaRepository<Hotel, Long> {
    @Query("select h from Hotel h where h.active = :active")
    List<Hotel> findAllByActiveTrue(@Param("active") boolean active);
}
