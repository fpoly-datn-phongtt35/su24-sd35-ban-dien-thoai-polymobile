package com.poly.polystore.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Nationalized;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "IMEI")
public class Imei {
    public enum TrangThai{
        TRONG_KHO,DA_BAN,CHO_BAN
    }
    @Id
    @Column(name = "IMEI", nullable = false)
    private String imei;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SAN_PHAM_CHI_TIET_ID")
    private SanPhamChiTiet sanPhamChiTiet;

    @Enumerated(EnumType.STRING)
    @Column(name = "TRANG_THAI")
    private TrangThai trangThai;


}
