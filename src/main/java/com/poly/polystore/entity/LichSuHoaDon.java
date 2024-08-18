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
@Table(name = "LICH_SU_HOA_DON")
public class LichSuHoaDon {
    @Id
    @Column(name = "ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_Tai_khoan")
    private TaiKhoan idTaiKhoan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_Hoa_don")
    private HoaDon idHoaDon;

    @Nationalized
    @Column(name = "Tieu_de")
    private String tieuDe;

    @Nationalized
    @Lob
    @Column(name = "Noi_dung")
    private String noiDung;

    @Column(name = "Thoi_gian")
    private Instant thoiGian;

}
