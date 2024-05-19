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
@Table(name = "IMEI")
public class Imei {
    @Id
    @Nationalized
    @Column(name = "IMEI", nullable = false, length = 50)
    private String imei;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_San_Pham_Chi_Tiet")
    private SanPhamChiTiet idSanPhamChiTiet;

}