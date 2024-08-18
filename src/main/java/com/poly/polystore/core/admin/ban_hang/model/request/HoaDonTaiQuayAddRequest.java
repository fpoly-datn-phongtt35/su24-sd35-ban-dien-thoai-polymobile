package com.poly.polystore.core.admin.ban_hang.model.request;

import com.poly.polystore.Constant.TRANGTHAIDONHANG;
import com.poly.polystore.entity.HoaDon;
import com.poly.polystore.entity.HoaDonChiTiet;
import com.poly.polystore.entity.KhachHang;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

/**
 * DTO for {@link HoaDon}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor

public class HoaDonTaiQuayAddRequest implements Serializable {
    KhachHangDto khachHang;
    String maGiamGia;
    String tenNguoiNhan;
    String soDienThoai;
    Integer tongSanPham;
    BigDecimal giamVoucher;
    BigDecimal phiGiaoHang;
    TRANGTHAIDONHANG trangThai;
    String hinhThucGiaoHang;
    Instant createdAt;
    Boolean deleted;
    String lastModifiedBy;
    BigDecimal tongTienHoaDon;
    Instant thoiGianNhanHang;
    String note;
    String email;
    String hinhThucThanhToan;
    String trangThaiThanhToan;
    List<HoaDonChiTietDto> hoaDonChiTiets;

    /**
     * DTO for {@link KhachHang}
     */
    @Value
    public static class KhachHangDto implements Serializable {
        Integer id;
        String ten;
        String soDienThoai;
        String email;
    }

    /**
     * DTO for {@link HoaDonChiTiet}
     */
    @Value
    public static class HoaDonChiTietDto implements Serializable {
        Integer id;
        BigDecimal giaGoc;
        BigDecimal giaBan;
        Integer dotGiamGiaId;
        String imeiImei;
    }
}