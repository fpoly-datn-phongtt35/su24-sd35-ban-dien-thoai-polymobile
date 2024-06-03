package com.poly.polystore.Response;

import java.util.Date;
import lombok.Getter;

public interface KhachHangRepose {

//   class để hiển thị danh sách khách hang lên bảng danh sách

  String getMaKhachHang();

  String getTenKhachHang();

  Date getNgaySinh();

  String getSDT();

  String getDiaChi();

  String getGioiTinh();

  String getEmailKhachHang();


  String getTrangThai();


}
