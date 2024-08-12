package com.poly.polystore.repository;

import com.poly.polystore.entity.HoaDonChiTiet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface HoaDonChiTietRepository extends JpaRepository<HoaDonChiTiet, Integer> {
    @Query("Select hd from HoaDonChiTiet hd where hd.hoaDon.id = :id")
    List<HoaDonChiTiet> findByHoaDonId(Integer id);
}
