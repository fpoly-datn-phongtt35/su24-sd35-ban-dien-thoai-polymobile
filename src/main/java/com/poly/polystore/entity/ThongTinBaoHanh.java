package com.poly.polystore.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Nationalized;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "THONG_TIN_BAO_HANH")
public class ThongTinBaoHanh {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Nationalized
    @Column(name = "IMEI")
    private String imei;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_Khach_hang")
    private KhachHang idKhachHang;

    @Column(name = "Thoi_gian_bao_hanh")
    private Instant thoiGianBaoHanh;

    @Column(name = "Ngay_bat_dau")
    private Instant ngayBatDau;
    @Column(name = "reason")
    private String reason;

    @Nationalized
    @Column(name = "Trang_thai")
    private String trangThai;

    public ThongTinBaoHanh(String imei, KhachHang idKhachHang, Instant thoiGianBaoHanh) {
        this.imei = imei;
        this.idKhachHang = idKhachHang;
        this.thoiGianBaoHanh = thoiGianBaoHanh;
        this.ngayBatDau = Instant.now();
    }
}
