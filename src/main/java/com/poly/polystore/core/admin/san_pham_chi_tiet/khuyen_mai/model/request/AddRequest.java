package com.poly.polystore.core.admin.san_pham_chi_tiet.khuyen_mai.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.constraints.URL;
import org.hibernate.validator.constraints.UniqueElements;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

import java.util.Date;

/**
 * DTO for {@link com.poly.polystore.entity.KhuyenMai}
 */
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class AddRequest implements Serializable {
    @NotNull
    @Size(max = 255)
    @NotBlank
    private String ten;
    @Size(max = 255)
    private String link;
    @DateTimeFormat(pattern = "dd-MM-YYYY hh:mm A")
    private Date  thoiGianBatDau;
    @DateTimeFormat(pattern = "dd-MM-YYYY hh:mm A")
    private Date  thoiGianKetThuc;
    final Boolean deleted = false;
}