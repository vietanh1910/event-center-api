package com.fpt.etc.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateProfileDto {
    private String name;
    private String phone;
    private String address;
    private String password;
    private MultipartFile avatar;
}
