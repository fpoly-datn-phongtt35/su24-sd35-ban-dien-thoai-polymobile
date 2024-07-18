package com.poly.polystore.core.admin.san_pham_chi_tiet.cong_nghe_man_hinh.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * DTO for {@link com.poly.polystore.entity.CongNgheManHinh}
 */
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class EditReq implements Serializable {
    @NotNull
    @Positive
    Integer id;
    @NotNull(message = "Tên không được trống")
    @Size(max = 255)
    @NotBlank(message = "Tên không được trống")
    private String ten;
    @Size(max = 255)
    private String link;
    @NotNull
    private Boolean deleted;
}