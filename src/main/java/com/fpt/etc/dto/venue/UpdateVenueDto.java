package com.fpt.etc.dto.venue;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class UpdateVenueDto {
    private String name;
    private String description;
    private String area;
    private Integer people;
    private String address;
    private String open;
    private String close;
    private List<String> days;

    // Hình ảnh
    private List<MultipartFile> addHeroBanners;
    private List<String> removeHeroBanners;

    private List<MultipartFile> addThumbnailImages;
    private List<String> removeThumbnailImages;

    private List<MultipartFile> addGalleryImages;
    private List<String> removeGalleryImages;

    // Phòng
    private List<Long> addRoomIds;
    private List<Long> removeRoomIds;

    private MultipartFile image;
}

