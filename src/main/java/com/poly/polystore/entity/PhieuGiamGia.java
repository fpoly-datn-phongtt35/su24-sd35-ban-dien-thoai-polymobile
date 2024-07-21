package com.poly.polystore.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Nationalized;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "PHIEU_GIAM_GIA")
public class PhieuGiamGia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Column(name = "Code", nullable = false,unique = true)
    private String code;

    @Column(name = "Gia_tri_giam")
    private BigDecimal giaTriGiam;

    @Column(name = "Don_vi")
    private String donvi;

    @Column(name = "Thoi_gian_ket_thuc")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Instant thoiGianKetThuc;

    @Column(name = "Thoi_gian_bat_dau")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Instant thoiGianBatDau;

    @Column(name = "Create_at")
    private Instant createAt;

    @Column(name = "Update_at")
    private Instant updateAt;

    @Nationalized
    @Column(name = "Last_modified_by")
    private String lastModifiedBy;

    @Column(name = "Deleted")
    private Boolean deleted;
}