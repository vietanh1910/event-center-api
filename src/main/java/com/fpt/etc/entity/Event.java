package com.fpt.etc.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "events")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    private String subName;
    private String icon;

    @NotBlank
    @Column(unique = true)
    private String slug;

    private String description;

    @ElementCollection
    @CollectionTable(name = "event_menus", joinColumns = @JoinColumn(name = "event_id"))
    @Column(name = "menu_id")
    private List<Long> menuIds = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "event_services", joinColumns = @JoinColumn(name = "event_id"))
    @Column(name = "service_id")
    private List<Long> serviceIds = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "event_images", joinColumns = @JoinColumn(name = "event_id"))
    @Column(name = "image_url")
    private List<String> images = new ArrayList<>();

    private boolean hot = false;
    private boolean deleted = false;
}

