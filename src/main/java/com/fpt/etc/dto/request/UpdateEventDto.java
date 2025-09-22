package com.fpt.etc.dto.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class UpdateEventDto {
    private String name;
    private String subName;
    private String description;
    private String icon;

    private List<MultipartFile> addImages;
    private List<String> removeImages;

    private Boolean hot;

    private List<Long> addMenuIds;
    private List<Long> removeMenuIds;

    private List<Long> addServiceIds;
    private List<Long> removeServiceIds;
}

