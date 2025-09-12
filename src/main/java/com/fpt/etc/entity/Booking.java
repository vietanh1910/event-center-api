package com.fpt.etc.entity;

import com.fpt.etc.entity.enums.BookingStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "bookings")
@Getter
@Setter
public class Booking extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "booking_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private User customer;

    @ManyToOne
    @JoinColumn(name = "combo_id")
    private Combo combo;

    @Column(name = "event_date")
    private java.time.LocalDate eventDate;

    @Column(name = "guest_count")
    private Integer guestCount;

    private String venue;

    @Enumerated(EnumType.STRING)
    private BookingStatus status;
}

