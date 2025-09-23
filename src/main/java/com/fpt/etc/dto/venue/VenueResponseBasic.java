package com.fpt.etc.dto.venue;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VenueResponseBasic {
    private Long id;
    private String name;
}
