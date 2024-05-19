package com.poly.polystore.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Nationalized;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "SAN_PHAM_CHI_TIET")
public class SanPhamChiTiet {
    @Id
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_San_pham")
    private SanPham idSanPham;

    @Column(name = "ID_Dot_giam_gia")
    private Integer idDotGiamGia;

    @Nationalized
    @Column(name = "Mau_sac")
    private String mauSac;

    @Nationalized
    @Column(name = "ROM")
    private String rom;

    @Nationalized
    @Column(name = "Tinh_trang")
    private String tinhTrang;

    @Column(name = "So_luong")
    private Integer soLuong;

    @Column(name = "Gia_ban", precision = 18, scale = 2)
    private BigDecimal giaBan;

    @Column(name = "Gia_von", precision = 18, scale = 2)
    private BigDecimal giaVon;

}