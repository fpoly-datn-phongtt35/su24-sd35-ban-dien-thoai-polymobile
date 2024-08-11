package com.poly.polystore.repository;

import com.poly.polystore.entity.GioHang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GioHangRepository extends JpaRepository<GioHang, Integer> {
    @Query(value = "select * from GIO_HANG g where g.ID_Tai_khoan = ?1",nativeQuery = true)
    List<GioHang> findByIdTaiKhoan(Integer id);
}
