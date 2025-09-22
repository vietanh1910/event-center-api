package com.fpt.etc.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CreateMenuDto {
    @NotBlank(message = "Name is required")
    private String name;

    private List<Long> dishIds;

    @NotNull
    @PositiveOrZero(message = "Price must be a positive number")
    private BigDecimal price;
}

