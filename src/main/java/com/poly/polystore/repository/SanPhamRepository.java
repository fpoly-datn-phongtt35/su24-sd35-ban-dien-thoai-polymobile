package com.poly.polystore.repository;

import com.poly.polystore.entity.SanPham;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface SanPhamRepository extends JpaRepository<SanPham, Integer> {
//    List<SanPham> findAllByDeletedIsFalse();
}