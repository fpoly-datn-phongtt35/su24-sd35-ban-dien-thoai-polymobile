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
@Table(name = "DONG_DAN_TRANG_THAI")
public class DongDanTrangThai {
    @Id
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Nationalized
    @Column(name = "Trang_thai")
    private String trangThai;

    @Column(name = "Thoi_gian")
    private Instant thoiGian;

    @Nationalized
    @Column(name = "Tieu_de")
    private String tieuDe;

    @Nationalized
    @Lob
    @Column(name = "Noi_dung")
    private String noiDung;

}