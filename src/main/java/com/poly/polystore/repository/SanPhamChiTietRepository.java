package com.poly.polystore.repository;

import com.poly.polystore.entity.SanPhamChiTiet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SanPhamChiTietRepository extends JpaRepository<SanPhamChiTiet, Integer> {
//    @Query("SELECT sp FROM SanPhamChiTiet.imeis")
//    public List<String> findAllImeiById(Integer id);
}