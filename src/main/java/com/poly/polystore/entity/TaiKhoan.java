package com.poly.polystore.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.Nationalized;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.util.Collection;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Builder
@Table(name = "TAI_KHOAN")
public class TaiKhoan implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;


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
    @Column(name = "Ngay_Sinh")
    private Instant ngaySinh;

    @Nationalized
    @Column(name = "Ten_dang_nhap")
    private String tenDangNhap;

    @Enumerated(EnumType.STRING)
    Role role;

    @OneToOne(mappedBy = "idTaiKhoan")
    private NhanVien nhanVien;

    @Size(max = 255)
    @Nationalized
    @Column(name = "Anh")
    private String anh;

    @Size(max = 50)
    @Nationalized
    @Column(name = "Ten", length = 50)
    private String ten;

    @Nationalized
    @Column(name = "Loai_tai_khoan", length = 255)
    private String loaiTaikhoan;

    @Nationalized
    @Column(name = "Trang_thai", length = 255)
    private Boolean trangthai;

    public TaiKhoan(Integer idTaiKhoan) {
        this.id=idTaiKhoan;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role==null?"ROLE_GUEST":role.name()));
    }

    @Override
    public String getPassword() {
        return getMatKhau();
    }

    @Override
    public String getUsername() {
        return getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
