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
@Table(name = "NHAN_VIEN")
public class NhanVien {
    @Id
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_Tai_khoan")
    private TaiKhoan idTaiKhoan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_Role")
    private Role idRole;

    @Nationalized
    @Column(name = "Ma_nhan_vien")
    private String maNhanVien;

    @Nationalized
    @Column(name = "Ten_nhan_vien")
    private String tenNhanVien;

    @Column(name = "Deleted")
    private Boolean deleted;

}