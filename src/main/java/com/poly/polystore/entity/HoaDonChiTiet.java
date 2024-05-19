package com.poly.polystore.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.Nationalized;

import java.math.BigDecimal;

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
    private Integer id;

    @Column(name = "ID_Hoa_don")
    private Integer idHoaDon;

    @Column(name = "ID_San_pham_chi_tiet")
    private Integer idSanPhamChiTiet;

    @Column(name = "Gia_goc", precision = 18, scale = 2)
    private BigDecimal giaGoc;

    @Column(name = "Gia_ban", precision = 18, scale = 2)
    private BigDecimal giaBan;

    @Column(name = "Gia_khuyen_mai", precision = 18, scale = 2)
    private BigDecimal giaKhuyenMai;

    @Nationalized
    @Column(name = "IMEI")
    private String imei;

    @Nationalized
    @Column(name = "Trang_thai")
    private String trangThai;

}