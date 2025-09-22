package com.fpt.etc.services;

import com.fpt.etc.dto.request.CreateDishDto;
import com.fpt.etc.dto.request.UpdateDishDto;
import com.fpt.etc.entity.Dish;
import com.fpt.etc.repository.DishRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DishService {

    private final DishRepository dishRepository;

    public Dish create(CreateDishDto dto) {
        Dish dish = Dish.builder()
                .name(dto.getName())
                .deleted(false)
                .build();
        return dishRepository.save(dish);
    }

    public List<Dish> getAll() {
        return dishRepository.findByDeletedFalse();
    }

    public Dish getById(Long id) {
        return dishRepository.findById(id)
                .filter(d -> !d.isDeleted())
                .orElseThrow(() -> new RuntimeException("Dish not found"));
    }

    @Transactional
    public Dish update(Long id, UpdateDishDto dto) {
        Dish dish = dishRepository.findById(id)
                .filter(d -> !d.isDeleted())
                .orElseThrow(() -> new RuntimeException("Dish not found"));

        if (dto.getName() == null || dto.getName().isBlank()) {
            throw new RuntimeException("No valid fields to update");
        }

        dish.setName(dto.getName());
        return dishRepository.save(dish);
    }

    @Transactional
    public void delete(Long id) {
        Dish dish = dishRepository.findById(id)
                .filter(d -> !d.isDeleted())
                .orElseThrow(() -> new RuntimeException("Dish not found or already deleted"));

        dish.setDeleted(true);
        dishRepository.save(dish);
    }
}

