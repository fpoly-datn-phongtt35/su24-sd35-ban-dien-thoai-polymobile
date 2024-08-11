package com.poly.polystore.core.client.api;

import com.poly.polystore.core.client.api.request.FeeRequest;
import com.poly.polystore.core.client.api.response.FeeResponse;
import com.poly.polystore.utils.ShippingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/client/shipping")
@RequiredArgsConstructor
public class ShippingApi {
    private final ShippingService shippingService;
    @PostMapping("/shipping-fee")
    public ResponseEntity<?> shippingFee(@RequestBody FeeRequest feeRequest){
        FeeResponse feeResponse = shippingService.calculateShippingFee(feeRequest);
        return ResponseEntity.ok(feeResponse);
    }
}
