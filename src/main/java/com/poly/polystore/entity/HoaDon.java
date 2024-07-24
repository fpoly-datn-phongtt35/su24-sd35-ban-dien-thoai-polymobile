package com.poly.polystore.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Nationalized;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "HOA_DON")
public class HoaDon {
    @Id
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_Khach_hang")
    private KhachHang idKhachHang;

    @Nationalized
    @Column(name = "Ma_giam_gia")
    private String maGiamGia;

    @Nationalized
    @Column(name = "Ma")
    private String ma;

    @Nationalized
    @Column(name = "Ten_nguoi_nhan")
    private String tenNguoiNhan;

    @Nationalized
    @Column(name = "So_dien_thoai")
    private String soDienThoai;

    @Nationalized
    @Column(name = "Dia_chi")
    private String diaChi;

    @Column(name = "Tong_san_pham")
    private Integer tongSanPham;

    @Column(name = "Giam_voucher", precision = 18, scale = 2)
    private BigDecimal giamVoucher;

    @Column(name = "Phi_giao_hang", precision = 18, scale = 2)
    private BigDecimal phiGiaoHang;

    @Nationalized
    @Column(name = "Trang_thai")
    private String trangThai;

    @Nationalized
    @Column(name = "Hinh_thuc_giao_hang")
    private String hinhThucGiaoHang;

    @Column(name = "Created_at")
    private Instant createdAt;

    @Column(name = "Deleted")
    private Boolean deleted;

    @Nationalized
    @Column(name = "Last_modified_by")
    private String lastModifiedBy;

    @Column(name = "tong_Tien_hoa_Don", precision = 38, scale = 2)
    private BigDecimal tongTienHoaDon;

    @Column(name = "thoi_Gian_Mua_Hang")
    private Instant thoiGianMuaHang;


}
