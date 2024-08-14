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
    String maGiamGiaId;
    String tenNguoiNhan;
    String soDienThoai;
    Integer tongSanPham;
    BigDecimal giamVoucher;
    BigDecimal phiGiaoHang;
    BigDecimal tongTienHoaDon;
    Instant thoiGianNhanHang;
    String note;
    String email;
    String hinhThucThanhToan;
    List<String> danhSachImei;

    /**
     * DTO for {@link KhachHang}
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class KhachHangDto implements Serializable {
        Integer id;
        String ten;
        String soDienThoai;
        String email;
    }

}