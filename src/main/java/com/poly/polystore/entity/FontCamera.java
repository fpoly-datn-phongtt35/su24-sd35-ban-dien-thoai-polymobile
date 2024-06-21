package com.poly.polystore.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Embeddable
public class FontCamera {
    @Column(name = "DO_PHAN_GIAI_CAMERA_TRUOC")
    private String doPhanGiai;
    @Column(name = "DEN_FLASH_CAMERA_TRUOC")
    private Boolean denFlash;
    @ManyToMany
    @JoinTable(
            name="CAMERA_TRUOC_TINH_NANG_CAMERA",
            joinColumns = @JoinColumn(name="SAN_PHAM_ID"),
            inverseJoinColumns = @JoinColumn(name="TINH_NANG_CAMERA_ID"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"SAN_PHAM_ID","TINH_NANG_CAMERA_ID"})
    )
    private Set<TinhNangCamera> tinhNangCameras;
}