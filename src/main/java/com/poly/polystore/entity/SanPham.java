package com.poly.polystore.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Nationalized;

import java.util.List;
import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "SAN_PHAM")
public class SanPham {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Nationalized
    @Column(name = "Ten_san_pham")
    private String tenSanPham;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BINH_LUAN_ID")
    private BinhLuan binhLuan;

    @Embedded
    private ManHinh manHinh;
    @Embedded
    private FontCamera cameraTruoc;
    @Embedded
    private BackCamera cameraSau;

    @Embedded
    private HeDieuHanhVaCpu heDieuHanhVaCpu;

    @Embedded
    private KetNoi ketNoi;

    @Embedded
    private PinVaSac pinVaSac;
    @Embedded
    private ThongTinChung thongTinChung;



    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "SAN_PHAM_KHUYEN_MAI",
            joinColumns = @JoinColumn(name = "SAN_PHAM_ID"),
            inverseJoinColumns = @JoinColumn(name = "KHUYEN_MAI_ID")

    )
    private List<KhuyenMai> khuyenMai;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SERIES_ID")
    private Series series;

    @OneToMany(
            fetch = FetchType.LAZY,
            mappedBy = "sanPham"
    )
    private Set<SanPhamChiTiet> sanPhamChiTiet;

    @Column(name = "RAM")
    private String ram;

    @Nationalized
    @Column(name = "THOI_GIAN_BAO_HANH")
    private String thoiGianBaoHanh;




}