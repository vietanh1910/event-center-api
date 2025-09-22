package com.fpt.etc.dto.request;

import lombok.Data;

@Data
public class UpdateUserIdDto {
    private String anonymousId;
    private String realUserId;
}

