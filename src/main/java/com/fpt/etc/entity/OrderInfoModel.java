package com.fpt.etc.entity;

import lombok.Data;

@Data
public class OrderInfoModel {
    private String fullName;
    private String amount;
    private String orderInfo;
    private String orderId;
    private String extraData;
}
