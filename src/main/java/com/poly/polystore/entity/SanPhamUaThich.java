package com.poly.polystore.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "SAN_PHAM_UA_THICH")
public class SanPhamUaThich {
    @Id
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Column(name = "ID_Khach_hang")
    private Integer idKhachHang;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_San_pham")
    private SanPham idSanPham;

    @Column(name = "Thoi_gian")
    private Instant thoiGian;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SAN_PHAM_ID")
    private SanPham sanPham;

}
