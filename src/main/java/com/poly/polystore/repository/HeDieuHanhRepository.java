package com.poly.polystore.repository;

import com.poly.polystore.entity.HeDieuHanh;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Arrays;
import java.util.Collection;

public interface HeDieuHanhRepository extends JpaRepository<HeDieuHanh, Integer> {
    Collection<HeDieuHanh> findAllByDeletedIsFalse();
}