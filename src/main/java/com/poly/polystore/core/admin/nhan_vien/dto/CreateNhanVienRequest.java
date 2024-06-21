package com.poly.polystore.core.admin.nhan_vien.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreateNhanVienRequest {
    private String email;
    private String sdt;
    private String name;
    private String loginName;
    private String position;
    private String department;
    private String cccd;
    private String gender;
    @JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
    @JsonProperty("dob")
    private Timestamp dob;
}
