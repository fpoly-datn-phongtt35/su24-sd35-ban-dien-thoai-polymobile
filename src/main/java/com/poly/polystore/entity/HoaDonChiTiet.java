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
    @ToString.Exclude
    private HoaDon hoaDon;

    @Column(name = "GIA_GOC", precision = 18)
    private BigDecimal giaGoc;// Là giá chưa áp dụng đợt giảm giá

    @Column(name = "GIA_BAN", precision = 18)
    private BigDecimal giaBan;// Là giá sau khi áp dụng đợt giảm giá

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DOT_GIAM_GIA_ID")
    private PhieuGiamGia dotGiamGia;

    @ToString.Exclude
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "imei")
    private Imei imei;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SAN_PHAM_CHI_TIET_ID")
    private SanPhamChiTiet sanPhamChiTiet;
    @Override
    public String toString() {
        return "id=" + id + ", imei=" + imei;
    }
}
