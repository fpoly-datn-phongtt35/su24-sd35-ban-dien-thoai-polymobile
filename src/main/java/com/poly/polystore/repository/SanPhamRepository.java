package com.poly.polystore.repository;

import com.poly.polystore.core.admin.san_pham.model.reponse.SanPhamDataTable;
import com.poly.polystore.dto.Select2Response;
import com.poly.polystore.entity.SanPham;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.*;

public interface SanPhamRepository extends JpaRepository<SanPham, Integer>, JpaSpecificationExecutor<SanPham> {
    List<SanPham> findAllByOrderByIdDesc();

    public enum TrangThai {
        IN_STOCK("Có sẵn"),
        OUT_OF_STOCK("Hết hàng"),
        TEMPORARILY_OUT_OF_STOCK("Hết hàng tạm thời"),
        COMING_SOON("Sắp ra mắt"),
        DISCONTINUED("Không kinh doanh");
        private final String displayName;

        TrangThai(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }
    @Query(nativeQuery = true,name = "findAllSanPhamDataTable")
    public List<SanPhamDataTable> findAllSanPhamDataTable();

    @Query("SELECT new com.poly.polystore.dto.Select2Response$Result(spct.id,CONCAT(CONCAT(CONCAT(CONCAT(sp.tenSanPham,' '),spct.rom),' '),spct.mauSac.ten)) " +
            "FROM SanPham sp LEFT JOIN sp.sanPhamChiTiet spct LEFT JOIN spct.mauSac ms " +
            "WHERE LOWER(CONCAT(CONCAT(CONCAT(CONCAT(sp.tenSanPham,' '),spct.rom),' '),spct.mauSac.ten)) LIKE LOWER(CONCAT('%',concat(:searchKey,'%') )) " +
            "OR concat(sp.id,'') like :searchKey OR concat(spct.id,'') like :searchKey")
    Page<Select2Response.Result> findAllSanPhamLike(String searchKey, Pageable pageAble);
    @Query("SELECT new com.poly.polystore.dto.Select2Response$Result(spct.id,CONCAT(CONCAT(CONCAT(CONCAT(sp.tenSanPham,' '),spct.rom),' '),spct.mauSac.ten)) " +
            "FROM SanPham sp LEFT JOIN sp.sanPhamChiTiet spct LEFT JOIN spct.mauSac ms ")
    Page<Select2Response.Result> findAllSanPham( Pageable pageAble);


//    @Query("SELECT new SanPhamDataTableBanHang(sp.id, sp.anh.url, sp.tenSanPham, sp.trangThai,SUM(spct.soLuong)) " +
//            "FROM SanPham sp " +
//            "LEFT JOIN sp.sanPhamChiTiet spct " +
//            "GROUP BY sp.id, sp.tenSanPham, sp.anh.url, sp.trangThai")
//    public List<SanPhamDataTableBanHang> findAllSanPhamDataTableBanHang();
//
//    @Query("SELECT new SanPhamDataTableBanHang(sp.id, sp.anh.url, sp.tenSanPham, sp.trangThai, SUM(spct.soLuong)) " +
//            "FROM SanPham sp " +
//            "LEFT JOIN sp.sanPhamChiTiet spct " +
//            "WHERE (:searchKey IS NULL OR sp.tenSanPham LIKE %:searchKey%)" +
//            "GROUP BY sp.id, sp.tenSanPham, sp.anh.url, sp.trangThai")
//    public List<SanPhamDataTableBanHang> findAllSanPhamDataTableBanHang(@Param("searchKey") String searchKey);
//
//    @Query("SELECT new SanPhamDataTableBanHang(sp.id, sp.anh.url, sp.tenSanPham, sp.trangThai, SUM(spct.soLuong)) " +
//            "FROM SanPham sp " +
//            "LEFT JOIN sp.sanPhamChiTiet spct " +
//            "GROUP BY sp.id, sp.tenSanPham, sp.anh.url, sp.trangThai")
//    public List<SanPhamDetailBanHang> findAllSanPhamDataTableBanHang(Integer id);




//    default SanPhamDataTable findAllSanPhamDataTable(Pageable pageable,String sortBy,String sortOder, String searchKey,Integer draw, EntityManager em) {
//        StringBuilder sqlQuery = new StringBuilder("""
//                WITH DistinctColors AS (
//                                        SELECT sp.ID AS San_Pham_ID, ms.MA AS MA_MAU
//                                        FROM dbo.SAN_PHAM sp
//                                        LEFT JOIN dbo.SAN_PHAM_CHI_TIET spct ON sp.ID = spct.SAN_PHAM_ID
//                                        LEFT JOIN dbo.MAU_SAC ms ON spct.MAU_SAC_ID = ms.ID
//                                        WHERE ms.TEN IS NOT NULL
//                                        GROUP BY sp.ID, ms.MA
//                                    ),
//                                    DistinctROMs AS (
//                                        SELECT sp.ID AS San_Pham_ID, spct.ROM
//                                        FROM dbo.SAN_PHAM sp
//                                        JOIN dbo.SAN_PHAM_CHI_TIET spct ON sp.ID = spct.SAN_PHAM_ID
//                                        WHERE spct.ROM IS NOT NULL
//                                        GROUP BY sp.ID, spct.ROM
//                                    ),
//                                    ColorAggregates AS (
//                                        SELECT San_Pham_ID, STRING_AGG(MA_MAU, ', ') AS Danh_Sach_Mau_Sac
//                                        FROM DistinctColors
//                                        GROUP BY San_Pham_ID
//                                    ),
//                                    ROMAggregates AS (
//                                        SELECT San_Pham_ID, STRING_AGG(ROM, ', ') AS Danh_Sach_ROM
//                                        FROM DistinctROMs
//                                        GROUP BY San_Pham_ID
//                                    )
//                                    SELECT
//                                        sp.ID AS id,
//                                        sp.TEN_SAN_PHAM AS tenSanPham,
//                                        ca.Danh_Sach_Mau_Sac as danhSachMauSac,
//                                        ra.Danh_Sach_ROM as danhSachRom,
//                                        COUNT(i.IMEI) AS soLuong,
//                                        sp.THOI_GIAN_BAO_HANH as thoiGianBaoHanh
//                                    FROM
//                                        dbo.SAN_PHAM sp
//                                    JOIN
//                                        dbo.SAN_PHAM_CHI_TIET spct ON sp.ID = spct.SAN_PHAM_ID
//                                    LEFT JOIN
//                                        ColorAggregates ca ON sp.ID = ca.San_Pham_ID
//                                    LEFT JOIN
//                                        ROMAggregates ra ON sp.ID = ra.San_Pham_ID
//                                    LEFT JOIN
//                                        dbo.IMEI i ON spct.ID = i.SAN_PHAM_CHI_TIET_ID
//                                    WHERE i.TRANG_THAI NOT LIKE N'DA_BAN'
//                                    GROUP BY
//                                        sp.ID, sp.TEN_SAN_PHAM, ca.Danh_Sach_Mau_Sac, ra.Danh_Sach_ROM, THOI_GIAN_BAO_HANH
//
//
//                """);
//        Map<String,Object> params=new HashMap<>();
//        if(searchKey!=null &&  (searchKey.trim().length()>0)) {
//            sqlQuery.append(" AND sp.ID LIKE '%:searchKey%' ");
//            params.put("searchKey", searchKey);
//        }
//        sqlQuery.append(" ORDER BY "+sortBy+" "+sortOder);
//        sqlQuery.append(" OFFSET :start ROWS FETCH NEXT :lenght ROWS ONLY");
//        params.put("start",pageable.getPageNumber()*pageable.getPageSize());
//        params.put("lenght",pageable.getPageSize());
//        Query query = em.createNativeQuery(sqlQuery.toString(), Object[].class);
//        Query countQuery = em.createNativeQuery("SELECT COUNT(*) FROM ("+sqlQuery.toString()+") as count");
//        for (Map.Entry<String,Object> entry:params.entrySet() ){
//            query.setParameter(entry.getKey(),entry.getValue());
//            countQuery.setParameter(entry.getKey(),entry.getValue());Z
//        }
//        Long totalRecords = ((Number) countQuery.getSingleResult()).longValue();
//        SanPhamDataTable spdt=new SanPhamDataTable();
//        spdt.setDraw(draw);
//        spdt.setRecordsTotal(totalRecords);
//        spdt.setRecordsFiltered(totalRecords);
//        List<SanPhamDataTable.Data> resultList = query.getResultList();
//        spdt.setData(resultList);
//        return spdt;
//    }
}