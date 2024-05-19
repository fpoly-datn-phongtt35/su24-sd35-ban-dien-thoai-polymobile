package com.poly.polystore.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Nationalized;

import java.math.BigDecimal;
import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "LICH_SU_GIAO_DICH")
public class LichSuGiaoDich {
    @Id
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Nationalized
    @Lob
    @Column(name = "Noi_dung")
    private String noiDung;

    @Column(name = "So_tien", precision = 18, scale = 2)
    private BigDecimal soTien;

    @Column(name = "Thoi_gian")
    private Instant thoiGian;

}