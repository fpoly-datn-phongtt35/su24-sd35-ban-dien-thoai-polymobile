package com.poly.polystore.core.client.models.response;

import com.poly.polystore.repository.SanPhamRepository;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * DTO for {@link com.poly.polystore.entity.SanPham}
 */
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Builder
public class SanPhamProductResponse implements Serializable {
    Integer id;
    String tenSanPham;
    Set<SanPhamChiTiet> sanPhamChiTiet;
    String thoiGianBaoHanh;
    SanPhamRepository.TrangThai trangThai;
    String moTa;

    /**
     * DTO for {@link com.poly.polystore.entity.BinhLuan}
     */
    @AllArgsConstructor
    @Getter
    @Setter
    @NoArgsConstructor
    @Builder
    public static class BinhLuan implements Serializable {
        Integer id;
        String binhLuanChiTiet;
    }

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
        List<KhuyenMai> khuyenMai;
        List<Anh> anh;
        MauSac mauSac;
        String rom;
        BigDecimal giaBan;
        SanPhamRepository.TrangThai trangThai;
        PhieuGiamGia dotGiamGia;
        /**
         * DTO for {@link com.poly.polystore.entity.KhuyenMai}
         */
        @AllArgsConstructor
        @Getter
        @Setter
        @NoArgsConstructor
        @Builder
        public static class KhuyenMai implements Serializable {
            @NotNull
            @Size(max = 255)
            String ten;
            @NotNull
            @Size(max = 255)
            String link;
            Date thoiGianBatDau;
            Date thoiGianKetThuc;
            Boolean deleted;
        }

        /**
         * DTO for {@link com.poly.polystore.entity.Anh}
         */
        @AllArgsConstructor
        @Getter
        @Setter
        @NoArgsConstructor
        @Builder
        public static class Anh implements Serializable {
            String url;
            String name;
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
            String ten;
            String ma;
        }


        }
        /**
         * DTO for {@link com.poly.polystore.entity.PhieuGiamGia}
         */
        @AllArgsConstructor
        @Getter
        @Setter
        @NoArgsConstructor
        @Builder
        public static class PhieuGiamGia implements Serializable {
            String code;
            BigDecimal giaTriGiam;
            String donvi;
            Instant thoiGianKetThuc;
            Instant thoiGianBatDau;
            Boolean deleted;
        }

}