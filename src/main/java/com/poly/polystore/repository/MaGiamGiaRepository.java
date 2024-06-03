package com.poly.polystore.repository;

import com.poly.polystore.entity.MaGiamGia;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MaGiamGiaRepository extends JpaRepository<MaGiamGia, Integer> {
    MaGiamGia findByCode(String code);
}