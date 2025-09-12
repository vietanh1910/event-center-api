package com.fpt.etc.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "combos")
@Getter
@Setter
public class Combo extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "combo_id")
    private Integer id;

    @Column(name = "combo_name", nullable = false)
    private String comboName;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(name = "min_people")
    private Integer minPeople;

    @Column(name = "max_people")
    private Integer maxPeople;

    @Column(name = "image_url")
    private String imageUrl;

    @OneToMany(mappedBy = "combo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ComboService> services;
}
