package com.poly.polystore.core.admin.don_hang.dto;

import lombok.Data;

import java.util.List;

@Data
public class ImeiDTO {
    private List<String> imei;
    private Integer productId;
    private Integer orderDetailId;
    private Integer orderId;
}
