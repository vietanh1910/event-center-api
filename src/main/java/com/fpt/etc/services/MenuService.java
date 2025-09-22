package com.fpt.etc.services;

import com.fpt.etc.dto.request.CreateMenuDto;
import com.fpt.etc.dto.request.DishDetailDto;
import com.fpt.etc.dto.request.UpdateMenuDto;
import com.fpt.etc.dto.response.MenuDetailDto;
import com.fpt.etc.entity.Menu;
import com.fpt.etc.repository.MenuRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;
    private final DishService dishService; // để load tên món

    public List<MenuDetailDto> getAll() {
        return menuRepository.findByDeletedFalse().stream().map(menu -> {
            List<DishDetailDto> dishes = dishService.getDishesByIds(menu.getDishIds());
            return new MenuDetailDto(menu.getId(), menu.getName(), menu.getPrice(), dishes);
        }).toList();
    }

    public MenuDetailDto getById(Long id) {
        Menu menu = menuRepository.findById(id)
                .filter(m -> !m.isDeleted())
                .orElseThrow(() -> new RuntimeException("Menu not found"));
        List<DishDetailDto> dishes = dishService.getDishesByIds(menu.getDishIds());
        return new MenuDetailDto(menu.getId(), menu.getName(), menu.getPrice(), dishes);
    }

    @Transactional
    public Menu create(CreateMenuDto dto) {
        Menu menu = Menu.builder()
                .name(dto.getName())
                .dishIds(dto.getDishIds() != null ? dto.getDishIds() : Collections.emptyList())
                .price(dto.getPrice())
                .deleted(false)
                .build();
        return menuRepository.save(menu);
    }

    @Transactional
    public Menu update(Long id, UpdateMenuDto dto) {
        Menu menu = menuRepository.findById(id)
                .filter(m -> !m.isDeleted())
                .orElseThrow(() -> new RuntimeException("Menu not found"));

        if (dto.getName() != null) menu.setName(dto.getName());
        if (dto.getDishIds() != null) menu.setDishIds(dto.getDishIds());
        if (dto.getPrice() != null) menu.setPrice(dto.getPrice());

        return menuRepository.save(menu);
    }

    @Transactional
    public void delete(Long id) {
        Menu menu = menuRepository.findById(id)
                .filter(m -> !m.isDeleted())
                .orElseThrow(() -> new RuntimeException("Menu not found or already deleted"));
        menu.setDeleted(true);
        menuRepository.save(menu);
    }
}

