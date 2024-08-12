package com.poly.polystore.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.Nationalized;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "HOA_DON_CHI_TIET")
public class HoaDonChiTiet {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "HOA_DON_ID")
    private HoaDon hoaDon;

    @Column(name = "GIA_GOC", precision = 18)
    private BigDecimal giaGoc;

    @Column(name = "GIA_BAN", precision = 18)
    private BigDecimal giaBan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DOT_GIAM_GIA_ID")
    private PhieuGiamGia dotGiamGia;

    @Size(max = 255)
    @Nationalized
    @Column(name = "IMEI")
    private String imei;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SAN_PHAM_CHI_TIET_ID")
    private SanPhamChiTiet sanPhamChiTiet;

}
