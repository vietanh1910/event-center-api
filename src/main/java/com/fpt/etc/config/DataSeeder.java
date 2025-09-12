package com.fpt.etc.config;

import com.fpt.etc.entity.User;
import com.fpt.etc.entity.enums.EPosition;
import com.fpt.etc.entity.enums.ERole;
import com.fpt.etc.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        // --- SEED USERS ---
        if (userRepository.count() == 0) {
            System.out.println("Seeding users...");
            User admin = User.builder()
                    .email("admin@gmail.com")
                    .password(passwordEncoder.encode("admin123"))
                    .role(ERole.ADMIN)
                    .fullName("Admin")
                    .build();
            userRepository.save(admin);

            User user = User.builder()
                    .email("customer1@gmail.com")
                    .password(passwordEncoder.encode("customer123"))
                    .role(ERole.CUSTOMER)
                    .fullName("Customer")
                    .build();
            userRepository.save(user);

            User allocator = User.builder()
                    .email("staff1@gmail.com")
                    .password(passwordEncoder.encode("staff123"))
                    .role(ERole.STAFF)
                    .position(EPosition.WAITER)
                    .fullName("Staff")
                    .build();
            userRepository.save(allocator);
            System.out.println("Users seeded successfully.");
        }
    }
}
