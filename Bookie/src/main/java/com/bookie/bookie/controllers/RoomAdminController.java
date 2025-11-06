package com.bookie.bookie.controllers;

import com.bookie.bookie.dtos.room.CreateRoomDto;
import com.bookie.bookie.dtos.room.RoomDto;
import com.bookie.bookie.entities.Room;
import com.bookie.bookie.services.RoomService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/admin/hotels/{hotelId}/rooms")
@RequiredArgsConstructor
public class RoomAdminController {

    private final RoomService roomService;

    @PostMapping
    public ResponseEntity<RoomDto> createNewRoom(@RequestBody @Valid CreateRoomDto createRoomDto, @PathVariable Long hotelId , HttpServletRequest req){
        RoomDto room = roomService.createNewRoom(createRoomDto, hotelId);
        URI uri = URI.create(req.getRequestURI() + "/" + room.getId());
        return ResponseEntity.created(uri).body(room);
    }

    @GetMapping
    public ResponseEntity<List<RoomDto>> getAllRoomsInHotel(@PathVariable Long hotelId){
        return ResponseEntity.ok(roomService.getAllRoomsInHotel(hotelId));
    }

    @GetMapping("{id}")
    public ResponseEntity<RoomDto> getRoomById(@PathVariable Long id, @PathVariable Long hotelId){
        return ResponseEntity.ok(roomService.getRoomById(id));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<RoomDto> deleteRoomById(@PathVariable Long id, @PathVariable Long hotelId){
        roomService.deleteRoomById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("{id}")
    public ResponseEntity<RoomDto> updateRoomById(@RequestBody CreateRoomDto createRoomDto,  @PathVariable Long id, @PathVariable Long hotelId){
        return ResponseEntity.ok(roomService.updateRoomById(createRoomDto, id));
    }
}
