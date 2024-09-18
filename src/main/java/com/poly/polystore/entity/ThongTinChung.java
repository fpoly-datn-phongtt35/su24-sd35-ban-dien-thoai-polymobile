package com.poly.polystore.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
    @ManyToMany
    @JoinTable(
            name = "SAN_PHAM_TINH_NANG_DAC_BIET",
            joinColumns = @JoinColumn(name = "SAN_PHAM_ID"),
            inverseJoinColumns = @JoinColumn(name="TINH_NANG_DAC_BIET_ID")
    )
    private Set<TinhNangDacBiet> tinhNangDacBiet;
    @Transient
    @ElementCollection
    @CollectionTable(name = "SAN_PHAM_TINH_NANG_DAC_BIET", joinColumns = @JoinColumn(name = "SAN_PHAM_ID"))
    @Column(name = "TINH_NANG_DAC_BIET_ID")
    private List<Integer> tinhNangDacBietIds = new ArrayList<>();
}