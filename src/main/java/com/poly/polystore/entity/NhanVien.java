package com.poly.polystore.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Nationalized;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "NHAN_VIEN")
public class NhanVien {
    @Id
    @Column(name = "ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToMany(mappedBy = "nhanVien")
    private List<HoaDon> hoaDons = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "Tai_khoan_ID", referencedColumnName = "ID")
    private TaiKhoan idTaiKhoan;

    @Nationalized
    @Column(name = "Ma_nhan_vien")
    private String maNhanVien;

    @Column(name = "Deleted")
    private Boolean deleted;

    @Column(name = "Vi_tri_cong_viec")
    private String position;

    @Column(name = "Bo_phan")
    private String department;

    @Column(name = "Gioi_tinh")
    private String gioiTinh;

    @Column(name = "CCCD")
    private String identityCard;
}
