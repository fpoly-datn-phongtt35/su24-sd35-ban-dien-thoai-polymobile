package com.poly.polystore.entity;

import jakarta.persistence.*;
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
@Table(name = "SAN_PHAM_CHI_TIET")
public class SanPhamChiTiet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SAN_PHAM_ID")
    private SanPham sanPham;
    @OneToMany(
            fetch = FetchType.LAZY,
            mappedBy = "sanPhamChiTiet"
    )
    private List<Imei> imeis;

//TODO: Triển khai đợt giảm giá, tương tự với voucher
//    @ManyToMany(fetch = FetchType.LAZY)
//    @JoinTable(
//            name = "SAN_PHAM_CHI_TIET_DOT_GIAM_GIA",
//            joinColumns = @JoinColumn(name="SAN_PHAM_CHI_TIET_ID"),
//            inverseJoinColumns = @JoinColumn(name="DOT_GIAM_GIA_ID")
//    )
//    private DotGiamGia dotGiamGia;


    @ManyToOne
    @JoinColumn(name = "MAU_SAC_ID")
    private MauSac mauSac;

    @Nationalized
    @Column(name = "ROM")
    private String rom;


    @Column(name = "Gia_ban", precision = 18, scale = 2)
    private BigDecimal giaBan;

    @Column(name = "Gia_von", precision = 18, scale = 2)
    private BigDecimal giaVon;

}