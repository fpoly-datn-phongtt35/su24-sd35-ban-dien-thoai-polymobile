package com.poly.polystore.repository;

import com.poly.polystore.entity.SanPham;
import com.poly.polystore.entity.SanPhamChiTiet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;

import java.util.List;

public interface SanPhamChiTietRepository extends JpaRepository<SanPhamChiTiet, Integer> {
    SanPhamChiTiet findOneBySanPham(SanPham id);
    List<SanPhamChiTiet> findBySanPhamAndRom(SanPham id, String rom);
//    @Query("SELECT sp FROM SanPhamChiTiet.imeis")
//    public List<String> findAllImeiById(Integer id);
    @Query("""
select spct from SanPhamChiTiet  spct 
join HoaDonChiTiet  hdct
on spct.id = hdct.sanPhamChiTiet.id 
where hdct.id =:ID 
""")
    SanPhamChiTiet findByHoaDonChiTiet(@Param("ID") Integer ID);
}
