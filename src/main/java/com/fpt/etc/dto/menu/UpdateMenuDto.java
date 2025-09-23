package com.fpt.etc.dto.menu;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class UpdateMenuDto {
    private String name;
    private List<Long> dishIds;
    private BigDecimal price;
}

