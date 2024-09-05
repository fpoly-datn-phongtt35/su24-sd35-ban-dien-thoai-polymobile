package com.poly.polystore.core.client.dto;

import com.poly.polystore.core.admin.san_pham.model.reponse.SanPhamEditResponse;
import com.poly.polystore.entity.HoaDon;
import com.poly.polystore.entity.Imei;
import com.poly.polystore.entity.PhieuGiamGia;
import com.poly.polystore.entity.SanPhamChiTiet;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
@Getter
@Setter
public class HoaDonChiTietDTO {
    private Integer id;
    private BigDecimal giaGoc;// Là giá chưa áp dụng đợt giảm giá

    private BigDecimal giaBan;// Là giá sau khi áp dụng đợt giảm giá
    private PhieuGiamGia dotGiamGia;

    private String imei;

    private SanPhamChiTietDTO sanPhamChiTiet;
}
