package com.fpt.etc.controller;

import com.fpt.etc.entity.Combo;
import com.fpt.etc.services.ComboService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/combos")
public class ComboController {

    private final ComboService comboService;

    public ComboController(ComboService comboService) {
        this.comboService = comboService;
    }

    @GetMapping
    public ResponseEntity<List<Combo>> getAllCombos() {
        return ResponseEntity.ok(comboService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Combo> getComboById(@PathVariable Integer id) {
        return comboService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Combo> createCombo(@RequestBody Combo combo) {
        return ResponseEntity.ok(comboService.save(combo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Combo> updateCombo(@PathVariable Integer id, @RequestBody Combo combo) {
        return ResponseEntity.ok(comboService.update(id, combo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCombo(@PathVariable Integer id) {
        comboService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
