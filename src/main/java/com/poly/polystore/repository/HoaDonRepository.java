package com.poly.polystore.repository;

import com.poly.polystore.Constant.TRANGTHAIDONHANG;
import com.poly.polystore.entity.HoaDon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface HoaDonRepository extends JpaRepository<HoaDon, Integer> {
    @Query(value = """
    select hd.* from HOA_DON hd
    join KHACH_HANG kh
    on hd.ID_KHACH_HANG = kh.ID
    where kh.id =:IDKH 
    AND (:status IS NULL OR hd.TRANG_THAI =:status)
    AND (:startDate IS NULL OR hd.Created_at >=:startDate)
    AND (:endDate IS NULL OR hd.Created_at <=:endDate)
    AND (:maDH IS NULL OR hd.Ma like concat( :maDH, '%'))
""", nativeQuery = true)
    List<HoaDon> getOrderByKHID(@Param("IDKH") Integer IDKH,
                                @Param("status") String status,
                                @Param("startDate") LocalDate startDate,
                                @Param("endDate") LocalDate endDate,
                                @Param("maDH") String maDH);
    @Query(value ="SELECT MONTH(o.Created_at) as month, YEAR(o.Created_at) as year, " +
            "SUM(o.tong_Tien_hoa_Don) as totalRevenue FROM HOA_DON o GROUP BY MONTH(o.Created_at), YEAR(o.Created_at) ORDER BY MONTH(o.Created_at)", nativeQuery = true)
    List<Object[]> calculateMonthlyRevenue();

    @Query(value = """
    select hd.* from HOA_DON hd
    where hd.id =:orderId
""", nativeQuery = true)
    HoaDon getOrderById(@Param("orderId") Integer orderId);

    List<HoaDon> findHoaDonByTrangThai(TRANGTHAIDONHANG trangthai);

    @Query(value = """
    select hd from HoaDon hd
    join KhachHang kh
    on hd.khachHang.id = kh.id
    where (:IDKH IS NULL OR kh.id =:IDKH) 
    AND (:enumStatus IS NULL OR hd.trangThai =:enumStatus)
    AND (:startDate IS NULL OR hd.createdAt >=:startDate)
    AND (:endDate IS NULL OR hd.createdAt <=:endDate)
    AND (:maDH IS NULL OR hd.id =:maDH)
""")
    Page<HoaDon> findByUserAndFilters(
            @Param("IDKH") Integer IDKH,
            @Param("enumStatus") TRANGTHAIDONHANG enumStatus,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("maDH") String maDH
            , Pageable pageable);
}