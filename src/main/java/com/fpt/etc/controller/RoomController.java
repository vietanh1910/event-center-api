package com.fpt.etc.controller;

import com.fpt.etc.dto.room.CreateRoomDto;
import com.fpt.etc.dto.room.UpdateRoomDto;
import com.fpt.etc.entity.Room;
import com.fpt.etc.services.RoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/room")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @GetMapping
    public ResponseEntity<List<Room>> getAll() {
        return ResponseEntity.ok(roomService.getAllRooms());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Room> getById(@PathVariable Long id) {
        return ResponseEntity.ok(roomService.getRoomById(id));
    }

    @PostMapping
    public ResponseEntity<Room> create(@ModelAttribute @Valid CreateRoomDto dto) {
        return ResponseEntity.ok(roomService.createRoom(dto));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Room> update(@PathVariable Long id,
                                       @ModelAttribute UpdateRoomDto dto) {
        return ResponseEntity.ok(roomService.updateRoom(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        roomService.deleteRoom(id);
        return ResponseEntity.ok("Room deleted (soft delete) successfully");
    }
}

