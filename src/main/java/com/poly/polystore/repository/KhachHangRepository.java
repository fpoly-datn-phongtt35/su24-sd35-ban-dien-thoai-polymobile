package com.poly.polystore.repository;

import com.poly.polystore.Response.KhachHangRepose;
import com.poly.polystore.entity.KhachHang;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface KhachHangRepository extends JpaRepository<KhachHang, Integer> {

      @Query(value = """
		  select k.ID as Id_KhachHang,
              k.Ten as TenKhachHang,
              k.Ngay_Sinh as NgaySinh,
              k.Email as Email,
              k.GioiTinh as GioiTinh,
              k.So_Dien_Thoai as SDT,
              k.TrangThai as TrangThai
              from KHACH_HANG K
			        join DIA_CHI_GIAO_HANG D
			        ON K.ID = D.ID
              """,nativeQuery = true)
  List<KhachHangRepose> getAllKhachHang();
    }