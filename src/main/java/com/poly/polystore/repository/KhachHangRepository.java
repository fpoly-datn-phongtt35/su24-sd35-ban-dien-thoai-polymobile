package com.poly.polystore.repository;

import com.poly.polystore.core.client.models.response.KhachHangRepose;
import com.poly.polystore.entity.KhachHang;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface KhachHangRepository extends JpaRepository<KhachHang, Integer> {

    @Query(value = """
            select k.ID as Id_KhachHang,
                    k.MaKhachHang as MaKhachHang,
                      k.Ten as TenKhachHang,
                      k.Ngay_Sinh as NgaySinh,
                      k.Email as EmailKhachHang,
                      k.GioiTinh as GioiTinh,
                      k.So_Dien_Thoai as SDT,
                      k.TrangThai as TrangThai
                      from KHACH_HANG K
                   join DIA_CHI_GIAO_HANG D
                   ON K.ID = D.ID
                      """, nativeQuery = true)
    List<KhachHangRepose> getAllKhachHang();

    @Query("Select KhachHang from KhachHang where idTaiKhoan.id = :idTaiKhoan")
    Optional<KhachHang> findByIdTaiKhoan(Integer idTaiKhoan);

    Page<KhachHang> findByEmailLikeOrSoDienThoaiLike(String s1,String s2, Pageable pageAble);
}
