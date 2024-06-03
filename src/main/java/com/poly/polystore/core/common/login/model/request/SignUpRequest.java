package com.poly.polystore.core.common.login.model.request;

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

