package com.poly.polystore.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.Nationalized;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "KHUYEN_MAI")
public class KhuyenMai {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Size(max = 255)
    @NotNull
    @Nationalized
    @Column(name = "TEN", nullable = false, unique = true)
    private String ten;

    @Size(max = 255)
    @NotNull
    @Column(name = "LINK", nullable = false)
    private String link;

    @DateTimeFormat(pattern = "DD-MM-YYYY hh:mm:ss")
    @Column(name = "THOI_GIAN_BAT_DAU", nullable = false)
    private Date  thoiGianBatDau;

    @DateTimeFormat(pattern = "DD-MM-YYYY hh:mm:ss")
    @Column(name = "THOI_GIAN_KET_THUC", nullable = false)
    private Date  thoiGianKetThuc;

    @Column(name = "DELETED")
    private Boolean deleted;

    @ManyToMany(mappedBy = "khuyenMai")
    private Set<SanPhamChiTiet> sanPhamChiTiets = new LinkedHashSet<>();

    @ManyToMany(mappedBy = "khuyenMai")
    private Set<SanPham> sanPhams = new LinkedHashSet<>();

}
