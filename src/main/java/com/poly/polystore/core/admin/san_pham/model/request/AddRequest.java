package com.poly.polystore.core.admin.san_pham.model.request;

import com.poly.polystore.repository.SanPhamRepository;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
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
public class AddRequest implements Serializable {
    Integer id;
    String anhName;
    String tenSanPham;
    Integer manHinhCongNgheManHinhId;
    String manHinhDoPhanGiai;
    String manHinhManHinhRong;
    String manHinhDoSangToiDa;
    Integer manHinhMatKinhCamUngId;
    String cameraTruocDoPhanGiai;
    Boolean cameraTruocDenFlash;
    Set<Integer> cameraTruocTinhNangCameraIds;
    String cameraSauDoPhanGiai;
    String cameraSauDenFlash;
    Set<Integer> cameraSauTinhNangCameraIds;
    Integer heDieuHanhVaCpuHeDieuHanhId;
    Integer heDieuHanhVaCpuCpuId;
    String ketNoiMangDiDong;
    String ketNoiSim;
    Set<Integer> ketNoiWifiIds;
    Set<Integer> ketNoiGpIds;
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
    Set<Integer> thongTinChungTinhNangDacBietIds;
    Integer seriesId;
    Set<SanPhamChiTiet> sanPhamChiTiet;
    String ram;
    String thoiGianBaoHanh;
    SanPhamRepository.TrangThai trangThai;
    Integer stt;

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
        Integer mauSacId;
        String rom;
        List<String> anh;
        List<Integer> khuyenMaiIds;
        BigDecimal giaBan;
        BigDecimal giaVon;

    }
}