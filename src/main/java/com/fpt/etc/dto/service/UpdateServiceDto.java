package com.fpt.etc.dto.service;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

@Data
public class UpdateServiceDto {
    private String name;
    private BigDecimal price;
    private String description;
    private String icon;

    private List<MultipartFile> addImages;
    private List<String> removeImages;
}

