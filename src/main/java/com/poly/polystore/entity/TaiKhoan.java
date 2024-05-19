package com.poly.polystore.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.Nationalized;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "TAI_KHOAN")
public class TaiKhoan {
    @Id
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Nationalized
    @Column(name = "Ten_dang_nhap")
    private String tenDangNhap;

    @Nationalized
    @Column(name = "So_dien_thoai")
    private String soDienThoai;

    @Nationalized
    @Column(name = "Email")
    private String email;

    @Nationalized
    @Column(name = "Mat_khau")
    private String matKhau;

    @Nationalized
    @Column(name = "Loai_tai_khoan")
    private String loaiTaiKhoan;

    @Nationalized
    @Column(name = "Trang_thai")
    private String trangThai;

    @Nationalized
    @Column(name = "Anh", length = 10)
    private String anh;

    @Column(name = "Ngay_Sinh")
    private LocalDate ngaySinh;

}