package com.poly.polystore.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "MAN_HINH")
public class ManHinh {
    @Id
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Nationalized
    @Column(name = "Ten")
    private String ten;

    @Nationalized
    @Column(name = "Tam_nen")
    private String tamNen;

    @Nationalized
    @Column(name = "Do_phan_giai")
    private String doPhanGiai;

    @Nationalized
    @Column(name = "Tan_so_quet")
    private String tanSoQuet;

    @Nationalized
    @Column(name = "Kich_thuoc")
    private String kichThuoc;

    @Nationalized
    @Column(name = "Do_sang_toi_da")
    private String doSangToiDa;

    @Nationalized
    @Column(name = "Mat_kinh_cam_ung")
    private String matKinhCamUng;

    @ColumnDefault("0")
    @Column(name = "Deleted")
    private Boolean deleted;

}