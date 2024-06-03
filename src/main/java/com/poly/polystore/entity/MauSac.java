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
@Table(name = "Mau_Sac")
public class MauSac {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Nationalized
    @Column(name = "Ma", nullable = false, length = 10)
    private String ma;

    @Nationalized
    @Column(name = "Ten", nullable = false, length = 50)
    private String ten;

    @Column(name = "TrangThai", nullable = false)
    private Boolean trangThai;

}