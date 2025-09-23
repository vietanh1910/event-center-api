package com.fpt.etc.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fpt.etc.entity.MomoCreatePaymentResponse;
import com.fpt.etc.entity.MomoOptionProperties;
import com.fpt.etc.entity.OrderInfoModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MomoService {

    private final MomoOptionProperties momoConfig;
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper mapper = new ObjectMapper();

    public MomoCreatePaymentResponse createPayment(OrderInfoModel model) throws Exception {
        String orderId = String.valueOf(System.currentTimeMillis());
        model.setOrderId(orderId);
        model.setOrderInfo("Khách hàng: " + model.getFullName() + ". Nội dung: " + model.getOrderInfo());

        String rawHash = "partnerCode=" + momoConfig.getPartnerCode()
                + "&accessKey=" + momoConfig.getAccessKey()
                + "&requestId=" + orderId
                + "&amount=" + model.getAmount()
                + "&orderId=" + orderId
                + "&orderInfo=" + model.getOrderInfo()
                + "&returnUrl=" + momoConfig.getReturnUrl()
                + "&notifyUrl=" + momoConfig.getNotifyUrl()
                + "&extraData=" + model.getExtraData();

        String signature = hmacSHA256(rawHash, momoConfig.getSecretKey());

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("partnerCode", momoConfig.getPartnerCode());
        requestBody.put("accessKey", momoConfig.getAccessKey());
        requestBody.put("requestId", orderId);
        requestBody.put("orderId", orderId);
        requestBody.put("orderInfo", model.getOrderInfo());
        requestBody.put("amount", model.getAmount());
        requestBody.put("returnUrl", momoConfig.getReturnUrl());
        requestBody.put("notifyUrl", momoConfig.getNotifyUrl());
        requestBody.put("extraData", model.getExtraData());
        requestBody.put("requestType", momoConfig.getRequestType());
        requestBody.put("signature", signature);
        requestBody.put("lang", "vi");

        String responseStr = restTemplate.postForObject(momoConfig.getMomoApiUrl(), requestBody, String.class);
        return mapper.readValue(responseStr, MomoCreatePaymentResponse.class);
    }

    private String hmacSHA256(String data, String key) throws Exception {
        Mac hmac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        hmac.init(secretKey);
        byte[] bytes = hmac.doFinal(data.getBytes(StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) sb.append(String.format("%02x", b));
        return sb.toString();
    }
}

