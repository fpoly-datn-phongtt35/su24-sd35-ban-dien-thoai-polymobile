package com.poly.polystore.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Builder
@Table(name = "CHI_TIET_LICH_SU_KHO")
public class ChiTietLichSuKho {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SAN_PHAM_CHI_TIET_ID")
    private SanPhamChiTiet sanPhamChiTiet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "LICH_SU_KHO_ID")
    private LichSuKho lichSuKho;

    @Column(name = "SO_LUONG", nullable = false)
    private Integer soLuong;

    @OneToMany(fetch = FetchType.EAGER,mappedBy = "chiTietLichSuKho",cascade = CascadeType.ALL)
    private List<Imei> imeis;

}