package com.poly.polystore.core.client.dto;

import com.poly.polystore.core.admin.don_hang.dto.ImeiDTO;
import com.poly.polystore.entity.*;
import com.poly.polystore.repository.SanPhamRepository;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
@Getter
@Setter
public class SanPhamChiTietDTO {
    private Integer id;

    private SanPhamDTO sanpham;
    private Imei imei;

//    private List<KhuyenMai> khuyenMai;

    private MauSac mauSac;

    private String rom;

    private BigDecimal giaBan;
    private BigDecimal giaNhap;


    private SanPhamRepository.TrangThai trangThai;
    private Integer stt;

    @Transient
    private BigDecimal giaKhuyenMai;

    private Integer soLuong;
    private PhieuGiamGia dotGiamGia;
    private Integer soLuongMua;
}
