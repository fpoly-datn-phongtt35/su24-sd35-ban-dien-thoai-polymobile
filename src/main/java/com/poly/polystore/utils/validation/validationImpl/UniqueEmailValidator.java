package com.poly.polystore.utils.validation.validationImpl;

import com.poly.polystore.core.common.login.service.TaiKhoanService;
import com.poly.polystore.utils.validation.UniqueEmail;
import com.poly.polystore.utils.validation.UniquePhoneNumber;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String>  {

    private final TaiKhoanService taikhoanService;

    @Override
    public boolean isValid(String phoneNumber, ConstraintValidatorContext constraintValidatorContext) {
        if (phoneNumber == null || phoneNumber.isEmpty()) {
            return true;
        }
        return !taikhoanService.existsBySoDienThoai(phoneNumber);
    }
}
