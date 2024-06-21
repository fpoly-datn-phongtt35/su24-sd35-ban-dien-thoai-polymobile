package com.poly.polystore.entity;

import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Embeddable
public class HeDieuHanhVaCpu {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "HE_DIEU_HANH_ID")
    private HeDieuHanh heDieuHanh;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CPU_ID")
    private Cpu cpu;
}