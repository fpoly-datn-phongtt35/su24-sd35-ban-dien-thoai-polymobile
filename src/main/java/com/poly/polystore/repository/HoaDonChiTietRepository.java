package com.poly.polystore.repository;

import com.poly.polystore.entity.HoaDonChiTiet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HoaDonChiTietRepository extends JpaRepository<HoaDonChiTiet, Integer> {
    @Query("Select hd from HoaDonChiTiet hd where hd.hoaDon.id = :id")
    List<HoaDonChiTiet> findByHoaDonId(Integer id);

    @Query(value = """

                    Select hd.*
                                                from HOA_DON_CHI_TIET as hd
                                                where hd.id =:orderDetailId
                                                  and hd.SAN_PHAM_CHI_TIET_ID =:productId
            """, nativeQuery = true)
    HoaDonChiTiet findByHoaDonCTAndSPCTId(@Param("orderDetailId") Integer orderDetailId, @Param("productId") Integer productId);
}
