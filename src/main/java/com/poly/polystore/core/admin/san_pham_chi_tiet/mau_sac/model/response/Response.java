package com.poly.polystore.core.admin.san_pham_chi_tiet.mau_sac.model.response;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;

import java.io.Serializable;

/**
 * DTO for {@link com.poly.polystore.entity.MauSac}
 */
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class Response implements Serializable {
    private Integer id;
    private String ten;
    private String ma;
    private Boolean deleted;
}