package com.fpt.etc.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateDishDto {
    @NotBlank(message = "Name is required")
    private String name;
}

