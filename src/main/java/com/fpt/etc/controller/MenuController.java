package com.fpt.etc.controller;

import com.fpt.etc.dto.menu.CreateMenuDto;
import com.fpt.etc.dto.menu.MenuWithDishesDto;
import com.fpt.etc.dto.menu.UpdateMenuDto;
import com.fpt.etc.entity.Menu;
import com.fpt.etc.services.MenuService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menus")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    @GetMapping
    public ResponseEntity<List<MenuWithDishesDto>> getAll() {
        return ResponseEntity.ok(menuService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MenuWithDishesDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(menuService.getById(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Menu> create(@Valid @RequestBody CreateMenuDto dto) {
        return ResponseEntity.ok(menuService.create(dto));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}")
    public ResponseEntity<Menu> update(@PathVariable Long id, @RequestBody UpdateMenuDto dto) {
        return ResponseEntity.ok(menuService.update(id, dto));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        menuService.delete(id);
        return ResponseEntity.ok("Deleted menu successfully.");
    }
}
