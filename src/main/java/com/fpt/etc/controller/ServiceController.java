package com.fpt.etc.controller;

import com.fpt.etc.dto.service.CreateServiceDto;
import com.fpt.etc.dto.service.UpdateServiceDto;
import com.fpt.etc.entity.ServiceEntity;
import com.fpt.etc.services.ServiceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/service")
@RequiredArgsConstructor
public class ServiceController {

    private final ServiceService serviceService;

    @GetMapping
    public ResponseEntity<List<ServiceEntity>> getAll() {
        return ResponseEntity.ok(serviceService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceEntity> getById(@PathVariable Long id) {
        return ResponseEntity.ok(serviceService.getById(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ServiceEntity> create(@ModelAttribute @Valid CreateServiceDto dto) {
        return ResponseEntity.ok(serviceService.create(dto));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}")
    public ResponseEntity<ServiceEntity> update(@PathVariable Long id,
                                                @ModelAttribute UpdateServiceDto dto) {
        return ResponseEntity.ok(serviceService.update(id, dto));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        serviceService.delete(id);
        return ResponseEntity.ok("Service soft deleted.");
    }
}

