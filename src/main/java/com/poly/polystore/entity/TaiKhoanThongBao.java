package com.poly.polystore.entity;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "TAI_KHOAN_THONG_BAO")
public class TaiKhoanThongBao {
    @Id
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_Tai_khoan")
    private TaiKhoan idTaiKhoan;

    @Column(name = "ID_Thong_bao")
    private Integer idThongBao;

}