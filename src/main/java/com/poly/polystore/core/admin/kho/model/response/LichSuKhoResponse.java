package com.poly.polystore.core.admin.kho.model.response;

import lombok.*;

import java.io.Serializable;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * DTO for {@link com.poly.polystore.entity.LichSuKho}
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class LichSuKhoResponse implements Serializable {
    private Integer id;
    private String thoiGian;
    private String ghiChu;
    private Boolean deleted;
    List<LichSuKhoChiTiet> lichSuKhoChiTiets;
    private TaiKhoan taiKhoan;

    public LichSuKhoResponse(Integer id, Instant thoiGian, String ghiChu, Boolean deleted, Integer taiKhoanId,String ten) {
        this.id = id;
        this.thoiGian = thoiGian.atZone(ZoneId.of("Asia/Ho_Chi_Minh")).format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
        this.ghiChu = ghiChu;
        this.deleted = deleted;
        this.taiKhoan=new TaiKhoan(taiKhoanId,ten);
    }



    /**
     * DTO for {@link com.poly.polystore.entity.ChiTietLichSuKho}
     */
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter @Setter
    public static class LichSuKhoChiTiet {
        Integer idSanPham, soLuong;
        String tenSanPham;

    }
    /**
     * DTO for {@link com.poly.polystore.entity.TaiKhoan}
     */
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter @Setter
    public static class TaiKhoan {
        Integer idTaiKhoan;
        String ten;
    }
}