package com.poly.polystore.repository;

import com.poly.polystore.Response.KhachHangRepose;
import com.poly.polystore.entity.KhachHang;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface KhachHangRepository extends JpaRepository<KhachHang, Integer> {

  @Query(value = """
      select k.ID as Id_KhachHang,
              k.MaKhachHang as MaKhachHang,
                k.Ten as TenKhachHang,
                k.Ngay_Sinh as NgaySinh,
                k.Email as EmailKhachHang,
                k.GioiTinh as GioiTinh,
                k.So_Dien_Thoai as SDT,
                k.Trang_Thai as TrangThai
                from KHACH_HANG K
             join DIA_CHI_GIAO_HANG D
             ON K.ID = D.ID
                """, nativeQuery = true)
  List<KhachHangRepose> getAllKhachHang();


@Query(value = """
  select k.ID as Id_KhachHang,
              k.MaKhachHang as MaKhachHang,
                k.Ten as TenKhachHang,
                k.Ngay_Sinh as NgaySinh,
                k.Email as EmailKhachHang,
                k.GioiTinh as GioiTinh,
                k.So_Dien_Thoai as SDT,
                k.Trang_Thai as TrangThai
                from KHACH_HANG K
            JOIN DIA_CHI_GIAO_HANG d ON k.ID = d.ID
            JOIN Hoa_Don h ON k.ID = h.id_khach_hang
        GROUP BY
            k.ID, k.MaKhachHang, k.Ten, k.Ngay_Sinh, k.Email, k.GioiTinh, k.So_Dien_Thoai, k.TrangThai
        HAVING 
            COUNT(CASE WHEN h.trang_Thai = '6' THEN 1 END) >= :soLanHuy
    """, nativeQuery = true)
List<KhachHangRepose> getAllKhachHang_den(Integer soLanHuy);

}