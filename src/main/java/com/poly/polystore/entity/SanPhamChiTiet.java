package com.poly.polystore.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.poly.polystore.repository.SanPhamRepository;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Nationalized;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "SAN_PHAM_CHI_TIET")
public class SanPhamChiTiet implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @JsonIgnore
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


    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "SAN_PHAM_CHI_TIET_KHUYEN_MAI",
            joinColumns = @JoinColumn(name = "SAN_PHAM_CHI_TIET_ID"),
            inverseJoinColumns = @JoinColumn(name = "KHUYEN_MAI_ID")

    )
    private List<KhuyenMai> khuyenMai;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "SAN_PHAM_CHI_TIET_ANH",
            joinColumns = @JoinColumn(name = "SAN_PHAM_CHI_TIET_ID"),
            inverseJoinColumns = @JoinColumn(name = "ANH_ID")
    )
    //Order by id ảnh nào đầu tiên thì là ảnh sản phẩm chi tiết chính
    private List<Anh> anh;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MAU_SAC_ID")
    private MauSac mauSac;

    @Nationalized
    @Column(name = "ROM")
    private String rom;


    @Column(name = "Gia_ban", precision = 18, scale = 2)
    private BigDecimal giaBan;

    @Column(name = "GIA_NHAP", precision = 18, scale = 2)
    private BigDecimal giaNhap;


//    IN_STOCK("Có sẵn"),
//    OUT_OF_STOCK("Hết hàng"),
//    TEMPORARILY_OUT_OF_STOCK("Hết hàng tạm thời"),
//    COMING_SOON("Sắp ra mắt"),
//    DISCONTINUED("Không kinh doanh");
    @Enumerated(EnumType.STRING)
    @Column(name="TRANG_THAI")
    private SanPhamRepository.TrangThai trangThai;

    @Nationalized
    @Column(name = "STT")
    private Integer stt;

    @Transient
    private BigDecimal giaKhuyenMai;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DOT_GIAM_GIA_ID")
    private PhieuGiamGia dotGiamGia;

}
