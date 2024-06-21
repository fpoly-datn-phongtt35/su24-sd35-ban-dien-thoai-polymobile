package com.poly.polystore.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Embeddable
public class ThongTinChung {
    @Column(name = "THIET_KE")
    private String thietKe;
    @Column(name = "CHAT_LIEU")
    private String chatLieu;
    @Column(name = "KICH_THUOC_KHOI_LUONG")
    private String kichThuocKhoiLuong;
}