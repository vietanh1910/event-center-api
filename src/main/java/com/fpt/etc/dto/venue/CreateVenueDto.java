package com.fpt.etc.dto.venue;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class CreateVenueDto {
    @NotBlank(message = "Venue name is required")
    private String name;

    @NotBlank(message = "Area is required")
    private String area;

    @NotNull(message = "People is required")
    @Min(value = 1, message = "People must be greater than 0")
    private Integer people;

    private String description;

    private List<Long> roomIds;

    private List<MultipartFile> heroBanners;
    private List<MultipartFile> thumbnailImages;
    private List<MultipartFile> galleryImages;

    @NotBlank(message = "Address is required")
    private String address;

    @NotBlank(message = "Open time is required")
    private String open;

    @NotBlank(message = "Close time is required")
    private String close;

    @NotNull(message = "Days are required")
    private List<String> days;

    private MultipartFile image;
}

