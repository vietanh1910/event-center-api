package com.fpt.etc.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class CreateEventDto {

    @NotBlank(message = "Event name is required")
    private String name;

    private String subName;
    private String description;
    private String icon;

    private List<MultipartFile> images;
    private boolean hot = false;
    private List<Long> menuIds;
    private List<Long> serviceIds;
}

