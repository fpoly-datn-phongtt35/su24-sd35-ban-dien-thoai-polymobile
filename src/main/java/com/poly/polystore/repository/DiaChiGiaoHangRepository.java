package com.poly.polystore.repository;

import com.poly.polystore.entity.DiaChiGiaoHang;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DiaChiGiaoHangRepository extends JpaRepository<DiaChiGiaoHang, Integer> {

//  public Long deleteDiaChiGiaoHangByIdKhachHang(Integer id);

  @Query(value = """
      update DIA_CHI_GIAO_HANG set Dia_chi =:newDiaChi where ID_Khach_hang =:idDIachi
      """, nativeQuery = true)
  public void updateDiaChi (String newDiaChi,Integer idDIachi);
  @Modifying
  @Transactional
  @Query(value = """
      update DIA_CHI_GIAO_HANG set Id_Khach_hang =:newDiaChi where ID =:IDDiaChi
      """, nativeQuery = true)
  public void updateId (@Param("newDiaChi") Integer newIdKhachHang,@Param("IDDiaChi") Integer IDDiaChi);
}