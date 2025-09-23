package com.fpt.etc.controller;

import com.fpt.etc.dto.dish.CreateDishDto;
import com.fpt.etc.dto.dish.UpdateDishDto;
import com.fpt.etc.entity.Dish;
import com.fpt.etc.services.DishService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dish")
@RequiredArgsConstructor
public class DishController {

    private final DishService dishService;

    // [POST] api/dish
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Dish> create(@Valid @RequestBody CreateDishDto dto) {
        return ResponseEntity.ok(dishService.create(dto));
    }

    // [GET] api/dish
    @GetMapping
    public ResponseEntity<List<Dish>> getAll() {
        return ResponseEntity.ok(dishService.getAll());
    }

    // [GET] api/dish/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Dish> getById(@PathVariable Long id) {
        return ResponseEntity.ok(dishService.getById(id));
    }

    // [PATCH] api/dish/{id}
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}")
    public ResponseEntity<Dish> update(@PathVariable Long id, @RequestBody UpdateDishDto dto) {
        return ResponseEntity.ok(dishService.update(id, dto));
    }

    // [DELETE] api/dish/{id}
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        dishService.delete(id);
        return ResponseEntity.ok("Dish deleted successfully.");
    }
}
