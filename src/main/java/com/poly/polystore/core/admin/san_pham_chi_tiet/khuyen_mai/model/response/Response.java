package com.poly.polystore.core.admin.san_pham_chi_tiet.khuyen_mai.model.response;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;
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
public class Response implements Serializable {
    private Integer id;
    private String ten;
    private String link;
    private Date  thoiGianBatDau;
    private Date  thoiGianKetThuc;
    private Boolean deleted;
}