package com.poly.polystore.entity;

import com.poly.polystore.Constant.TRANGTHAIDONHANG;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Nationalized;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_Khach_hang")
    private KhachHang khachHang;

    @Nationalized
    @Column(name = "Ma_giam_gia")
    private String maGiamGia;

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

    @Enumerated(EnumType.STRING)
    @Column(name = "Trang_thai")
    private TRANGTHAIDONHANG trangThai;

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

    @Column(name = "THOI_GIAN_NHAN_HANG")
    private Instant thoiGianNhanHang;

    @Column(name = "note")
    private String note;

    @Column(name = "email")
    private String email;

    @Column(name = "Hinh_Thuc_Thanh_Toan")
    private String hinhThucThanhToan;

    @Column(name = "TRANG_THAI_THANH_TOAN")
    private String trangThaiThanhToan;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="NHAN_VIEN_ID")
    private NhanVien nhanVien;

    @ToString.Exclude
    @OneToMany(mappedBy = "hoaDon", orphanRemoval = true)
    private List<HoaDonChiTiet> hoaDonChiTiets = new ArrayList<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "idHoaDon", orphanRemoval = true)
    private List<LichSuHoaDon> lichSuHoaDons = new ArrayList<>();

    public HoaDon(Integer idHoaDon) {
        this.id=idHoaDon;
    }
}
