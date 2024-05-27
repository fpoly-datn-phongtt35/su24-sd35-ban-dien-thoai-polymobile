package com.poly.polystore.Request;

import com.poly.polystore.entity.TaiKhoan;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Nationalized;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class RequestAddKhachHang {
  private Integer id;

  private TaiKhoan idTaiKhoan;

  private String tenKhachHang;

  private String DiaChi;

  private String IDDiaChi;

  private String soDienThoai;

  private Date ngaySinh;

  private String AnhKhachHang;

  private String trangThai;

  private String Email;

  private int deteted;
}
