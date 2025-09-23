package com.fpt.etc.repository;

import com.fpt.etc.entity.Venue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VenueRepository extends JpaRepository<Venue, Long> {
    List<Venue> findAllByDeletedFalse();
    Optional<Venue> findBySlugAndDeletedFalse(String slug);
    Optional<Venue> findByIdAndDeletedFalse(Long id);
    boolean existsBySlug(String slug);
}

