package com.poly.polystore.repository;

import com.poly.polystore.entity.CongNgheManHinh;
import com.poly.polystore.entity.KhuyenMai;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface KhuyenMaiRepository extends JpaRepository<KhuyenMai, Integer> {
    List<KhuyenMai> findAllByDeletedIsFalse();
}