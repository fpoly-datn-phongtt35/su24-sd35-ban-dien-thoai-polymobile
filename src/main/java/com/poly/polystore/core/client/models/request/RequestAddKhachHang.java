package com.poly.polystore.core.client.models.request;

import com.poly.polystore.entity.TaiKhoan;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class RequestAddKhachHang {

  private Integer id;

  private TaiKhoan idTaiKhoan;

  private String tenKhachHang;

  private String tenNguoiNhan;

  private String diaChi;

  private String thanhPho;

  private String huyen;

  private String xa;

  private String IDDiaChi;

  private String soDienThoai;

  private String soDienThoaiNguoiNhan;

  private String ngaySinhInput;

  private String anhKhachHang;

  private String trangThai;

  private String gioiTinh;

  private String email;

  private int deteted;
}
