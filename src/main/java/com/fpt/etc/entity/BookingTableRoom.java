package com.fpt.etc.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "booking_table_room")
@Getter
@Setter
public class BookingTableRoom {
    @EmbeddedId
    private BookingTableRoomId id = new BookingTableRoomId();

    @ManyToOne
    @MapsId("bookingId")
    @JoinColumn(name = "booking_id")
    private Booking booking;

    @ManyToOne
    @MapsId("tableRoomId")
    @JoinColumn(name = "table_room_id")
    private TableRoom tableRoom;
}

@Embeddable
@Getter
@Setter
class BookingTableRoomId implements java.io.Serializable {
    private Integer bookingId;
    private Integer tableRoomId;
}

