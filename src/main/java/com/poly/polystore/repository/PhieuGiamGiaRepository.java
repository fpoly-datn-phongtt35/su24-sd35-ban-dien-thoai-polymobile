package com.poly.polystore.repository;

import com.poly.polystore.entity.PhieuGiamGia;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhieuGiamGiaRepository extends JpaRepository<PhieuGiamGia, Integer> {
    PhieuGiamGia findByCode(String code);
}