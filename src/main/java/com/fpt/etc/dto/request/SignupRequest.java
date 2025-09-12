package com.fpt.etc.dto.request;

import com.fpt.etc.entity.enums.ERole;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequest {
    private String email;
    private String password;
    private String fullName;
    private String phone;
}
