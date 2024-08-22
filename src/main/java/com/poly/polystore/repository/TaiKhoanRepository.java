package com.poly.polystore.repository;

import com.poly.polystore.core.admin.kho.model.response.LichSuKhoResponse;
import com.poly.polystore.entity.TaiKhoan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
public interface TaiKhoanRepository extends JpaRepository<TaiKhoan, Integer> {

    Optional<TaiKhoan> findByEmail(String email);
    @Query("SELECT new com.poly.polystore.core.admin.kho.model.response.LichSuKhoResponse$TaiKhoan(tk.id,tk.ten) FROM TaiKhoan tk WHERE (tk.id = :searchKey OR tk.ten like %:searchKey% OR tk.soDienThoai like %:searchKey%) AND tk.role != 'ROLE_GUEST' AND tk.role != 'ROLE_CUSTOMER'")
    Page<LichSuKhoResponse.TaiKhoan> findTaiKhoanNhanVienByCodeLike(String searchKey, Pageable pageAble);
    @Query("SELECT new com.poly.polystore.core.admin.kho.model.response.LichSuKhoResponse$TaiKhoan(tk.id,tk.ten) FROM TaiKhoan tk WHERE tk.role != 'ROLE_GUEST' AND tk.role != 'ROLE_CUSTOMER'")
    Page<LichSuKhoResponse.TaiKhoan> findAllTaiKhoanRessp(Pageable pageAble);

    boolean existsBySoDienThoai(String soDienThoai);

    boolean existsByEmail(String email);
}
