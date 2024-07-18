package com.poly.polystore.repository;

import com.poly.polystore.entity.NhanVien;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface NhanVienRepository extends JpaRepository<NhanVien, Integer> {
    @Query(value = "Select nv from NhanVien nv where nv.idTaiKhoan.email = :email")
    NhanVien findByEmail(String email);
    @Query(value = "Select nv from NhanVien nv where nv.idTaiKhoan.tenDangNhap = :tendangnhap")
    NhanVien findByTenDangNhap(String tendangnhap);
}
