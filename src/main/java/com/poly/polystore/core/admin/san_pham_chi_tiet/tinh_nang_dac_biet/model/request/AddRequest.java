package com.poly.polystore.core.admin.san_pham_chi_tiet.tinh_nang_dac_biet.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;

/**
 * DTO for {@link com.poly.polystore.entity.TinhNangDacBiet}
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
    final Boolean deleted=false;
}