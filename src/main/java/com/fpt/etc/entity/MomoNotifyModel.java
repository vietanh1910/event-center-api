package com.fpt.etc.entity;

import lombok.Data;

@Data
public class MomoNotifyModel {
    private String partnerCode;
    private String accessKey;
    private String orderId;
    private String requestId;
    private String amount;
    private String orderInfo;
    private String orderType;
    private String transId;
    private String message;
    private String localMessage;
    private String responseTime;
    private String errorCode;
    private String payType;
    private String extraData;
    private String signature;
}

