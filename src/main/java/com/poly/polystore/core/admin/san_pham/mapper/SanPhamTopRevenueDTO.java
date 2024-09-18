package com.poly.polystore.core.admin.san_pham.mapper;

import jakarta.persistence.ColumnResult;
import jakarta.persistence.ConstructorResult;
import jakarta.persistence.SqlResultSetMapping;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SanPhamTopRevenueDTO implements Serializable {

    private Integer id;

    private String anhSanPham;
    private String tenSanPham;
    private String series;
    private String danhSachMauSac;
    private String danhSachRom;
    private Integer soLuong;
    private String trangThai;
    private BigDecimal doanhthu;
    private String formattedRevenue;

}