package com.poly.polystore.repository;

import com.poly.polystore.entity.MaGiamGia;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MaGiamGiaRepository extends JpaRepository<MaGiamGia, Integer> {
    MaGiamGia findByCode(String code);
    Page<MaGiamGia> findByCodeLike(String code, Pageable pageable);
}