package com.fpt.etc.controller;

import com.fpt.etc.dto.request.CreateMenuDto;
import com.fpt.etc.dto.request.UpdateMenuDto;
import com.fpt.etc.dto.response.MenuDetailDto;
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

    // [GET] api/menus
    @GetMapping
    public ResponseEntity<List<MenuDetailDto>> getAll() {
        return ResponseEntity.ok(menuService.getAll());
    }

    // [GET] api/menus/{id}
    @GetMapping("/{id}")
    public ResponseEntity<MenuDetailDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(menuService.getById(id));
    }

    // [POST] api/menus
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Menu> create(@Valid @RequestBody CreateMenuDto dto) {
        return ResponseEntity.ok(menuService.create(dto));
    }

    // [PATCH] api/menus/{id}
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}")
    public ResponseEntity<Menu> update(@PathVariable Long id, @RequestBody UpdateMenuDto dto) {
        return ResponseEntity.ok(menuService.update(id, dto));
    }

    // [DELETE] api/menus/{id}
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        menuService.delete(id);
        return ResponseEntity.ok("Deleted menu successfully.");
    }
}

