package com.fpt.etc.controller;

import com.fpt.etc.dto.request.UserProfileUpdateDTO;
import com.fpt.etc.dto.response.UpdateProfileDto;
import com.fpt.etc.entity.User;
import com.fpt.etc.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        return ResponseEntity.ok(userService.create(user));
    }

    @PutMapping("/{id}/change-password")
    public ResponseEntity<Void> changePassword(
            @PathVariable Long id,
            @RequestBody Map<String, String> request) {

        String newPassword = request.get("password");
        if (newPassword == null || newPassword.isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        userService.changePassword(id, newPassword);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getById(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateProfile(
            @PathVariable Long id,
            @ModelAttribute UpdateProfileDto dto) {
        return ResponseEntity.ok(userService.updateProfile(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> softDelete(@PathVariable Long id) {
        return ResponseEntity.ok(userService.softDelete(id));
    }
}
