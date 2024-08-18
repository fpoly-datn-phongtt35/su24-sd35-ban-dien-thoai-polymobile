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
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "ID_Tai_khoan")
  private TaiKhoan idTaiKhoan;

  @Nationalized
  @Column(name = "Ten")
  private String ten;


  @Nationalized
  @Column(name = "So_Dien_Thoai", length = 13)
  private String soDienThoai;
  @OneToMany(
          fetch = FetchType.LAZY,
          mappedBy = "idKhachHang",
          cascade =  CascadeType.ALL,
          orphanRemoval = true
  )
  private Set<DiaChiGiaoHang> idDiaChi;


  @Column(name = "Email", length = 10)
  private String email;


  @Column(name = "Deteted", length = 10)
  private Integer deleted;
}
