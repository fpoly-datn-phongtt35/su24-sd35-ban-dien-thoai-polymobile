package com.poly.polystore.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Nationalized;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "BINH_LUAN_CHI_TIET")
public class BinhLuanChiTiet {
    @Id
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Column(name = "ID_Tai_khoan")
    private Integer idTaiKhoan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_Binh_luan")
    private BinhLuan idBinhLuan;

    @Column(name = "Thoi_gian")
    private Instant thoiGian;

    @Nationalized
    @Column(name = "Nguoi_dang")
    private String nguoiDang;

    @Nationalized
    @Lob
    @Column(name = "Noi_dung")
    private String noiDung;

    @Nationalized
    @Lob
    @Column(name = "Phan_hoi_binh_luan")
    private String phanHoiBinhLuan;

    @Nationalized
    @Column(name = "Trang_thai")
    private String trangThai;

}