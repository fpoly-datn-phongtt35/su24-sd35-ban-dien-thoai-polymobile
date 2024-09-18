package com.poly.polystore.core.client.api.response;

import com.poly.polystore.entity.MaGiamGia;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MagiamgiaResp {
    private MaGiamGia maGiamGia;
    private double total;
    private double giamgia;
}
