package com.poly.polystore.repository;

import com.poly.polystore.entity.Imei;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ImeiRepository extends JpaRepository<Imei, String> {
    Integer countBySanPhamChiTiet_Id(Integer id);

    Imei findFirstByTrangThaiLike(Imei.TrangThai trangThai);

    @Query(value = """

            select im.* from IMEI im
                                  where im.imei =:imei
                                    and im.SAN_PHAM_CHI_TIET_ID =:spctId
                                    and im.TRANG_THAI ='TRONG_KHO'
    """, nativeQuery = true)
    Imei findByImeiAndSanPhamChiTiet(@Param("imei") String imei, @Param("spctId") Integer spctId);
}