package com.poly.polystore.repository;

import com.poly.polystore.Response.HoaDonResponse;
import com.poly.polystore.entity.HoaDon;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface HoaDonRepository extends JpaRepository<HoaDon, Integer> {

  @Query(value = """
      select hd.id as IDHoaDon, hd.Ma as MaHoaDon, spct.anh_San_Pham as AnhSanPham,sp.Ten_san_pham as TenSanPham,
      hd.Trang_thai as TrangThaiHoaDon, hd.tong_Tien_hoa_Don as TongTienHoaDon
      from HOA_DON hd join HOA_DON_CHI_TIET hdct\s
      on hd.ID = hdct.ID_Hoa_don join SAN_PHAM_CHI_TIET spct\s
      on hdct.ID_San_pham_chi_tiet = spct.ID join SAN_PHAM sp
      on sp.ID = spct.ID_San_pham
      """, nativeQuery = true)
  public List<HoaDonResponse> lichSuDHoaDon();

  @Query(value = """
      select hd.id as IDHoaDon, hd.Ma as MaHoaDon, spct.anh_San_Pham as AnhSanPham,sp.Ten_san_pham as TenSanPham,
      hd.Trang_thai as TrangThaiHoaDon, hd.tong_Tien_hoa_Don as TongTienHoaDon
      from HOA_DON hd join HOA_DON_CHI_TIET hdct\s
      on hd.ID = hdct.ID_Hoa_don join SAN_PHAM_CHI_TIET spct\s
      on hdct.ID_San_pham_chi_tiet = spct.ID join SAN_PHAM sp
      on sp.ID = spct.ID_San_pham where hd.trang_thai =:trangThaiHoaDon
      """, nativeQuery = true)
  public List<HoaDonResponse> lichSuDHoaDonTT(String trangThaiHoaDon);


  @Query(value = """
        select hd.Ma as MaHoaDon, spct.anh_San_Pham as AnhSanPham,sp.Ten_san_pham as TenSanPham,
        hd.Trang_thai as TrangThaiHoaDon, hd.tong_Tien_hoa_Don as TongTienHoaDon,
        hd.Tong_san_pham as soLuongSanPham, hd.thoi_Gian_Mua_Hang as thoiGianMuaHang,
        spct.Gia_ban as GiaTienSanPham
        from HOA_DON hd join HOA_DON_CHI_TIET hdct\s
        on hd.ID = hdct.ID_Hoa_don join SAN_PHAM_CHI_TIET spct\s
        on hdct.ID_San_pham_chi_tiet = spct.ID join SAN_PHAM sp
        on sp.ID = spct.ID_San_pham where hd.ID =:id
""",nativeQuery = true)
  public HoaDonResponse lichSuDHoaDonID(String id);
}