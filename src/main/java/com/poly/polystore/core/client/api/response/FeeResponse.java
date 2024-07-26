package com.poly.polystore.core.client.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeeResponse {
    private int code;
    private String message;
    private Data data;

    // getters and setters
    @lombok.Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Data {
        private int total;
    }
}
