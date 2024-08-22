package com.poly.polystore.core.common.login.model.request;

import jakarta.validation.constraints.Pattern;

public class VerifyUser extends SignUpRequest{
    @Pattern(regexp = "/d{4}",message = "OTP Chưa chính xác")
    String otp;
}
