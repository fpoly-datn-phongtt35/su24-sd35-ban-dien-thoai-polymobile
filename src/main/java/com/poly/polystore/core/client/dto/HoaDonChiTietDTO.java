package com.poly.polystore.core.client.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.poly.polystore.core.admin.don_hang.dto.ImeiDTO;
import com.poly.polystore.entity.Imei;
import com.poly.polystore.entity.PhieuGiamGia;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class HoaDonChiTietDTO {
    private Integer id;
    private BigDecimal giaGoc;// Là giá chưa áp dụng đợt giảm giá

    private BigDecimal giaBan;// Là giá sau khi áp dụng đợt giảm giá
    private PhieuGiamGia dotGiamGia;

    private String imei;

    private SanPhamChiTietDTO sanPhamChiTiet;
}
