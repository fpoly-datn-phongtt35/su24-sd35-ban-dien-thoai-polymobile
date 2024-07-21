package com.poly.polystore.repository;

import com.poly.polystore.entity.GioHang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GioHangRepository extends JpaRepository<GioHang, Integer> {
    @Query("SELECT GioHang from GioHang where idKhachHang.id = :id")
    List<GioHang> findByIdKhachHang(Integer id);
}
