package com.fpt.etc.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MomoCreatePaymentResponse {
    private String requestId;
    private int errorCode;
    private String orderId;
    private String message;
    private String localMessage;
    private String requestType;
    private String payUrl;
    private String signature;
    private String qrCodeUrl;
    private String deeplink;
    private String deeplinkWebInApp;
}
