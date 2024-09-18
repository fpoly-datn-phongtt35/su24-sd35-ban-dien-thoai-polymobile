package com.poly.polystore.repository;

import com.poly.polystore.entity.PhieuGiamGia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PhieuGiamGiaRepository extends JpaRepository<PhieuGiamGia, Integer> {
    PhieuGiamGia findByCode(String code);
    @Query("""
select p from PhieuGiamGia  p 
where p.id in :spctID
""")
    List<PhieuGiamGia> findAllBySPCTID(@Param("spctID") List<Integer> spctID);
}