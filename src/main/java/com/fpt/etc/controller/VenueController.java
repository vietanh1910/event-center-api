package com.fpt.etc.controller;

import com.fpt.etc.dto.venue.CreateVenueDto;
import com.fpt.etc.dto.venue.UpdateVenueDto;
import com.fpt.etc.entity.Venue;
import com.fpt.etc.services.VenueService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/venue")
@RequiredArgsConstructor
public class VenueController {

    private final VenueService venueService;

    @GetMapping
    public ResponseEntity<List<Venue>> getAll() {
        return ResponseEntity.ok(venueService.getAll());
    }

    @GetMapping("/{slug}")
    public ResponseEntity<Venue> getBySlug(@PathVariable String slug) {
        return ResponseEntity.ok(venueService.getBySlug(slug));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Venue> create(@ModelAttribute @Valid CreateVenueDto dto) {
        return ResponseEntity.ok(venueService.create(dto));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}")
    public ResponseEntity<Venue> update(@PathVariable Long id,
                                        @ModelAttribute UpdateVenueDto dto) {
        return ResponseEntity.ok(venueService.update(id, dto));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        venueService.delete(id);
        return ResponseEntity.ok("Venue marked as deleted.");
    }
}

