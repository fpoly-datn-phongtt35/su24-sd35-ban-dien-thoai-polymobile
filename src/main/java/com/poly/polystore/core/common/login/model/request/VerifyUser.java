package com.poly.polystore.core.common.login.model.request;

import com.poly.polystore.utils.validation.UniqueEmail;
import com.poly.polystore.utils.validation.UniquePhoneNumber;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VerifyUser{
    @NotBlank(message = "Tên không được để trống")
    String ten;
    @Pattern(regexp = "(84|0[3|5|7|8|9])+([0-9]{8})",message = "Số điện thoại không đúng định dạng")
    @UniquePhoneNumber
    String soDienThoai;
    @Email(message = "Email không đúng định dạng")
    @UniqueEmail
    String email;
    @Pattern(regexp = "\\d{4}",message = "OTP Chưa chính xác")
    String otp;
}
