package com.poly.polystore.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "PIN")
public class Pin {
    @Id
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Nationalized
    @Column(name = "Ten")
    private String ten;

    @Nationalized
    @Column(name = "Dung_luong")
    private String dungLuong;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_Cong_nghe_sac")
    private CongNgheSac idCongNgheSac;

    @ColumnDefault("0")
    @Column(name = "Deleted")
    private Boolean deleted;

}