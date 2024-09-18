package com.poly.polystore.core.client.models.response;

import com.poly.polystore.entity.KhuyenMai;
import com.poly.polystore.entity.PhieuGiamGia;
import com.poly.polystore.entity.SanPham;
import com.poly.polystore.repository.SanPhamRepository;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;

/**
 * DTO for {@link com.poly.polystore.entity.SanPham}
 */
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)

public class SanPhamResponse implements Serializable {
    Integer id;
    String anhUrl;
    String anhName;
    String tenSanPham;
    Set<SanPhamChiTiet> sanPhamChiTiet;
    String thoiGianBaoHanh;
    SanPhamRepository.TrangThai trangThai;


    /**
     * DTO for {@link com.poly.polystore.entity.SanPhamChiTiet}
     */
    @AllArgsConstructor
    @Getter
    @Setter
    @NoArgsConstructor
    @Builder
    public static class SanPhamChiTiet implements Serializable {
        Integer id;
        String rom;
        BigDecimal giaBan;
        SanPhamRepository.TrangThai trangThai;
        private PhieuGiamGia dotGiamGia;
        private MauSac mauSac;


        /**
         * DTO for {@link com.poly.polystore.entity.PhieuGiamGia}
         */
        @AllArgsConstructor
        @Getter
        @Setter
        @NoArgsConstructor
        @Builder
        public static class PhieuGiamGia implements Serializable {
            BigDecimal giaTriGiam;
            String donvi;
            Instant thoiGianKetThuc;
            Instant thoiGianBatDau;
            Boolean deleted;
        }
        /**
         * DTO for {@link com.poly.polystore.entity.MauSac}
         */
        @AllArgsConstructor
        @Getter
        @Setter
        @NoArgsConstructor
        @Builder
        public static class MauSac implements Serializable {
            Integer id;
            String ten;
            String ma;
        }



    }

}