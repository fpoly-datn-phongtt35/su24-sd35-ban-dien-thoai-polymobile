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
@Table(name = "CAMERA")
public class Camera {
    @Id
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Nationalized
    @Column(name = "TEN")
    private String ten;

    @Nationalized
    @Column(name = "DO_PHAN_GIAI")
    private String doPhanGiai;

    @ColumnDefault("0")
    @Column(name = "Deleted")
    private Boolean deleted;

}