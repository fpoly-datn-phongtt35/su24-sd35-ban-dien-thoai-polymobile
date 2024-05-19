package com.poly.polystore.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "TINH_NANG_DAC_BIET")
public class TinhNangDacBiet {
    @Id
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_Camera", nullable = false)
    private Camera idCamera;

    @Lob
    @Column(name = "Mo_ta_tinh_nang")
    private String moTaTinhNang;

    @ColumnDefault("0")
    @Column(name = "Deleted")
    private Boolean deleted;

}