package com.poly.polystore.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.io.Serializable;

/**
 * DTO for {@link com.poly.polystore.entity.MauSac}
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Data
public class MauSacDto implements Serializable {
    @NotNull
    @Positive
    Integer id;
    String ma;
    String ten;
    Boolean trangThai;
}