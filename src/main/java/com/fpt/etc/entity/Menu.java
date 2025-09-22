package com.fpt.etc.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "menus")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    @ElementCollection
    @CollectionTable(name = "menu_dishes", joinColumns = @JoinColumn(name = "menu_id"))
    @Column(name = "dish_id")
    private List<Long> dishIds = new ArrayList<>();

    @NotNull(message = "Price is required")
    @PositiveOrZero(message = "Price must be a positive number")
    private BigDecimal price;

    private boolean deleted = false;
}

