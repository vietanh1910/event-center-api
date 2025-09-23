package com.fpt.etc.controller;

import com.fpt.etc.dto.event.CreateEventDto;
import com.fpt.etc.dto.event.EventResponse;
import com.fpt.etc.dto.event.UpdateEventDto;
import com.fpt.etc.entity.Event;
import com.fpt.etc.services.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @GetMapping
    public ResponseEntity<List<EventResponse>> getAll() {
        return ResponseEntity.ok(eventService.getAll());
    }

    @GetMapping("/{slug}")
    public ResponseEntity<Event> getBySlug(@PathVariable String slug) {
        return ResponseEntity.ok(eventService.getBySlug(slug));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Event> create(@Valid @ModelAttribute CreateEventDto dto) {
        return ResponseEntity.ok(eventService.create(dto));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}")
    public ResponseEntity<Event> update(@PathVariable Long id, @ModelAttribute UpdateEventDto dto) {
        return ResponseEntity.ok(eventService.update(id, dto));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        eventService.delete(id);
        return ResponseEntity.ok("Deleted successfully.");
    }
}

