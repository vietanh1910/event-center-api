package com.fpt.etc.dto.room;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@Data
public class UpdateRoomDto {
    private String name;
    private String area;
    private Integer people;
    private Integer table;
    private BigDecimal price;
    private MultipartFile image;
}

