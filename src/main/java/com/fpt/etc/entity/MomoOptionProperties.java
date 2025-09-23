package com.fpt.etc.entity;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "momo")
public class MomoOptionProperties {
    private String momoApiUrl;
    private String secretKey;
    private String accessKey;
    private String returnUrl;
    private String notifyUrl;
    private String partnerCode;
    private String requestType;
}
