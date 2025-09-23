package com.fpt.etc.repository;

import com.fpt.etc.entity.ServiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ServiceRepository extends JpaRepository<ServiceEntity, Long> {
    List<ServiceEntity> findByDeletedFalse();
    Optional<ServiceEntity> findByIdAndDeletedFalse(Long id);
}

