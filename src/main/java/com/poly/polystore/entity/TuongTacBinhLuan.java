package com.poly.polystore.entity;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "TUONG_TAC_BINH_LUAN")
public class TuongTacBinhLuan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Column(name = "ID_Binh_luan")
    private Integer idBinhLuan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_Tai_khoan")
    private TaiKhoan idTaiKhoan;

    @Column(name = "Like_or_dislike")
    private Boolean likeOrDislike;

}