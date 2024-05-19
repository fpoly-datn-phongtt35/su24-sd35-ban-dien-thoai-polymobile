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
@Table(name = "CPU")
public class Cpu {
    @Id
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Nationalized
    @Column(name = "CPU")
    private String cpu;

    @Nationalized
    @Column(name = "GPU")
    private String gpu;

    @Nationalized
    @Column(name = "Ten")
    private String ten;

    @Nationalized
    @Lob
    @Column(name = "Mo_ta")
    private String moTa;

    @Nationalized
    @Column(name = "Duong_dan")
    private String duongDan;

    @ColumnDefault("0")
    @Column(name = "Deleted")
    private Boolean deleted;

}