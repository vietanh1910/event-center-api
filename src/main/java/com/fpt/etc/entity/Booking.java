package com.fpt.etc.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "bookings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false)
    private String userId;

    @NotNull
    @Column(nullable = false, unique = true)
    private String orderCode;

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Email is required")
    @Email
    private String email;

    @NotBlank(message = "Phone is required")
    private String phone;

    @NotBlank(message = "Event date is required")
    private String eventDate;

    @NotBlank(message = "Event time is required")
    private String eventTime;

    @NotNull(message = "EventId is required")
    private String eventId;

    @Min(value = 1, message = "People must be greater than 0")
    private int people;

    private String address;

    @NotBlank(message = "Payment method is required")
    @Pattern(regexp = "^(full|deposit)$", message = "Payment method must be 'full' or 'deposit'")
    private String paymentMethod;

    @NotNull(message = "MenuId is required")
    private Long menuId;

    private Long roomId;
    private Long venueId;

    @ElementCollection
    @CollectionTable(name = "booking_services", joinColumns = @JoinColumn(name = "booking_id"))
    @Column(name = "service_id")
    private List<Long> serviceIds;

    private String notes;

    private String status = "pending";
    private String paymentStatus = "unpaid";

    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime cancelledAt;
    private String cancelReason;
    private String cancelledBy; // "user" | "admin"
}



