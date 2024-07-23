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
@Table(name = "TINH_NANG_CAMERA")
public class TinhNangCamera {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Size(max = 255)
    @NotNull
    @Nationalized
    @Column(name = "TEN", nullable = false)
    private String ten;

    @Size(max = 255)
    @Column(name = "LINK")
    private String link;

    @ColumnDefault("0")
    @Column(name = "DELETED")
    private Boolean deleted;

    @ManyToMany
    @JoinTable(name = "CAMERA_TRUOC_TINH_NANG_CAMERA",
            joinColumns = @JoinColumn(name = "TINH_NANG_CAMERA_ID"),
            inverseJoinColumns = @JoinColumn(name = "SAN_PHAM_ID"))
    private Set<SanPham> sanPhams = new LinkedHashSet<>();

}
