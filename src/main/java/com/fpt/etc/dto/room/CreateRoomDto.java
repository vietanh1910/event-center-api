package com.fpt.etc.dto.room;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class CreateRoomDto {

    @NotBlank(message = "Room name is required")
    private String name;

    @NotBlank(message = "Area is required")
    private String area;

    @NotNull(message = "People capacity is required")
    @Min(value = 1, message = "People must be greater than 0")
    private Integer people;

    @NotNull(message = "Table number is required")
    @Min(value = 1, message = "Table must be greater than 0")
    private Integer table;

    @NotNull(message = "Price is required")
    @Min(value = 0, message = "Price must be non-negative")
    private Double price;

    @NotNull(message = "Image is required")
    private MultipartFile image;
}

