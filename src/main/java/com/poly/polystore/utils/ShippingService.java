package com.poly.polystore.utils;

import com.poly.polystore.core.client.api.request.*;
import com.poly.polystore.core.client.api.response.FeeResponse;
import com.poly.polystore.core.client.api.response.OrderGhnReq;
import com.poly.polystore.core.client.api.response.OrderGhnResp;
import com.poly.polystore.entity.DiaChiGiaoHang;
import com.poly.polystore.repository.DiaChiGiaoHangRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ShippingService {
    @Autowired
    private DiaChiGiaoHangRepository diaChiGiaoHangRepository;
    @Value("${ghn.api.url}")
    private String ghnApiUrl;

    @Value("${ghn.api.token}")
    private String ghnApiToken;

    @Value("${ghn.api-order.url}")
    private String ghnApiOrderUrl;
    @Value("${ghn.api-district.url}")
    private String ghnApiDistrictUrl;
    @Value("${ghn.api-ward.url}")
    private String ghnApiWardUrl;
    public FeeResponse calculateShippingFee(FeeRequest feeRequest) {
        Optional<DiaChiGiaoHang> optionalDiaChiGiaoHang = diaChiGiaoHangRepository.findById(Integer.parseInt(feeRequest.getIddiachi()));
        if(optionalDiaChiGiaoHang.isPresent()) {
            feeRequest.setTo_ward_code(optionalDiaChiGiaoHang.get().getStreet());
            feeRequest.setTo_district_id(Integer.parseInt(optionalDiaChiGiaoHang.get().getWard()));
        }
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        List<FeeRequest.Detail> items = new ArrayList<>();
        FeeRequest.Detail detail = new FeeRequest.Detail();
        detail.setName("abc");
        detail.setQuantity(1);
        detail.setWeight(1000);
        items.add(detail);
        feeRequest.setItems(items);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Token", ghnApiToken);
        headers.set("ShopId", "192777");
        HttpEntity<FeeRequest> entity = new HttpEntity<>(feeRequest, headers);
        ResponseEntity<FeeResponse> response = restTemplate.exchange(ghnApiUrl, HttpMethod.POST, entity, FeeResponse.class);
        return response.getBody();
    }

    public OrderGhnResp createOrderGHN(OrderGhnReq orderGhnReq){
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Token", ghnApiToken);
        headers.set("ShopId", "192777");
        HttpEntity<OrderGhnReq> entity = new HttpEntity<>(orderGhnReq, headers);
        ResponseEntity<OrderGhnResp> response = restTemplate.exchange(ghnApiOrderUrl, HttpMethod.POST, entity, OrderGhnResp.class);
        return response.getBody();
    }

    public DistrictResp getDistrictByDistrictID(DistrictReq districtReq,int districtID){
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Token", ghnApiToken);
        HttpEntity<DistrictReq> entity = new HttpEntity<>(districtReq, headers);
        ResponseEntity<List<DistrictResp>> response = restTemplate.exchange(ghnApiDistrictUrl, HttpMethod.POST, entity, new ParameterizedTypeReference<List<DistrictResp>>(){});
        List<DistrictResp> data = response.getBody();
        assert data != null;
        return data.stream().filter(n -> n.getDistrictID() == districtID).toList().get(0);
    }

    public WardResp getWardByWardID(WardReq wardReq,int wardCode){
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Token", ghnApiToken);
        HttpEntity<WardReq> entity = new HttpEntity<>(wardReq, headers);
        ResponseEntity<List<WardResp>> response = restTemplate.exchange(ghnApiWardUrl, HttpMethod.POST, entity, new ParameterizedTypeReference<List<WardResp>>(){});
        List<WardResp> data = response.getBody();
        assert data != null;
        return data.stream().filter(n -> n.getWardCode() == wardCode).toList().get(0);
    }
}
