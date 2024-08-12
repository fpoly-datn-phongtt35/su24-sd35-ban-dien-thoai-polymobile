package com.poly.polystore.core.client.api.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeeRequest {
    private String to_ward_code;
    private int to_district_id;
    private int service_type_id;
    private int weight;
    private int quantity;
    private List<Detail> items;
    private String iddiachi;
    @lombok.Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Detail {
        private int quantity;
        private String name;
        private int weight;
    }
}
