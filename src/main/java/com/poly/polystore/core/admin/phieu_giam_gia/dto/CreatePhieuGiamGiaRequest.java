package com.poly.polystore.core.admin.phieu_giam_gia.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreatePhieuGiamGiaRequest {
    @NotBlank(message = "Mã code rỗng")
    @Pattern(regexp="^[0-9A-Z-]+$", message = "Mã code không đúng định dạng")
    private String code;

    @JsonProperty("discount_value")
    private long discountValue;
    @JsonProperty("apply_value")
    private Integer applyValue;

    @JsonProperty("type")
    private String type;

    @JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
    @JsonProperty("expired_date")
    private Timestamp expiredDate;

    @JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
    @JsonProperty("begin_Date")
    private Timestamp beginDate;
}
