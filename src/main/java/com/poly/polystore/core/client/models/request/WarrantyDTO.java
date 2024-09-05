package com.poly.polystore.core.client.models.request;

import com.poly.polystore.entity.KhachHang;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class WarrantyDTO {
    private Long ID;
    private KhachHang idKhachHang;
    private Date thoiGianBH;
    private Date ngayBD;
    private String trangThai;
    private Long SANPHAMID;
    private String TENSP;
    private String tenKH;
    private String sdt;
    private String imei;
    private String diffTime;
}
