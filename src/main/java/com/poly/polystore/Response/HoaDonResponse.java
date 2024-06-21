package com.poly.polystore.Response;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;


public interface HoaDonResponse {

  String getIDHoaDon();

  String getMaHoaDon();

  String getAnhSanPham();

  String getTenSanPham();

  String getTrangThaiHoaDon();

  BigDecimal getTongTienHoaDon();

  Integer getSoLuongSanPham();

  Timestamp getThoiGianMuaHang();

  BigDecimal getGiaTienSanPham();

}
