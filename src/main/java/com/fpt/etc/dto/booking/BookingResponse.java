package com.fpt.etc.dto.booking;

import com.fpt.etc.dto.event.EventResponse;
import com.fpt.etc.dto.event.EventResponseBasic;
import com.fpt.etc.dto.menu.MenuResponse;
import com.fpt.etc.dto.room.RoomResponse;
import com.fpt.etc.dto.service.ServiceResponse;
import com.fpt.etc.dto.venue.VenueResponseBasic;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookingResponse {
    private Long id;
    private String orderCode;
    private String name;
    private String email;
    private String phone;
    private String eventDate;
    private String eventTime;
    private int people;
    private String address;

    private String paymentMethod;
    private String status;
    private String paymentStatus;

    private LocalDateTime createdAt;
    private LocalDateTime cancelledAt;
    private String cancelReason;
    private String cancelledBy;

    // Thay vì chỉ ID, ta trả object
    private MenuResponse menu;
    private RoomResponse room;
    private VenueResponseBasic venue;
    private List<ServiceResponse> services;
    private EventResponseBasic event;
}

