package com.poly.polystore.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "GIO_HANG")
public class GioHang {
    @Id
    @Column(name = "ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_Khach_hang")
    private KhachHang idKhachHang;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_San_pham_chi_tiet")
    private SanPhamChiTiet idSanPhamChiTiet;

    @Column(name = "So_luong")
    private Integer soLuong;

    @Column(name = "ID_Tai_khoan")
    private Integer idTaiKhoan;
    @Transient
    private BigDecimal realPrice;

    @Transient
    private List<SanPhamChiTiet> sanPhamCungLoai;
}
