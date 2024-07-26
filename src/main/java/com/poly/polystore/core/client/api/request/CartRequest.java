package com.poly.polystore.core.client.api.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartRequest {
    private int idSanPhamChiTiet;
    private int quantity;
    private String action;
}
