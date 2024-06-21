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
  @GeneratedValue(strategy =  GenerationType.IDENTITY)
  private Integer id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "ID_Tai_khoan")
  private TaiKhoan idTaiKhoan;


  @JoinColumn(name = "MaKhachHang")
  private String maKhachHang;

  @Nationalized
  @Column(name = "Ten")

  private String ten;

  @Nationalized
  @Column(name = "So_Dien_Thoai", length = 10)
  private String soDienThoai;

  @JoinColumn(name = "IDDiaChi")
  @ManyToOne
  private DiaChiGiaoHang idDiaChi;


  @Column(name = "Ngay_Sinh", length = 10)
  private Date ngaySinh;


  @Column(name = "Anh_Khach_Hang", length = 10)
  private String AnhKhachHang;


  @Column(name = "Trang_Thai", length = 10)
  private String trangThai;


  @Column(name = "Email", length = 10)
  private String Email;


  @Column(name = "Deleted", length = 10)
  private int deteted;
}