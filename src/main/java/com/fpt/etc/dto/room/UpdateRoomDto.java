package com.fpt.etc.dto.room;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UpdateRoomDto {
    private String name;
    private String area;
    private Integer people;
    private Integer table;
    private Double price;
    private MultipartFile image;
}

