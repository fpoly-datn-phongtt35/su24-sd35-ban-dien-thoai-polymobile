package com.poly.polystore.entity;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "OAUTH")
public class Oauth {


    public enum Provider{
        GOOGLE,FACEBOOK;
    }
    @Id
    @Column(length = 50, name = "PROVIDER_ID")
    private String providerId;

    @Column(name = "PROVIDER")
    @Enumerated(EnumType.STRING)
    private Provider provider;

    @ManyToOne
    @JoinColumn(name="TAI_KHOAN_ID")
    private TaiKhoan taiKhoan;

}