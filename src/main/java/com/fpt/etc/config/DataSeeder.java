package com.fpt.etc.config;

import com.fpt.etc.entity.User;
import com.fpt.etc.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

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
                    .name("Admin")
                    .password(passwordEncoder.encode("admin123"))
                    .role("ADMIN")
                    .phone("0569262694")
                    .address("123 Admin St, City")
                    .build();
            userRepository.save(admin);

            User user = User.builder()
                    .email("user-test@gmail.com")
                    .name("User Test")
                    .password(passwordEncoder.encode("user123"))
                    .role("CUSTOMER")
                    .phone("0123456789")
                    .address("234 User Ave, City")
                    .build();
            userRepository.save(user);
        }
    }
}