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
    @Column(name = "ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "ID_Hoa_don")
    private Integer idHoaDon;

    @Column(name = "ID_San_pham_chi_tiet")
    private Integer idSanPhamChiTiet;

    @Column(name = "Gia_goc", precision = 18, scale = 2)
    private BigDecimal giaGoc;

    @Column(name = "Gia_ban", precision = 18, scale = 2)
    private BigDecimal giaBan;

    @Nationalized
    @Column(name = "IMEI")
    private String imei;

    @Nationalized
    @Column(name = "Trang_thai")
    private String trangThai;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DOT_GIAM_GIA_ID")
    private PhieuGiamGia dotGiamGia;

    @Size(max = 255)
    @Column(name = "anh_San_Pham")
    private String anhSanPham;

    @Column(name = "so_luong")
    private int soLuong;

}
