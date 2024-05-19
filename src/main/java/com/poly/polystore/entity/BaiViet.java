package com.poly.polystore.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "BAI_VIET")
public class BaiViet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_Binh_luan")
    private BinhLuan idBinhLuan;

    @Nationalized
    @Column(name = "Duong_dan")
    private String duongDan;

    @Column(name = "Thoi_gian")
    private Instant thoiGian;

    @Nationalized
    @Column(name = "Nguoi_tao")
    private String nguoiTao;

    @Nationalized
    @Column(name = "Tieu_de")
    private String tieuDe;

    @ColumnDefault("0")
    @Column(name = "Luot_xem")
    private Integer luotXem;

    @Nationalized
    @Lob
    @Column(name = "Noi_dung")
    private String noiDung;

}