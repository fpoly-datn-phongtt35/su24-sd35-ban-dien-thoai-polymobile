package com.poly.polystore.entity;

import jakarta.persistence.*;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

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


  @JoinColumn(name = "MaKhachHang")
  private String maKhachHang;

  @Nationalized
  @Column(name = "Ten")
  private String ten;


  @Nationalized
  @Column(name = "So_Dien_Thoai", length = 10)
  private String soDienThoai;

  @OneToMany(mappedBy = "idKhachHang")
  private List<DiaChiGiaoHang> idDiaChi;


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

    @OneToMany(mappedBy = "idKhachHang")
    private Set<DanhGia> danhGias = new LinkedHashSet<>();

    @OneToMany(mappedBy = "idKhachHang")
    private Set<DiaChiGiaoHang> diaChiGiaoHangs = new LinkedHashSet<>();

    @OneToMany(mappedBy = "idKhachHang")
    private Set<GioHang> gioHangs = new LinkedHashSet<>();

    @OneToMany(mappedBy = "idKhachHang")
    private Set<HoaDon> hoaDons = new LinkedHashSet<>();

    @OneToMany(mappedBy = "idKhachHang")
    private Set<ThongTinBaoHanh> thongTinBaoHanhs = new LinkedHashSet<>();

}
