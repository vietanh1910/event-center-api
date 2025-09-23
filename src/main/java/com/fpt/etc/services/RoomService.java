package com.fpt.etc.services;

import com.fpt.etc.dto.room.CreateRoomDto;
import com.fpt.etc.dto.room.UpdateRoomDto;
import com.fpt.etc.entity.Room;
import com.fpt.etc.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final CloudinaryService cloudinaryService;

    public List<Room> getAllRooms() {
        return roomRepository.findByDeletedFalse();
    }

    public Room getRoomById(Long id) {
        return roomRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("Room not found"));
    }

    public Room createRoom(CreateRoomDto dto) {
        String imageUrl = cloudinaryService.upload(dto.getImage());

        Room room = Room.builder()
                .name(dto.getName())
                .area(dto.getArea())
                .people(dto.getPeople())
                .tableNumber(dto.getTable())
                .price(dto.getPrice())
                .image(imageUrl)
                .deleted(false)
                .build();

        return roomRepository.save(room);
    }

    public Room updateRoom(Long id, UpdateRoomDto dto) {
        Room room = getRoomById(id);

        if (dto.getName() != null) room.setName(dto.getName());
        if (dto.getArea() != null) room.setArea(dto.getArea());
        if (dto.getPeople() != null) room.setPeople(dto.getPeople());
        if (dto.getTable() != null) room.setTableNumber(dto.getTable());
        if (dto.getPrice() != null) room.setPrice(dto.getPrice());

        MultipartFile newImage = dto.getImage();
        if (newImage != null) {
            cloudinaryService.delete(room.getImage());
            String newImageUrl = cloudinaryService.upload(newImage);
            room.setImage(newImageUrl);
        }

        return roomRepository.save(room);
    }

    public void deleteRoom(Long id) {
        Room room = getRoomById(id);
        room.setDeleted(true);
        roomRepository.save(room);
    }
}

