package com.fpt.etc.dto.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class UserProfileUpdateDTO {
    private String name;
    private String email;
    private String phone;
    private String address;
    private MultipartFile avatar; // file upload
}
