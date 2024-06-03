package com.poly.polystore.repository;

import com.poly.polystore.entity.DiaChiGiaoHang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DiaChiGiaoHangRepository extends JpaRepository<DiaChiGiaoHang, Integer> {

//  public Long deleteDiaChiGiaoHangByIdKhachHang(Integer id);

  @Query(value = """
      update DIA_CHI_GIAO_HANG set Dia_chi =:newDiaChi where ID_Khach_hang =:idDIachi
      """, nativeQuery = true)
  public void updateDiaChi (String newDiaChi,Integer idDIachi);
}