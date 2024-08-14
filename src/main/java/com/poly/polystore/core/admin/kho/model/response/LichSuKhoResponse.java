package com.poly.polystore.core.admin.kho.model.response;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;
import java.time.Instant;

/**
 * DTO for {@link com.poly.polystore.entity.LichSuKho}
 */
@Data
public class LichSuKhoResponse implements Serializable {
    Integer id;
    Integer lichSuKhoId;
    Integer sanPhamChiTietId;
    private Instant thoiGian;
    @NotNull
    private Integer nhanVienId;
    @Size(max = 1)
    private String ghiChu;
    private Boolean deleted;
}