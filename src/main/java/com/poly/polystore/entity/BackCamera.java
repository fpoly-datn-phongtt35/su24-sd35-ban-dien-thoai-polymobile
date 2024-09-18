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
public class BackCamera {
    @Column(name = "DO_PHAN_GIAI_CAMERA_SAU")
    private String doPhanGiai;
    @Column(name = "DEN_FLASH_CAMERA_SAU")
    private String denFlash;
    @ManyToMany
    @JoinTable(
            name="CAMERA_SAU_TINH_NANG_CAMERA",
            joinColumns = @JoinColumn(name="SAN_PHAM_ID"),
            inverseJoinColumns = @JoinColumn(name="TINH_NANG_CAMERA_ID"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"SAN_PHAM_ID","TINH_NANG_CAMERA_ID"})
    )
    private Set<TinhNangCamera> tinhNangCameras;
    @Transient
    @ElementCollection
    @CollectionTable(name = "CAMERA_SAU_TINH_NANG_CAMERA", joinColumns = @JoinColumn(name = "SAN_PHAM_ID"))
    @Column(name = "TINH_NANG_CAMERA_ID")
    private List<Integer> tinhNangCameraIds = new ArrayList<>();
}