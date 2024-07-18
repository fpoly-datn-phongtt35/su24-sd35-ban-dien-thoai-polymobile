package com.poly.polystore.repository;

import com.poly.polystore.entity.MauSac;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MauSacRepository extends JpaRepository<MauSac, Integer> {
    List<MauSac> findAllByDeletedIsFalseOrderByIdDesc();
}