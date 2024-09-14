package com.poly.polystore.utils;

import com.poly.polystore.core.client.api.request.FeeRequest;
import com.poly.polystore.core.client.api.response.FeeResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Component
public class ShippingService {
    @Value("${ghn.api.url}")
    private String ghnApiUrl;

    @Value("${ghn.api.token}")
    private String ghnApiToken;

    public FeeResponse calculateShippingFee(FeeRequest feeRequest) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        List<FeeRequest.Detail> items = new ArrayList<>();
        FeeRequest.Detail detail = new FeeRequest.Detail();
        detail.setName("abc");
        detail.setQuantity(1);
        detail.setWeight(1000);
        items.add(detail);
        feeRequest.setItems(items);
        feeRequest.setService_type_id(2);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Token", ghnApiToken);
        headers.set("ShopId", "192777");
        HttpEntity<FeeRequest> entity = new HttpEntity<>(feeRequest, headers);
        ResponseEntity<FeeResponse> response = restTemplate.exchange(ghnApiUrl, HttpMethod.POST, entity, FeeResponse.class);
        return response.getBody();
    }
}
