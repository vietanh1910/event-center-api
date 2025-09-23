package com.fpt.etc.dto.menu;

import com.fpt.etc.dto.dish.DishDetailDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
public class MenuDetailDto {
    private Long id;
    private String name;
    private BigDecimal price;
    private List<DishDetailDto> dishes;
}

