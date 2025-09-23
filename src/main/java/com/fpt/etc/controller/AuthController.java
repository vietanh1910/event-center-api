package com.fpt.etc.controller;

import com.fpt.etc.dto.auth.LoginRequest;
import com.fpt.etc.dto.auth.SignupRequest;
import com.fpt.etc.entity.User;
import com.fpt.etc.services.AuthService;
import com.fpt.etc.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final UserService userService;
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody SignupRequest dto) {
        User user = authService.register(dto);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest dto) {
        return ResponseEntity.ok(authService.login(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getById(id));
    }
}