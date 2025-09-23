package com.fpt.etc.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name = "rooms")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Room name is required")
    private String name;

    @NotBlank(message = "Area is required")
    private String area;

    @NotNull(message = "People capacity is required")
    @Min(value = 1, message = "People must be greater than 0")
    private Integer people;

    @NotNull(message = "Table number is required")
    @Min(value = 1, message = "Table must be greater than 0")
    private Integer tableNumber;

    @NotBlank(message = "Image is required")
    private String image;

    @NotNull(message = "Price is required")
    @Min(value = 0, message = "Price must be non-negative")
    private Double price;

    private boolean deleted = false;
}

