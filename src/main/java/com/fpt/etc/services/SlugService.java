package com.fpt.etc.services;

import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.util.Locale;

@Service
public class SlugService {

    public String generate(String input) {
        if (input == null || input.isBlank()) return "";
        // Bỏ dấu tiếng Việt
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");
        // Lowercase, thay space thành dấu gạch ngang
        String slug = normalized.toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("\\s+", "-")
                .replaceAll("-{2,}", "-")
                .replaceAll("^-|-$", "");
        return slug;
    }
}

