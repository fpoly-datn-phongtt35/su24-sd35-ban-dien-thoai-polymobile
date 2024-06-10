package com.poly.polystore.core.admin.san_pham_chi_tiet.mat_kinh_cam_ung.model.response;

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
 * DTO for {@link com.poly.polystore.entity.MatKinhCamUng}
 */
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class Response implements Serializable {
    @Positive
    private Integer id;
    @NotNull
    @Size(max = 255)
    @NotBlank
    private String ten;
    @Size(max = 255)
    @URL
    private String link;
    private Boolean deleted;
}