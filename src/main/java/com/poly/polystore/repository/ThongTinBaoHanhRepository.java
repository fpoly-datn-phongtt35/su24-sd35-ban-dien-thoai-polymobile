package com.poly.polystore.repository;

import com.poly.polystore.core.client.models.request.WarrantyDTO;
import com.poly.polystore.entity.KhachHang;
import com.poly.polystore.entity.ThongTinBaoHanh;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface ThongTinBaoHanhRepository extends JpaRepository<ThongTinBaoHanh, Integer> {
    public interface WarrantyProjection {
        Long getID();
        KhachHang getIdKhachHang();
        Date getThoiGianBH();
        Date getNgayBD(); // Giả sử ngày bắt đầu là kiểu Date
        String getTrangThai();
        Long getSANPHAMID();
        String getTENSP();
        String getTenKH();
        String getSdt();
        String getImei();
        String getReason();
    }
    @Query(value = """
select
    TTBH.id as ID,
    TTBH.idKhachHang as idKhachHang,
    TTBH.thoiGianBaoHanh as thoiGianBH,
    TTBH.ngayBatDau as ngayBD,
    TTBH.trangThai as trangThai,
    SP.id AS SANPHAMID,
    SP.tenSanPham as TENSP,
    KH.ten as tenKH,
    KH.soDienThoai as sdt,
    TTBH.imei as imei
from ThongTinBaoHanh TTBH
JOIN Imei IM
ON TTBH.imei = IM.imei
JOIN SanPhamChiTiet SPCT
ON IM.sanPhamChiTiet.id = SPCT.id
JOIN SanPham SP
ON SPCT.sanPham.id = SP.id
JOIN KhachHang KH
ON TTBH.idKhachHang.id = KH.id
WHERE ((:IDKH IS NULL OR TTBH.idKhachHang.id = :IDKH) 
AND (COALESCE(:EMEI, '') = '' OR IM.imei = :EMEI)
AND (:PHONE IS NULL OR KH.soDienThoai = :PHONE))

""")
    List<WarrantyProjection> thongTinBaoHanh(@Param("IDKH") Integer IDKH, @Param("EMEI") String EMEI , @Param("PHONE") String PHONE);
}