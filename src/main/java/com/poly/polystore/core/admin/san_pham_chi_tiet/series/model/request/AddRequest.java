package com.poly.polystore.core.admin.san_pham_chi_tiet.series.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;

/**
 * DTO for {@link com.poly.polystore.entity.Series}
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
    final Boolean deleted=false;
}