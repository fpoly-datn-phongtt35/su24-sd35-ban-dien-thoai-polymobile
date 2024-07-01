package com.poly.polystore.core.admin.san_pham.model.reponse;

import com.poly.polystore.entity.SanPham;
import com.poly.polystore.repository.SanPhamRepository;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

/**
 * DTO for {@link SanPham}
 */



@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class SanPhamDataTable implements Serializable {
    private Integer id;
    private String tenSanPham;
    private String series;
    private String danhSachMauSac;
    private String danhSachRom;
    private Integer soLuong;
    private String thoiGianBaoHanh;
    private String trangThai;

}