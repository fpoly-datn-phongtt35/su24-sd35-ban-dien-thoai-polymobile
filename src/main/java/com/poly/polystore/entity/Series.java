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
@Table(name = "SERIES")
public class Series {
    @Id
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Column(name = "Ma_series", length = 20)
    private String maSeries;

    @Nationalized
    @Column(name = "TEN")
    private String ten;

    @ColumnDefault("0")
    @Column(name = "Deleted")
    private Boolean deleted;

}