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
@Table(name = "DANH_GIA")
public class DanhGia {
    @Id
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_San_pham_chi_tiet")
    private SanPhamChiTiet idSanPhamChiTiet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_Khach_Hang")
    private KhachHang idKhachHang;

    @Column(name = "ID_Noi_dung")
    private Integer idNoiDung;

    @Column(name = "So_sao")
    private Integer soSao;

    @Column(name = "Thoi_gian")
    private Instant thoiGian;

    @Nationalized
    @Column(name = "Hinh_anh")
    private String hinhAnh;

    @Nationalized
    @Column(name = "Trang_thai")
    private String trangThai;

    @Nationalized
    @Lob
    @Column(name = "Noi_dung_phan_hoi")
    private String noiDungPhanHoi;

}