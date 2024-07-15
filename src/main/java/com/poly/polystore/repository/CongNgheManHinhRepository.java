package com.poly.polystore.repository;

import com.poly.polystore.entity.CongNgheManHinh;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Arrays;
import java.util.List;

public interface CongNgheManHinhRepository extends JpaRepository<CongNgheManHinh, Integer> {
    List<CongNgheManHinh> findAllByDeletedIsFalseOrderByIdDesc();
}