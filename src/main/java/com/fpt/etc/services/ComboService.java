package com.fpt.etc.services;

import com.fpt.etc.entity.Combo;
import com.fpt.etc.repository.ComboRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ComboService {

    private final ComboRepository comboRepository;

    public ComboService(ComboRepository comboRepository) {
        this.comboRepository = comboRepository;
    }

    public List<Combo> findAll() {
        return comboRepository.findAll();
    }

    public Optional<Combo> findById(Integer id) {
        return comboRepository.findById(id);
    }

    public Combo save(Combo combo) {
        return comboRepository.save(combo);
    }

    public Combo update(Integer id, Combo combo) {
        Combo existing = comboRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Combo not found with id: " + id));

        existing.setComboName(combo.getComboName());
        existing.setDescription(combo.getDescription());
        existing.setPrice(combo.getPrice());
        existing.setMinPeople(combo.getMinPeople());
        existing.setMaxPeople(combo.getMaxPeople());
        existing.setImageUrl(combo.getImageUrl());
        existing.setServices(combo.getServices());

        return comboRepository.save(existing);
    }

    public void delete(Integer id) {
        if (!comboRepository.existsById(id)) {
            throw new RuntimeException("Combo not found with id: " + id);
        }
        comboRepository.deleteById(id);
    }
}
