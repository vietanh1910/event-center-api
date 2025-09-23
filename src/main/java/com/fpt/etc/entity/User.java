package com.fpt.etc.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;   // tương tự ObjectId bên Mongo

    @NotBlank(message = "Name is required")
    @Column(nullable = false)
    private String name;

    @Email(message = "Email is invalid")
    @NotBlank(message = "Email is required")
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Builder.Default
    private String role = "CUSTOMER";

    private String avatar;

    @NotBlank(message = "Phone is required")
    @Column(nullable = false, unique = true)
    private String phone;

    private String address;

    @Column(nullable = false)
    @Builder.Default
    private String status = "active";

    @Column(nullable = false)
    @Builder.Default
    private boolean deleted = false;
}


