package com.poly.polystore.core.admin.san_pham.model.reponse;

import com.poly.polystore.core.client.models.response.SanPhamProductResponse;
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
public class SanPhamEditResponse implements Serializable {
    Integer id;
    String tenSanPham;
    Integer manHinhCongNgheManHinhId;
    String manHinhDoPhanGiai;
    String manHinhManHinhRong;
    String manHinhDoSangToiDa;
    Integer manHinhMatKinhCamUngId;
    String cameraTruocDoPhanGiai;
    Set<Integer> cameraTruocTinhNangCameraIds;
    String cameraSauDoPhanGiai;
    String cameraSauDenFlash;
    Set<Integer> cameraSauTinhNangCameraIds;
    Integer heDieuHanhVaCpuHeDieuHanhId;
    Integer heDieuHanhVaCpuCpuId;
    String ketNoiMangDiDong;
    String ketNoiSim;
    Set<Integer> ketNoiWifiIds;
    Set<Integer> ketNoiGpsIds;
    Set<Integer> ketNoiBluetoothIds;
    String ketNoiCongSac;
    String ketNoiJackTaiNghe;
    String pinVaSacDungLuongPin;
    String pinVaSacLoaiPin;
    String pinVaSacHoTroSacToiDa;
    Set<Integer> pinVaSacCongNghePinIds;
    String thongTinChungThietKe;
    String thongTinChungChatLieu;
    String thongTinChungKichThuocKhoiLuong;
    List<Integer> khuyenMaiIds;
    Integer seriesId;
    String ram;
    String thoiGianBaoHanh;
    SanPhamRepository.TrangThai trangThai;
    Integer stt;
    Set<Integer> sanPhamChiTietMauSacIds;
    Set<Integer> thongTinChungTinhNangDacBietIds;
    Set<String> sanPhamChiTietRoms;
    Set<SanPhamChiTiet> sanPhamChiTiet;


    @AllArgsConstructor
    @Getter
    @Setter
    @NoArgsConstructor
    @Builder
    public static class SanPhamChiTiet implements Serializable {
        Integer id;
        List<SanPhamChiTiet.KhuyenMai> khuyenMai;
        List<SanPhamChiTiet.Anh> anh;
        SanPhamChiTiet.MauSac mauSac;
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