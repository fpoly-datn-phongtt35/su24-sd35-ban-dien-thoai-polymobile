package com.poly.polystore.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Embeddable
public class ManHinh {

    @ManyToOne
    @JoinColumn(name = "CONG_NGHE_MAN_HINH_ID")
    private CongNgheManHinh congNgheManHinh;
    @Column(name = "DO_PHAN_GIAI")
    private String doPhanGiai;
    @Column(name = "MAN_HINH_RONG")
    private String manHinhRong;
    @Column(name = "DO_SANG_TOI_DA")
    private String doSangToiDa;
    @ManyToOne
    @JoinColumn(name = "MAT_KINH_CAM_UNG_ID")
    private MatKinhCamUng matKinhCamUng;
}