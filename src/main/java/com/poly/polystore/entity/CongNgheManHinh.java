package com.poly.polystore.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.Nationalized;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "CONG_NGHE_MAN_HINH")
public class CongNgheManHinh {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Size(max = 255)
    @NotNull
    @Nationalized
    @Column(name = "TEN", nullable = false, unique = true)
    private String ten;

    @Size(max = 255)
    @NotNull
    @Column(name = "LINK", nullable = false)
    private String link;

    @Column(name = "DELETED")
    private Boolean deleted;

}