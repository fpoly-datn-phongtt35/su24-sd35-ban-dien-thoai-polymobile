package com.poly.polystore.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Nationalized;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "DIA_CHI_GIAO_HANG")
public class DiaChiGiaoHang {
    @Id
    @Column(name = "ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_Khach_hang")
    private KhachHang idKhachHang;

    @Nationalized
    @Column(name = "Ten_nguoi_nhan")
    private String tenNguoiNhan;

    @Nationalized
    @Column(name = "So_dien_thoai")
    private String soDienThoai;

    @Nationalized
    @Column(name = "Dia_chi")
    private String diaChi;

    @Column(name = "La_dia_chi_mac_dinh")
    private Boolean laDiaChiMacDinh;

}