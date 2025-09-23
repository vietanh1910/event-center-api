package com.fpt.etc.dto.venue;

import com.fpt.etc.dto.room.RoomResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VenueResponse {
    private Long id;
    private String name;
    private String area;
    private Integer people;
    private String description;
    private String address;
    private String openTime;
    private String closeTime;
    private List<RoomResponse> rooms;
}

