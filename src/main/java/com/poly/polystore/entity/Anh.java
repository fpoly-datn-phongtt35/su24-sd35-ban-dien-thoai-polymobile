package com.poly.polystore.entity;

import jakarta.persistence.*;
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
@Table(name = "ANH")
public class Anh {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Size(max = 255)
    @Nationalized
    @Column(name = "URL")
    private String url;

    @Size(max = 255)
    @Nationalized
    @Column(name = "NAME")
    private String name;

    @OneToOne(mappedBy = "anh")
    private SanPham sanPhams;

    @ManyToMany
    @JoinTable(name = "SAN_PHAM_CHI_TIET_ANH",
            joinColumns = @JoinColumn(name = "ANH_ID"),
            inverseJoinColumns = @JoinColumn(name = "SAN_PHAM_CHI_TIET_ID"))
    private Set<SanPhamChiTiet> sanPhamChiTiets = new LinkedHashSet<>();

}
