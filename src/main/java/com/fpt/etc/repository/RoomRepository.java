package com.fpt.etc.repository;

import com.fpt.etc.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> findByDeletedFalse();
    Optional<Room> findByIdAndDeletedFalse(Long id);
    List<Room> findByIdIn(List<Long> ids);
}

