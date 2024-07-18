package com.poly.polystore.core.admin.san_pham_chi_tiet.series.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * DTO for {@link com.poly.polystore.entity.Series}
 */
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class ImportReq implements Serializable {
    Integer id;
    @NotNull(message = "Tên không được trống")
    @Size(max = 255)
    @NotBlank(message = "Tên không được trống")
    private String ten;
    private Boolean deleted;
}