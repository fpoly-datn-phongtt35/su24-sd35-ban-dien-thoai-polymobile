package com.poly.polystore.core.admin.ban_hang.model.response;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

/**
 * DTO for {@link com.poly.polystore.entity.KhachHang}
 */
@Data
public class CustomerDto implements Serializable {
    Integer id;
    String email;
   String ten;
    String soDienThoai;
    Integer deleted;
}