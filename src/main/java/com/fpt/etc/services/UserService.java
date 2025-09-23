package com.fpt.etc.services;

import com.fpt.etc.dto.response.UpdateProfileDto;
import com.fpt.etc.entity.User;
import com.fpt.etc.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CloudinaryService cloudinaryService;

    public User changePassword(Long id, String newPassword) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        // mã hóa password trước khi lưu
        user.setPassword(passwordEncoder.encode(newPassword));
        return userRepository.save(user);
    }

    public User create(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        // mã hóa password khi tạo mới
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public User getById(Long id) {
        return userRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public String updateProfile(Long id, UpdateProfileDto dto) {
        User user = getById(id);

        if (dto.getName() != null && !dto.getName().isBlank()) {
            user.setName(dto.getName());
        }
        if (dto.getPhone() != null && !dto.getPhone().isBlank()) {
            user.setPhone(dto.getPhone());
        }
        if (dto.getAddress() != null && !dto.getAddress().isBlank()) {
            user.setAddress(dto.getAddress());
        }
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        if (dto.getAvatar() != null && !dto.getAvatar().isEmpty()) {
            if (user.getAvatar() != null && !user.getAvatar().isEmpty()) {
                cloudinaryService.delete(user.getAvatar());
            }
            String avatarUrl = cloudinaryService.upload(dto.getAvatar());
            user.setAvatar(avatarUrl);
        }

        userRepository.save(user);
        return "Profile updated successfully.";
    }

    public String softDelete(Long id) {
        User user = getById(id);
        user.setDeleted(true);
        userRepository.save(user);
        return "User deleted (soft)";
    }
}

