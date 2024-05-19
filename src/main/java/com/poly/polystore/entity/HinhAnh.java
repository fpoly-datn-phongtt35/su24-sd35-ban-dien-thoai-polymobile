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
@Table(name = "HINH_ANH")
public class HinhAnh {
    @Id
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_San_pham_chi_tiet")
    private SanPhamChiTiet idSanPhamChiTiet;

    @Nationalized
    @Column(name = "Duong_dan")
    private String duongDan;

    @Nationalized
    @Column(name = "Ten")
    private String ten;

}