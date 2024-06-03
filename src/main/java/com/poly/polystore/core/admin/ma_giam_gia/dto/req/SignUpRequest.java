package com.poly.polystore.core.admin.ma_giam_gia.dto.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequest {
    String ten;
    String soDienThoai;
    String email;
    String matKhau;
}

