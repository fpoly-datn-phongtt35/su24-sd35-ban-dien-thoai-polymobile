package com.poly.polystore.core.client.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.poly.polystore.Constant.TRANGTHAIDONHANG;
import com.poly.polystore.entity.HoaDonChiTiet;
import com.poly.polystore.entity.KhachHang;
import com.poly.polystore.entity.LichSuHoaDon;
import com.poly.polystore.entity.NhanVien;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Nationalized;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class HoaDonDTO {
    private Integer id;

    private String idKhachHang;

    private String maGiamGia;

    private String tenNguoiNhan;
    private String soDienThoai;

    private String diaChi;

    private Integer tongSanPham;

    private BigDecimal giamVoucher;

    private BigDecimal phiGiaoHang;
    private TRANGTHAIDONHANG trangThai;

    private String hinhThucGiaoHang;

    private Instant createdAt;
    private Boolean deleted;

    private String lastModifiedBy;

    private BigDecimal tongTienHoaDon;

    private Instant thoiGianNhanHang;

    private String note;
    private String email;
    private String hinhThucThanhToan;
    private String ma;

    private String trangThaiThanhToan;
    private String leadingItem;
    private String imageUrl;
    private List<HoaDonChiTietDTO> hoaDonChiTiets;
    private List<LichSuHoaDonDTO> lichSuHoaDons;
}
