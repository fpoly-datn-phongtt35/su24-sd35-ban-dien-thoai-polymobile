package com.poly.polystore.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;

import java.util.LinkedHashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "SERIES")
public class Series {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Size(max = 255)
    @NotNull
    @Nationalized
    @Column(name = "TEN", nullable = false)
    private String ten;
    @ColumnDefault("0")
    @Column(name = "DELETED")
    private Boolean deleted;

    @Size(max = 20)
    @Column(name = "Ma_series", length = 20)
    private String maSeries;

    @OneToMany(mappedBy = "series")
    private Set<SanPham> sanPhams = new LinkedHashSet<>();

}
