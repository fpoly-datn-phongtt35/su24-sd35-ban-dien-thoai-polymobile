package com.poly.polystore.repository;

import com.poly.polystore.entity.HoaDon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface HoaDonRepository extends JpaRepository<HoaDon, Integer> {
    @Query(value ="SELECT MONTH(o.Created_at) as month, YEAR(o.Created_at) as year, " +
            "SUM(o.tong_Tien_hoa_Don) as totalRevenue FROM HOA_DON o GROUP BY MONTH(o.Created_at), YEAR(o.Created_at) ORDER BY MONTH(o.Created_at)", nativeQuery = true)
    List<Object[]> calculateMonthlyRevenue();
}