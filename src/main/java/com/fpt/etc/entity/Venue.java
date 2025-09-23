package com.fpt.etc.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "venues")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Venue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Venue name is required")
    private String name;

    @NotBlank(message = "Slug is required")
    private String slug;

    private String description;

    @NotBlank(message = "Area is required")
    private String area;

    @NotNull(message = "People is required")
    @Min(value = 1, message = "People must be greater than 0")
    private Integer people;

    // liên kết tới Room bằng id
    @ElementCollection
    @CollectionTable(name = "venue_rooms", joinColumns = @JoinColumn(name = "venue_id"))
    @Column(name = "room_id")
    private List<Long> roomIds = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "venue_hero_banners", joinColumns = @JoinColumn(name = "venue_id"))
    @Column(name = "image_url")
    private List<String> heroBanners = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "venue_thumbnail_images", joinColumns = @JoinColumn(name = "venue_id"))
    @Column(name = "image_url")
    private List<String> thumbnailImages = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "venue_gallery_images", joinColumns = @JoinColumn(name = "venue_id"))
    @Column(name = "image_url")
    private List<String> galleryImages = new ArrayList<>();

    @NotBlank(message = "Address is required")
    private String address;

    @NotBlank(message = "Open time is required")
    private String openTime;

    @NotBlank(message = "Close time is required")
    private String closeTime;

    @ElementCollection
    @CollectionTable(name = "venue_days", joinColumns = @JoinColumn(name = "venue_id"))
    @Column(name = "day")
    private List<String> days = new ArrayList<>();

    @NotBlank(message = "Image is required")
    private String image;

    private boolean deleted = false;
}

