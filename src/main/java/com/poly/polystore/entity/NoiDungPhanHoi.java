package com.poly.polystore.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Nationalized;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "NOI_DUNG_PHAN_HOI")
public class NoiDungPhanHoi {
    @Id
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Nationalized
    @Lob
    @Column(name = "Noi_dung")
    private String noiDung;

}