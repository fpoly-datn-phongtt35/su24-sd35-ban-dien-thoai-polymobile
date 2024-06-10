package com.poly.polystore.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Nationalized;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "SAN_PHAM")
public class SanPham {
    @Id
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Nationalized
    @Column(name = "Ten_san_pham")
    private String tenSanPham;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_Binh_luan")
    private BinhLuan idBinhLuan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_Man_Hinh")
    private CongNgheManHinh idCongNgheManHinh;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_Camera_truoc")
    private Camera idCameraTruoc;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_Camera_sau")
    private Camera idCameraSau;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_He_dieu_hanh")
    private HeDieuHanh idHeDieuHanh;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_CPU")
    private Cpu idCpu;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_Series")
    private Series idSeries;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_Pin")
    private Pin idPin;

    @Nationalized
    @Column(name = "RAM")
    private String ram;

    @Nationalized
    @Column(name = "Thoi_gian_bao_hanh")
    private String thoiGianBaoHanh;

    @Nationalized
    @Lob
    @Column(name = "Bai_viet")
    private String baiViet;

    @Nationalized
    @Lob
    @Column(name = "Mo_ta_noi_dung")
    private String moTaNoiDung;

}