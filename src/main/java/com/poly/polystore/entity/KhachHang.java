package com.poly.polystore.entity;

import jakarta.persistence.*;
import java.util.Date;
import lombok.*;
import org.hibernate.annotations.Nationalized;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "KHACH_HANG")
public class KhachHang {

  @Id
  @Column(name = "ID", nullable = false)
  private Integer id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "ID_Tai_khoan")
  private TaiKhoan idTaiKhoan;

  @Nationalized
  @Column(name = "Ten")
  private String ten;

  @Nationalized
  @Column(name = "So_Dien_Thoai", length = 10)
  private String soDienThoai;
  @Column(name = "Ngay_Sinh", length = 10)
  private Date ngaySinh;
  @Column(name = "Anh_Khach_Hang", length = 10)
  private String AnhKhachHang;
  @Column(name = "Trang_thai", length = 10)
  private String trangThai;
  @Column(name = "Email", length = 10)
  private String Email;
  @Column(name = "Deteted", length = 10)
  private int deteted;
}