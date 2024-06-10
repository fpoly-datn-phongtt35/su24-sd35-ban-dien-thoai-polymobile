package com.poly.polystore.core.admin.san_pham_chi_tiet.mau_sac.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.io.Serializable;

/**
 * DTO for {@link com.poly.polystore.entity.MauSac}
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Data
public class MauSacDto implements Serializable {
    @Positive
    Integer id;
    @NotBlank
    @Pattern(regexp = "\\S+")
    String ma;
    @NotBlank
    String ten;
    Boolean trangThai;
}