package com.fpt.etc.services;

import com.fpt.etc.dto.auth.JwtResponse;
import com.fpt.etc.dto.auth.LoginRequest;
import com.fpt.etc.dto.auth.SignupRequest;
import com.fpt.etc.entity.User;
import com.fpt.etc.repository.UserRepository;
import com.fpt.etc.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public User register(SignupRequest dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = User.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .phone(dto.getPhone())
                .address(dto.getAddress())
                .role("CUSTOMER")
                .status("active")
                .deleted(false)
                .build();

        return userRepository.save(user);
    }

    public JwtResponse login(LoginRequest dto) {
        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        if (!"active".equalsIgnoreCase(user.getStatus())) {
            throw new RuntimeException("Account is inactive or blocked");
        }

        String token = jwtUtils.generateToken(user.getId(), user.getRole());

        return new JwtResponse(token, user.getId(), user.getName(), user.getEmail(), user.getRole());
    }
}

