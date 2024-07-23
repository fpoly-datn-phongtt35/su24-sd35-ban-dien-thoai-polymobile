package com.poly.polystore.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;

import java.util.LinkedHashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "CONG_NGHE_PIN")
public class CongNghePin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Size(max = 255)
    @NotNull
    @Nationalized
    @Column(name = "TEN", nullable = false)
    private String ten;

    @Size(max = 255)
    @Nationalized
    @Column(name = "LINK")
    private String link;

    @ColumnDefault("0")
    @Column(name = "DELETED")
    private Boolean deleted;

    @ManyToMany
    @JoinTable(name = "SAN_PHAM_CONG_NGHE_PIN",
            joinColumns = @JoinColumn(name = "CONG_NGHE_PIN_ID"),
            inverseJoinColumns = @JoinColumn(name = "SAN_PHAM_ID"))
    private Set<SanPham> sanPhams = new LinkedHashSet<>();

}
