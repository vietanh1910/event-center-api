package com.fpt.etc.controller;

import com.fpt.etc.dto.request.CancelBookingDto;
import com.fpt.etc.dto.request.CreateBookingDto;
import com.fpt.etc.dto.request.UpdateBookingStatusDto;
import com.fpt.etc.entity.Booking;
import com.fpt.etc.services.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @GetMapping
    public ResponseEntity<List<Booking>> getAll(@RequestParam(required = false) String status) {
        return ResponseEntity.ok(bookingService.getAll(status));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Booking> getById(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.getById(id));
    }

    @PostMapping
    public ResponseEntity<Booking> create(@Valid @RequestBody CreateBookingDto dto) {
        return ResponseEntity.ok(bookingService.create(dto));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Booking>> getByUserId(@PathVariable String userId) {
        return ResponseEntity.ok(bookingService.getByUserId(userId));
    }

    @PatchMapping("cancel/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Booking> cancel(@PathVariable Long id, @RequestBody CancelBookingDto dto) {
        return ResponseEntity.ok(bookingService.cancel(id, dto));
    }

    @PatchMapping("status/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Booking> updateStatus(@PathVariable Long id, @RequestBody UpdateBookingStatusDto dto) {
        return ResponseEntity.ok(bookingService.updateStatus(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        bookingService.delete(id);
        return ResponseEntity.ok("Booking deleted");
    }
}

