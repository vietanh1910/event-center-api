package com.fpt.etc.entity;

import com.fpt.etc.entity.enums.EPosition;
import com.fpt.etc.entity.enums.ERole;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(unique = true, nullable = false)
    private String email;

    private String phone;

    private String password;

    @Enumerated(EnumType.STRING)
    private ERole role;

    private String address;

    private EPosition position;

    @Column(name = "image_url")
    private String imageUrl;
}

