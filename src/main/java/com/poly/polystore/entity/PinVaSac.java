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
public class PinVaSac {
  @Column(name="DUNG_LUONG_PIN")
  private String dungLuongPin;
  @Column(name="LOAI_PIN")
  private String loaiPin;
  @Column(name="HO_TRO_SAC_TOI_DA")
  private String hoTroSacToiDa;
  @ManyToMany
  @JoinTable(
          name="SAN_PHAM_CONG_NGHE_PIN",
          joinColumns = @JoinColumn(name="SAN_PHAM_ID"),
          inverseJoinColumns = @JoinColumn(name="CONG_NGHE_PIN_ID")
  )
  private Set<CongNghePin> congNghePin;

}