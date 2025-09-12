package com.fpt.etc.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "booking_services")
@Getter
@Setter
public class BookingService {
    @EmbeddedId
    private BookingServiceId id = new BookingServiceId();

    @ManyToOne
    @MapsId("bookingId")
    @JoinColumn(name = "booking_id")
    private Booking booking;

    @ManyToOne
    @MapsId("serviceId")
    @JoinColumn(name = "service_id")
    private ComboService service;

    private Integer quantity;

    private java.math.BigDecimal price;
}

@Embeddable
@Getter
@Setter
class BookingServiceId implements java.io.Serializable {
    private Integer bookingId;
    private Integer serviceId;
}

