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
@Table(name = "LICH_SU_BAO_HANH")
public class LichSuBaoHanh {
    @Id
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_Thong_tin_bao_hanh")
    private ThongTinBaoHanh idThongTinBaoHanh;

    @Column(name = "Thoi_gian")
    private Instant thoiGian;

    @Nationalized
    @Lob
    @Column(name = "Noi_dung")
    private String noiDung;

    @Column(name = "Deleted")
    private Boolean deleted;

}