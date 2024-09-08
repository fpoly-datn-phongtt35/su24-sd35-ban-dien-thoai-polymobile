package com.poly.polystore.repository;

import com.poly.polystore.core.admin.san_pham.mapper.SanPhamTopRevenue;
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

    @Query(nativeQuery = true, name="topRevenue")
    public List<SanPhamTopRevenue> topRevenue();

}