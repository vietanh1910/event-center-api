package com.fpt.etc.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "combo_services")
@Getter
@Setter
public class ComboService extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "service_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "combo_id")
    private Combo combo;

    @Column(name = "service_name", nullable = false)
    private String serviceName;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "extra_price")
    private BigDecimal extraPrice;
}

