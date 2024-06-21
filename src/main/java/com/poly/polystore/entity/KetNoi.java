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
public class KetNoi {
    @Column(name = "MANG_DI_DONG")
    private String mangDiDong;
    @Column(name = "SIM")
    private String sim;
    @ManyToMany
    @JoinTable(
            name = "SAN_PHAM_WIFI",
            joinColumns = @JoinColumn(name="SAN_PHAM_ID"),
            inverseJoinColumns = @JoinColumn(name = "WIFI_ID")
    )
    private Set<Wifi> wifi;
    @ManyToMany
    @JoinTable(
            name = "SAN_PHAM_GPS",
            joinColumns = @JoinColumn(name="SAN_PHAM_ID"),
            inverseJoinColumns = @JoinColumn(name = "GPS_ID")
    )
    private Set<Gps> gps;
    @ManyToMany
    @JoinTable(
            name = "SAN_PHAM_BLUETOOTH",
            joinColumns = @JoinColumn(name="SAN_PHAM_ID"),
            inverseJoinColumns = @JoinColumn(name = "BLUETOOTH_ID")
    )
    private Set<Bluetooth> bluetooth;
    @Column(name="CONG_SAC")
    private String congSac;
    @Column(name="JACK_TAI_NGHE")
    private String jackTaiNghe;


}