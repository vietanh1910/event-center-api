package com.fpt.etc.dto.event;

import com.fpt.etc.dto.menu.MenuResponse;
import com.fpt.etc.dto.menu.MenuWithDishesDto;
import com.fpt.etc.dto.service.ServiceResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventResponse {
    private Long id;
    private String name;
    private String subName;
    private String icon;
    private String slug;
    private String description;
    private List<String> images;
    private boolean hot;

    private List<MenuWithDishesDto> menus;
    private List<ServiceResponse> services;
}

