package com.poly.polystore.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "LICH_SU_KHO")
public class LichSuKho {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ColumnDefault("getdate()")
    @Column(name = "THOI_GIAN")
    private Instant thoiGian;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TAI_KHOAN_ID")
    private TaiKhoan taiKhoan;

    @Size(max = 1)
    @Nationalized
    @Column(name = "GHI_CHU", length = 1)
    private String ghiChu;

    @ColumnDefault("0")
    @Column(name = "DELETED")
    private Boolean deleted;

    @ToString.Exclude
    @OneToMany(mappedBy = "lichSuKho", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChiTietLichSuKho> chiTietLichSuKhos = new ArrayList<>();

}