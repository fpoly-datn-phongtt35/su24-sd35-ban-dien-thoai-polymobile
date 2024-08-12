package com.poly.polystore.core.client.api.response;

import com.poly.polystore.core.client.api.request.FeeRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderGhnReq {
    private String to_name;
    private String to_phone;
    private String to_address;
    private String to_ward_name;
    private String to_district_name;
    private int weight = 1000;
    private int length = 100;
    private int width = 100;
    private int height = 100;
    private int service_type_id = 5;
    private int payment_type_id = 1;
    private String required_note = "CHOXEMHANGKHONGTHU";
    private List<OrderGhnReq.Detail> items;
    @lombok.Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Detail {
        private int quantity;
        private String name;
        private int weight;
    }
}
